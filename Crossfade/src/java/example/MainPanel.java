package example;
//-*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public final class MainPanel extends JPanel {
    private final JCheckBox box = new JCheckBox("Crossfade Type?", true);
    public MainPanel() {
        super(new BorderLayout());
        ImageIcon i1 = new ImageIcon(getClass().getResource("test.png"));
        ImageIcon i2 = new ImageIcon(getClass().getResource("test.jpg"));
        final Crossfade crossfade = new Crossfade(i1, i2);
        JButton button = new JButton(new AbstractAction("change") {
            @Override public void actionPerformed(ActionEvent ae) {
                crossfade.animationStart();
            }
        });
        add(crossfade);
        add(button,    BorderLayout.NORTH);
        add(box,       BorderLayout.SOUTH);
        setPreferredSize(new Dimension(320, 240));
    }

    class Crossfade extends JComponent implements ActionListener {
        private final Timer animator;
        private final ImageIcon icon1;
        private final ImageIcon icon2;
        private int alpha = 10;
        private boolean mode = true;
        public Crossfade(ImageIcon icon1, ImageIcon icon2) {
            super();
            this.icon1 = icon1;
            this.icon2 = icon2;
            animator = new Timer(50, this);
        }
        public void animationStart() {
            mode ^= true;
            animator.start();
        }
        @Override public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setPaint(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
            if (mode && alpha < 10) {
                alpha = alpha + 1;
            } else if (!mode && alpha > 0) {
                alpha = alpha - 1;
            } else {
                animator.stop();
            }
            if (box.isSelected()) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - alpha * .1f));
            }
            g2d.drawImage(icon1.getImage(), 0, 0, (int) icon1.getIconWidth(), (int) icon1.getIconHeight(), this);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * .1f));
            g2d.drawImage(icon2.getImage(), 0, 0, (int) icon2.getIconWidth(), (int) icon2.getIconHeight(), this);
            g2d.dispose();
        }
        @Override public void actionPerformed(ActionEvent e) {
            repaint();
        }
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
