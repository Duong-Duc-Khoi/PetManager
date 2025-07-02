package org.example.Views.Pet;

import org.example.Controllers.PetCategoryController;
import org.example.Models.PetCategory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PetCategoryView extends JFrame {
    private PetCategoryController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtName, txtDescription;
    private JButton btnAdd, btnUpdate, btnDelete, btnExportExcel;
    private int selectedId = -1;

    public PetCategoryView() {
        controller = new PetCategoryController();
        setTitle("Pet Category Management");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        loadTable();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        txtName = new JTextField();
        txtDescription = new JTextField();
        formPanel.add(new JLabel("Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(txtDescription);
        panel.add(formPanel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel();
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnExportExcel = new JButton("Export Excel");
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnExportExcel);
        panel.add(btnPanel, BorderLayout.SOUTH);

        add(panel);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCategory();
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCategory();
            }
        });
        btnExportExcel.addActionListener(e -> exportToExcel());
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedId = (int) tableModel.getValueAt(row, 0);
                txtName.setText((String) tableModel.getValueAt(row, 1));
                txtDescription.setText((String) tableModel.getValueAt(row, 2));
            }
        });
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<PetCategory> categories = controller.getAllCategories();
        for (PetCategory c : categories) {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getDescription()});
        }
    }

    private void addCategory() {
        String name = txtName.getText().trim();
        String desc = txtDescription.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required");
            return;
        }
        PetCategory c = new PetCategory(0, name, desc);
        if (controller.addCategory(c)) {
            loadTable();
            txtName.setText("");
            txtDescription.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Add failed");
        }
    }

    private void updateCategory() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Select a category to update");
            return;
        }
        String name = txtName.getText().trim();
        String desc = txtDescription.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required");
            return;
        }
        PetCategory c = new PetCategory(selectedId, name, desc);
        if (controller.updateCategory(c)) {
            loadTable();
            txtName.setText("");
            txtDescription.setText("");
            selectedId = -1;
        } else {
            JOptionPane.showMessageDialog(this, "Update failed");
        }
    }

    private void deleteCategory() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Select a category to delete");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteCategory(selectedId)) {
                loadTable();
                txtName.setText("");
                txtDescription.setText("");
                selectedId = -1;
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed");
            }
        }
    }

    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }
            boolean result = controller.exportCategoriesToExcel(filePath);
            if (result) {
                JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
