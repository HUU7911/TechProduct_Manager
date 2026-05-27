package com.techstore.ui;

import com.techstore.service.AdminService;
import com.techstore.service.ProductService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainDashboard extends JPanel {
    private final ProductService ps;
    private final AdminService as;
    private final Runnable onLogout;
    private JPanel content;
    private CardLayout cards;
    private final Map<String, JButton> navBtns = new LinkedHashMap<>();

    static final String[][] NAV = {
            {"\uD83C\uDFE0", "Tổng quan",   "DASH"},
            {"\uD83D\uDCE6", "Sản phẩm",    "PROD"},
            {"\uD83D\uDC64", "Tài khoản",   "ACCT"},
    };

    public MainDashboard(ProductService ps, AdminService as, Runnable onLogout) {
        this.ps = ps; this.as = as; this.onLogout = onLogout;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
        show("DASH");
    }

    // ── Sidebar ──────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(UITheme.SIDEBAR);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(UITheme.BORDER);
                g.fillRect(getWidth() - 1, 0, 1, getHeight());
            }
        };
        sidebar.setLayout(new BorderLayout());
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new Dimension(215, 0));

        // Logo
        JPanel logo = new JPanel();
        logo.setLayout(new BoxLayout(logo, BoxLayout.Y_AXIS));
        logo.setOpaque(false);
        logo.setBorder(new EmptyBorder(28, 20, 24, 20));

        JLabel icon = new JLabel("\u26A1", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 38));
        icon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel name = new JLabel("TECH STORE", SwingConstants.CENTER);
        name.setFont(new Font("SansSerif", Font.BOLD, 17));
        name.setForeground(Color.WHITE);
        name.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Management System", SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 10));
        sub.setForeground(UITheme.TEXT_DIM);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        logo.add(icon); logo.add(Box.createVerticalStrut(4));
        logo.add(name); logo.add(Box.createVerticalStrut(2)); logo.add(sub);

        // Nav
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setOpaque(false);
        nav.setBorder(new EmptyBorder(8, 10, 8, 10));

        JLabel navLbl = new JLabel("MENU");
        navLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        navLbl.setForeground(new Color(55, 75, 130));
        navLbl.setBorder(new EmptyBorder(0, 8, 10, 0));
        nav.add(navLbl);

        for (String[] item : NAV) {
            JButton b = navBtn(item[0], item[1], item[2]);
            navBtns.put(item[2], b);
            nav.add(b); nav.add(Box.createVerticalStrut(4));
        }

        // Bottom: admin info + logout
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(0, 10, 18, 10));

        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        bottom.add(sep); bottom.add(Box.createVerticalStrut(12));

        String adminName = SessionManager.isLoggedIn() ? SessionManager.getCurrentAdmin().getFullName() : "Admin";
        JLabel adminLbl = new JLabel("\uD83D\uDC64 " + adminName);
        adminLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        adminLbl.setForeground(new Color(160, 180, 230));
        adminLbl.setBorder(new EmptyBorder(0, 8, 0, 0));
        bottom.add(adminLbl); bottom.add(Box.createVerticalStrut(8));

        JButton logout = logoutBtn();
        bottom.add(logout);

        JPanel navWrap = new JPanel(new BorderLayout());
        navWrap.setOpaque(false);
        navWrap.add(nav, BorderLayout.NORTH);

        sidebar.add(logo, BorderLayout.NORTH);
        sidebar.add(navWrap, BorderLayout.CENTER);
        sidebar.add(bottom, BorderLayout.SOUTH);
        return sidebar;
    }

    private JButton navBtn(String icon, String label, String key) {
        JButton btn = new JButton(icon + "  " + label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = Boolean.TRUE.equals(getClientProperty("active"));
                if (active) {
                    g2.setPaint(new GradientPaint(0, 0, new Color(28, 18, 68), getWidth(), 0, new Color(0, 38, 78)));
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                    g2.setPaint(new GradientPaint(0, 0, UITheme.ACCENT2, 0, getHeight(), UITheme.ACCENT1));
                    g2.fillRoundRect(0, 0, 3, getHeight(), 3, 3);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(22, 26, 52));
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        boolean first = key.equals("DASH");
        btn.putClientProperty("active", first);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(first ? Color.WHITE : UITheme.TEXT_DIM);
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(11, 13, 11, 13));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> { show(key); setActive(key); });
        return btn;
    }

    private JButton logoutBtn() {
        JButton btn = new JButton("\uD83D\uDEAA  Đăng xuất") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(75, 18, 18));
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(UITheme.RED);
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 13, 10, 13));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                SessionManager.logout();
                onLogout.run();
            }
        });
        return btn;
    }

    private void setActive(String key) {
        for (Map.Entry<String, JButton> e : navBtns.entrySet()) {
            boolean active = e.getKey().equals(key);
            e.getValue().putClientProperty("active", active);
            e.getValue().setForeground(active ? Color.WHITE : UITheme.TEXT_DIM);
            e.getValue().repaint();
        }
    }

    // ── Content area ─────────────────────────────────────────────────────────
    private JPanel buildContent() {
        cards = new CardLayout();
        content = new JPanel(cards);
        content.setBackground(UITheme.BG);
        content.add(new DashboardOverviewPanel(ps, as), "DASH");
        content.add(new ProductPanel(ps), "PROD");
        content.add(new AccountPanel(as), "ACCT");
        return content;
    }

    private void show(String key) { cards.show(content, key); }
}

