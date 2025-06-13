package ui.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HouseManagementScreen extends JFrame {
    private JTable houseTable;
    private JButton backButton, editHouseButton, deleteHouseButton, addHouseButton;

    public HouseManagementScreen() {
        setTitle("İlan Yönetimi");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Tüm Tiny House İlanları", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Sahte veri
        String[] columns = {"İlan ID", "Ev Sahibi ID", "Başlık", "Fiyat", "Konum", "Durum"};
        Object[][] data = {
                {1, 2, "Dağ Evi", 500.00, "Bolu", "Aktif"},
                {2, 3, "Deniz Kıyısı Evi", 750.00, "İzmir", "Pasif"},
                {3, 2, "Orman İçinde Bungalov", 650.00, "Sapanca", "Aktif"}
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columns);
        houseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(houseTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Butonlar
        JPanel buttonPanel = new JPanel();
        editHouseButton = new JButton("✏️ Düzenle");
        deleteHouseButton = new JButton("🗑️ Sil");
        addHouseButton = new JButton("➕ İlan Ekle");
        backButton = new JButton("← Admin Paneline Dön");

        buttonPanel.add(editHouseButton);
        buttonPanel.add(deleteHouseButton);
        buttonPanel.add(addHouseButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Geri dön butonu
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminDashboard().setVisible(true);
            }
        });

        // İlan düzenleme butonu
        editHouseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = houseTable.getSelectedRow();
                if (row != -1) {
                    String houseTitle = houseTable.getValueAt(row, 2).toString();
                    String price = houseTable.getValueAt(row, 3).toString();
                    String location = houseTable.getValueAt(row, 4).toString();

                    // İlan bilgilerini düzenlemek için input alıyoruz
                    String newTitle = JOptionPane.showInputDialog("Yeni İlan Başlığı", houseTitle);
                    String newPrice = JOptionPane.showInputDialog("Yeni Fiyat", price);
                    String newLocation = JOptionPane.showInputDialog("Yeni Konum", location);

                    if (newTitle != null && newPrice != null && newLocation != null) {
                        // Düzenlenen veriyi tabloya ekliyoruz
                        houseTable.setValueAt(newTitle, row, 2);
                        houseTable.setValueAt(newPrice, row, 3);
                        houseTable.setValueAt(newLocation, row, 4);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Düzenlemek için bir ilan seçin!");
                }
            }
        });

        // İlan silme butonu
        deleteHouseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = houseTable.getSelectedRow();
                if (row != -1) {
                    int confirm = JOptionPane.showConfirmDialog(null, "İlanı silmek istediğinize emin misiniz?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        DefaultTableModel model = (DefaultTableModel) houseTable.getModel();
                        model.removeRow(row); // İlanı silme
                    }
                }
            }
        });

        // İlan ekleme butonu
        addHouseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // İlan eklemek için yeni bir ekran açabiliriz
                String houseTitle = JOptionPane.showInputDialog("Yeni İlan Başlığı");
                String price = JOptionPane.showInputDialog("Fiyat");
                String location = JOptionPane.showInputDialog("Konum");

                if (houseTitle != null && price != null && location != null) {
                    // Yeni veriyi tabloya ekliyoruz
                    DefaultTableModel model = (DefaultTableModel) houseTable.getModel();
                    model.addRow(new Object[]{model.getRowCount() + 1, 1, houseTitle, Double.parseDouble(price), location, "Aktif"});
                }
            }
        });
    }
}
