package ui.renter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HouseDetailScreen extends JFrame {
    private JLabel titleLabel, priceLabel, locationLabel, descriptionLabel;
    private JButton reserveButton, backButton;

    public HouseDetailScreen() {
        setTitle("Ev Detayları");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(250, 250, 250));
        add(panel);

        titleLabel = new JLabel("Başlık: Orman Evi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(50, 30, 500, 30);
        panel.add(titleLabel);

        priceLabel = new JLabel("Fiyat: 600 TL / Gece");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        priceLabel.setBounds(50, 80, 300, 25);
        panel.add(priceLabel);

        locationLabel = new JLabel("Konum: Sapanca");
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        locationLabel.setBounds(50, 120, 300, 25);
        panel.add(locationLabel);

        descriptionLabel = new JLabel("<html>Açıklama: Doğa içinde huzurlu bir tiny house deneyimi.<br>Göl kenarında, sessiz bir ortamda harika bir kaçamak fırsatı.</html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setBounds(50, 160, 500, 80);
        panel.add(descriptionLabel);

        reserveButton = new JButton("📅 Rezervasyon Yap");
        reserveButton.setBounds(180, 300, 200, 40);
        panel.add(reserveButton);

        backButton = new JButton("← Geri Dön");
        backButton.setBounds(20, 400, 100, 30);
        panel.add(backButton);

        // Buton İşlevleri
        reserveButton.addActionListener((ActionEvent e) -> {
            dispose();
            new PaymentScreen().setVisible(true); // Sonraki adım: Ödeme ekranı
        });

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HouseSearchScreen().setVisible(true);
        });
    }
}

