package ui.renter;

import ui.LoginScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RenterDashboard extends JFrame {

    public RenterDashboard() {
        setTitle("Kiracı Paneli");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 245, 245));
        add(panel);

        JLabel title = new JLabel("Kiracı Yönetim Paneli");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(140, 20, 300, 30);
        panel.add(title);

        JButton searchHouseButton = new JButton("🏡 Ev Ara / Listele");
        searchHouseButton.setBounds(150, 80, 200, 40);
        panel.add(searchHouseButton);

        JButton popularHousesButton = new JButton("🔥 Popüler Evler");
        popularHousesButton.setBounds(150, 130, 200, 40);
        panel.add(popularHousesButton);

        JButton myReservationsButton = new JButton("📄 Rezervasyonlarım");
        myReservationsButton.setBounds(150, 180, 200, 40);
        panel.add(myReservationsButton);

        JButton addReviewButton = new JButton("💬 Yorum Yap / Puan Ver");
        addReviewButton.setBounds(150, 230, 200, 40);
        panel.add(addReviewButton);

        JButton logoutButton = new JButton("← Çıkış Yap");
        logoutButton.setBounds(20, 420, 100, 30);
        panel.add(logoutButton);

        // Buton İşlevleri - Gerçek ekranlara yönlendirildi

        searchHouseButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HouseSearchScreen().setVisible(true);
        });

        popularHousesButton.addActionListener((ActionEvent e) -> {
            dispose();
            new PopularHousesScreen().setVisible(true);
        });


        myReservationsButton.addActionListener((ActionEvent e) -> {
            dispose();
            new MyReservationsScreen().setVisible(true);
        });

        addReviewButton.addActionListener((ActionEvent e) -> {
            dispose();
            new AddReviewScreen().setVisible(true);
        });


        logoutButton.addActionListener((ActionEvent e) -> {
            dispose();
            new LoginScreen().setVisible(true);
        });
    }
}
