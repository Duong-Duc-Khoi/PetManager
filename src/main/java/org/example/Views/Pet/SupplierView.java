package org.example.Views.Pet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.Controllers.SupplierController;
import org.example.Models.Session;
import org.example.Models.Supplier;
import org.mindrot.jbcrypt.BCrypt;

import javax.management.openmbean.CompositeData;
import javax.swing.*;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class SupplierView extends JFrame {
    private JTextField nameField, phoneField, emailField, addressField;
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private SupplierController supplierController = new SupplierController();
    private SupplierController controller;

    public SupplierView() {
        setTitle("Quản lý nhà cung cấp");
        setSize(800, 500);
        setLocationRelativeTo(null);
        initUI();
        loadSupplier();
        controller = new SupplierController();

    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        phoneField = new JTextField(20);
        phoneField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || phoneField.getText().length() >= 10) {
                    e.consume();
                }
            }
        });

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
        JButton exportExcelButton = new JButton("Xuất Excel");



        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(exportExcelButton);

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
        exportExcelButton.addActionListener(e -> onExportExcel());
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

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xoá nhà cung cấp này?",
                "Xác nhận xoá",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = supplierController.deleteSupplier(id);
            if (success) {
                loadSupplier();
                Refesh();
                JOptionPane.showMessageDialog(this, "Xoá thành công.");
            } else {
                JOptionPane.showMessageDialog(this, "Xoá thất bại.");
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
    private void onExportExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        int result = fileChooser.showSaveDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) return;

        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
        if (!filePath.endsWith(".xlsx")) {
            filePath += ".xlsx";
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Suppliers");

            // Header
            String[] headers = {"ID", "Tên nhà cung cấp", "SĐT", "Email", "Địa chỉ"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Dữ liệu

            List<Supplier> suppliers = controller.getAllSuppliers();
            int rowIdx = 1;
            for (Supplier s : suppliers) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(s.getSupplierId());
                row.createCell(1).setCellValue(s.getName());
                row.createCell(2).setCellValue(s.getPhone());
                row.createCell(3).setCellValue(s.getEmail());
                row.createCell(4).setCellValue(s.getAddress());
            }

            // Ghi file
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
                JOptionPane.showMessageDialog(this, "Xuất Excel thành công!");

                File excelFile = new File(filePath);
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(excelFile);
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể mở file Excel tự động.");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + ex.getMessage());
        }
    }


}

