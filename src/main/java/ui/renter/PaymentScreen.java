package ui.renter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PaymentScreen extends JFrame {
    private JTextField startDateField, endDateField, cardNumberField, cardNameField;
    private JButton payButton, backButton;

    public PaymentScreen() {
        setTitle("Rezervasyon ve Ödeme");
        setSize(600, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(250, 250, 250));
        add(panel);

        JLabel titleLabel = new JLabel("Rezervasyon Bilgileri");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(180, 20, 300, 30);
        panel.add(titleLabel);

        JLabel startLabel = new JLabel("Başlangıç Tarihi (YYYY-MM-DD):");
        startLabel.setBounds(50, 80, 250, 25);
        panel.add(startLabel);

        startDateField = new JTextField();
        startDateField.setBounds(300, 80, 200, 25);
        panel.add(startDateField);

        JLabel endLabel = new JLabel("Bitiş Tarihi (YYYY-MM-DD):");
        endLabel.setBounds(50, 120, 250, 25);
        panel.add(endLabel);

        endDateField = new JTextField();
        endDateField.setBounds(300, 120, 200, 25);
        panel.add(endDateField);

        JLabel cardNumberLabel = new JLabel("Kart Numarası:");
        cardNumberLabel.setBounds(50, 180, 250, 25);
        panel.add(cardNumberLabel);

        cardNumberField = new JTextField();
        cardNumberField.setBounds(300, 180, 200, 25);
        panel.add(cardNumberField);

        JLabel cardNameLabel = new JLabel("Kart Üzerindeki İsim:");
        cardNameLabel.setBounds(50, 220, 250, 25);
        panel.add(cardNameLabel);

        cardNameField = new JTextField();
        cardNameField.setBounds(300, 220, 200, 25);
        panel.add(cardNameField);

        payButton = new JButton("💳 Ödeme Yap");
        payButton.setBounds(200, 300, 180, 40);
        panel.add(payButton);

        backButton = new JButton("← Geri Dön");
        backButton.setBounds(20, 450, 100, 30);
        panel.add(backButton);

        // Buton İşlevleri
        payButton.addActionListener((ActionEvent e) -> {
            String startDate = startDateField.getText();
            String endDate = endDateField.getText();
            String cardNumber = cardNumberField.getText();
            String cardName = cardNameField.getText();

            if (startDate.isEmpty() || endDate.isEmpty() || cardNumber.isEmpty() || cardName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Lütfen tüm alanları doldurun!");
            } else {
                JOptionPane.showMessageDialog(null, "Rezervasyon ve ödeme başarıyla tamamlandı!\n" +
                        "Başlangıç: " + startDate + "\nBitiş: " + endDate);
                dispose();
                new RenterDashboard().setVisible(true); // Ana panele dönüyoruz
            }
        });

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HouseDetailScreen().setVisible(true);
        });
    }
}

