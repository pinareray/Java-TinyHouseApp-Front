package ui.host;

import business.services.houseService.HouseService;
import business.services.houseService.IHouseService;
import core.session.UserSession;
import entites.dtos.HouseUpdateDto;
import entites.dtos.HouseDto;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditHouseScreen extends JFrame {

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField priceField;
    private JTextField locationField;
    private JComboBox<String> statusCombo;
    private JButton updateButton, cancelButton;
    private final HouseDto houseDto; // Güncellenecek evin DTO'su

    public EditHouseScreen(HouseDto houseDto) {
        this.houseDto = houseDto;

        setTitle("İlanı Düzenle");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        populateFields();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(250, 250, 250));
        add(panel);

        JLabel titleLabel = new JLabel("Başlık:");
        titleLabel.setBounds(50, 50, 100, 25);
        panel.add(titleLabel);

        titleField = new JTextField();
        titleField.setBounds(150, 50, 250, 25);
        panel.add(titleField);

        JLabel descriptionLabel = new JLabel("Açıklama:");
        descriptionLabel.setBounds(50, 100, 100, 25);
        panel.add(descriptionLabel);

        descriptionArea = new JTextArea();
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setBounds(150, 100, 250, 100);
        panel.add(descScroll);

        JLabel priceLabel = new JLabel("Fiyat:");
        priceLabel.setBounds(50, 220, 100, 25);
        panel.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(150, 220, 250, 25);
        setNumericOnlyFilter(priceField); // <== Buraya ekle
        panel.add(priceField);

        JLabel locationLabel = new JLabel("Konum:");
        locationLabel.setBounds(50, 270, 100, 25);
        panel.add(locationLabel);

        locationField = new JTextField();
        locationField.setBounds(150, 270, 250, 25);
        panel.add(locationField);

        JLabel statusLabel = new JLabel("Durum:");
        statusLabel.setBounds(50, 320, 100, 25);
        panel.add(statusLabel);

        String[] statusOptions = {"Aktif", "Pasif"};
        statusCombo = new JComboBox<>(statusOptions);
        statusCombo.setBounds(150, 320, 250, 25);
        panel.add(statusCombo);

        updateButton = new JButton("Güncelle");
        updateButton.setBounds(150, 400, 120, 40);
        panel.add(updateButton);

        cancelButton = new JButton("İptal");
        cancelButton.setBounds(280, 400, 120, 40);
        panel.add(cancelButton);

        updateButton.addActionListener((ActionEvent e) -> {
            String title = titleField.getText();
            String description = descriptionArea.getText();
            String priceText = priceField.getText();
            String location = locationField.getText();
            String status = statusCombo.getSelectedItem().toString();

            if (title.isEmpty() || priceText.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Başlık, fiyat ve konum boş olamaz!");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Fiyat sayısal olmalıdır!");
                return;
            }

            HouseUpdateDto dto = new HouseUpdateDto();
            dto.setId(houseDto.getId());
            dto.setTitle(title);
            dto.setDescription(description);
            dto.setPrice(price);
            dto.setLocation(location);
            dto.setStatus(status);
            dto.setRequesterId(UserSession.currentUser.getId());

            IHouseService houseService = new HouseService();
            houseService.update(dto).thenAccept(result -> {
                if (result.isSuccess()) {
                    JOptionPane.showMessageDialog(null, "İlan başarıyla güncellendi!");
                    SwingUtilities.invokeLater(() -> {
                        dispose();
                        new MyHousesScreen().setVisible(true);
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "Hata: " + result.getMessage());
                }
            }).exceptionally(ex -> {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Sunucu hatası: " + ex.getMessage());
                return null;
            });
        });

        cancelButton.addActionListener((ActionEvent e) -> {
            dispose();
            new MyHousesScreen().setVisible(true);
        });
    }

    private void populateFields() {
        titleField.setText(houseDto.getTitle());
        descriptionArea.setText(houseDto.getDescription());
        priceField.setText(String.valueOf(houseDto.getPrice()));
        locationField.setText(houseDto.getLocation());
        statusCombo.setSelectedItem(houseDto.getStatus());
    }

    private void setNumericOnlyFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string.matches("[0-9.]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text.matches("[0-9.]*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }
}
