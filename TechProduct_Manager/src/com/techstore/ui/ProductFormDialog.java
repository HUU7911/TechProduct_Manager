package com.techstore.ui;

import com.techstore.entity.Product;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ProductFormDialog extends JDialog {
    private Product result;
    private final Product original;
    private JTextField txtName, txtBrand, txtPrice, txtQty, txtDesc;
    private JComboBox<String> cmbCat;
    private JLabel lblError;
    static final String[] CATS = {"Điện thoại","Laptop","Tablet","Tai nghe","Đồng hồ thông minh","Phụ kiện"};

    public ProductFormDialog(JFrame parent, Product p) {
        super(parent, p == null ? "Thêm sản phẩm" : "Sửa sản phẩm", true);
        this.original = p;
        setSize(480, 530);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        buildUI();
        if (p != null) fill(p);
    }

    private void buildUI() {
        JPanel main = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setPaint(new GradientPaint(0, 0, UITheme.ACCENT2, getWidth(), 0, UITheme.ACCENT1));
                g2.fillRoundRect(0, 0, getWidth(), 4, 4, 4);
                g2.dispose();
            }
        };
        main.setLayout(new BorderLayout());
        main.setOpaque(false);

        // Header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        hdr.setBorder(new EmptyBorder(22, 28, 14, 28));
        JLabel title = new JLabel(original == null ? "\u2795  Thêm sản phẩm mới" : "\u270F  Sửa sản phẩm");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        JButton close = closeBtn();
        hdr.add(title, BorderLayout.WEST);
        hdr.add(close, BorderLayout.EAST);

        // Form
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(0, 28, 24, 28));

        txtName  = row(form, "Tên sản phẩm *");

        form.add(UITheme.fieldLabel("Danh mục *"));
        form.add(Box.createVerticalStrut(4));
        cmbCat = new JComboBox<>(CATS);
        cmbCat.setBackground(UITheme.FIELD);
        cmbCat.setForeground(UITheme.TEXT);
        cmbCat.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbCat.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        cmbCat.setAlignmentX(LEFT_ALIGNMENT);
        form.add(cmbCat);
        form.add(Box.createVerticalStrut(12));

        txtBrand = row(form, "Thương hiệu *");
        txtPrice = row(form, "Giá (VNĐ) *");
        txtQty   = row(form, "Số lượng *");
        txtDesc  = row(form, "Mô tả");

        lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblError.setForeground(UITheme.RED);
        lblError.setAlignmentX(LEFT_ALIGNMENT);
        form.add(lblError);
        form.add(Box.createVerticalStrut(12));

        JPanel btns = new JPanel(new GridLayout(1, 2, 10, 0));
        btns.setOpaque(false);
        btns.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btns.setAlignmentX(LEFT_ALIGNMENT);
        JButton cancel = plainBtn("Hủy"); cancel.addActionListener(e -> dispose());
        JButton save = UITheme.gradientButton(original == null ? "Thêm sản phẩm" : "Lưu thay đổi", UITheme.ACCENT2, UITheme.ACCENT1, 0, 40);
        save.addActionListener(e -> doSave());
        btns.add(cancel); btns.add(save);
        form.add(btns);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setOpaque(false); scroll.getViewport().setOpaque(false); scroll.setBorder(null);
        main.add(hdr, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        setContentPane(main);
    }

    private JTextField row(JPanel p, String lbl) {
        p.add(UITheme.fieldLabel(lbl));
        p.add(Box.createVerticalStrut(4));
        JTextField f = UITheme.styledField();
        p.add(f); p.add(Box.createVerticalStrut(12));
        return f;
    }

    private JButton closeBtn() {
        JButton b = new JButton("✕");
        b.setBackground(null); b.setForeground(UITheme.TEXT_DIM);
        b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setFont(new Font("SansSerif", Font.PLAIN, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> dispose());
        return b;
    }

    private JButton plainBtn(String t) {
        JButton b = new JButton(t);
        b.setBackground(new Color(35, 40, 68)); b.setForeground(UITheme.TEXT_DIM);
        b.setFont(new Font("SansSerif", Font.BOLD, 13)); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void fill(Product p) {
        txtName.setText(p.getName());
        txtBrand.setText(p.getBrand());
        txtPrice.setText(String.valueOf((long) p.getPrice()));
        txtQty.setText(String.valueOf(p.getQuantity()));
        txtDesc.setText(p.getDescription());
        for (int i = 0; i < CATS.length; i++) if (CATS[i].equals(p.getCategory())) { cmbCat.setSelectedIndex(i); break; }
    }

    private void doSave() {
        try {
            String name  = txtName.getText().trim();
            String cat   = (String) cmbCat.getSelectedItem();
            String brand = txtBrand.getText().trim();
            String ps    = txtPrice.getText().trim().replaceAll("[^0-9.]", "");
            String qs    = txtQty.getText().trim().replaceAll("[^0-9]", "");
            String desc  = txtDesc.getText().trim();
            if (name.isEmpty() || brand.isEmpty() || ps.isEmpty() || qs.isEmpty())
                throw new IllegalArgumentException("Vui lòng điền đầy đủ các trường bắt buộc (*)!");
            double price = Double.parseDouble(ps);
            int qty = Integer.parseInt(qs);
            if (price < 0) throw new IllegalArgumentException("Giá không được âm!");
            if (qty < 0)   throw new IllegalArgumentException("Số lượng không được âm!");
            if (original == null) {
                result = new Product(name, cat, price, qty, brand, desc);
            } else {
                result = new Product(original.getId(), name, cat, price, qty, brand, desc, original.getHeaderColor());
            }
            dispose();
        } catch (NumberFormatException ex) {
            lblError.setText("Giá hoặc số lượng không hợp lệ!");
        } catch (Exception ex) {
            lblError.setText("⚠  " + ex.getMessage());
        }
    }

    public Product getResult() { return result; }
}

