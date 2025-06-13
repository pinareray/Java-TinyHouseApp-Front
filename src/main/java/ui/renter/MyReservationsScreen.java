package ui.renter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MyReservationsScreen extends JFrame {
    private JTable reservationsTable;
    private JButton cancelButton, backButton;

    public MyReservationsScreen() {
        setTitle("Rezervasyonlarım");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Geçmiş ve Aktif Rezervasyonlarım", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Sahte veri (ileride veritabanı bağlayacağız)
        String[] columns = {"Rezervasyon ID", "Ev", "Başlangıç", "Bitiş", "Ödeme Durumu", "Rezervasyon Durumu"};
        Object[][] data = {
                {201, "Orman Evi", "2025-05-10", "2025-05-15", "Ödendi", "Aktif"},
                {202, "Dağ Evi", "2025-04-01", "2025-04-03", "Ödendi", "Tamamlandı"},
                {203, "Göl Tiny House", "2025-06-20", "2025-06-25", "Bekliyor", "Beklemede"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        reservationsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        cancelButton = new JButton("❌ Rezervasyonu İptal Et");
        backButton = new JButton("← Ana Panele Dön");

        buttonPanel.add(cancelButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Buton İşlevleri
        cancelButton.addActionListener((ActionEvent e) -> {
            int selectedRow = reservationsTable.getSelectedRow();
            if (selectedRow != -1) {
                reservationsTable.setValueAt("İptal Edildi", selectedRow, 5);
                JOptionPane.showMessageDialog(null, "Rezervasyon iptal edildi!");
            } else {
                JOptionPane.showMessageDialog(null, "İptal etmek için bir rezervasyon seçin!");
            }
        });

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new RenterDashboard().setVisible(true);
        });
    }
}

