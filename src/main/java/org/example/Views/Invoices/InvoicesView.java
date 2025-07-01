package org.example.Views.Invoices;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.Controllers.InvoiceController;
import org.example.Models.Invoice;
import com.toedter.calendar.JDateChooser;
import org.example.Utils.UIManagers;
import org.example.Views.Invoices.InvoiceDetailDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class InvoicesView extends JFrame {
    private JTable invoiceTable;
    private DefaultTableModel invoiceModel;
    private InvoiceController controller = new InvoiceController();

    private JTextField txtSearch;
    private JDateChooser fromDateChooser, toDateChooser;

    public InvoicesView() {
        setTitle("Danh sách Hóa đơn");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadInvoices(null, null, null);

        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        txtSearch = new JTextField(20);
        fromDateChooser = new JDateChooser();
        toDateChooser = new JDateChooser();
        fromDateChooser.setDateFormatString("dd/MM/yyyy");
        toDateChooser.setDateFormatString("dd/MM/yyyy");

        JButton btnFilter = new JButton("Lọc");
        JButton btnExportExcel = new JButton("Xuất Excel");



        filterPanel.add(new JLabel("Tìm KH:"));
        filterPanel.add(txtSearch);
        filterPanel.add(new JLabel("Từ ngày:"));
        filterPanel.add(fromDateChooser);
        filterPanel.add(new JLabel("Đến ngày:"));
        filterPanel.add(toDateChooser);
        filterPanel.add(btnFilter);
        filterPanel.add(btnExportExcel);

        add(filterPanel, BorderLayout.NORTH);

        invoiceModel = new DefaultTableModel(new Object[]{"Mã HĐ", "Khách hàng", "SĐT", "Tổng tiền", "Ngày tạo"}, 0);
        invoiceTable = new JTable(invoiceModel);
        invoiceTable.setRowHeight(25);
        invoiceTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        invoiceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        add(scrollPane, BorderLayout.CENTER);

        invoiceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = invoiceTable.getSelectedRow();
                if (row != -1) {
                    int invoiceId = (int) invoiceModel.getValueAt(row, 0);
                    new InvoiceDetailDialog(InvoicesView.this, invoiceId).setVisible(true);
                }
            }
        });

        btnFilter.addActionListener(e -> {
            String keyword = txtSearch.getText().trim().toLowerCase();
            Date from = fromDateChooser.getDate();
            Date to = toDateChooser.getDate();
            loadInvoices(keyword, from, to);
        });
        btnExportExcel.addActionListener(e -> exportToExcel());


    }

    private void loadInvoices(String keyword, Date fromDate, Date toDate) {
        invoiceModel.setRowCount(0);
        List<Invoice> invoices = controller.searchInvoices(keyword, fromDate, toDate);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (Invoice inv : invoices) {
            invoiceModel.addRow(new Object[]{
                    inv.getInvoiceId(),
                    inv.getCustomerName(),
                    inv.getCustomerPhone(),
                    currencyFormat.format(inv.getTotalAmount()),
                    inv.getCreatedAt()
            });
        }
    }

    private void exportToExcel() {
        try (Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("HoaDon");

            // Ghi header
            Row header = sheet.createRow(0);
            for (int i = 0; i < invoiceModel.getColumnCount(); i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(invoiceModel.getColumnName(i));
            }

            // Ghi dữ liệu
            for (int i = 0; i < invoiceModel.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < invoiceModel.getColumnCount(); j++) {
                    Object value = invoiceModel.getValueAt(i, j);
                    row.createCell(j).setCellValue(value != null ? value.toString() : "");
                }
            }

            // Tự động resize cột
            for (int i = 0; i < invoiceModel.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Tạo file Excel tạm thời
            File tempFile = File.createTempFile("HoaDon_", ".xlsx");
            tempFile.deleteOnExit(); // Tự xóa khi đóng app

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                workbook.write(out);
            }

            // Mở file Excel
            Desktop.getDesktop().open(tempFile);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể mở Excel: " + ex.getMessage());
        }
    }

}

