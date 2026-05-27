package com.techstore.ui;

import com.techstore.entity.Admin;
import com.techstore.exception.GlobalExceptionHandler;
import com.techstore.service.AdminService;
import com.techstore.service.PasswordUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AccountPanel extends JPanel {
    private final AdminService svc;
    private JTable table;
    private DefaultTableModel model;
    private List<Admin> list;
    private JLabel lblStatus;
    static final String[] COLS = {"ID", "Tên đăng nhập", "Họ tên", "Email", "Điện thoại"};

    public AccountPanel(AdminService svc) {
        this.svc = svc;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        buildUI();
        load();
    }

    private void buildUI() {
        add(topBar(), BorderLayout.NORTH);
        add(tableArea(), BorderLayout.CENTER);
        add(bottomBar(), BorderLayout.SOUTH);
    }

    private JPanel topBar() {
        JPanel bar = new JPanel(new BorderLayout(16, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(24, 28, 16, 28));

        JLabel title = new JLabel("\uD83D\uDC64  Quản lý tài khoản");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        JButton bAdd = btn("\u2795 Thêm tài khoản", UITheme.GREEN, new Color(0, 110, 60));
        JButton bRef = btn("\u21BA Làm mới", new Color(50, 60, 110), new Color(30, 38, 80));
        bAdd.addActionListener(e -> showAdd());
        bRef.addActionListener(e -> load());
        right.add(bAdd); right.add(bRef);

        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel tableArea() {
        model = new DefaultTableModel(COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setBackground(UITheme.TABLE_R1);
        table.setForeground(UITheme.TEXT);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(32, 44, 100));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader h = table.getTableHeader();
        h.setBackground(new Color(16, 18, 42));
        h.setForeground(new Color(100, 140, 220));
        h.setFont(new Font("SansSerif", Font.BOLD, 13));
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UITheme.BORDER));
        h.setReorderingAllowed(false);

        int[] ws = {90, 150, 200, 220, 130};
        for (int i = 0; i < ws.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(ws[i]);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                l.setBorder(new EmptyBorder(0, 14, 0, 14));
                if (!sel) l.setBackground(r % 2 == 0 ? UITheme.TABLE_R1 : UITheme.TABLE_R2);
                return l;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) showEdit(table.getSelectedRow());
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        sp.setBackground(UITheme.BG); sp.getViewport().setBackground(UITheme.BG);
        sp.getVerticalScrollBar().setUnitIncrement(16);

        JPanel w = new JPanel(new BorderLayout());
        w.setOpaque(false); w.setBorder(new EmptyBorder(0, 28, 0, 28));
        w.add(sp); return w;
    }

    private JPanel bottomBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false); bar.setBorder(new EmptyBorder(10, 28, 18, 28));
        lblStatus = new JLabel("Đang tải...");
        lblStatus.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblStatus.setForeground(UITheme.TEXT_DIM);

        JPanel acts = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acts.setOpaque(false);
        JButton bEdit = btn("\u270F Sửa", new Color(60, 140, 220), new Color(30, 90, 160));
        JButton bPwd  = btn("\uD83D\uDD11 Đổi MK", UITheme.ORANGE, new Color(170, 90, 0));
        JButton bDel  = btn("\uD83D\uDDD1 Xóa", UITheme.RED, new Color(160, 30, 30));

        bEdit.addActionListener(e -> { int r = table.getSelectedRow(); if (r < 0) err("Chọn tài khoản để sửa!"); else showEdit(r); });
        bPwd.addActionListener(e  -> { int r = table.getSelectedRow(); if (r < 0) err("Chọn tài khoản!"); else changePwd(r); });
        bDel.addActionListener(e  -> { int r = table.getSelectedRow(); if (r < 0) err("Chọn tài khoản để xóa!"); else doDel(r); });

        acts.add(bPwd); acts.add(bEdit); acts.add(bDel);
        bar.add(lblStatus, BorderLayout.WEST);
        bar.add(acts, BorderLayout.EAST);
        return bar;
    }

    private void load() {
        list = svc.getAllAdmins();
        model.setRowCount(0);
        for (Admin a : list)
            model.addRow(new Object[]{
                    a.getId().substring(0, 8).toUpperCase(),
                    a.getUsername(), a.getFullName(), a.getEmail(), a.getPhone()
            });
        lblStatus.setText("Tổng " + list.size() + " tài khoản");
        lblStatus.setForeground(UITheme.TEXT_DIM);
    }

    private void showAdd() {
        new AdminFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), null, svc).setVisible(true);
        load();
    }

    private void showEdit(int row) {
        new AdminFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), list.get(row), svc).setVisible(true);
        load();
    }

    private void doDel(int row) {
        Admin a = list.get(row);
        Admin cur = SessionManager.getCurrentAdmin();
        if (cur != null && cur.getId().equals(a.getId())) { err("Không thể xóa tài khoản đang đăng nhập!"); return; }
        if (JOptionPane.showConfirmDialog(this, "Xóa tài khoản: " + a.getUsername() + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)
            try { svc.deleteAdmin(a.getId()); load(); ok("Đã xóa tài khoản!"); } catch (Exception ex) { err(ex.getMessage()); }
    }

    private void changePwd(int row) {
        Admin a = list.get(row);
        JPasswordField np = new JPasswordField(), cp = new JPasswordField();
        JPanel panel = new JPanel(new GridLayout(4, 1, 4, 4));
        panel.add(new JLabel("Mật khẩu mới:")); panel.add(np);
        panel.add(new JLabel("Xác nhận:")); panel.add(cp);
        if (JOptionPane.showConfirmDialog(this, panel, "Đổi mật khẩu: " + a.getUsername(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String np2 = new String(np.getPassword()), cp2 = new String(cp.getPassword());
            if (np2.length() < 6) { err("Mật khẩu ít nhất 6 ký tự!"); return; }
            if (!np2.equals(cp2)) { err("Mật khẩu không khớp!"); return; }
            try {
                a.setPasswordHash(PasswordUtil.HashPassword(np2));
                svc.updateAdmin(a);
                ok("Đổi mật khẩu thành công!");
            } catch (Exception ex) {
                GlobalExceptionHandler.handle(this, ex);
            }
        }
    }

    private JButton btn(String text, Color c1, Color c2) {
        JButton b = UITheme.gradientButton(text, c1, c2, 0, 32);
        b.setPreferredSize(new Dimension(130, 32)); return b;
    }
    private void err(String m) { JOptionPane.showMessageDialog(this, m, "Lỗi", JOptionPane.ERROR_MESSAGE); }
    private void ok(String m) {
        lblStatus.setForeground(UITheme.GREEN); lblStatus.setText("\u2714 " + m);
        new Timer(3000, e -> { lblStatus.setForeground(UITheme.TEXT_DIM); lblStatus.setText("Tổng " + list.size() + " tài khoản"); }).start();
    }
}

