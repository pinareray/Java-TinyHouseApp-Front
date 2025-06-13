package ui.host;

import ui.LoginScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HostDashboard extends JFrame {

    public HostDashboard() {
        setTitle("Ev Sahibi Paneli");
        setSize(500, 500); // Boyutu biraz büyüttüm ki yeni buton rahat sığsın
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 245, 245));
        add(panel);

        JLabel title = new JLabel("Ev Sahibi Yönetim Paneli");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(120, 20, 300, 30);
        panel.add(title);

        JButton myHousesButton = new JButton("🏡 İlanlarımı Yönet");
        myHousesButton.setBounds(150, 80, 200, 40);
        panel.add(myHousesButton);

        JButton reservationButton = new JButton("🛎️ Rezervasyonlarım");
        reservationButton.setBounds(150, 130, 200, 40);
        panel.add(reservationButton);

        JButton commentButton = new JButton("💬 Kullanıcı Yorumları");
        commentButton.setBounds(150, 180, 200, 40);
        panel.add(commentButton);

        JButton paymentsButton = new JButton("💰 Ödeme Geçmişim");
        paymentsButton.setBounds(150, 230, 200, 40);
        panel.add(paymentsButton);

        JButton incomeReportButton = new JButton("📊 Gelir Raporu"); // 🔥 Burayı ekledim
        incomeReportButton.setBounds(150, 280, 200, 40);
        panel.add(incomeReportButton);

        JButton logoutButton = new JButton("← Çıkış Yap");
        logoutButton.setBounds(20, 420, 100, 30); // Aşağıya kaydırdım
        panel.add(logoutButton);

        // Buton İşlevleri
        myHousesButton.addActionListener((ActionEvent e) -> {
            dispose();
            new MyHousesScreen().setVisible(true);
        });

        reservationButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HostReservationsScreen().setVisible(true);
        });

        commentButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HostCommentsScreen().setVisible(true);
        });

        paymentsButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HostPaymentsScreen().setVisible(true);
        });

        incomeReportButton.addActionListener((ActionEvent e) -> { // 🔥 Yeni butonun action'u
            dispose();
            new IncomeReportScreen().setVisible(true);
        });

        logoutButton.addActionListener((ActionEvent e) -> {
            dispose();
            new LoginScreen().setVisible(true);
        });
    }
}


