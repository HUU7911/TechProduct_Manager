package com.techstore.ui;

import com.techstore.entity.Admin;
import com.techstore.exception.AppException;
import com.techstore.exception.ErrorCode;
import com.techstore.exception.GlobalExceptionHandler;
import com.techstore.service.AdminService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginPanel extends JPanel {
    private final AdminService adminService;
    private final Runnable onLoginSuccess;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblError;

    public LoginPanel(AdminService adminService, Runnable onLoginSuccess) {
        this.adminService = adminService;
        this.onLoginSuccess = onLoginSuccess;
        setLayout(new GridBagLayout());
        setBackground(UITheme.BG);
        buildUI();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, new Color(8, 5, 25), getWidth(), getHeight(), new Color(5, 12, 35)));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.07f));
        g2.setColor(UITheme.ACCENT2);
        g2.fillOval(-80, -80, 360, 360);
        g2.setColor(UITheme.ACCENT1);
        g2.fillOval(getWidth() - 180, getHeight() - 180, 360, 360);
        g2.dispose();
    }

    private void buildUI() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 22, 22));
                g2.setPaint(new GradientPaint(0, 0, UITheme.ACCENT2, getWidth(), 0, UITheme.ACCENT1));
                g2.fillRoundRect(0, 0, getWidth(), 4, 4, 4);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(44, 50, 44, 50));
        card.setPreferredSize(new Dimension(420, 490));

        // Logo
        JLabel icon = new JLabel("⚡", SwingConstants.CENTER);
        icon.setFont(new Font("Serif", Font.PLAIN, 48));
        icon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel title = new JLabel("TECH STORE", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Hệ thống quản lý sản phẩm", SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(UITheme.TEXT_DIM);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        card.add(icon);
        card.add(Box.createVerticalStrut(6));
        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(34));

        card.add(UITheme.fieldLabel("Tên đăng nhập"));
        card.add(Box.createVerticalStrut(5));
        txtUsername = UITheme.styledField();
        txtUsername.setPreferredSize(new Dimension(320, 45));
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(txtUsername);
        card.add(Box.createVerticalStrut(16));

        card.add(UITheme.fieldLabel("Mật khẩu"));
        card.add(Box.createVerticalStrut(5));
        txtPassword = UITheme.styledPasswordField();
        txtPassword.setPreferredSize(new Dimension(320, 45));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(10));

        lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblError.setForeground(UITheme.RED);
        lblError.setAlignmentX(CENTER_ALIGNMENT);
        card.add(lblError);
        card.add(Box.createVerticalStrut(14));

        JButton btnLogin = UITheme.gradientButton("ĐĂNG NHẬP", UITheme.ACCENT2, UITheme.ACCENT1, 0, 44);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnLogin.setAlignmentX(CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> handleLogin());
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(14));

        JLabel lblReg = new JLabel("Chưa có tài khoản? Đăng ký ngay", SwingConstants.CENTER);
        lblReg.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblReg.setForeground(UITheme.ACCENT1);
        lblReg.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblReg.setAlignmentX(CENTER_ALIGNMENT);
        lblReg.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { showRegisterDialog(); }
        });
        card.add(lblReg);

        KeyAdapter enter = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_ENTER) handleLogin(); }
        };
        txtUsername.addKeyListener(enter);
        txtPassword.addKeyListener(enter);

        add(card);
    }

    private void handleLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());
        try {
            if (user.isEmpty() || pass.isEmpty()) {
                throw new AppException(
                        ErrorCode.VALIDATION_ERROR
                );
            }
            Admin admin = adminService.login(user, pass);
            SessionManager.setCurrentAdmin(admin);
            lblError.setText(" ");
            onLoginSuccess.run();
        } catch (Exception ex) {
            GlobalExceptionHandler.handle(this, ex);
        }
    }

    private void showRegisterDialog() {
        new RegisterDialog((JFrame) SwingUtilities.getWindowAncestor(this), adminService).setVisible(true);
    }
}
