package org.example.Views.Pet;

import com.toedter.calendar.JDateChooser;
import org.example.Controllers.PetController;
import org.example.Controllers.RecordLogController;
import org.example.DTO.ComboItem;
import org.example.Models.Pet;
import org.example.Models.PetMedicalRecord;
import org.example.Models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordLogView extends JFrame {
    private JTextField txtRecordId, txtPetId, txtDescription, txtVetName, txtSearch;
    private JComboBox<String> cbType, filterCombo;
    private JDateChooser dateChooser, nextAppointmentChooser;
    private JTable recordTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete;
    private RecordLogController recordLogController;
    private PetController petController;
    private Map<Integer, String> petNameMap = new HashMap<>();

    public RecordLogView() {
        setTitle("Quản lý hồ sơ thú cưng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1050, 700);
        setLocationRelativeTo(null);
        recordLogController = new RecordLogController();
        petController = new PetController();
        initUI();
        loadTable();
        loadMaps();
        addListeners();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        gbc.gridy = y++;
        addLabeledField(mainPanel, gbc, "Mã hồ sơ:", txtRecordId = new JTextField(20), 0);
        txtRecordId.setEditable(false);
        txtRecordId.setBackground(new Color(230, 230, 230));
        addLabeledField(mainPanel, gbc, "Mã thú cưng:", txtPetId = new JTextField(20), 2);

        gbc.gridy = y++;
        String[] types = {"Khám bệnh", "Tiêm ngừa", "Phẫu thuật", "Xét nghiệm", "Tư vấn", "Khác"};
        cbType = new JComboBox<>(types);
        addLabeledField(mainPanel, gbc, "Loại:", cbType, 0);
        addLabeledField(mainPanel, gbc, "Bác sĩ:", txtVetName = new JTextField(20), 2);

        gbc.gridy = y++;
        addLabeledField(mainPanel, gbc, "Mô tả:", txtDescription = new JTextField(20), 0);
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        addLabeledField(mainPanel, gbc, "Ngày khám:", dateChooser, 2);

        gbc.gridy = y++;
        nextAppointmentChooser = new JDateChooser();
        nextAppointmentChooser.setDateFormatString("dd/MM/yyyy");
        addLabeledField(mainPanel, gbc, "Hẹn tái khám:", nextAppointmentChooser, 0);

        gbc.gridy = y++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        String[] columnNames = {"Mã hồ sơ", "Mã thú cưng", "Tên thú cưng", "Loại", "Mô tả", "Bác sĩ", "Ngày khám", "Tái khám"};
        tableModel = new DefaultTableModel(columnNames, 0);
        recordTable = new JTable(tableModel);
        recordTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(recordTable);
        mainPanel.add(scrollPane, gbc);

        gbc.gridy = y++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    private void addLabeledField(JPanel panel, GridBagConstraints gbc, String label, Component field, int gridx) {
        gbc.gridx = gridx;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = gridx + 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        panel.add(field, gbc);
        gbc.weightx = 0;
    }

    private void loadMaps() {
        for (Pet pet : petController.getAllPets()) {
            petNameMap.put(pet.getPetId(), pet.getName());
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<PetMedicalRecord> list = recordLogController.getRecordLogService();
        PetController petController = new PetController();

        for (PetMedicalRecord r : list) {
            String petName = petController.getPetNameById(r.getPetId());
            tableModel.addRow(new Object[]{
                    r.getRecordId(),
                    r.getPetId(),
                    petName,
                    r.getType(),
                    r.getDescription(),
                    r.getVetName(),
                    sdf.format(r.getDate()),
                    (r.getNextAppointment() != null ? sdf.format(r.getNextAppointment()) : "")
            });
        }
    }

    private void addListeners() {
        btnAdd.addActionListener(e -> showAddRecordDialog());

        btnEdit.addActionListener(e -> {
            try {
                PetMedicalRecord record = getFormData(true);
                int cf = JOptionPane.showConfirmDialog(this,
                        "Xác nhận sửa thông tin pet",
                        "Xác nhận",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (cf == JOptionPane.OK_OPTION) {
                    boolean rs = recordLogController.updateRecordLogService(record);
                    if (rs) {
                        if (recordLogController.updateRecordLogService(record)) {
                            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                            loadTable();
                        } else {
                            JOptionPane.showMessageDialog(this, "Cập nhật thất bại.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Xảy ra lỗi ! Vui lòng thử lại !");
                    }
                } else return;

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi !");
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtRecordId.getText());

                PetMedicalRecord record = new PetMedicalRecord();
                record.setRecordId(id);
                if (recordLogController.deleteRecordLogService(record)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi !");
            }
        });

        recordTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = recordTable.getSelectedRow();
                if (row >= 0) {
                    txtRecordId.setText(recordTable.getValueAt(row, 0).toString());
                    txtPetId.setText(recordTable.getValueAt(row, 1).toString());
                    cbType.setSelectedItem(recordTable.getValueAt(row, 3).toString());
                    txtDescription.setText(recordTable.getValueAt(row, 4).toString());
                    txtVetName.setText(recordTable.getValueAt(row, 5).toString());
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        String dateStr = recordTable.getValueAt(row, 6).toString();
                        String nextStr = recordTable.getValueAt(row, 7).toString();

                        if (!dateStr.isEmpty()) {
                            Date date = sdf.parse(dateStr);
                            dateChooser.setDate(date);
                        } else {
                            dateChooser.setDate(null);
                        }

                        if (!nextStr.isEmpty()) {
                            Date next = sdf.parse(nextStr);
                            nextAppointmentChooser.setDate(next);
                        } else {
                            nextAppointmentChooser.setDate(null);
                        }

                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private PetMedicalRecord getFormData(boolean includeId) throws Exception {
        int recordId = includeId ? Integer.parseInt(txtRecordId.getText()) : 0;
        int petId = Integer.parseInt(txtPetId.getText());
        String type = cbType.getSelectedItem().toString();
        String desc = txtDescription.getText();
        String vet = txtVetName.getText();
        Date date = dateChooser.getDate();
        Date next = nextAppointmentChooser.getDate();

        if (date == null) {
            throw new Exception("Ngày khám không được để trống.");
        }

        return new PetMedicalRecord(recordId, petId, type, desc, vet, date, next);
    }

    private void showAddRecordDialog() {
        JComboBox<ComboItem> petCombo = new JComboBox<>();
        JTextField petSearch = new JTextField(10);
        JButton petSearchBtn = new JButton("Lọc thú");

        JComboBox<String> cbType = new JComboBox<>(new String[]{"Khám bệnh", "Tiêm ngừa", "Phẫu thuật", "Xét nghiệm", "Tư vấn", "Khác"});
        JTextField vetField = new JTextField(15);
        JTextField descField = new JTextField(15);
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        JDateChooser nextChooser = new JDateChooser();
        nextChooser.setDateFormatString("dd/MM/yyyy");

        petCombo.removeAllItems();
        petNameMap.forEach((id, name) -> petCombo.addItem(new ComboItem(id, name)));

        petSearchBtn.addActionListener(e -> {
            String keyword = petSearch.getText().trim().toLowerCase();
            petCombo.removeAllItems();
            petNameMap.forEach((id, name) -> {
                if (name.toLowerCase().contains(keyword)) {
                    petCombo.addItem(new ComboItem(id, name));
                }
            });
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        int y = 0;

        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("Tìm thú cưng:"), gbc);
        gbc.gridx = 1;
        panel.add(petSearch, gbc);
        gbc.gridx = 2;
        panel.add(petSearchBtn, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("Thú cưng:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(petCombo, gbc);
        gbc.gridwidth = 1;
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("Loại hồ sơ:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(cbType, gbc);
        gbc.gridwidth = 1;
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("Bác sĩ:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(vetField, gbc);
        gbc.gridwidth = 1;
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(descField, gbc);
        gbc.gridwidth = 1;
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("Ngày khám:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(dateChooser, gbc);
        gbc.gridwidth = 1;
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("Tái khám:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(nextChooser, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Hồ Sơ Khám Bệnh", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int petId = ((ComboItem) petCombo.getSelectedItem()).getId();
                String type = (String) cbType.getSelectedItem();
                String vet = vetField.getText().trim();
                String desc = descField.getText().trim();
                Date date = dateChooser.getDate();
                Date next = nextChooser.getDate();

                if (vet.isEmpty() || date == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
                    return;
                }

                PetMedicalRecord record = new PetMedicalRecord(0, petId, type, desc, vet, date, next);
                if (recordLogController.addRecordLogService(record)) {
                    JOptionPane.showMessageDialog(this, "Thêm hồ sơ thành công!");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

}
