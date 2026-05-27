package com.techstore.ui;

import com.techstore.service.AdminService;
import com.techstore.service.ProductService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardOverviewPanel extends JPanel {
    private final ProductService ps;
    private final AdminService as;

    public DashboardOverviewPanel(ProductService ps, AdminService as) {
        this.ps = ps; this.as = as;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        JScrollPane scroll = new JScrollPane(buildContent());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll);
    }

    private JPanel buildContent() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(28, 28, 28, 28));
        p.add(buildBanner());
        p.add(Box.createVerticalStrut(26));
        p.add(buildStats());
        p.add(Box.createVerticalStrut(24));
        p.add(buildCategoryBars());
        return p;
    }

    private JPanel buildBanner() {
        JPanel b = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(18, 8, 55), getWidth(), getHeight(), new Color(4, 45, 90)));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.10f));
                g2.setColor(UITheme.ACCENT1);
                g2.fillOval(getWidth() - 160, -40, 240, 240);
                g2.setColor(UITheme.ACCENT2);
                g2.fillOval(getWidth() - 60, getHeight() - 60, 160, 160);
                // Grid overlay
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.04f));
                g2.setColor(Color.WHITE);
                for (int x = 0; x < getWidth(); x += 40) g2.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 40) g2.drawLine(0, y, getWidth(), y);
                g2.dispose();
            }
        };
        b.setLayout(new GridBagLayout());
        b.setOpaque(false);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 175));
        b.setPreferredSize(new Dimension(100, 175));

        JPanel txt = new JPanel();
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        txt.setOpaque(false);

        JLabel tag = new JLabel("⚡ MANAGEMENT SYSTEM v2.0");
        tag.setFont(new Font("SansSerif", Font.BOLD, 11));
        tag.setForeground(UITheme.ACCENT1);

        JLabel h1 = new JLabel("Tech Product Manager");
        h1.setFont(new Font("SansSerif", Font.BOLD, 30));
        h1.setForeground(Color.WHITE);

        JLabel h2 = new JLabel("Quản lý sản phẩm công nghệ — điện thoại, laptop, tablet và hơn thế nữa.");
        h2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        h2.setForeground(new Color(160, 190, 235));

        String name = SessionManager.isLoggedIn() ? SessionManager.getCurrentAdmin().getFullName() : "Admin";
        JLabel hello = new JLabel("Xin chào, " + name + " \uD83D\uDC4B");
        hello.setFont(new Font("SansSerif", Font.ITALIC, 13));
        hello.setForeground(new Color(100, 160, 220));

        txt.add(tag); txt.add(Box.createVerticalStrut(6));
        txt.add(h1);  txt.add(Box.createVerticalStrut(6));
        txt.add(h2);  txt.add(Box.createVerticalStrut(8));
        txt.add(hello);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 32, 0, 0);
        b.add(txt, gbc);
        return b;
    }

    private JPanel buildStats() {
        JPanel g = new JPanel(new GridLayout(1, 4, 16, 0));
        g.setOpaque(false);
        g.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        g.add(statCard("📦", "Tổng sản phẩm", nf.format(ps.totalCount()), UITheme.ACCENT1, UITheme.ACCENT2));
        g.add(statCard("💰", "Tổng giá trị", fmtMoney(ps.totalValue()), UITheme.GREEN, new Color(0, 140, 90)));
        g.add(statCard("📱", "Điện thoại", nf.format(ps.countByCategory("Điện thoại")), UITheme.ORANGE, new Color(190, 100, 0)));
        g.add(statCard("👤", "Tài khoản", String.valueOf(as.getAllAdmins().size()), new Color(170, 90, 255), new Color(100, 40, 200)));
        return g;
    }

    private JPanel statCard(String icon, String label, String value, Color c1, Color c2) {
        JPanel c = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.setPaint(new GradientPaint(0, getHeight() - 4, c2, getWidth(), getHeight() - 4, c1));
                g2.fillRoundRect(0, getHeight() - 4, getWidth(), 4, 4, 4);
                g2.dispose();
            }
        };
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setOpaque(false);
        c.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel ic = new JLabel(icon); ic.setFont(new Font("SansSerif", Font.PLAIN, 26)); ic.setAlignmentX(LEFT_ALIGNMENT);
        JLabel vl = new JLabel(value); vl.setFont(new Font("SansSerif", Font.BOLD, 22)); vl.setForeground(Color.WHITE); vl.setAlignmentX(LEFT_ALIGNMENT);
        JLabel ll = new JLabel(label); ll.setFont(new Font("SansSerif", Font.PLAIN, 12)); ll.setForeground(UITheme.TEXT_DIM); ll.setAlignmentX(LEFT_ALIGNMENT);

        c.add(ic); c.add(Box.createVerticalStrut(8)); c.add(vl); c.add(Box.createVerticalStrut(2)); c.add(ll);
        return c;
    }

    private JPanel buildCategoryBars() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel title = new JLabel("Thống kê theo danh mục");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(LEFT_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(16));

        String[] cats = {"Điện thoại", "Laptop", "Tablet", "Tai nghe", "Đồng hồ thông minh", "Phụ kiện"};
        Color[] cols = {UITheme.ACCENT1, UITheme.GREEN, UITheme.ORANGE, new Color(170, 90, 255), UITheme.RED, new Color(0, 190, 255)};
        int total = ps.totalCount();

        for (int i = 0; i < cats.length; i++) {
            long cnt = ps.countByCategory(cats[i]);
            float pct = total > 0 ? (float) cnt / total : 0f;
            p.add(catBar(cats[i], cnt, pct, cols[i % cols.length]));
            p.add(Box.createVerticalStrut(10));
        }
        return p;
    }

    private JPanel catBar(String name, long count, float pct, Color color) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel nl = new JLabel(name);
        nl.setFont(new Font("SansSerif", Font.BOLD, 13));
        nl.setForeground(UITheme.TEXT);
        nl.setPreferredSize(new Dimension(165, 24));

        JPanel bar = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 28, 55));
                g2.fill(new RoundRectangle2D.Float(0, 9, getWidth(), 11, 11, 11));
                int fw = (int)(getWidth() * pct);
                if (fw > 0) {
                    g2.setPaint(new GradientPaint(0, 0, color.darker(), fw, 0, color));
                    g2.fill(new RoundRectangle2D.Float(0, 9, fw, 11, 11, 11));
                }
                g2.dispose();
            }
        };
        bar.setOpaque(false);

        JLabel cl = new JLabel(count + " (" + String.format("%.1f", pct * 100) + "%)");
        cl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cl.setForeground(UITheme.TEXT_DIM);
        cl.setPreferredSize(new Dimension(120, 24));
        cl.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(nl, BorderLayout.WEST);
        row.add(bar, BorderLayout.CENTER);
        row.add(cl, BorderLayout.EAST);
        return row;
    }

    private String fmtMoney(double v) {
        if (v >= 1_000_000_000) return String.format("%.1fB \u20ab", v / 1_000_000_000);
        if (v >= 1_000_000) return String.format("%.0fM \u20ab", v / 1_000_000);
        return String.format("%.0f \u20ab", v);
    }
}

