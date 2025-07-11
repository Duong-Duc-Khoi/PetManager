package org.example.Views.Pet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.Controllers.PetLogController;
import org.example.DTO.ComboItem;
import org.example.Models.PetLog;
import org.example.Models.Pet;
import org.example.Models.User;
import org.example.Controllers.PetController;
import org.example.Controllers.UserController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetLogView extends JFrame {
    private JTable logTable;
    private DefaultTableModel tableModel;
    private PetLogController petLogController;
    private PetController petController;
    private UserController userController;
    private JComboBox<String> statusFilterCombo;
    private JTextField petIdFilterField, userIdFilterField;
    private JButton filterButton, addButton, updateButton, deleteButton;
    private Map<Integer, String> petNameMap = new HashMap<>();
    private Map<Integer, String> userNameMap = new HashMap<>();
    private JButton exportExcelButton;

    public PetLogView() {
        petLogController = new PetLogController();
        petController = new PetController();
        userController = new UserController();
        initUI();
        loadMaps();
        loadLogs();
    }

    private void initUI() {
        setUIFont(new Font("Segoe UI", Font.PLAIN, 14));
        setTitle("Quản lý Nhật ký Thú cưng");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Pet ID:"));
        petIdFilterField = new JTextField(5);
        filterPanel.add(petIdFilterField);

        filterPanel.add(new JLabel("User ID:"));
        userIdFilterField = new JTextField(5);
        filterPanel.add(userIdFilterField);

        filterPanel.add(new JLabel("Trạng thái:"));
        statusFilterCombo = new JComboBox<>(new String[]{
                "Tất cả",
                "Healthy",
                "Sick",
                "Under Treatment",
                "Vaccinated",
                "Isolated",
                "Recovered",
                "Deceased"
        });
        filterPanel.add(statusFilterCombo);

        filterButton = new JButton("Lọc");
        filterPanel.add(filterButton);

        filterButton.addActionListener(e -> loadLogs());

        tableModel = new DefaultTableModel(new Object[]{
                "Log ID", "Tên Thú cưng", "Trạng thái", "Ghi chú", "Ngày tạo", "Nhân viên"
        }, 0);
        logTable = new JTable(tableModel);
        logTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(logTable);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Thêm");
        deleteButton = new JButton("Xoá");
        updateButton = new JButton("Sửa");
        exportExcelButton = new JButton("Xuất Excel");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportExcelButton);

        addButton.addActionListener(e -> handleAddLog());
        updateButton.addActionListener(e -> handleUpdateLog());
        deleteButton.addActionListener(e-> handleDeleteLog());
        exportExcelButton.addActionListener(e -> exportToExcel());

        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadMaps() {
        for (Pet pet : petController.getAllPets()) {
            petNameMap.put(pet.getPetId(), pet.getName());
        }
        for (User user : userController.getAllUsers()) {
            userNameMap.put(user.getUserId(), user.getUsername());
        }
    }

    private void loadLogs() {
        tableModel.setRowCount(0);
        String selectedStatus = (String) statusFilterCombo.getSelectedItem();
        String statusFilter = "Tất cả".equalsIgnoreCase(selectedStatus)
                ? null
                : selectedStatus;
        String petIdText = petIdFilterField.getText().trim();
        String userIdText = userIdFilterField.getText().trim();

        Integer petId = petIdText.isEmpty() ? null : Integer.parseInt(petIdText);
        Integer userId = userIdText.isEmpty() ? null : Integer.parseInt(userIdText);

        List<PetLog> logs = petLogController.filterLogs(petId, userId, statusFilter);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (PetLog log : logs) {
            String created = log.getCreatedAt() != null ? sdf.format(log.getCreatedAt()) : "";
            String petName = petNameMap.getOrDefault(log.getPetId(), "(ID: " + log.getPetId() + ")");
            String userName = log.getUserId() != null ? userNameMap.getOrDefault(log.getUserId(), "(ID: " + log.getUserId() + ")") : "";
            tableModel.addRow(new Object[]{
                    log.getLogId(),
                    petName,
                    log.getStatus(),
                    log.getNote(),
                    created,
                    userName
            });
        }
    }

    private void handleAddLog() {
        JComboBox<ComboItem> petCombo = new JComboBox<>();
        JComboBox<String> statusField = new JComboBox<>(new String[]{
                "Healthy",
                "Sick",
                "Under Treatment",
                "Vaccinated",
                "Isolated",
                "Recovered",
                "Deceased"
        });
        JTextArea noteField = new JTextArea(3, 20);
        JComboBox<ComboItem> userCombo = new JComboBox<>();
        JTextField petSearch = new JTextField(10);
        JTextField userSearch = new JTextField(10);
        JButton petSearchBtn = new JButton("Lọc thú");
        JButton userSearchBtn = new JButton("Lọc nhân viên");

        petCombo.removeAllItems();
        petNameMap.forEach((id, name) -> petCombo.addItem(new ComboItem(id, name)));
        userCombo.removeAllItems();
        userNameMap.forEach((id, name) -> userCombo.addItem(new ComboItem(id, name)));

        petSearchBtn.addActionListener(e -> {
            String keyword = petSearch.getText().trim().toLowerCase();
            petCombo.removeAllItems();
            petNameMap.forEach((id, name) -> {
                if (name.toLowerCase().contains(keyword)) {
                    petCombo.addItem(new ComboItem(id, name));
                }
            });
        });

        userSearchBtn.addActionListener(e -> {
            String keyword = userSearch.getText().trim().toLowerCase();
            userCombo.removeAllItems();
            userNameMap.forEach((id, name) -> {
                if (name.toLowerCase().contains(keyword)) {
                    userCombo.addItem(new ComboItem(id, name));
                }
            });
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Tìm Thú cưng:"), gbc);
        gbc.gridx = 1;
        panel.add(petSearch, gbc);
        gbc.gridx = 2;
        panel.add(petSearchBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Thú cưng:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(petCombo, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(statusField, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(new JScrollPane(noteField), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Tìm nhân viên:"), gbc);
        gbc.gridx = 1;
        panel.add(userSearch, gbc);
        gbc.gridx = 2;
        panel.add(userSearchBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Nhân viên:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(userCombo, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            PetLog log = new PetLog();
            log.setPetId(((ComboItem) petCombo.getSelectedItem()).getId());
            log.setStatus((String) statusField.getSelectedItem());
            log.setNote(noteField.getText());
            log.setUserId(((ComboItem) userCombo.getSelectedItem()).getId());

            petLogController.addLog(log);
            loadLogs();
        }
    }

    private void handleUpdateLog() {
        int selectedRow = logTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một log để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int row = logTable.getSelectedRow();

        if (row >= 0) {
            int logId = (int) tableModel.getValueAt(row, 0);
            String petName = (String) tableModel.getValueAt(row, 1);
            String statusStr = tableModel.getValueAt(row, 2).toString();
            String noteStr = tableModel.getValueAt(row, 3).toString();
            String userName = (String) tableModel.getValueAt(row, 5);

            JTextField petIdField = new JTextField();
            for (Map.Entry<Integer, String> entry : petNameMap.entrySet()) {
                if (entry.getValue().equals(petName)) {
                    petIdField.setText(String.valueOf(entry.getKey()));
                    break;
                }
            }

            JComboBox<String> statusField = new JComboBox<>(new String[]{"imported", "checked", "sold", "returned", "sick"});
            statusField.setSelectedItem(statusStr);
            JTextArea noteField = new JTextArea(noteStr, 3, 20);
            JTextField userIdField = new JTextField();
            for (Map.Entry<Integer, String> entry : userNameMap.entrySet()) {
                if (entry.getValue().equals(userName)) {
                    userIdField.setText(String.valueOf(entry.getKey()));
                    break;
                }
            }

            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("Pet ID:"));
            panel.add(petIdField);
            panel.add(new JLabel("Trạng thái:"));
            panel.add(statusField);
            panel.add(new JLabel("Ghi chú:"));
            panel.add(new JScrollPane(noteField));
            panel.add(new JLabel("User ID:"));
            panel.add(userIdField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Sửa", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                PetLog log = new PetLog();
                log.setLogId(logId);
                log.setPetId(Integer.parseInt(petIdField.getText()));
                log.setStatus((String) statusField.getSelectedItem());
                log.setNote(noteField.getText());
                log.setUserId(userIdField.getText().isEmpty() ? null : Integer.parseInt(userIdField.getText()));
                petLogController.updateLog(log);
                loadLogs();
            }
        }
    }

    private void handleDeleteLog() {
        int selectedRow = logTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một log để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int logId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa log này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = petLogController.deleteLog(logId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa log thành công.");
                loadLogs();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa log thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void setUIFont(Font font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().endsWith(".xlsx")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Danh sách nhà cung cấp");

                // Tạo dòng tiêu đề
                Row header = sheet.createRow(0);
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    Cell cell = header.createCell(i);
                    cell.setCellValue(tableModel.getColumnName(i));
                }

                // Ghi dữ liệu
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    Row dataRow = sheet.createRow(row + 1);
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        Cell cell = dataRow.createCell(col);
                        Object value = tableModel.getValueAt(row, col);
                        cell.setCellValue(value != null ? value.toString() : "");
                    }
                }

                // Lưu file
                FileOutputStream fileOut = new FileOutputStream(fileToSave);
                workbook.write(fileOut);
                fileOut.close();

                JOptionPane.showMessageDialog(this, "Xuất Excel thành công!");

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(fileToSave);
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể mở file tự động. Vui lòng mở thủ công.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Xuất Excel thất bại: " + ex.getMessage());
            }
        }
    }


}