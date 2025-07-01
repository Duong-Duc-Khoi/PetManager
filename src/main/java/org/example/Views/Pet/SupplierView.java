package org.example.Views.Pet;

import org.example.Controllers.SupplierController;
import org.example.Models.Session;
import org.example.Models.Supplier;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SupplierView extends JFrame {
    private JTextField nameField, phoneField, emailField, addressField;
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private SupplierController supplierController = new SupplierController();

    public SupplierView() {
        setTitle("Quản lý nhà cung cấp");
        setSize(800, 500);
        setLocationRelativeTo(null);
        initUI();
        loadSupplier();

    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        addressField = new JTextField(20);

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Tên nhà cung cấp:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1;
        formPanel.add(addressField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Thêm");
        JButton updateButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xoá");
        JButton resetButton = new JButton("Reset");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(resetButton);

        String[] columnNames = {"ID", "Tên", "SĐT", "Địa chỉ", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        supplierTable = new JTable(tableModel);
        supplierTable.setRowHeight(25);
        supplierTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        JScrollPane tableScroll = new JScrollPane(supplierTable);

        supplierTable.getSelectionModel().addListSelectionListener(e -> onFillterTable());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScroll, BorderLayout.CENTER);

        getContentPane().add(mainPanel);

        addButton.addActionListener(e -> onAddSupplier());
        updateButton.addActionListener(e -> onUpdateSupplier());
        deleteButton.addActionListener(e -> onDeleteSupplier());
        resetButton.addActionListener(e -> Refesh());
    }


    private void loadSupplier() {
        tableModel.setRowCount(0);
        List<Supplier> suppliers = supplierController.getAllSuppliers();
        for (Supplier s : suppliers) {
            tableModel.addRow(new Object[]{
                    s.getSupplierId(),
                    s.getName(),
                    s.getPhone(),
                    s.getAddress(),
                    s.getEmail()
            });
        }
    }

    private void onAddSupplier() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên và SĐT là bắt buộc");
            return;
        }

        Supplier newSupplier = new Supplier(0, name, phone, address, email);
        boolean success = supplierController.addSupplier(newSupplier);

        if (success) {
            loadSupplier();
            Refesh();
            JOptionPane.showMessageDialog(this, "Thêm thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại");
        }
    }

    private void onUpdateSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp để sửa.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();

        Supplier updatedSupplier = new Supplier(id, name, phone, address, email);
        boolean success = supplierController.updateSupplier(updatedSupplier);

        if (success) {
            loadSupplier();
            Refesh();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công.");
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại.");
        }
    }

    private void onDeleteSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp để xoá.");
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
                boolean success = supplierController.deleteSupplier(id);
                if (success) {
                    loadSupplier();
                    Refesh();
                    JOptionPane.showMessageDialog(this, "Xoá thành công.");
                } else {
                    JOptionPane.showMessageDialog(this, "Xoá thất bại.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Mật khẩu không đúng. Không thể xoá.");
            }
        }
    }


    private void onFillterTable() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow != -1) {
            nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            phoneField.setText((String) tableModel.getValueAt(selectedRow, 2));
            addressField.setText((String) tableModel.getValueAt(selectedRow, 3));
            emailField.setText((String) tableModel.getValueAt(selectedRow, 4));
        }
    }


    private void Refesh() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        tableModel.fireTableDataChanged();
    }
}

