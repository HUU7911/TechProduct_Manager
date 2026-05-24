package com.techstore.ui;

import com.techstore.entity.Product;
import com.techstore.service.ProductService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductPanel extends JPanel {
    private final ProductService ps;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JLabel lblStatus;
    private List<Product> current;
    static final String[] COLS = {"ID", "Tên sản phẩm", "Danh mục", "Thương hiệu", "Giá (₫)", "SL"};

    public ProductPanel(ProductService ps) {
        this.ps = ps;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        buildUI();
        load(ps.getAllProducts());
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

        JLabel title = new JLabel("\uD83D\uDCE6  Quản lý sản phẩm");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        txtSearch = new JTextField(18);
        txtSearch.setBackground(UITheme.FIELD);
        txtSearch.setForeground(UITheme.TEXT);
        txtSearch.setCaretColor(UITheme.ACCENT1);
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1), new EmptyBorder(6, 11, 6, 11)));

        JButton bSearch = btn("\uD83D\uDD0D Tìm", UITheme.ACCENT1, new Color(0,120,160));
        JButton bAdd    = btn("\u2795 Thêm", UITheme.GREEN, new Color(0,110,60));
        JButton bAsc    = btn("\u2191 Giá tăng", new Color(100,80,220), new Color(60,40,160));
        JButton bDesc   = btn("\u2193 Giá giảm", UITheme.ORANGE, new Color(180,90,0));
        JButton bRef    = btn("\u21BA Làm mới", new Color(55,65,110), new Color(30,40,80));

        bSearch.addActionListener(e -> search());
        txtSearch.addActionListener(e -> search());
        bAdd.addActionListener(e -> showAdd());
        bAsc.addActionListener(e -> load(ps.sortByPriceAsc()));
        bDesc.addActionListener(e -> load(ps.sortByPriceDesc()));
        bRef.addActionListener(e -> load(ps.getAllProducts()));

        right.add(txtSearch); right.add(bSearch); right.add(bAsc);
        right.add(bDesc); right.add(bAdd); right.add(bRef);
        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel tableArea() {
        model = new DefaultTableModel(COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable();

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        sp.setBackground(UITheme.BG); sp.getViewport().setBackground(UITheme.BG);
        sp.getVerticalScrollBar().setUnitIncrement(16);

        JPanel w = new JPanel(new BorderLayout());
        w.setOpaque(false); w.setBorder(new EmptyBorder(0, 28, 0, 28));
        w.add(sp); return w;
    }

    private void styleTable() {
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

        int[] ws = {90, 260, 130, 110, 140, 60};
        for (int i = 0; i < ws.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(ws[i]);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                l.setBorder(new EmptyBorder(0, 14, 0, 14));
                if (!sel) {
                    l.setBackground(r % 2 == 0 ? UITheme.TABLE_R1 : UITheme.TABLE_R2);
                    l.setForeground(c == 4 ? UITheme.GREEN : UITheme.TEXT);
                }
                l.setHorizontalAlignment(c == 4 || c == 5 ? RIGHT : LEFT);
                return l;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) showEdit(table.getSelectedRow());
            }
        });
    }

    private JPanel bottomBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false); bar.setBorder(new EmptyBorder(10, 28, 18, 28));
        lblStatus = new JLabel("Đang tải...");
        lblStatus.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblStatus.setForeground(UITheme.TEXT_DIM);

        JPanel acts = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acts.setOpaque(false);
        JButton bEdit = btn("\u270F Sửa", new Color(60,140,220), new Color(30,90,160));
        JButton bDel  = btn("\uD83D\uDDD1 Xóa", UITheme.RED, new Color(160,30,30));
        bEdit.addActionListener(e -> { int r = table.getSelectedRow(); if (r < 0) err("Chọn sản phẩm để sửa!"); else showEdit(r); });
        bDel.addActionListener(e  -> { int r = table.getSelectedRow(); if (r < 0) err("Chọn sản phẩm để xóa!"); else doDel(r); });
        acts.add(bEdit); acts.add(bDel);
        bar.add(lblStatus, BorderLayout.WEST); bar.add(acts, BorderLayout.EAST);
        return bar;
    }

    private void load(List<Product> list) {
        this.current = list;
        model.setRowCount(0);
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        for (Product p : list)
            model.addRow(new Object[]{
                    p.getId().substring(0, 8).toUpperCase(),
                    p.getName(), p.getCategory(), p.getBrand(),
                    nf.format((long) p.getPrice()) + " \u20ab", p.getQuantity()
            });
        lblStatus.setText("Hiển thị " + list.size() + " sản phẩm");
        lblStatus.setForeground(UITheme.TEXT_DIM);
    }

    private void search() { load(ps.searchProducts(txtSearch.getText().trim())); }

    private void showAdd() {
        ProductFormDialog d = new ProductFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        d.setVisible(true);
        if (d.getResult() != null) try { ps.addProduct(d.getResult()); load(ps.getAllProducts()); ok("Thêm thành công!"); } catch (Exception ex) { err(ex.getMessage()); }
    }

    private void showEdit(int row) {
        if (row < 0 || row >= current.size()) return;
        ProductFormDialog d = new ProductFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), current.get(row));
        d.setVisible(true);
        if (d.getResult() != null) try { ps.updateProduct(d.getResult()); load(ps.getAllProducts()); ok("Cập nhật thành công!"); } catch (Exception ex) { err(ex.getMessage()); }
    }

    private void doDel(int row) {
        if (row < 0 || row >= current.size()) return;
        Product p = current.get(row);
        if (JOptionPane.showConfirmDialog(this, "Xóa sản phẩm: " + p.getName() + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            try { ps.deleteProduct(p.getId()); load(ps.getAllProducts()); ok("Đã xóa!"); } catch (Exception ex) { err(ex.getMessage()); }
    }

    private JButton btn(String text, Color c1, Color c2) {
        JButton b = UITheme.gradientButton(text, c1, c2, 0, 32);
        b.setPreferredSize(new Dimension(110, 32));
        return b;
    }

    private void err(String m) { JOptionPane.showMessageDialog(this, m, "Lỗi", JOptionPane.ERROR_MESSAGE); }
    private void ok(String m) {
        lblStatus.setForeground(UITheme.GREEN); lblStatus.setText("\u2714 " + m);
        new Timer(3000, e -> { lblStatus.setForeground(UITheme.TEXT_DIM); lblStatus.setText("Hiển thị " + current.size() + " sản phẩm"); }).start();
    }
}

