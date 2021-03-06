package example;
//-*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public final class MainPanel extends JPanel {
    private MainPanel() {
        super(new BorderLayout());

        //JTable table = new HighlightableTable(model);
        JTable table = new JTable(new DefaultTableModel(10, 10));
        //table.setAutoCreateRowSorter(true);
        table.setRowSelectionAllowed(false);

        HighlightListener highlighter = new HighlightListener(table);
        table.addMouseListener(highlighter);
        table.addMouseMotionListener(highlighter);

        table.setDefaultRenderer(Object.class, new HighlightRenderer(highlighter));
        table.setDefaultRenderer(Number.class, new HighlightRenderer(highlighter));

        add(new JScrollPane(table));
        setPreferredSize(new Dimension(320, 240));
    }
    public static void main(String... args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGUI();
            }
        });
    }
    public static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("@title@");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class HighlightListener extends MouseAdapter {
    private static final Color HIGHLIGHT1 = new Color(200, 200, 255);
    private static final Color HIGHLIGHT2 = new Color(240, 240, 255);
    private int row = -1;
    private int col = -1;
    private final JTable table;

    public HighlightListener(JTable table) {
        super();
        this.table = table;
    }
    public Color getHighlightableCellColor(int row, int column) {
        if (this.row == row || this.col == column) {
            if (this.row == row && this.col == column) {
                return HIGHLIGHT1;
            } else {
                return HIGHLIGHT2;
            }
        }
        return null;
    }
    private void setHighlighTableCell(Point pt) {
        row = table.rowAtPoint(pt);
        col = table.columnAtPoint(pt);
        if (row < 0 || col < 0) {
            row = -1;
            col = -1;
        }
        table.repaint();
    }
    @Override public void mouseMoved(MouseEvent e) {
        setHighlighTableCell(e.getPoint());
    }
    @Override public void mouseDragged(MouseEvent e) {
        setHighlighTableCell(e.getPoint());
    }
    @Override public void mouseExited(MouseEvent e) {
        row = -1;
        col = -1;
        table.repaint();
    }
}

class HighlightRenderer extends DefaultTableCellRenderer {
    private final transient HighlightListener highlighter;

    public HighlightRenderer(HighlightListener highlighter) {
        super();
        this.highlighter = highlighter;
    }
    @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(value instanceof Number ? RIGHT : LEFT);
        Color highlight = highlighter.getHighlightableCellColor(row, column);
        if (highlight == null) {
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        } else {
            setBackground(highlight);
        }
        return this;
    }
}

// class HighlightableTable extends JTable {
//     private final HighlightListener highlighter;
//     public HighlightableTable(TableModel model) {
//         super(model);
//         highlighter = new HighlightListener(this);
//         addMouseListener(highlighter);
//         addMouseMotionListener(highlighter);
//     }
//     @Override public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
//         Component c = super.prepareRenderer(renderer, row, column);
//         if (highlighter.isHighlightableCell(row, column)) {
//             c.setBackground(Color.RED);
//         } else if (isRowSelected(row)) {
//             c.setBackground(getSelectionBackground());
//         } else {
//             c.setBackground(Color.WHITE);
//         }
//         return c;
//     }
// }
