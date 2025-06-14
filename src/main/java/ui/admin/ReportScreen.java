package ui.admin;

import business.services.adminService.AdminService;
import business.services.adminService.IAdminService;
import core.utilities.results.DataResult;
import entites.dtos.SystemStatisticsDto;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

public class ReportScreen extends JFrame {
    private JButton backButton;
    private final IAdminService adminService = new AdminService();

    public ReportScreen() {
        setTitle("İstatistik ve Raporlar");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadStatistics();
    }

    private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private JPanel panel;

    private void initUI() {
        panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Sistem İstatistikleri", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        backButton = new JButton("← Admin Paneline Dön");
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new AdminDashboard().setVisible(true);
        });
    }

    private void loadStatistics() {
        adminService.getSystemStatistics().thenAccept(result -> {
            if (result.isSuccess()) {
                SystemStatisticsDto stats = result.getData();
                SwingUtilities.invokeLater(() -> {
                    populateChart(stats);
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "İstatistikler yüklenemedi: " + result.getMessage());
                });
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Sunucu hatası: " + ex.getMessage());
            });
            return null;
        });
    }

    private void populateChart(SystemStatisticsDto stats) {
        dataset.clear();

        // Kullanıcı rolleri ve sayıları
        Map<String, Integer> userCounts = stats.getUserCounts();
        if (userCounts != null) {
            userCounts.forEach((role, count) -> dataset.addValue(count, "Kullanıcılar", role));
        }

        // Aylık rezervasyonlar
        Map<String, Integer> monthlyReservations = stats.getMonthlyReservations();
        if (monthlyReservations != null) {
            monthlyReservations.forEach((month, count) -> dataset.addValue(count, "Rezervasyonlar", month));
        }

        // Aylık gelirler
        Map<String, Double> monthlyIncome = stats.getMonthlyIncome();
        if (monthlyIncome != null) {
            monthlyIncome.forEach((month, income) -> dataset.addValue(income, "Gelir (₺)", month));
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Sistem Kullanım Grafiği",
                "Kategori / Ay",
                "Değer",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }
}
