package com.techstore.ui;

import com.techstore.entity.Admin;
import com.techstore.exception.AppException;
import com.techstore.exception.ErrorCode;
import com.techstore.exception.GlobalExceptionHandler;
import com.techstore.service.AdminService;
import com.techstore.service.PasswordUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AdminFormDialog extends JDialog {
    private final Admin original;
    private final AdminService svc;
    private JTextField txtUsername, txtFullName, txtEmail, txtPhone;
    private JPasswordField txtPass, txtConfirm;
    private JLabel lblError;

    public AdminFormDialog(JFrame parent, Admin admin, AdminService svc) {
        super(parent, admin == null ? "Thêm tài khoản" : "Sửa tài khoản", true);
        this.original = admin; this.svc = svc;
        setSize(440, admin == null ? 560 : 440);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        buildUI();
        if (admin != null) fill(admin);
    }

    private void buildUI() {
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
        main.setBorder(new EmptyBorder(28, 36, 28, 36));

        JLabel title = new JLabel(original == null ? "\u2795  Thêm tài khoản mới" : "\u270F  Sửa tài khoản");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(LEFT_ALIGNMENT);
        main.add(title); main.add(Box.createVerticalStrut(22));

        if (original == null) txtUsername = addRow(main, "Tên đăng nhập *");
        txtFullName = addRow(main, "Họ tên đầy đủ *");
        txtEmail    = addRow(main, "Email *");
        txtPhone    = addRow(main, "Số điện thoại");

        if (original == null) {
            main.add(UITheme.fieldLabel("Mật khẩu *")); main.add(Box.createVerticalStrut(4));
            txtPass = UITheme.styledPasswordField(); main.add(txtPass); main.add(Box.createVerticalStrut(10));
            main.add(UITheme.fieldLabel("Xác nhận mật khẩu *")); main.add(Box.createVerticalStrut(4));
            txtConfirm = UITheme.styledPasswordField(); main.add(txtConfirm); main.add(Box.createVerticalStrut(10));
        }

        lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblError.setForeground(UITheme.RED); lblError.setAlignmentX(LEFT_ALIGNMENT);
        main.add(lblError); main.add(Box.createVerticalStrut(12));

        JPanel btns = new JPanel(new GridLayout(1, 2, 10, 0));
        btns.setOpaque(false); btns.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); btns.setAlignmentX(LEFT_ALIGNMENT);
        JButton cancel = new JButton("Hủy");
        cancel.setBackground(new Color(35, 40, 68)); cancel.setForeground(UITheme.TEXT_DIM);
        cancel.setFont(new Font("SansSerif", Font.BOLD, 13)); cancel.setBorderPainted(false);
        cancel.addActionListener(e -> dispose());
        JButton save = UITheme.gradientButton(original == null ? "Tạo tài khoản" : "Lưu thay đổi", UITheme.ACCENT2, UITheme.ACCENT1, 0, 40);
        save.addActionListener(e -> doSave());
        btns.add(cancel); btns.add(save);
        main.add(btns);
        setContentPane(main);
    }

    private JTextField addRow(JPanel p, String lbl) {
        p.add(UITheme.fieldLabel(lbl)); p.add(Box.createVerticalStrut(4));
        JTextField f = UITheme.styledField(); p.add(f); p.add(Box.createVerticalStrut(10));
        return f;
    }

    private void fill(Admin a) {
        txtFullName.setText(a.getFullName()); txtEmail.setText(a.getEmail());
        txtPhone.setText(a.getPhone() != null ? a.getPhone() : "");
    }

    private void doSave() {
        try {
            String full = txtFullName.getText().trim();
            String mail = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            if (full.isEmpty() || mail.isEmpty()) {
                throw new AppException(ErrorCode.VALIDATION_ERROR);
            }
            if (original == null) {
                String user = txtUsername.getText().trim();
                String pass = getString(user);
                Admin newAdmin = Admin.builder()
                        .username(user)
                        .passwordHash(PasswordUtil.hashPassword(pass))
                        .email(mail)
                        .fullName(full)
                        .phone(phone)
                        .build();
                svc.register(newAdmin);
            } else {
                original.setFullName(full); original.setEmail(mail); original.setPhone(phone);
                svc.updateAdmin(original);
            }
            dispose();
        } catch (Exception ex) {
            if (ex instanceof AppException appEx) {

                lblError.setText(
                        "⚠ " + appEx.getMessage()
                );

            } else {

                GlobalExceptionHandler.handle(this, ex);
            }
        }
    }

    private String getString(String user) {
        String pass = new String(txtPass.getPassword());
        String conf = new String(txtConfirm.getPassword());
        if (user.isEmpty()) {
            throw new AppException(
                    "Tên đăng nhập không được trống!",
                    ErrorCode.VALIDATION_ERROR,
                    null
            );
        }
        if (pass.length() < 6) {
            throw new AppException(
                    "Mật khẩu ít nhất 6 ký tự!",
                    ErrorCode.VALIDATION_ERROR,
                    null
            );
        }
        if (!pass.equals(conf)) {
            throw new AppException(
                    "Mật khẩu không khớp!",
                    ErrorCode.VALIDATION_ERROR,
                    null
            );
        }
        return pass;
    }
}


