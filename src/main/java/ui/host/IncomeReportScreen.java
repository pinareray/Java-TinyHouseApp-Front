package ui.host;

import business.services.paymentService.IPaymentService;
import business.services.paymentService.PaymentService;
import core.session.UserSession;
import entites.dtos.MonthlyIncomeDto;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Month;
import java.util.List;

public class IncomeReportScreen extends JFrame {
    private JButton backButton;
    private final IPaymentService paymentService = new PaymentService();

    public IncomeReportScreen() {
        setTitle("Gelir Raporu");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Gelir İstatistikleri", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        backButton = new JButton("← Ana Panele Dön");
        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HostDashboard().setVisible(true);
        });
        add(backButton, BorderLayout.SOUTH);
    }

    private void loadData() {
        paymentService.getMonthlyIncomeByHostId(UserSession.currentUser.getId())
                .thenAccept(response -> {
                    if (response.isSuccess()) {
                        SwingUtilities.invokeLater(() -> drawChart(response.getData()));
                    } else {
                        JOptionPane.showMessageDialog(null, "Gelir verileri yüklenemedi: " + response.getMessage());
                    }
                });
    }

    private void drawChart(List<MonthlyIncomeDto> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (MonthlyIncomeDto dto : data) {
            String monthName = convertMonth(dto.getMonth());
            dataset.addValue(dto.getTotalIncome(), "Gelir", monthName);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Aylık Gelir Dağılımı",
                "Ay",
                "Gelir (TL)",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(barChart);
        add(chartPanel, BorderLayout.CENTER);
        revalidate(); // arayüzü güncelle
    }

    private String convertMonth(String monthString) {
        try {
            int monthNumber = Integer.parseInt(monthString.split("-")[1]);
            Month month = Month.of(monthNumber);
            return switch (month) {
                case JANUARY -> "Ocak";
                case FEBRUARY -> "Şubat";
                case MARCH -> "Mart";
                case APRIL -> "Nisan";
                case MAY -> "Mayıs";
                case JUNE -> "Haziran";
                case JULY -> "Temmuz";
                case AUGUST -> "Ağustos";
                case SEPTEMBER -> "Eylül";
                case OCTOBER -> "Ekim";
                case NOVEMBER -> "Kasım";
                case DECEMBER -> "Aralık";
            };
        } catch (Exception e) {
            return monthString; // hatalıysa orijinali göster
        }
    }
}
