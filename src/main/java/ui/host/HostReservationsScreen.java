package ui.host;

import business.services.reservationService.IReservationService;
import business.services.reservationService.ReservationService;
import core.session.UserSession;
import entites.dtos.ReservationDto;
import entites.dtos.ReservationListDto;
import entites.dtos.ReservationUpdateDto;
import entites.enums.ReservationStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HostReservationsScreen extends JFrame {
    private JTable reservationTable;
    private JButton approveButton, rejectButton, backButton;

    private final IReservationService reservationService = new ReservationService();
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Rez. ID", "Kiracı", "Ev Başlığı", "Başlangıç", "Bitiş", "Durum"}, 0);

    public HostReservationsScreen() {
        setTitle("Gelen Rezervasyonlar");
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

        JLabel titleLabel = new JLabel("Rezervasyon Talepleri", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

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

        approveButton.addActionListener(this::approveReservation);
        rejectButton.addActionListener(this::rejectReservation);
        backButton.addActionListener(e -> {
            dispose();
            new HostDashboard().setVisible(true);
        });
    }

    private void loadReservations() {
        model.setRowCount(0); // Tabloyu temizle

        reservationService.getAll(UserSession.currentUser.getId()).thenAccept(res -> {
            if (res.isSuccess()) {
                List<ReservationListDto> reservations = res.getData();
                for (ReservationListDto dto : reservations) {
                    if (dto.getHouse().getId() > 0 && dto.getHouse().getTitle() != null) {
                        model.addRow(new Object[]{
                                dto.getId(),
                                dto.getRenter().getFirstName() + " " + dto.getRenter().getLastName(),
                                dto.getHouse().getTitle(),
                                dto.getStartDate().format(DateTimeFormatter.ISO_DATE),
                                dto.getEndDate().format(DateTimeFormatter.ISO_DATE),
                                getReadableStatus(dto.getStatus())
                        });
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Rezervasyonlar yüklenemedi: " + res.getMessage());
            }
        });
    }

    private String getReadableStatus(ReservationStatus status) {
        return switch (status) {
            case APPROVED -> "Kabul Edildi";
            case PENDING -> "Beklemede";
            case CANCELLED -> "Reddedildi";
        };
    }

    private void approveReservation(ActionEvent e) {
        updateReservationStatus(ReservationStatus.APPROVED);
    }

    private void rejectReservation(ActionEvent e) {
        updateReservationStatus(ReservationStatus.CANCELLED);
    }

    private void updateReservationStatus(ReservationStatus newStatus) {
        int row = reservationTable.getSelectedRow();
        if (row != -1) {
            int reservationId = (int) reservationTable.getValueAt(row, 0);

            ReservationUpdateDto dto = new ReservationUpdateDto();
            dto.setId(reservationId);
            dto.setStatus(newStatus);

            reservationService.update(dto).thenAccept(res -> {
                if (res.isSuccess()) {
                    reservationTable.setValueAt(getReadableStatus(newStatus), row, 5);
                    JOptionPane.showMessageDialog(null, "Rezervasyon durumu güncellendi: " + getReadableStatus(newStatus));
                } else {
                    JOptionPane.showMessageDialog(null, "Güncelleme başarısız: " + res.getMessage());
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, "Lütfen bir rezervasyon seçin!");
        }
    }
}
