package ui.admin;

import javax.swing.*;
import java.awt.*;

public class AdminSummaryScreen extends JFrame {

    public AdminSummaryScreen() {
        setTitle("Sistem Özeti - Admin Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        add(panel);

        JLabel title = new JLabel("Sistem Durumu Özeti");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(180, 20, 300, 30);
        panel.add(title);

        JLabel userLabel = new JLabel("👤 Toplam Kullanıcı: 120");
        userLabel.setBounds(50, 80, 200, 25);
        panel.add(userLabel);

        JLabel hostLabel = new JLabel("🏡 Ev Sahibi Sayısı: 45");
        hostLabel.setBounds(50, 110, 200, 25);
        panel.add(hostLabel);

        JLabel renterLabel = new JLabel("🧍 Kiracı Sayısı: 75");
        renterLabel.setBounds(50, 140, 200, 25);
        panel.add(renterLabel);

        JLabel activeReservations = new JLabel("📅 Aktif Rezervasyonlar: 32");
        activeReservations.setBounds(50, 170, 250, 25);
        panel.add(activeReservations);

        JLabel incomeLabel = new JLabel("💰 Aylık Toplam Gelir: 48.500₺");
        incomeLabel.setBounds(50, 200, 250, 25);
        panel.add(incomeLabel);

        JButton backButton = new JButton("← Admin Paneline Dön");
        backButton.setBounds(20, 310, 180, 30);
        panel.add(backButton);

        backButton.addActionListener(e -> {
            dispose();
            new AdminDashboard().setVisible(true);
        });
    }
}

