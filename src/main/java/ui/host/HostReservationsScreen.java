package ui.host;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HostReservationsScreen extends JFrame {
    private JTable reservationTable;
    private JButton approveButton, rejectButton, backButton;

    public HostReservationsScreen() {
        setTitle("Gelen Rezervasyonlar");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Rezervasyon Talepleri", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Sahte veri
        String[] columns = {"Rez. ID", "Kiracı", "Ev Başlığı", "Başlangıç", "Bitiş", "Ödeme", "Durum"};
        Object[][] data = {
                {101, "Ayşe Yılmaz", "Orman Evi", "2025-05-01", "2025-05-05", "Ödendi", "Beklemede"},
                {102, "Mehmet Demir", "Dağ Evi", "2025-06-10", "2025-06-15", "Bekleniyor", "Beklemede"},
                {103, "Zeynep Kaya", "Orman Evi", "2025-07-01", "2025-07-05", "Ödendi", "Kabul Edildi"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        reservationTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        approveButton = new JButton("✅ Onayla");
        rejectButton = new JButton("❌ Reddet");
        backButton = new JButton("← Ana Panele Dön");

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Buton işlevleri
        approveButton.addActionListener((ActionEvent e) -> {
            int row = reservationTable.getSelectedRow();
            if (row != -1) {
                reservationTable.setValueAt("Kabul Edildi", row, 6);
                JOptionPane.showMessageDialog(null, "Rezervasyon onaylandı!");
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen bir rezervasyon seçin!");
            }
        });

        rejectButton.addActionListener((ActionEvent e) -> {
            int row = reservationTable.getSelectedRow();
            if (row != -1) {
                reservationTable.setValueAt("Reddedildi", row, 6);
                JOptionPane.showMessageDialog(null, "Rezervasyon reddedildi!");
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen bir rezervasyon seçin!");
            }
        });

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HostDashboard().setVisible(true);
        });
    }
}

