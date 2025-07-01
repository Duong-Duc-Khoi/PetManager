package org.example.Views.Statistic;

import org.example.Controllers.StatisticsController;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class StatisticsView extends JFrame {

    private JLabel lblTotalInvoices, lblTotalRevenue, lblSoldPets, lblAvailablePets;

    public StatisticsView() {
        setTitle("Thống kê tổng quan");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadMockData(); // Sau này thay bằng controller thực

        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelTop = new JPanel(new GridLayout(2, 2, 20, 20));
        lblTotalInvoices = createStatCard("Tổng số hóa đơn", "0");
        lblTotalRevenue = createStatCard("Tổng doanh thu", "0 VNĐ");
        lblSoldPets = createStatCard("Số thú đã bán", "0");
        lblAvailablePets = createStatCard("Thú còn hàng", "0");

        panelTop.add(lblTotalInvoices);
        panelTop.add(lblTotalRevenue);
        panelTop.add(lblSoldPets);
        panelTop.add(lblAvailablePets);

        add(panelTop, BorderLayout.NORTH);

        // Biểu đồ
        JPanel panelChart = new JPanel(new BorderLayout());
        panelChart.setBorder(BorderFactory.createTitledBorder("Doanh thu theo tháng"));

        JFreeChart chart = createMonthlyRevenueChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        panelChart.add(chartPanel, BorderLayout.CENTER);

        add(panelChart, BorderLayout.CENTER);
    }

    private JLabel createStatCard(String title, String value) {
        JLabel label = new JLabel("<html><center>" + title + "<br><h2>" + value + "</h2></center></html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return label;
    }

    private JFreeChart createMonthlyRevenueChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Dữ liệu mẫu (thay bằng dữ liệu thực từ DB)
        dataset.addValue(10_000_000, "Doanh thu", "01/2025");
        dataset.addValue(20_000_000, "Doanh thu", "02/2025");
        dataset.addValue(30_000_000, "Doanh thu", "03/2025");

        return ChartFactory.createBarChart(
                "Doanh thu theo tháng",
                "Tháng",
                "VNĐ",
                dataset
        );
    }
    private String formatCurrency(double value) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(value);
    }

    private void loadMockData() {
        StatisticsController stats = new StatisticsController();

        lblTotalInvoices.setText("<html><center>Tổng số hóa đơn<br><h2>" + stats.getTotalInvoices() + "</h2></center></html>");
        lblTotalRevenue.setText("<html><center>Tổng doanh thu<br><h2>" + formatCurrency(stats.getTotalRevenue()) + "</h2></center></html>");
        lblSoldPets.setText("<html><center>Thú đã bán<br><h2>" + stats.getSoldPets() + "</h2></center></html>");
        lblAvailablePets.setText("<html><center>Thú còn hàng<br><h2>" + stats.getAvailablePets() + "</h2></center></html>");

    }
}
