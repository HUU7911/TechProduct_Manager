package com.techstore;

import com.techstore.service.AdminService;
import com.techstore.service.ProductService;
import com.techstore.ui.LoginPanel;
import com.techstore.ui.MainDashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    private static JFrame frame;
    private static ProductService productService;
    private static AdminService adminService;

    public static void main(String[] args) {
        // Use Nimbus for a cleaner look than Metal default
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            try {
                productService = new ProductService();
                adminService   = new AdminService();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Uncategorized error: " + e.getMessage(),
                        "Lỗi khởi động", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            frame = new JFrame("Tech Product Manager");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1380, 780);
            frame.setMinimumSize(new Dimension(1280, 620));
            frame.setLocationRelativeTo(null);
            frame.setIconImage(makeIcon());

            showLogin();
            frame.setVisible(true);
        });
    }

    static void showLogin() {
        frame.setContentPane(new LoginPanel(adminService, Main::showDashboard));
        frame.revalidate(); frame.repaint();
    }

    static void showDashboard() {
        frame.setContentPane(new MainDashboard(productService, adminService, Main::showLogin));
        frame.revalidate(); frame.repaint();
    }

    private static Image makeIcon() {
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, new Color(120, 60, 255), 64, 64, new Color(0, 200, 255)));
        g2.fillRoundRect(0, 0, 64, 64, 18, 18);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 38));
        g2.drawString("T", 18, 48);
        g2.dispose();
        return img;
    }
}
