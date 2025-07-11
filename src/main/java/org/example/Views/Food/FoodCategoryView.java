package org.example.Views.Food;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.Controllers.FoodCategoryController;
import org.example.Models.FoodCategory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class FoodCategoryView extends JFrame {
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private FoodCategoryController controller = new FoodCategoryController();

    public FoodCategoryView() {
        setTitle("Quản lý danh mục thức ăn");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        loadData();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // FORM
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(20);
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Tên danh mục:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        // BUTTONS
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

        // TABLE
        String[] columns = {"ID", "Tên danh mục", "Mô tả"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        categoryTable = new JTable(tableModel);
        categoryTable.setRowHeight(25);
        categoryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        JScrollPane tableScroll = new JScrollPane(categoryTable);

        // SELECTION EVENT
        categoryTable.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());

        // LAYOUT
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScroll, BorderLayout.CENTER);
        getContentPane().add(mainPanel);

        // ACTIONS
        addButton.addActionListener(e -> onAdd());
        updateButton.addActionListener(e -> onUpdate());
        deleteButton.addActionListener(e -> onDelete());
        resetButton.addActionListener(e -> clearForm());
        exportExcelButton.addActionListener(e -> onExportExcel());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<FoodCategory> list = controller.getAll();
        for (FoodCategory c : list) {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getDescription()});
        }
    }

    private void onAdd() {
        String name = nameField.getText().trim();
        String desc = descriptionArea.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên không được để trống.");
            return;
        }

        boolean success = controller.add(new FoodCategory(0, name, desc));
        if (success) {
            loadData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Thêm thành công.");
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại.");
        }
    }

    private void onUpdate() {
        int row = categoryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn danh mục để sửa.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        String name = nameField.getText().trim();
        String desc = descriptionArea.getText().trim();
        boolean success = controller.update(new FoodCategory(id, name, desc));
        if (success) {
            loadData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công.");
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại.");
        }
    }

    private void onDelete() {
        int row = categoryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn danh mục để xoá.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Xoá danh mục này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.delete(id)) {
                loadData();
                clearForm();
                JOptionPane.showMessageDialog(this, "Xoá thành công.");
            } else {
                JOptionPane.showMessageDialog(this, "Xoá thất bại.");
            }
        }
    }

    private void clearForm() {
        nameField.setText("");
        descriptionArea.setText("");
        categoryTable.clearSelection();
    }

    private void fillFormFromTable() {
        int row = categoryTable.getSelectedRow();
        if (row != -1) {
            nameField.setText((String) tableModel.getValueAt(row, 1));
            descriptionArea.setText((String) tableModel.getValueAt(row, 2));
        }
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
            Sheet sheet = workbook.createSheet("Food Categories");

            Row header = sheet.createRow(0);
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                header.createCell(i).setCellValue(tableModel.getColumnName(i));
            }

            for (int row = 0; row < tableModel.getRowCount(); row++) {
                Row dataRow = sheet.createRow(row + 1);
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    Object value = tableModel.getValueAt(row, col);
                    dataRow.createCell(col).setCellValue(value != null ? value.toString() : "");
                }
            }

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
                JOptionPane.showMessageDialog(this, "Xuất Excel thành công!");
                File excelFile = new File(filePath);
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(excelFile);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + ex.getMessage());
        }
    }
}
