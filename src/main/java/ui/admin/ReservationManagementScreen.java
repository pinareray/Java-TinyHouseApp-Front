package ui.admin;

import business.services.reservationService.IReservationService;
import business.services.reservationService.ReservationService;
import core.session.UserSession;
import entites.dtos.ReservationListDto;
import entites.dtos.ReservationUpdateDto;
import entites.enums.ReservationStatus;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

public class ReservationManagementScreen extends JFrame {
    private JTable reservationTable;
    private DefaultTableModel model;
    private JButton backButton, editReservationButton, deleteReservationButton;
    private final IReservationService reservationService = new ReservationService();

    public ReservationManagementScreen() {
        setTitle("Rezervasyon Yönetimi");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadReservations();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Tüm Rezervasyonlar", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Rez.ID", "Ev", "Kiracı", "Başlangıç", "Bitiş", "Durum"}, 0);
        reservationTable = new JTable(model);
        panel.add(new JScrollPane(reservationTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        editReservationButton = new JButton("✏️ Düzenle");
        deleteReservationButton = new JButton("🗑️ Sil");
        backButton = new JButton("← Admin Paneline Dön");

        buttonPanel.add(editReservationButton);
        buttonPanel.add(deleteReservationButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        editReservationButton.addActionListener(this::editReservation);
        deleteReservationButton.addActionListener(this::deleteReservation);
        backButton.addActionListener(e -> {
            dispose();
            new AdminDashboard().setVisible(true);
        });
    }

    private void loadReservations() {
        model.setRowCount(0);
        reservationService.getAll(UserSession.currentUser.getId()).thenAccept(result -> {
            if (result.isSuccess()) {
                List<ReservationListDto> reservations = result.getData();
                SwingUtilities.invokeLater(() -> {
                    for (ReservationListDto r : reservations) {
                        model.addRow(new Object[]{
                                r.getId(),
                                r.getHouse().getTitle(),
                                r.getRenter().getFirstName() + " " + r.getRenter().getLastName(),
                                r.getStartDate().format(DateTimeFormatter.ISO_DATE),
                                r.getEndDate().format(DateTimeFormatter.ISO_DATE),
                                r.getStatus().name()
                        });
                    }
                });
            } else {
                JOptionPane.showMessageDialog(null, "Rezervasyonlar yüklenemedi: " + result.getMessage());
            }
        });
    }

    private void editReservation(ActionEvent e) {
        int row = reservationTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Düzenlemek için bir rezervasyon seçin!");
            return;
        }

        int id = (int) reservationTable.getValueAt(row, 0);
        // Tabloda string olarak tuttuğumuz durum
        String currentStatusStr = reservationTable.getValueAt(row, 5).toString();
        // Enum'a çeviriyoruz
        ReservationStatus currentStatus = ReservationStatus.valueOf(currentStatusStr);

        // --- Tarih modellerini hazırla ---
        LocalDate oldStart = LocalDate.parse(reservationTable.getValueAt(row, 3).toString());
        LocalDate oldEnd   = LocalDate.parse(reservationTable.getValueAt(row, 4).toString());

        UtilDateModel startModel = new UtilDateModel();
        startModel.setDate(oldStart.getYear(), oldStart.getMonthValue() - 1, oldStart.getDayOfMonth());
        startModel.setSelected(true);

        UtilDateModel endModel = new UtilDateModel();
        endModel.setDate(oldEnd.getYear(), oldEnd.getMonthValue() - 1, oldEnd.getDayOfMonth());
        endModel.setSelected(true);

        Properties p = new Properties();
        p.put("text.today", "Bugün");
        p.put("text.month", "Ay");
        p.put("text.year", "Yıl");
        JDatePickerImpl startPicker = new JDatePickerImpl(
                new JDatePanelImpl(startModel, p),
                new core.utilities.helpers.DateLabelFormatter()
        );
        JDatePickerImpl endPicker = new JDatePickerImpl(
                new JDatePanelImpl(endModel, p),
                new core.utilities.helpers.DateLabelFormatter()
        );

        // --- Durum seçiciyi enum üzerinden doldur ve mevcut durumu seç ---
        JComboBox<ReservationStatus> statusCombo = new JComboBox<>(ReservationStatus.values());
        statusCombo.setSelectedItem(currentStatus);

        // --- Paneli oluştur ---
        JPanel editPanel = new JPanel(new GridLayout(3,2,8,8));
        editPanel.add(new JLabel("Yeni Başlangıç Tarihi:"));
        editPanel.add(startPicker);
        editPanel.add(new JLabel("Yeni Bitiş Tarihi:"));
        editPanel.add(endPicker);
        editPanel.add(new JLabel("Durum:"));
        editPanel.add(statusCombo);

        int result = JOptionPane.showConfirmDialog(
                this,
                editPanel,
                "Rezervasyonu Güncelle",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        // --- Yeni değerleri al ---
        LocalDate newStart = LocalDate.of(
                startModel.getYear(),
                startModel.getMonth() + 1,
                startModel.getDay()
        );
        LocalDate newEnd = LocalDate.of(
                endModel.getYear(),
                endModel.getMonth() + 1,
                endModel.getDay()
        );
        ReservationStatus newStatus = (ReservationStatus) statusCombo.getSelectedItem();

        // --- DTO'yu hazırlayıp servis çağrısı yap ---
        ReservationUpdateDto dto = new ReservationUpdateDto();
        dto.setId(id);
        dto.setStartDate(newStart);
        dto.setEndDate(newEnd);
        dto.setStatus(newStatus);

        reservationService.update(dto).thenAccept(res -> {
            SwingUtilities.invokeLater(() -> {
                if (res.isSuccess()) {
                    model.setValueAt(newStart.format(DateTimeFormatter.ISO_DATE), row, 3);
                    model.setValueAt(newEnd.format(DateTimeFormatter.ISO_DATE), row, 4);
                    model.setValueAt(newStatus.name(), row, 5);
                    JOptionPane.showMessageDialog(this, "Rezervasyon güncellendi!");
                } else {
                    JOptionPane.showMessageDialog(this, "Güncelleme başarısız oldu: " + res.getMessage());
                }
            });
        }).exceptionally(ex -> {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(this, "Güncelleme hatası: " + ex.getMessage())
            );
            return null;
        });
    }


    private void deleteReservation(ActionEvent e) {
        int row = reservationTable.getSelectedRow();
        if (row != -1) {
            int confirm = JOptionPane.showConfirmDialog(null, "Rezervasyonu silmek istediğinize emin misiniz?");
            if (confirm == JOptionPane.YES_OPTION) {
                int id = (int) reservationTable.getValueAt(row, 0);
                reservationService.delete(id, UserSession.currentUser.getId()).thenAccept(res -> {
                    if (res.isSuccess()) {
                        model.removeRow(row);
                        JOptionPane.showMessageDialog(null, "Rezervasyon silindi.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Silme başarısız: " + res.getMessage());
                    }
                });
            }
        }
    }
}
