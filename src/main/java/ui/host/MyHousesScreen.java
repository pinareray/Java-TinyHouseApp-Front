package ui.host;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MyHousesScreen extends JFrame {
    private JTable houseTable;
    private JButton addButton, editButton, deleteButton, backButton;

    public MyHousesScreen() {
        setTitle("İlanlarımı Yönet");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("İlanlarım", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Sahte veri
        String[] columns = {"İlan ID", "Başlık", "Fiyat", "Konum", "Durum"};
        Object[][] data = {
                {11, "Orman Evi", 650.00, "Sapanca", "Aktif"},
                {12, "Deniz Kenarı Bungalov", 800.00, "Bodrum", "Pasif"},
                {13, "Dağ Evi", 700.00, "Uludağ", "Aktif"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        houseTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(houseTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        addButton = new JButton("➕ İlan Ekle");
        editButton = new JButton("✏️ İlan Düzenle");
        deleteButton = new JButton("🗑️ İlanı Pasif Yap");
        backButton = new JButton("← Ana Panele Dön");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Buton İşlevleri
        addButton.addActionListener((ActionEvent e) -> {
            dispose();
            new AddHouseScreen().setVisible(true);
        });


        editButton.addActionListener((ActionEvent e) -> {
            int selectedRow = houseTable.getSelectedRow();
            if (selectedRow != -1) {
                dispose(); // Önce mevcut MyHousesScreen ekranını kapat
                new EditHouseScreen().setVisible(true); // Edit ekranı aç
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen düzenlemek için bir ilan seçin!");
            }
        });



        deleteButton.addActionListener((ActionEvent e) -> {
            int selectedRow = houseTable.getSelectedRow();
            if (selectedRow != -1) {
                houseTable.setValueAt("Pasif", selectedRow, 4);
                JOptionPane.showMessageDialog(null, "İlan pasif yapıldı.");
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen pasif yapmak için bir ilan seçin!");
            }
        });

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HostDashboard().setVisible(true);
        });
    }
}
