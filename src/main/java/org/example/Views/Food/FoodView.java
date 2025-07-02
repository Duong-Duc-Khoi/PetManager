package org.example.Views.Food;

import org.example.Controllers.FoodController;
import org.example.Controllers.SupplierController;
import org.example.DTO.SupplierItems;
import org.example.Models.Food;
import org.example.Models.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileOutputStream;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FoodView extends JFrame {
    private JTextField idField, nameField, brandField, descriptionField, priceField, quantityField;
    private JComboBox<SupplierItems> supplierComboBox;
    private JTable foodTable;
    private DefaultTableModel tableModel;

    private FoodController foodController;
    private SupplierController supplierController;

    private Map<Integer, String> supplierNameMap = new HashMap<>();
    private Map<Integer, Integer> foodIdToSupplierIdMap = new HashMap<>();

    public FoodView() {
        this.foodController = new FoodController();
        this.supplierController = new SupplierController();
        initUI();
        loadSuppliers();
        loadData();
    }

    private void initUI() {
        setTitle("Quản lý Thức ăn");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        idField = new JTextField();
        idField.setEnabled(false);
        nameField = new JTextField();
        brandField = new JTextField();
        descriptionField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();
        supplierComboBox = new JComboBox<>();

        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Tên:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Thương hiệu:"));
        inputPanel.add(brandField);
        inputPanel.add(new JLabel("Mô tả:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Giá:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Số lượng trong kho:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Nhà cung cấp:"));
        inputPanel.add(supplierComboBox);

        JButton addButton = new JButton("Thêm");
        JButton updateButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xoá");
        JButton clearButton = new JButton("Clear");
        JButton exportButton = new JButton("Xuất Excel");


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exportButton);


        String[] columnNames = {"ID", "Tên", "Thương hiệu", "Mô tả", "Giá", "Số lượng", "Nhà cung cấp"};
        tableModel = new DefaultTableModel(columnNames, 0);
        foodTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(foodTable);

        add(inputPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            Food food = getFoodFromForm();
            if (food != null) {
                if (foodController.addFood(food)) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    loadData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            }
        });

        updateButton.addActionListener(e -> {
            if (idField.getText().isEmpty()) return;
            Food food = getFoodFromForm();
            if (food != null) {
                food.setFoodId(Integer.parseInt(idField.getText()));
                if (foodController.updateFood(food)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int row = foodTable.getSelectedRow();
            if (row == -1) return;
            int foodId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            if (foodController.deleteFood(foodId)) {
                JOptionPane.showMessageDialog(this, "Xoá thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xoá thất bại!");
            }
        });

        clearButton.addActionListener(e -> clearForm());
        exportButton.addActionListener(e -> exportToExcel());


        foodTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = foodTable.getSelectedRow();
                if (row >= 0) {
                    idField.setText(tableModel.getValueAt(row, 0).toString());
                    nameField.setText(tableModel.getValueAt(row, 1).toString());
                    brandField.setText(tableModel.getValueAt(row, 2).toString());
                    descriptionField.setText(tableModel.getValueAt(row, 3).toString());
                    priceField.setText(tableModel.getValueAt(row, 4).toString());
                    quantityField.setText(tableModel.getValueAt(row, 5).toString());

                    int foodId = Integer.parseInt(idField.getText());
                    int supplierId = foodIdToSupplierIdMap.getOrDefault(foodId, -1);
                    for (int i = 0; i < supplierComboBox.getItemCount(); i++) {
                        SupplierItems item = supplierComboBox.getItemAt(i);
                        if (item.getId() == supplierId) {
                            supplierComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void loadSuppliers() {
        supplierComboBox.removeAllItems();
        supplierNameMap.clear();

        List<Supplier> suppliers = supplierController.getAllSuppliers();
        for (Supplier s : suppliers) {
            SupplierItems item = new SupplierItems(s.getSupplierId(), s.getName());
            supplierComboBox.addItem(item);
            supplierNameMap.put(s.getSupplierId(), s.getName());
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        foodIdToSupplierIdMap.clear();

        List<Food> foods = foodController.getAllFoods();
        for (Food food : foods) {
            int foodId = food.getFoodId();
            int supplierId = food.getSupplierId();
            foodIdToSupplierIdMap.put(foodId, supplierId);
            String supplierName = supplierNameMap.getOrDefault(supplierId, "N/A");

            tableModel.addRow(new Object[]{
                    food.getFoodId(),
                    food.getName(),
                    food.getBrand(),
                    food.getDescription(),
                    food.getPrice(),
                    food.getQuantityInStock(),
                    supplierName
            });
        }
    }

    private Food getFoodFromForm() {
        try {
            String name = nameField.getText();
            String brand = brandField.getText();
            String desc = descriptionField.getText();
            BigDecimal price = new BigDecimal(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            SupplierItems selectedSupplier = (SupplierItems) supplierComboBox.getSelectedItem();
            int supplierId = selectedSupplier != null ? selectedSupplier.getId() : -1;

            return new Food(0, name, brand, desc, price, quantity, supplierId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        brandField.setText("");
        descriptionField.setText("");
        priceField.setText("");
        quantityField.setText("");
        if (supplierComboBox.getItemCount() > 0) supplierComboBox.setSelectedIndex(0);
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            clearForm();
            loadData();
            loadSuppliers();
        }
        super.setVisible(b);
    }

    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection != JFileChooser.APPROVE_OPTION) return;

        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
        if (!filePath.endsWith(".xlsx")) {
            filePath += ".xlsx";
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Foods");

            String[] headers = {"ID", "Tên", "Thương hiệu", "Mô tả", "Giá", "Số lượng", "Nhà cung cấp"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            List<Food> foodList = foodController.getAllFoods();
            Map<Integer, String> supplierMap = new HashMap<>();
            for (Supplier s : supplierController.getAllSuppliers()) {
                supplierMap.put(s.getSupplierId(), s.getName());
            }

            int rowNum = 1;
            for (Food food : foodList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(food.getFoodId());
                row.createCell(1).setCellValue(food.getName());
                row.createCell(2).setCellValue(food.getBrand());
                row.createCell(3).setCellValue(food.getDescription());
                row.createCell(4).setCellValue(food.getPrice().doubleValue());
                row.createCell(5).setCellValue(food.getQuantityInStock());
                row.createCell(6).setCellValue(supplierMap.getOrDefault(food.getSupplierId(), "N/A"));
            }

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
                JOptionPane.showMessageDialog(this, "Xuất Excel thành công!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + ex.getMessage());
        }
    }

}