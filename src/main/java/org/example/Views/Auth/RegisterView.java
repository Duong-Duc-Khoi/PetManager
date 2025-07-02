package org.example.Views.Auth;

import org.example.Controllers.AuthController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterView extends JFrame {
    private JTextField usernameField, nameField, roleField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;

    private AuthController controller;
    private JFrame loginViewRef;

    public RegisterView() {
        controller = new AuthController();
        initUI();
    }
    public RegisterView(JFrame loginViewRef) {
        this();
        this.loginViewRef = loginViewRef;
    }

    private void initUI() {
        setTitle("Register");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 20, 80, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 200, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 60, 80, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 60, 200, 25);
        add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(20, 100, 80, 25);
        add(roleLabel);

        String[] roles = {"staff"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(100, 100, 200, 25);
        add(roleComboBox);

        registerButton = new JButton("Register");
        registerButton.setBounds(120, 150, 100, 25);
        add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRegisterClicked();
            }
        });
    }

    private void onRegisterClicked() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();

        boolean success = controller.register(username, password, role);
        if (success) {
            JOptionPane.showMessageDialog(this, "Đăng kí thành công !");
            this.dispose();
            if (loginViewRef != null) {
                loginViewRef.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi đăng ký! Vui lòng thử lại!");
        }
    }
}
