package org.example.Views;

import org.example.Models.Session;
import org.example.Utils.ViewManager;
import org.example.Views.Auth.LoginView;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JFrame {
    public HomeView() {
        setTitle("PET SHOP 36 - Home");
        setSize(1150, 600); // phù hợp ảnh gốc
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font titleFont = new Font("Tahoma", Font.BOLD, 36);
        Font sloganFont = new Font("Segoe UI", Font.ITALIC, 14);
        Color mainColor = new Color(0, 102, 204);
        Color buttonColor = new Color(230, 240, 250);
        Color hoverColor = new Color(200, 220, 250);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(mainColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("~ PET SHOP 36 ~", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.YELLOW);

        JLabel sloganLabel = new JLabel("Your trusted pet partner !", SwingConstants.CENTER);
        sloganLabel.setFont(sloganFont);
        sloganLabel.setForeground(Color.WHITE);

        JPanel titleContainer = new JPanel(new GridLayout(2, 1));
        titleContainer.setOpaque(false);
        titleContainer.add(titleLabel);
        titleContainer.add(sloganLabel);

        headerPanel.add(titleContainer, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(9, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnPet = createStyledButton("Quản Lý Thú Cưng", buttonFont, buttonColor, hoverColor);
        JButton btnRecord = createStyledButton("Theo Dõi Sức Khỏe", buttonFont, buttonColor, hoverColor);
        JButton btnSale = createStyledButton("Bán Thú Cưng", buttonFont, buttonColor, hoverColor);
        JButton btnSupplier = createStyledButton("Nhà Cung Cấp", buttonFont, buttonColor, hoverColor);
        JButton btnAccount = createStyledButton("Quản Lý Tài Khoản", buttonFont, buttonColor, hoverColor);
        JButton btnInvoices = createStyledButton("Hóa Đơn", buttonFont, buttonColor, hoverColor);
        JButton btnStatic = createStyledButton("Thống Kê", buttonFont, buttonColor, hoverColor);
        JButton btnFood = createStyledButton("Quản Lý Thức Ăn", buttonFont, buttonColor, hoverColor);
        JButton btnFoodCategory = createStyledButton("Danh Mục Thức Ăn", buttonFont, buttonColor, hoverColor);

        JButton btnLogout = createStyledButton("Đăng Xuất", buttonFont, new Color(255, 220, 220), new Color(255, 180, 180));
        JButton btnChangePassword = createStyledButton("Đổi Mật Khẩu", buttonFont, buttonColor, hoverColor);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnLogout.setForeground(Color.RED);
        buttonPanel.add(btnSale);
        buttonPanel.add(btnInvoices);
        buttonPanel.add(btnFood);
        buttonPanel.add(btnFoodCategory);

        if ("admin".equalsIgnoreCase(Session.getInstance().getUser().getRole())) {
            buttonPanel.add(btnSupplier);
            buttonPanel.add(btnAccount);
            buttonPanel.add(btnStatic);
        }
        buttonPanel.add(btnPet);
        buttonPanel.add(btnRecord);
        buttonPanel.add(btnLogout);
        // Chỉ hiển thị nút Đổi Mật Khẩu cho staff
        if ("staff".equalsIgnoreCase(Session.getInstance().getUser().getRole())) {
            buttonPanel.add(btnChangePassword);
        }

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            String imageUrl = "https://i.pinimg.com/1200x/af/8b/4a/af8b4ae2596c629997277c4bf2cd0235.jpg";
            ImageIcon icon = new ImageIcon(new java.net.URL(imageUrl));

            ScalableImageLabel imgLabel = new ScalableImageLabel(icon);
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imgLabel.setVerticalAlignment(SwingConstants.CENTER);

            imagePanel.add(imgLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            imagePanel.add(new JLabel("Image failed to load"), BorderLayout.CENTER);
        }


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonPanel, imagePanel);
        splitPane.setDividerLocation(380);
        splitPane.setEnabled(false);
        splitPane.setDividerSize(2);

        btnAccount.addActionListener(e -> ViewManager.showAccountView());
        btnSupplier.addActionListener(e -> ViewManager.showSupplier());
        btnPet.addActionListener(e -> ViewManager.showPetView());
        btnRecord.addActionListener(e -> ViewManager.showRecordLog());
        btnSale.addActionListener(e -> ViewManager.showPetSale());
        btnInvoices.addActionListener(e -> ViewManager.showInvoice());
        btnLogout.addActionListener(e -> logout());
        btnStatic.addActionListener(e -> ViewManager.showStatics());
        btnFood.addActionListener(e -> ViewManager.showFood());
        btnFoodCategory.addActionListener(e -> ViewManager.showFoodCategory());

        btnChangePassword.addActionListener(e -> {
            int userId = Session.getInstance().getUser().getUserId();
            String username = Session.getInstance().getUser().getUsername();
            new org.example.Views.Auth.ChangePasswordDialog(this, userId, username).setVisible(true);
        });


        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(splitPane, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Font font, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void logout() {
        Session.getInstance().logout();
        ViewManager.closeAll();
        JOptionPane.showMessageDialog(this, "Bạn đã đăng xuất!");
        dispose();
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }

    //co giãn ảnh
    private static class ScalableImageLabel extends JLabel {
        private Image image;

        public ScalableImageLabel(ImageIcon icon) {
            this.image = icon.getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Vẽ ảnh tự động giãn ra đúng kích thước
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(),getHeight(), this);
            }
        }
    }

}
