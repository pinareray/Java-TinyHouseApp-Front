package ui.renter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PopularHousesScreen extends JFrame {
    private JTable popularTable;
    private JButton backButton;

    public PopularHousesScreen() {
        setTitle("Popüler Evler");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("En Yüksek Puanlı Tiny House İlanları", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Sahte popüler ev verisi
        String[] columns = {"Ev Başlığı", "Konum", "Fiyat", "Puan"};
        Object[][] data = {
                {"Göl Kenarı Tiny House", "Abant", 800.0, 5.0},
                {"Deniz Bungalovu", "Bodrum", 750.0, 4.8},
                {"Orman Evi", "Sapanca", 600.0, 4.5}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        popularTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(popularTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("← Ana Panele Dön");
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new RenterDashboard().setVisible(true);
        });
    }
}

