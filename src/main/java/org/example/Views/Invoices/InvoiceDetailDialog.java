package org.example.Views.Invoices;

import org.example.Controllers.InvoiceController;
import org.example.Models.Invoice;
import org.example.Models.InvoiceDetail;
import org.example.Utils.UIManagers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class InvoiceDetailDialog extends JDialog {
    private final InvoiceController controller = new InvoiceController();

    public InvoiceDetailDialog(Frame owner, int invoiceId) {
        super(owner, "Chi tiết hóa đơn - Mã HĐ: " + invoiceId, true);
        UIManagers.applyGlobalFont();
        setSize(600, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        Invoice invoice = controller.getInvoiceById(invoiceId);
        if (invoice == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 13);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(invoice.getCreatedAt());

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        JLabel lblName = new JLabel("Khách hàng:");
        lblName.setFont(labelFont);
        infoPanel.add(lblName, gbc);

        gbc.gridx = 1;
        JLabel valName = new JLabel(invoice.getCustomerName());
        valName.setFont(valueFont);
        infoPanel.add(valName, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        JLabel lblPhone = new JLabel("SĐT:");
        lblPhone.setFont(labelFont);
        infoPanel.add(lblPhone, gbc);

        gbc.gridx = 1;
        JLabel valPhone = new JLabel(invoice.getCustomerPhone());
        valPhone.setFont(valueFont);
        infoPanel.add(valPhone, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        JLabel lblDate = new JLabel("Ngày tạo:");
        lblDate.setFont(labelFont);
        infoPanel.add(lblDate, gbc);

        gbc.gridx = 1;
        JLabel valDate = new JLabel(formattedDate);
        valDate.setFont(valueFont);
        infoPanel.add(valDate, gbc);

        add(infoPanel, BorderLayout.NORTH);

        DefaultTableModel detailModel = new DefaultTableModel(
                new Object[]{"Mã thú cưng", "Tên", "Loài", "Giá bán"}, 0
        );
        JTable detailTable = new JTable(detailModel);
        detailTable.setRowHeight(25);
        detailTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        List<InvoiceDetail> details = controller.getDetailsByInvoiceId(invoiceId);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        for (InvoiceDetail d : details) {
            String formattedPrice = currencyFormat.format(d.getPrice());
            detailModel.addRow(new Object[]{
                    d.getPetId(),
                    d.getPetName(),
                    d.getPetSpecies(),
                    formattedPrice
            });
        }

        add(new JScrollPane(detailTable), BorderLayout.CENTER);

        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
