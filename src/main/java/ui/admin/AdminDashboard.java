package ui.admin;

import ui.LoginScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Yönetim Paneli");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Ortala
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 245));
        add(panel);

        JLabel titleLabel = new JLabel("Admin Yönetim Paneli");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setBounds(160, 20, 400, 40);
        panel.add(titleLabel);

        JButton dashboardButton = new JButton("📋 Dashboard (Özet Durum)");
        dashboardButton.setBounds(180, 90, 250, 40);
        panel.add(dashboardButton);

        JButton userButton = new JButton("👥 Kullanıcıları Yönet");
        userButton.setBounds(180, 140, 250, 40);
        panel.add(userButton);

        JButton reservationButton = new JButton("🛎️ Rezervasyonları Yönet");
        reservationButton.setBounds(180, 190, 250, 40);
        panel.add(reservationButton);

        JButton houseButton = new JButton("🏡 İlanları Yönet");
        houseButton.setBounds(180, 240, 250, 40);
        panel.add(houseButton);

        JButton paymentButton = new JButton("💰 Ödeme Yönetimi");
        paymentButton.setBounds(180, 290, 250, 40);
        panel.add(paymentButton);

        JButton reportButton = new JButton("📈 Raporlar ve İstatistikler");
        reportButton.setBounds(180, 340, 250, 40);
        panel.add(reportButton);

        JButton logoutButton = new JButton("← Çıkış Yap");
        logoutButton.setBounds(20, 410, 100, 30);
        panel.add(logoutButton);

        // --- Buton İşlevleri ---
        dashboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // eski satırı sil
                new AdminSummaryScreen().setVisible(true); // ✅ bu satırı yaz
            }
        });


        userButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Admin panelini kapat
                new UserManagementScreen().setVisible(true);
            }
        });


        reservationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ReservationManagementScreen().setVisible(true);
            }
        });


        houseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HouseManagementScreen().setVisible(true);
            }
        });


        paymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new PaymentManagementScreen().setVisible(true);
            }
        });


        reportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ReportScreen().setVisible(true);
            }
        });


        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginScreen().setVisible(true);
            }
        });
    }
}
