package ui.admin;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ReportScreen extends JFrame {
    private JButton backButton;

    public ReportScreen() {
        setTitle("İstatistik ve Raporlar");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Sistem İstatistikleri", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Grafik için sahte veri seti
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10, "Kullanıcılar", "Admin");
        dataset.addValue(25, "Kullanıcılar", "Ev Sahibi");
        dataset.addValue(50, "Kullanıcılar", "Kiracı");

        dataset.addValue(30, "Rezervasyonlar", "Ocak");
        dataset.addValue(45, "Rezervasyonlar", "Şubat");
        dataset.addValue(60, "Rezervasyonlar", "Mart");

        JFreeChart chart = ChartFactory.createBarChart(
                "Sistem Kullanım Grafiği",
                "Kategori",
                "Sayı",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel, BorderLayout.CENTER);

        backButton = new JButton("← Admin Paneline Dön");
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new AdminDashboard().setVisible(true);
        });
    }
}

