package ui.admin;

import business.services.paymentService.IPaymentService;
import business.services.paymentService.PaymentService;
import core.session.UserSession;
import entites.dtos.PaymentListDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PaymentManagementScreen extends JFrame {
    private JTable paymentTable;
    private DefaultTableModel model;
    private JButton backButton;
    private final IPaymentService paymentService = new PaymentService();

    public PaymentManagementScreen() {
        setTitle("Ödeme Yönetimi");
        setSize(700, 400);
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
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Ödeme ID", "Rezervasyon ID", "Tutar", "Tarih", "Kiracı"}, 0);
        paymentTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("← Admin Paneline Dön");
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new AdminDashboard().setVisible(true);
        });
    }

    private void loadPayments() {
        model.setRowCount(0); // önce tabloyu temizle

        paymentService.getAll(UserSession.currentUser.getId()).thenAccept(result -> {
            if (result.isSuccess()) {
                List<PaymentListDto> payments = result.getData();
                SwingUtilities.invokeLater(() -> {
                    for (PaymentListDto p : payments) {
                        model.addRow(new Object[]{
                                p.getId(),
                                p.getReservation().getId(),
                                p.getAmount() + " TL",
                                p.getPaymentDate().format(DateTimeFormatter.ISO_DATE),
                                p.getUser().getFirstName() + " " + p.getUser().getLastName()
                        });
                    }
                });
            } else {
                JOptionPane.showMessageDialog(null, "Ödemeler yüklenemedi: " + result.getMessage());
            }
        });
    }
}
