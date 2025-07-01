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
import java.util.Map;

public class StatisticsView extends JFrame {
    private ChartPanel chartPanel;
    private StatisticsController stats = new StatisticsController();


    private JLabel lblTotalInvoices, lblTotalRevenue, lblSoldPets, lblAvailablePets;

    public StatisticsView() {
        setTitle("Thống kê tổng quan");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadStatistics();

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

        chartPanel = new ChartPanel(createMonthlyRevenueChart());
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
        Map<String, Double> revenueMap = stats.getMonthlyRevenue();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Double> entry : revenueMap.entrySet()) {
            dataset.addValue(entry.getValue(), "Doanh thu", entry.getKey());
        }

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

    private void loadStatistics() {
        lblTotalInvoices.setText("<html><center>Tổng số hóa đơn<br><h2>" + stats.getTotalInvoices() + "</h2></center></html>");
        lblTotalRevenue.setText("<html><center>Tổng doanh thu<br><h2>" + formatCurrency(stats.getTotalRevenue()) + "</h2></center></html>");
        lblSoldPets.setText("<html><center>Thú đã bán<br><h2>" + stats.getSoldPets() + "</h2></center></html>");
        lblAvailablePets.setText("<html><center>Thú còn hàng<br><h2>" + stats.getAvailablePets() + "</h2></center></html>");
    }
    public void reload() {
        loadStatistics();
        chartPanel.setChart(createMonthlyRevenueChart());
    }



}
