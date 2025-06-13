package ui.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReservationManagementScreen extends JFrame {
    private JTable reservationTable;
    private JButton backButton, editReservationButton, deleteReservationButton;

    public ReservationManagementScreen() {
        setTitle("Rezervasyon Yönetimi");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Tüm Rezervasyonlar", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Sahte veri
        String[] columns = {"Rez.ID", "Ev ID", "Kiracı ID", "Başlangıç", "Bitiş", "Durum", "Ödeme"};
        Object[][] data = {
                {101, 1, 2, "2025-05-01", "2025-05-03", "approved", "paid"},
                {102, 3, 4, "2025-06-10", "2025-06-15", "pending", "unpaid"},
                {103, 2, 5, "2025-07-20", "2025-07-25", "cancelled", "unpaid"}
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columns);
        reservationTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Butonlar
        JPanel buttonPanel = new JPanel();
        editReservationButton = new JButton("✏️ Düzenle");
        deleteReservationButton = new JButton("🗑️ Sil");
        backButton = new JButton("← Admin Paneline Dön");

        buttonPanel.add(editReservationButton);
        buttonPanel.add(deleteReservationButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Geri dön butonu
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminDashboard().setVisible(true);
            }
        });

        // Rezervasyon düzenleme butonu
        editReservationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = reservationTable.getSelectedRow();
                if (row != -1) {
                    String startDate = reservationTable.getValueAt(row, 3).toString();
                    String endDate = reservationTable.getValueAt(row, 4).toString();

                    // Tarihleri düzenlemek için input alıyoruz
                    String newStartDate = JOptionPane.showInputDialog("Yeni Başlangıç Tarihi", startDate);
                    String newEndDate = JOptionPane.showInputDialog("Yeni Bitiş Tarihi", endDate);

                    if (newStartDate != null && newEndDate != null) {
                        // Güncellenmiş tarihleri tabloya ekliyoruz
                        reservationTable.setValueAt(newStartDate, row, 3);
                        reservationTable.setValueAt(newEndDate, row, 4);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Düzenlemek için bir rezervasyon seçin!");
                }
            }
        });

        // Rezervasyon silme butonu
        deleteReservationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = reservationTable.getSelectedRow();
                if (row != -1) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Rezervasyonu silmek istediğinize emin misiniz?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        DefaultTableModel model = (DefaultTableModel) reservationTable.getModel();
                        model.removeRow(row); // Rezervasyonu silme
                    }
                }
            }
        });
    }
}

