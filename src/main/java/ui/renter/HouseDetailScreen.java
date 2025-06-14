package ui.renter;

import business.services.houseService.HouseService;
import business.services.houseService.IHouseService;
import core.session.UserSession;
import entites.dtos.HouseDto;
import entites.dtos.CommentDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HouseDetailScreen extends JFrame {
    private JLabel titleLabel, priceLabel, locationLabel, descriptionLabel;
    private JTextArea commentArea; // Yorum alanı
    private JButton reserveButton, backButton;
    private final int houseId;

    private HouseDto house;

    public HouseDetailScreen(int houseId) {
        this.houseId = houseId;

        setTitle("Ev Detayları");
        setSize(600, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadHouseDetails();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(250, 250, 250));
        add(panel);

        titleLabel = new JLabel("Başlık:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(50, 30, 500, 30);
        panel.add(titleLabel);

        priceLabel = new JLabel("Fiyat:");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        priceLabel.setBounds(50, 80, 300, 25);
        panel.add(priceLabel);

        locationLabel = new JLabel("Konum:");
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        locationLabel.setBounds(50, 120, 300, 25);
        panel.add(locationLabel);

        descriptionLabel = new JLabel("Açıklama:");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setBounds(50, 160, 500, 50);
        panel.add(descriptionLabel);

        JLabel commentLabel = new JLabel("Yorumlar:");
        commentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        commentLabel.setBounds(50, 220, 500, 25);
        panel.add(commentLabel);

        commentArea = new JTextArea();
        commentArea.setEditable(false);
        commentArea.setFont(new Font("Arial", Font.PLAIN, 13));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);

        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setBounds(50, 250, 500, 120);
        panel.add(commentScroll);

        reserveButton = new JButton("📅 Rezervasyon Yap");
        reserveButton.setBounds(180, 390, 200, 40);
        panel.add(reserveButton);

        backButton = new JButton("← Geri Dön");
        backButton.setBounds(20, 450, 100, 30);
        panel.add(backButton);

        reserveButton.addActionListener((ActionEvent e) -> {
            if (house != null) {
                dispose();
                new PaymentScreen(house).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Ev bilgileri yüklenemedi.");
            }
        });

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HouseSearchScreen().setVisible(true);
        });
    }

    private void loadHouseDetails() {
        IHouseService houseService = new HouseService();
        houseService.getById(houseId, UserSession.currentUser.getId()).thenAccept(result -> {
            if (result.isSuccess()) {
                house = result.getData();
                SwingUtilities.invokeLater(() -> {
                    titleLabel.setText("Başlık: " + house.getTitle());
                    priceLabel.setText("Fiyat: " + house.getPrice() + " TL / Gece");
                    locationLabel.setText("Konum: " + house.getLocation());
                    descriptionLabel.setText("<html>Açıklama: " + house.getDescription() + "</html>");

                    StringBuilder commentText = new StringBuilder();
                    if (house.getComments() != null && !house.getComments().isEmpty()) {
                        for (CommentDto comment : house.getComments()) {
                            // rating kadar yıldız oluştur
                            String stars = "★".repeat(Math.max(0, comment.getRating()));
                            commentText
                                    .append(stars)                // ★★★★ gibi
                                    .append(" ")
                                    .append(comment.getContent()) // yorum metni
                                    .append("\n\n");
                        }
                    } else {
                        commentText.append("Henüz yorum yapılmamış.");
                    }
                    commentArea.setText(commentText.toString());
                });
            } else {
                JOptionPane.showMessageDialog(null, "Detaylar yüklenemedi: " + result.getMessage());
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Sunucu hatası: " + ex.getMessage());
            return null;
        });
    }
}
