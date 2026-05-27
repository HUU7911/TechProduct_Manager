package com.techstore.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class UITheme {
    public static final Color BG       = new Color(10, 10, 22);
    public static final Color SIDEBAR  = new Color(12, 14, 32);
    public static final Color CARD     = new Color(18, 20, 42);
    public static final Color TABLE_R1 = new Color(14, 16, 34);
    public static final Color TABLE_R2 = new Color(18, 20, 42);
    public static final Color ACCENT1  = new Color(0, 200, 255);
    public static final Color ACCENT2  = new Color(120, 60, 255);
    public static final Color GREEN    = new Color(0, 210, 140);
    public static final Color ORANGE   = new Color(255, 160, 30);
    public static final Color RED      = new Color(240, 60, 70);
    public static final Color TEXT     = new Color(215, 225, 255);
    public static final Color TEXT_DIM = new Color(100, 120, 180);
    public static final Color BORDER   = new Color(28, 35, 75);
    public static final Color FIELD    = new Color(22, 26, 52);

    public static JButton gradientButton(String text, Color c1, Color c2, int w, int h) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color a = getModel().isPressed() ? c1.darker() : getModel().isRollover() ? c1.brighter() : c1;
                Color b = getModel().isPressed() ? c2.darker() : getModel().isRollover() ? c2.brighter() : c2;
                g2.setPaint(new GradientPaint(0, 0, a, getWidth(), 0, b));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 9, 9));
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (w > 0) btn.setPreferredSize(new Dimension(w, h > 0 ? h : 36));
        return btn;
    }

    public static JTextField styledField() {
        JTextField f = new JTextField();

        f.setBackground(FIELD);
        f.setForeground(TEXT);
        f.setCaretColor(ACCENT1);

        f.setFont(new Font("SansSerif", Font.PLAIN, 14));

        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(10, 14, 10, 14)
        ));

        f.setPreferredSize(new Dimension(380, 45));
        f.setMaximumSize(new Dimension(380, 45));

        f.setAlignmentX(Component.LEFT_ALIGNMENT);

        return f;
    }

    public static JPasswordField styledPasswordField() {
        JPasswordField f = new JPasswordField();
        f.setBackground(FIELD);
        f.setForeground(TEXT);
        f.setCaretColor(ACCENT1);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    public static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(TEXT_DIM);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
}
