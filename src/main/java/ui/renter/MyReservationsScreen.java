package ui.renter;

import business.services.reservationService.IReservationService;
import business.services.reservationService.ReservationService;
import core.session.UserSession;
import entites.dtos.ReservationListDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MyReservationsScreen extends JFrame {
    private JTable reservationsTable;
    private DefaultTableModel model;
    private JButton cancelButton, backButton;

    private final IReservationService reservationService = new ReservationService();

    public MyReservationsScreen() {
        setTitle("Rezervasyonlarım");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadReservations();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Geçmiş ve Aktif Rezervasyonlarım", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"ID", "Ev", "Başlangıç", "Bitiş", "Durum"};
        model = new DefaultTableModel(columns, 0);
        reservationsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        cancelButton = new JButton("❌ Rezervasyonu İptal Et");
        backButton = new JButton("← Ana Panele Dön");

        buttonPanel.add(cancelButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(this::cancelReservation);
        backButton.addActionListener(e -> {
            dispose();
            new RenterDashboard().setVisible(true);
        });
    }

    private void loadReservations() {
        reservationService.getByUserId(UserSession.currentUser.getId(), UserSession.currentUser.getId())
                .thenAccept(result -> {
                    if (result.isSuccess()) {
                        List<ReservationListDto> reservations = result.getData();
                        SwingUtilities.invokeLater(() -> {
                            model.setRowCount(0); // clear table
                            for (ReservationListDto r : reservations) {
                                model.addRow(new Object[]{
                                        r.getId(),
                                        r.getHouse().getTitle(),
                                        r.getStartDate(),
                                        r.getEndDate(),
                                        r.getStatus().toString()
                                });
                            }
                        });
                    } else {
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(this, "Rezervasyonlar yüklenemedi: " + result.getMessage()));
                    }
                });
    }

    private void cancelReservation(ActionEvent e) {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "İptal etmek için bir rezervasyon seçin!");
            return;
        }

        int reservationId = (int) model.getValueAt(selectedRow, 0);

        reservationService.delete(reservationId, UserSession.currentUser.getId())
                .thenAccept(result -> {
                    if (result.isSuccess()) {
                        SwingUtilities.invokeLater(() -> {
                            model.setValueAt("İptal Edildi", selectedRow, 4);
                            JOptionPane.showMessageDialog(this, "Rezervasyon iptal edildi!");
                        });
                    } else {
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(this, "İptal işlemi başarısız: " + result.getMessage()));
                    }
                });
    }
}
