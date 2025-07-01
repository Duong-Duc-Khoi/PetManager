package org.example.Views.Sale;

import com.toedter.calendar.JDateChooser;
import org.example.Controllers.PetController;
import org.example.Controllers.SalesController;
import org.example.DTO.ComboItem;
import org.example.Models.Invoice;
import org.example.Models.InvoiceDetail;
import org.example.Models.Pet;
import org.example.Models.Session;
import org.example.Services.SaleService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PetSaleView extends JFrame {
    private JTable petTable;
    private JTextField txtCustomerName, txtPhone, txtPrice, txtSearch;
    private JComboBox<ComboItem> petCombo;
    private JComboBox<String> speciesFilter;
    private JDateChooser saleDateChooser;
    private JButton btnAddSale, btnClear;
    private SalesController salesController = new SalesController();
    private PetController petController = new PetController();

    public PetSaleView() {
        setTitle("Bán thú cưng");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadPetTable("Tất cả", "");
        loadPetCombo("Tất cả", "");
        setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        speciesFilter = new JComboBox<>(new String[]{"Tất cả", "Dog", "Cat"});
        txtSearch = new JTextField(20);
        JButton btnFilter = new JButton("Lọc");

        searchPanel.add(new JLabel("Lọc theo loài:"));
        searchPanel.add(speciesFilter);
        searchPanel.add(new JLabel("Tìm theo tên:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnFilter);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        String[] columnNames = {"Mã", "Tên", "Loài", "Giống", "Tuổi", "Giới tính", "Giá", "Tình trạng"};
        petTable = new JTable(new DefaultTableModel(columnNames, 0));
        petTable.setRowHeight(28);
        petTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        ((DefaultTableCellRenderer) petTable.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
        petTable.setGridColor(new Color(200, 200, 200));
        petTable.setShowGrid(true);
        petTable.setIntercellSpacing(new Dimension(1, 1));
        JScrollPane tableScroll = new JScrollPane(petTable);
        mainPanel.add(tableScroll, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        int y = 0;

        JLabel shopLabel = new JLabel("~ SHOP PET 36 ~", SwingConstants.CENTER);
        shopLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        shopLabel.setForeground(new Color(0, 128, 255));
        gbc.gridx = 0;
        gbc.gridy = y++;
        gbc.gridwidth = 2;
        formPanel.add(shopLabel, gbc);
        gbc.gridwidth = 1;

        txtCustomerName = new JTextField(20);
        txtPhone = new JTextField(15);
        txtPrice = new JTextField(10);
        petCombo = new JComboBox<>();
        saleDateChooser = new JDateChooser();
        saleDateChooser.setDateFormatString("dd/MM/yyyy");
        saleDateChooser.setDate(new Date());

        addLabeledField(formPanel, gbc, "Tên khách hàng:", txtCustomerName, 0, y++);
        addLabeledField(formPanel, gbc, "Số điện thoại:", txtPhone, 0, y++);
        addLabeledField(formPanel, gbc, "Chọn thú cưng:", petCombo, 0, y++);
        addLabeledField(formPanel, gbc, "Ngày bán:", saleDateChooser, 0, y++);
        addLabeledField(formPanel, gbc, "Giá bán (VND):", txtPrice, 0, y++);

        btnAddSale = new JButton("Thanh toán");
        btnClear = new JButton("Xóa form");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.add(btnAddSale);
        btnPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = y++;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.EAST);
        add(mainPanel);

        btnAddSale.addActionListener(e -> checkout());
        btnClear.addActionListener(e -> clearForm());

        petTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = petTable.getSelectedRow();
                if (selectedRow != -1) {
                    DefaultTableModel model = (DefaultTableModel) petTable.getModel();
                    int petId = (int) model.getValueAt(selectedRow, 0);
                    BigDecimal price = new BigDecimal(model.getValueAt(selectedRow, 6).toString());

                    for (int i = 0; i < petCombo.getItemCount(); i++) {
                        ComboItem item = petCombo.getItemAt(i);
                        if (item.getId() == petId) {
                            petCombo.setSelectedIndex(i);
                            break;
                        }
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    txtPrice.setText(decimalFormat.format(price) + " VND");
                }
            }
        });

        btnFilter.addActionListener(e -> {
            String species = (String) speciesFilter.getSelectedItem();
            String keyword = txtSearch.getText().trim().toLowerCase();
            loadPetTable(species, keyword);
            loadPetCombo(species, keyword);
        });
    }

    private void addLabeledField(JPanel panel, GridBagConstraints gbc, String label, Component field, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = x + 1;
        panel.add(field, gbc);
    }

    private void loadPetTable(String speciesFilter, String keyword) {
        DefaultTableModel model = (DefaultTableModel) petTable.getModel();
        model.setRowCount(0);
        List<Pet> pets = petController.getAvailablePets();
        for (Pet p : pets) {
            boolean matchSpecies = "Tất cả".equals(speciesFilter) || p.getSpecies().equalsIgnoreCase(speciesFilter);
            boolean matchName = keyword.isEmpty() || p.getName().toLowerCase().contains(keyword);
            if (matchSpecies && matchName) {
                model.addRow(new Object[]{
                        p.getPetId(),
                        p.getName(),
                        p.getSpecies(),
                        p.getBreed(),
                        p.getAge(),
                        p.getGender(),
                        p.getPrice(),
                        p.getStatus()
                });
            }
        }
    }

    private void loadPetCombo(String speciesFilter, String keyword) {
        petCombo.removeAllItems();
        for (Pet p : petController.getAvailablePets()) {
            boolean matchSpecies = "Tất cả".equals(speciesFilter) || p.getSpecies().equalsIgnoreCase(speciesFilter);
            boolean matchName = keyword.isEmpty() || p.getName().toLowerCase().contains(keyword);
            if (matchSpecies && matchName) {
                petCombo.addItem(new ComboItem(p.getPetId(), p.getName() + " - " + p.getBreed()));
            }
        }
    }

    private void checkout() {
        try {
            String customer = txtCustomerName.getText().trim();
            String phone = txtPhone.getText().trim();
            Date saleDate = saleDateChooser.getDate();
            ComboItem selectedPet = (ComboItem) petCombo.getSelectedItem();
            String priceStr = txtPrice.getText().trim()
                    .replaceAll("[^\\d]", "");

            if (customer.isEmpty() || phone.isEmpty() || saleDate == null || selectedPet == null || priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
                return;
            }

            int petId = selectedPet.getId();
            BigDecimal price = new BigDecimal(priceStr);

            Invoice invoice = new Invoice(
                    0,
                    Session.getInstance().getUser().getUserId(),
                    customer,
                    phone,
                    price,
                    saleDate
            );

            InvoiceDetail detail = new InvoiceDetail(
                    0,
                    0,
                    petId,
                    price
            );

            List<InvoiceDetail> details = new ArrayList<>();
            details.add(detail);

            boolean result = salesController.checkout(invoice, details);

            if (result) {
                JOptionPane.showMessageDialog(this, "Bán thành công!");
                loadPetTable((String) speciesFilter.getSelectedItem(), txtSearch.getText().trim().toLowerCase());
                loadPetCombo((String) speciesFilter.getSelectedItem(), txtSearch.getText().trim().toLowerCase());
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Bán thất bại!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi! Vui lòng kiểm tra lại.");
        }
    }


    private void clearForm() {
        txtCustomerName.setText("");
        txtPhone.setText("");
        txtPrice.setText("");
        saleDateChooser.setDate(new Date());
    }
}
