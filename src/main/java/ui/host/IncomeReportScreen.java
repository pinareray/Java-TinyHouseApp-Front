package ui.host;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class IncomeReportScreen extends JFrame {
    private JButton backButton;

    public IncomeReportScreen() {
        setTitle("Gelir Raporu");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Gelir İstatistikleri", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Sahte gelir verisi
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(3000, "Gelir", "Ocak");
        dataset.addValue(4200, "Gelir", "Şubat");
        dataset.addValue(2500, "Gelir", "Mart");
        dataset.addValue(3700, "Gelir", "Nisan");

        JFreeChart barChart = ChartFactory.createBarChart(
                "Aylık Gelir Dağılımı",
                "Ay",
                "Gelir (TL)",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(barChart);
        panel.add(chartPanel, BorderLayout.CENTER);

        backButton = new JButton("← Ana Panele Dön");
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HostDashboard().setVisible(true);
        });
    }
}

