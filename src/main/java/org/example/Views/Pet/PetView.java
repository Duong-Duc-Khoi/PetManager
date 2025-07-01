package org.example.Views.Pet;

import org.example.Controllers.PetController;
import org.example.Controllers.SupplierController;
import org.example.DTO.SupplierItems;
import org.example.Models.Pet;
import org.example.Models.Session;
import org.example.Models.Supplier;
import org.example.Utils.ViewManager;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PetView extends JFrame {
    private JTextField idField, nameField, breedField, ageField, priceField, searchField;
    private JTable petTable;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> speciesComboBox;
    private JComboBox<String> statusComboBox;
    private JComboBox<SupplierItems> supplierComboBox;
    private JComboBox<String> filterSpeciesComboBox;
    private JComboBox<SupplierItems> filterSupplierComboBox;
    private DefaultTableModel tableModel;
    private PetController petController;
    private SupplierController supplierController;
    private Map<Integer, String> supplierNameMap = new HashMap<>();
    private Map<Integer, Integer> petIdToSupplierIdMap = new HashMap<>();
    private List<Pet> allPets = new ArrayList<>();

    public PetView() {
        this.petController = new PetController();
        this.supplierController = new SupplierController();
        initUI();
        loadSuppliers();
        loadData();
    }

    private void initUI() {
        setTitle("Quản lý Thú cưng");
        setSize(1100, 700);
        setLocationRelativeTo(null);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Dimension fieldSize = new Dimension(180, 30);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:") {{
            setFont(labelFont);
        }}, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        idField = new JTextField();
        idField.setEnabled(false);
        idField.setPreferredSize(fieldSize);
        idField.setFont(fieldFont);
        inputPanel.add(idField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Tên:") {{
            setFont(labelFont);
        }}, gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        nameField = new JTextField();
        nameField.setPreferredSize(fieldSize);
        nameField.setFont(fieldFont);
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Loài:") {{
            setFont(labelFont);
        }}, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        speciesComboBox = new JComboBox<>(new String[]{"Dog", "Cat"});
        speciesComboBox.setPreferredSize(fieldSize);
        speciesComboBox.setFont(fieldFont);
        inputPanel.add(speciesComboBox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Giới tính:") {{
            setFont(labelFont);
        }}, gbc);
        gbc.gridx = 3;
        gbc.gridy = 1;
        genderComboBox = new JComboBox<>(new String[]{"male", "female"});
        genderComboBox.setPreferredSize(fieldSize);
        genderComboBox.setFont(fieldFont);
        inputPanel.add(genderComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Giống:") {{
            setFont(labelFont);
        }}, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        breedField = new JTextField();
        breedField.setPreferredSize(fieldSize);
        breedField.setFont(fieldFont);
        inputPanel.add(breedField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Tuổi:") {{
            setFont(labelFont);
        }}, gbc);
        gbc.gridx = 3;
        gbc.gridy = 2;
        ageField = new JTextField();
        ageField.setPreferredSize(fieldSize);
        ageField.setFont(fieldFont);
        inputPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Giá:") {{
            setFont(labelFont);
        }}, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        priceField = new JTextField();
        priceField.setPreferredSize(fieldSize);
        priceField.setFont(fieldFont);
        inputPanel.add(priceField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Trạng thái:") {{
            setFont(labelFont);
        }}, gbc);
        gbc.gridx = 3;
        gbc.gridy = 3;
        statusComboBox = new JComboBox<>(new String[]{"available", "sold", "reserved"});
        statusComboBox.setPreferredSize(fieldSize);
        statusComboBox.setFont(fieldFont);
        inputPanel.add(statusComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Nhà cung cấp:") {{
            setFont(labelFont);
        }}, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        supplierComboBox = new JComboBox<>();
        supplierComboBox.setPreferredSize(new Dimension(400, 30));
        supplierComboBox.setFont(fieldFont);
        inputPanel.add(supplierComboBox, gbc);
        gbc.gridwidth = 1;

        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel filterPanel = new JPanel();
        filterPanel.setFont(labelFont);
        filterPanel.add(new JLabel("Lọc theo Loài:") {{
            setFont(labelFont);
        }});
        filterSpeciesComboBox = new JComboBox<>(new String[]{"All", "Dog", "Cat"});
        filterSpeciesComboBox.setPreferredSize(new Dimension(120, 30));
        filterSpeciesComboBox.setFont(fieldFont);
        filterPanel.add(filterSpeciesComboBox);

        filterPanel.add(new JLabel("Nhà cung cấp:") {{
            setFont(labelFont);
        }});
        filterSupplierComboBox = new JComboBox<>();
        filterSupplierComboBox.setPreferredSize(new Dimension(180, 30));
        filterSupplierComboBox.setFont(fieldFont);
        filterPanel.add(filterSupplierComboBox);

        filterPanel.add(new JLabel("Tìm kiếm tên:") {{
            setFont(labelFont);
        }});
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setFont(fieldFont);
        filterPanel.add(searchField);

        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setFont(labelFont);
        searchButton.setPreferredSize(new Dimension(100, 30));
        filterPanel.add(searchButton);

        centerPanel.add(filterPanel, BorderLayout.NORTH);
        String[] columnNames = {"ID", "Tên", "Loài", "Giống", "Tuổi", "Giới tính", "Giá", "Trạng thái", "Nhà cung cấp"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        petTable = new JTable(tableModel);
        petTable.setRowHeight(28);
        petTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane tableScrollPane = new JScrollPane(petTable);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Thêm");
        JButton updateButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton clearButton = new JButton("Clear");
        JButton showPetLog = new JButton("Trạng thái Pet");
        for (JButton btn : new JButton[]{addButton, updateButton, deleteButton, clearButton, showPetLog}) {
            btn.setFont(labelFont);
            btn.setPreferredSize(new Dimension(150, 35));
            buttonPanel.add(btn);
        }

        add(inputPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        petTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = petTable.getSelectedRow();
                if (row >= 0) {
                    int petId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    idField.setText(String.valueOf(petId));
                    nameField.setText(tableModel.getValueAt(row, 1).toString());
                    speciesComboBox.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    breedField.setText(tableModel.getValueAt(row, 3).toString());
                    ageField.setText(tableModel.getValueAt(row, 4).toString());
                    genderComboBox.setSelectedItem(tableModel.getValueAt(row, 5).toString());
                    priceField.setText(tableModel.getValueAt(row, 6).toString());
                    statusComboBox.setSelectedItem(tableModel.getValueAt(row, 7).toString());

                    int supplierId = petIdToSupplierIdMap.getOrDefault(petId, -1);
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

        addButton.addActionListener(e -> {
            Pet pet = getPetFromForm();
            if (pet != null) {
                int cf = JOptionPane.showConfirmDialog(this,
                        "Xác nhận thêm pet mới",
                        "Xác nhận",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (cf == JOptionPane.OK_OPTION) {
                    boolean rs = petController.addPet(pet);
                    if (rs) {
                        JOptionPane.showMessageDialog(this, "Thêm thành công !");
                        loadData();
                        clearForm();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xảy ra lỗi ! Vui lòng thử lại !");
                    }
                } else return;
            }
        });

        updateButton.addActionListener(e -> {
            Pet pet = getPetFromForm();
            if (pet != null && !idField.getText().isEmpty()) {
                pet.setPetId(Integer.parseInt(idField.getText()));
                int cf = JOptionPane.showConfirmDialog(this,
                        "Xác nhận sửa thông tin pet",
                        "Xác nhận",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (cf == JOptionPane.OK_OPTION) {
                    boolean rs = petController.updatePet(pet);
                    if (rs) {
                        JOptionPane.showMessageDialog(this, "Sửa thành công !");
                        loadData();
                        clearForm();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xảy ra lỗi ! Vui lòng thử lại !");
                    }
                } else return;
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = petTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn pet để xoá.");
                return;
            }
            JPasswordField passwordField = new JPasswordField();
            passwordField.setEchoChar('*');

            int passConfirm = JOptionPane.showConfirmDialog(
                    this,
                    passwordField,
                    "Nhập mật khẩu để xác nhận xoá",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (passConfirm == JOptionPane.OK_OPTION) {
                String enteredPassword = new String(passwordField.getPassword());
                String passHased = Session.getInstance().getUser().getPassword();
                if (BCrypt.checkpw(enteredPassword, passHased)) {
                    int petId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    boolean rs = petController.deletePet(petId);
                    if (rs) {
                        JOptionPane.showMessageDialog(this, "Xoá thành công !");
                        loadData();
                        clearForm();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xảy ra lỗi ! Vui lòng thử lại !");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Sai mật khẩu! Vui lòng thử lại !");
                }
            }
        });

        clearButton.addActionListener(e -> {
            clearForm();
        });

        showPetLog.addActionListener(e -> {
            ViewManager.showPetLog();
        });

        searchButton.addActionListener(e -> filterData());
    }

    private Pet getPetFromForm() {
        try {
            String name = nameField.getText();
            String species = (String) speciesComboBox.getSelectedItem();
            String gender = (String) genderComboBox.getSelectedItem();
            String breed = breedField.getText();
            int age = Integer.parseInt(ageField.getText());
            BigDecimal price = new BigDecimal(priceField.getText());
            String status = (String) statusComboBox.getSelectedItem();
            SupplierItems selectedSupplier = (SupplierItems) supplierComboBox.getSelectedItem();
            int supplierId = selectedSupplier != null ? selectedSupplier.getId() : -1;
            return new Pet(0, name, species, breed, age, gender, price, status, supplierId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra lại dữ liệu nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        petIdToSupplierIdMap.clear();
        allPets = petController.getAllPets();
        for (Pet pet : allPets) {
            int petId = pet.getPetId();
            int supplierId = pet.getSupplierId();
            petIdToSupplierIdMap.put(petId, supplierId);
            String supplierName = supplierNameMap.getOrDefault(supplierId, "N/A");
            tableModel.addRow(new Object[]{
                    petId, pet.getName(), pet.getSpecies(), pet.getBreed(),
                    pet.getAge(), pet.getGender(), pet.getPrice(), pet.getStatus(), supplierName
            });
        }
    }

    private void loadSuppliers() {
        supplierNameMap.clear();
        List<Supplier> suppliers = supplierController.getAllSuppliers();
        supplierComboBox.removeAllItems();
        filterSupplierComboBox.removeAllItems();
        filterSupplierComboBox.addItem(new SupplierItems(-1, "All"));
        for (Supplier s : suppliers) {
            SupplierItems item = new SupplierItems(s.getSupplierId(), s.getName());
            supplierComboBox.addItem(item);
            filterSupplierComboBox.addItem(item);
            supplierNameMap.put(s.getSupplierId(), s.getName());
        }
    }

    private void filterData() {
        String selectedSpecies = (String) filterSpeciesComboBox.getSelectedItem();
        SupplierItems selectedSupplier = (SupplierItems) filterSupplierComboBox.getSelectedItem();
        String searchKeyword = searchField.getText().toLowerCase();

        tableModel.setRowCount(0);

        for (Pet pet : allPets) {
            boolean matchSpecies = selectedSpecies.equals("All") || pet.getSpecies().equalsIgnoreCase(selectedSpecies);
            boolean matchSupplier = selectedSupplier == null || selectedSupplier.getId() == -1 || pet.getSupplierId() == selectedSupplier.getId();
            boolean matchName = pet.getName().toLowerCase().contains(searchKeyword);

            if (matchSpecies && matchSupplier && matchName) {
                String supplierName = supplierNameMap.getOrDefault(pet.getSupplierId(), "N/A");
                tableModel.addRow(new Object[]{
                        pet.getPetId(), pet.getName(), pet.getSpecies(), pet.getBreed(),
                        pet.getAge(), pet.getGender(), pet.getPrice(), pet.getStatus(), supplierName
                });
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        speciesComboBox.setSelectedIndex(0);
        genderComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        supplierComboBox.setSelectedIndex(0);
        breedField.setText("");
        ageField.setText("");
        priceField.setText("");
        loadData();
        loadSuppliers();
        tableModel.fireTableDataChanged();
    }
}
