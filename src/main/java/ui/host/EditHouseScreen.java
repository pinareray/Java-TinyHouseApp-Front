package ui.host;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditHouseScreen extends JFrame {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField priceField;
    private JTextField locationField;
    private JComboBox<String> statusCombo;
    private JButton updateButton, cancelButton;

    public EditHouseScreen() {
        setTitle("İlanı Düzenle");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(250, 250, 250));
        add(panel);

        JLabel titleLabel = new JLabel("Başlık:");
        titleLabel.setBounds(50, 50, 100, 25);
        panel.add(titleLabel);

        titleField = new JTextField("Seçilen İlan Başlığı"); // Şu anda sahte veri
        titleField.setBounds(150, 50, 250, 25);
        panel.add(titleField);

        JLabel descriptionLabel = new JLabel("Açıklama:");
        descriptionLabel.setBounds(50, 100, 100, 25);
        panel.add(descriptionLabel);

        descriptionArea = new JTextArea("Seçilen İlan Açıklaması");
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setBounds(150, 100, 250, 100);
        panel.add(descScroll);

        JLabel priceLabel = new JLabel("Fiyat:");
        priceLabel.setBounds(50, 220, 100, 25);
        panel.add(priceLabel);

        priceField = new JTextField("700"); // Sahte veri
        priceField.setBounds(150, 220, 250, 25);
        panel.add(priceField);

        JLabel locationLabel = new JLabel("Konum:");
        locationLabel.setBounds(50, 270, 100, 25);
        panel.add(locationLabel);

        locationField = new JTextField("Sapanca"); // Sahte veri
        locationField.setBounds(150, 270, 250, 25);
        panel.add(locationField);

        JLabel statusLabel = new JLabel("Durum:");
        statusLabel.setBounds(50, 320, 100, 25);
        panel.add(statusLabel);

        String[] statusOptions = {"Aktif", "Pasif"};
        statusCombo = new JComboBox<>(statusOptions);
        statusCombo.setBounds(150, 320, 250, 25);
        statusCombo.setSelectedItem("Aktif");
        panel.add(statusCombo);

        updateButton = new JButton("Güncelle");
        updateButton.setBounds(150, 400, 120, 40);
        panel.add(updateButton);

        cancelButton = new JButton("İptal");
        cancelButton.setBounds(280, 400, 120, 40);
        panel.add(cancelButton);

        // Buton İşlevleri
        updateButton.addActionListener((ActionEvent e) -> {
            String title = titleField.getText();
            String description = descriptionArea.getText();
            String price = priceField.getText();
            String location = locationField.getText();
            String status = statusCombo.getSelectedItem().toString();

            JOptionPane.showMessageDialog(null, "İlan güncellendi!\nBaşlık: " + title);
            // Burada veritabanı güncellemesi olacak (sonra)
            dispose();
            new MyHousesScreen().setVisible(true);
        });

        cancelButton.addActionListener((ActionEvent e) -> {
            dispose();
            new MyHousesScreen().setVisible(true);
        });
    }
}

