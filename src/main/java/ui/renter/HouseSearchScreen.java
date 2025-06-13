package ui.renter;

import ui.renter.RenterDashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HouseSearchScreen extends JFrame {
    private JTable houseTable;
    private JButton detailButton, backButton;

    public HouseSearchScreen() {
        setTitle("Ev Ara / Listele");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Aktif Tiny House İlanları", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Sahte veri (ileride veritabanı bağlayacağız)
        String[] columns = {"İlan ID", "Başlık", "Fiyat", "Konum", "Puan"};
        Object[][] data = {
                {1, "Orman Evi", 600.0, "Sapanca", 4.5},
                {2, "Deniz Bungalovu", 750.0, "Bodrum", 4.8},
                {3, "Dağ Evi", 550.0, "Uludağ", 4.2},
                {4, "Göl Kenarı Tiny House", 800.0, "Abant", 5.0}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        houseTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(houseTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        detailButton = new JButton("🔍 Detayları Gör");
        backButton = new JButton("← Ana Panele Dön");

        buttonPanel.add(detailButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Buton İşlevleri
        detailButton.addActionListener((ActionEvent e) -> {
            int selectedRow = houseTable.getSelectedRow();
            if (selectedRow != -1) {
                dispose();
                new HouseDetailScreen().setVisible(true); // Sonraki adımda yapacağız
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen detayını görmek için bir ev seçin!");
            }
        });

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new RenterDashboard().setVisible(true);
        });
    }
}

