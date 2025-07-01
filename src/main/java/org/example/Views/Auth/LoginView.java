package org.example.Views.Auth;

import org.example.Controllers.AuthController;
import org.example.Utils.ViewManager;
import org.example.Views.HomeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblError;
    private JButton btnLogin;

    private AuthController controller;

    public LoginView() {
        this.controller = new AuthController();
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBackground(Color.WHITE);

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Username:"), gbc);

        txtUsername = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Password:"), gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        lblError = new JLabel(" ");
        lblError.setForeground(Color.RED);
        panel.add(lblError, gbc);

        btnLogin = new JButton("Login");
        gbc.gridy = 3;
        panel.add(btnLogin, gbc);

        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerPanel.setBackground(Color.WHITE);
        JLabel lblPrompt = new JLabel("Chưa có tài khoản?");

        JLabel lblRegister = new JLabel("<html><u>Đăng ký</u></html>");
        lblRegister.setForeground(new Color(0, 102, 204));
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        registerPanel.add(lblPrompt);
        registerPanel.add(lblRegister);

        gbc.gridy = 4;
        panel.add(registerPanel, gbc);

        lblRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onRegisterClicked();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblRegister.setForeground(new Color(30, 120, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblRegister.setForeground(new Color(0, 102, 204));
            }
        });

        btnLogin.addActionListener(e -> onLoginClicked());

        getContentPane().add(panel);
    }


    private void onRegisterClicked() {
        ViewManager.showRegisterView();
    }

    private void onLoginClicked() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            setError("Username và password không được để trống.");
            return;
        }

        btnLogin.setEnabled(false);
        setError("Đang đăng nhập...");
        boolean result = controller.login(username, password);
        if (result) {
            setVisible(false);
            dispose();
            HomeView homeView = new HomeView();
            homeView.setVisible(true);
        } else {
            refresh();
        }
    }

    public void setError(String message) {
        lblError.setText(message);
    }

    public void refresh() {
        txtUsername.setText("");
        txtPassword.setText("");
        btnLogin.setEnabled(true);
        lblError.setText("Sai tài khoản hoặc mật khẩu !");
    }
}
