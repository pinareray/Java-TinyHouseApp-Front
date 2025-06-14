package ui.host;

import business.services.houseService.HouseService;
import business.services.houseService.IHouseService;
import business.services.paymentService.IPaymentService;
import business.services.paymentService.PaymentService;
import core.session.UserSession;
import entites.dtos.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HostPaymentsScreen extends JFrame {
    private JTable paymentTable;
    private DefaultTableModel model;
    private JButton backButton;

    private final IHouseService houseService = new HouseService();
    private final IPaymentService paymentService = new PaymentService();

    public HostPaymentsScreen() {
        setTitle("Ödeme Geçmişi");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadPayments();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Ödeme Geçmişi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Kiracı", "Ev", "Tutar", "Tarih", "Durum"};
        model = new DefaultTableModel(columns, 0);
        paymentTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("← Ana Panele Dön");
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            dispose();
            new HostDashboard().setVisible(true);
        });
    }

    private void loadPayments() {
        int hostId = UserSession.currentUser.getId();

        houseService.getByHostId(hostId, hostId).thenAccept(houseRes -> {
            if (houseRes.isSuccess()) {
                List<Integer> hostHouseIds = houseRes.getData()
                        .stream()
                        .map(HouseListDto::getId)
                        .toList();

                paymentService.getAll(UserSession.currentUser.getId()).thenAccept(paymentRes -> {
                    if (paymentRes.isSuccess()) {
                        List<PaymentListDto> payments = paymentRes.getData();

                        List<PaymentListDto> filtered = payments.stream()
                                .filter(p -> {
                                    ReservationDto res = p.getReservation();
                                    return res != null && res.getHouse() != null &&
                                            hostHouseIds.contains(res.getHouse().getId());
                                })
                                .toList();

                        SwingUtilities.invokeLater(() -> {
                            model.setRowCount(0);
                            for (PaymentListDto p : filtered) {
                                String renterName = p.getUser().getFirstName() + " " + p.getUser().getLastName();
                                String houseTitle = p.getReservation().getHouse().getTitle();
                                String date = p.getPaymentDate().toLocalDate().toString();
                                String amount = p.getAmount() + " TL";
                                String status = "Tamamlandı"; // DTO'da durum yoksa sabit

                                model.addRow(new Object[]{renterName, houseTitle, amount, date, status});
                            }
                        });

                    } else {
                        showError("Ödeme verileri yüklenemedi: " + paymentRes.getMessage());
                    }
                });

            } else {
                showError("Ev bilgileri yüklenemedi: " + houseRes.getMessage());
            }
        });
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null, message, "Hata", JOptionPane.ERROR_MESSAGE));
    }
}
