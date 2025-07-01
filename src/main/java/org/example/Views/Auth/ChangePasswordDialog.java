package org.example.Views.Auth;

import org.example.Controllers.UserController;
import org.example.Models.Session;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordDialog extends JDialog {
    private JPasswordField oldPasswordField, newPasswordField, confirmPasswordField;
    private JButton confirmButton, cancelButton;
    private JLabel errorLabel;
    private int userId;

    public ChangePasswordDialog(JFrame parent, int userId, String username) {
        super(parent, "Đổi mật khẩu cho: " + username, true);
        this.userId = userId;

        initUI();
        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Mật khẩu mới:") {{
            setFont(font);
        }}, gbc);
        gbc.gridx = 1;
        newPasswordField = new JPasswordField();
        newPasswordField.setFont(font);
        formPanel.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Xác nhận lại:") {{
            setFont(font);
        }}, gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(font);
        formPanel.add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        errorLabel = new JLabel("Vui lòng nhập đầy đủ thông tin !", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(font);
        formPanel.add(errorLabel, gbc);

        JPanel buttonPanel = new JPanel();
        confirmButton = new JButton("Xác nhận");
        cancelButton = new JButton("Hủy");
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        confirmButton.addActionListener(e -> handleChangePassword());
        cancelButton.addActionListener(e -> dispose());
    }

    private void handleChangePassword() {
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            errorLabel.setText("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            errorLabel.setText("Mật khẩu xác nhận không khớp!");
            return;
        }
        JPasswordField passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        int passConfirm = JOptionPane.showConfirmDialog(
                this,
                passwordField,
                "Nhập mật khẩu để xác nhận thay đổi",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        UserController controller = new UserController();
        if (passConfirm == JOptionPane.OK_OPTION) {
            String enteredPassword = new String(passwordField.getPassword());
            String passHased = Session.getInstance().getUser().getPassword();
            if (BCrypt.checkpw(enteredPassword, passHased)) {
                boolean success = controller.changePassword(userId, newPass);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    errorLabel.setText("Lỗi hệ thống!");
                }
            }else{
                JOptionPane.showMessageDialog(this, "Sai mật khẩu !");
            }
        }

    }
}