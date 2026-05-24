package com.techstore.ui;

import com.techstore.entity.Admin;
import com.techstore.service.AdminService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RegisterDialog extends JDialog {
    private final AdminService adminService;
    private JTextField txtUsername, txtEmail, txtFullName, txtPhone;
    private JPasswordField txtPassword, txtConfirm;
    private JLabel lblError;

    public RegisterDialog(JFrame parent, AdminService adminService) {
        super(parent, "Đăng ký tài khoản", true);
        this.adminService = adminService;
        setSize(440, 560);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        buildUI();
    }

    private void buildUI() {
        JPanel main = getJPanel();
        JLabel title = new JLabel("Tạo tài khoản mới");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(LEFT_ALIGNMENT);
        main.add(title);
        main.add(Box.createVerticalStrut(22));

        txtUsername = addRow(main, "Tên đăng nhập *");
        txtFullName = addRow(main, "Họ tên đầy đủ *");
        txtEmail    = addRow(main, "Email *");
        txtPhone    = addRow(main, "Số điện thoại");

        main.add(UITheme.fieldLabel("Mật khẩu *"));
        main.add(Box.createVerticalStrut(4));
        txtPassword = UITheme.styledPasswordField();
        main.add(txtPassword);
        main.add(Box.createVerticalStrut(10));

        main.add(UITheme.fieldLabel("Xác nhận mật khẩu *"));
        main.add(Box.createVerticalStrut(4));
        txtConfirm = UITheme.styledPasswordField();
        main.add(txtConfirm);
        main.add(Box.createVerticalStrut(10));

        lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblError.setForeground(UITheme.RED);
        lblError.setAlignmentX(LEFT_ALIGNMENT);
        main.add(lblError);
        main.add(Box.createVerticalStrut(12));

        JPanel row = new JPanel(new GridLayout(1, 2, 10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setAlignmentX(LEFT_ALIGNMENT);

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setBackground(new Color(35, 40, 68));
        btnCancel.setForeground(UITheme.TEXT_DIM);
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnCancel.setBorderPainted(false);
        btnCancel.addActionListener(e -> dispose());

        JButton btnOk = UITheme.gradientButton("Đăng ký", UITheme.ACCENT2, UITheme.ACCENT1, 0, 40);
        btnOk.addActionListener(e -> doRegister());

        row.add(btnCancel); row.add(btnOk);
        main.add(row);
        setContentPane(main);
    }

    private static JPanel getJPanel() {
        JPanel main = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
                g2.setPaint(new GradientPaint(0, 0, UITheme.ACCENT2, getWidth(), 0, UITheme.ACCENT1));
                g2.fillRoundRect(0, 0, getWidth(), 4, 4, 4);
                g2.dispose();
            }
        };
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(30, 38, 30, 38));
        return main;
    }

    private JTextField addRow(JPanel p, String label) {
        p.add(UITheme.fieldLabel(label));
        p.add(Box.createVerticalStrut(4));
        JTextField f = UITheme.styledField();
        p.add(f);
        p.add(Box.createVerticalStrut(10));
        return f;
    }

    private void doRegister() {
        try {
            String user = txtUsername.getText().trim();
            String full = txtFullName.getText().trim();
            String mail = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            String pass = new String(txtPassword.getPassword());
            String confirm = new String(txtConfirm.getPassword());

            if (user.isEmpty() || full.isEmpty() || mail.isEmpty()) throw new IllegalArgumentException("Vui lòng điền đầy đủ các trường *");
            if (pass.length() < 6) throw new IllegalArgumentException("Mật khẩu phải ít nhất 6 ký tự!");
            if (!pass.equals(confirm)) throw new IllegalArgumentException("Mật khẩu xác nhận không khớp!");

            Admin a = Admin.builder()
                    .username(user)
                    .passwordHash(pass)
                    .phone(phone)
                    .email(mail)
                    .fullName(full)
                    .build();
            adminService.register(a);
            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Hãy đăng nhập.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            lblError.setText("⚠  " + ex.getMessage());
        }
    }
}