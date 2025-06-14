package ui.admin;

import business.services.adminService.AdminService;
import business.services.adminService.IAdminService;
import core.utilities.results.DataResult;
import entites.dtos.AdminSummaryDto;

import javax.swing.*;
import java.awt.*;

public class AdminSummaryScreen extends JFrame {

    private final IAdminService adminService = new AdminService();

    private JLabel userLabel;
    private JLabel hostLabel;
    private JLabel renterLabel;
    private JLabel activeReservationsLabel;
    private JLabel monthlyReservationsLabel;  // Yeni satır
    private JLabel incomeLabel;

    public AdminSummaryScreen() {
        setTitle("Sistem Özeti - Admin Dashboard");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        loadSummary();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        add(panel);

        JLabel title = new JLabel("Sistem Durumu Özeti");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(180, 20, 300, 30);
        panel.add(title);

        userLabel = new JLabel("👤 Toplam Kullanıcı: ...");
        userLabel.setBounds(50, 80, 300, 25);
        panel.add(userLabel);

        hostLabel = new JLabel("🏡 Ev Sahibi Sayısı: ...");
        hostLabel.setBounds(50, 120, 300, 25);
        panel.add(hostLabel);

        renterLabel = new JLabel("🧍 Kiracı Sayısı: ...");
        renterLabel.setBounds(50, 160, 300, 25);
        panel.add(renterLabel);

        activeReservationsLabel = new JLabel("📅 Aktif Rezervasyonlar: ...");
        activeReservationsLabel.setBounds(50, 200, 300, 25);
        panel.add(activeReservationsLabel);

        monthlyReservationsLabel = new JLabel("📊 Aylık Rezervasyon Sayısı: ...");
        monthlyReservationsLabel.setBounds(50, 240, 300, 25);
        panel.add(monthlyReservationsLabel);

        incomeLabel = new JLabel("💰 Aylık Toplam Gelir: ...");
        incomeLabel.setBounds(50, 280, 300, 25);
        panel.add(incomeLabel);

        JButton backButton = new JButton("← Admin Paneline Dön");
        backButton.setBounds(20, 360, 180, 30);
        panel.add(backButton);

        backButton.addActionListener(e -> {
            dispose();
            new AdminDashboard().setVisible(true);
        });
    }

    private void loadSummary() {
        adminService.getSystemSummary().thenAccept(result -> {
            if (result.isSuccess()) {
                AdminSummaryDto summary = result.getData();

                SwingUtilities.invokeLater(() -> {
                    userLabel.setText("👤 Toplam Kullanıcı: " + summary.getTotalUsers());
                    hostLabel.setText("🏡 Ev Sahibi Sayısı: " + summary.getTotalHosts());
                    renterLabel.setText("🧍 Kiracı Sayısı: " + summary.getTotalRenters());
                    activeReservationsLabel.setText("📅 Aktif Rezervasyonlar: " + summary.getActiveReservations());
                    monthlyReservationsLabel.setText("📊 Aylık Rezervasyon Sayısı: " + summary.getMonthlyReservations());
                    incomeLabel.setText("💰 Aylık Toplam Gelir: " + String.format("%.2f₺", summary.getMonthlyIncome()));
                });
            } else {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(null, "Özet veriler yüklenemedi: " + result.getMessage())
                );
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null, "Sunucu hatası: " + ex.getMessage())
            );
            return null;
        });
    }
}
