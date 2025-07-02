package org.example.Views.Auth;

import org.example.Controllers.UserController;
import org.example.Models.Session;
import org.example.Models.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;

public class AccountView extends JFrame {
    private JTextField idField, usernameField;
    private JComboBox<String> roleComboBox;
    private JTable accountTable;
    private DefaultTableModel tableModel;

    private UserController userController;

    public AccountView() {
        userController = new UserController();
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Quản lý Tài khoản");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(15, 30, 15, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(labelFont);
        inputPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        idField = new JTextField(15);
        idField.setFont(fieldFont);
        idField.setEnabled(false);
        inputPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setFont(labelFont);
        inputPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setFont(fieldFont);
        inputPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel roleLabel = new JLabel("Vai trò:");
        roleLabel.setFont(labelFont);
        inputPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        roleComboBox = new JComboBox<>(new String[]{"admin", "staff"});
        roleComboBox.setFont(fieldFont);
        inputPanel.add(roleComboBox, gbc);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Username", "Role", "Ngày tạo"}, 0);
        accountTable = new JTable(tableModel);
        accountTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accountTable.setRowHeight(25);
        accountTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        JScrollPane scrollPane = new JScrollPane(accountTable);
        scrollPane.setBorder(new EmptyBorder(0, 30, 0, 30));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 30, 20, 30));
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        JButton updateButton = new JButton("Sửa");
        updateButton.setFont(buttonFont);
        JButton deleteButton = new JButton("Xóa");
        deleteButton.setFont(buttonFont);
        JButton changePasswordButton = new JButton("Đổi mật khẩu");
        changePasswordButton.setFont(buttonFont);
        JButton addButton = new JButton("Thêm tài khoản");
        addButton.setFont(buttonFont);
        JButton exportButton = new JButton("Xuất Excel");
        exportButton.setFont(buttonFont);

        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(changePasswordButton);
        buttonPanel.add(addButton);
        buttonPanel.add(exportButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        accountTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = accountTable.getSelectedRow();
                if (row >= 0) {
                    idField.setText(tableModel.getValueAt(row, 0).toString());
                    usernameField.setText(tableModel.getValueAt(row, 1).toString());
                    roleComboBox.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                }
            }
        });

        updateButton.addActionListener(e -> {
            if (!idField.getText().isEmpty()) {
                int id = Integer.parseInt(idField.getText());
                String username = usernameField.getText();
                String role = roleComboBox.getSelectedItem().toString();
                User user = new User(id, username, null, role, null);
                boolean rs = userController.updateUser(user);
                if (rs) {
                    JOptionPane.showMessageDialog(this, "OK !");
                    clearForm();
                } else {
                    return;
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = accountTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản để xoá.");
                return;
            }
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            JPasswordField passwordField = new JPasswordField();
            passwordField.setEchoChar('*');

            int passConfirm = JOptionPane.showConfirmDialog(
                    this,
                    passwordField,
                    "Nhập mật khẩu để xác nhận xoá",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (passConfirm == JOptionPane.OK_OPTION) {
                String enteredPassword = new String(passwordField.getPassword());
                String passHased = Session.getInstance().getUser().getPassword();
                if (BCrypt.checkpw(enteredPassword, passHased)) {
                    boolean success = userController.deleteUser(id);
                    if (success) {
                        clearForm();
                        JOptionPane.showMessageDialog(this, "Xoá thành công.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Xoá thất bại.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Mật khẩu không đúng. Không thể xoá.");
                }
            }
        });

        changePasswordButton.addActionListener(e -> {
            int row = accountTable.getSelectedRow();
            if (row >= 0) {
                int userId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                String username = tableModel.getValueAt(row, 1).toString();
                new ChangePasswordDialog(this, userId, username).setVisible(true);
            }
        });

        addButton.addActionListener(e -> {
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JComboBox<String> roleBox = new JComboBox<>(new String[]{"admin", "staff"});
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Tên đăng nhập:"));
            panel.add(usernameField);
            panel.add(new JLabel("Mật khẩu:"));
            panel.add(passwordField);
            panel.add(new JLabel("Vai trò:"));
            panel.add(roleBox);
            int result = JOptionPane.showConfirmDialog(this, panel, "Thêm tài khoản mới", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String role = (String) roleBox.getSelectedItem();
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
                    return;
                }
                String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
                User user = new User(0, username, hashed, role, null);
                boolean success = userController.addUser(user);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm tài khoản thất bại!");
                }
            }
        });
        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".xlsx")) filePath += ".xlsx";
                boolean success = userController.exportUsersToExcel(filePath);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thất bại!");
                }
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<User> users = userController.getAllUsers();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (User user : users) {
            String createdStr = user.getCreatedAt() != null ? sdf.format(user.getCreatedAt()) : "";
            tableModel.addRow(new Object[]{
                    user.getUserId(), user.getUsername(), user.getRole(), createdStr
            });
        }
    }

    private void clearForm() {
        idField.setText("");
        usernameField.setText("");
        roleComboBox.setSelectedIndex(0);
        loadData();
    }
}
