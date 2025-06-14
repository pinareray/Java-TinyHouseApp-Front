package ui.host;

import business.services.houseService.HouseService;
import business.services.houseService.IHouseService;
import core.session.UserSession;
import core.utilities.helpers.DateLabelFormatter;
import entites.dtos.HouseDto;
import entites.dtos.HouseUpdateDto;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.Properties;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class EditHouseScreen extends JFrame {

    private JTextField titleField, priceField, locationField;
    private JDatePickerImpl fromPicker, toPicker;
    private JTextArea descriptionArea;
    private JComboBox<String> statusCombo;
    private JButton updateButton, cancelButton;
    private final HouseDto houseDto;

    public EditHouseScreen(HouseDto houseDto) {
        this.houseDto = houseDto;

        setTitle("İlanı Düzenle");
        setSize(520, 640);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        populateFields();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        add(panel);

        int y = 30;

        panel.add(createLabel("Başlık:", 50, y));
        titleField = createTextField(150, y);
        panel.add(titleField);
        y += 50;

        panel.add(createLabel("Açıklama:", 50, y));
        descriptionArea = new JTextArea();
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setBounds(150, y, 300, 100);
        panel.add(descScroll);
        y += 120;

        panel.add(createLabel("Fiyat:", 50, y));
        priceField = createTextField(150, y);
        setNumericOnlyFilter(priceField);
        panel.add(priceField);
        y += 50;

        panel.add(createLabel("Konum:", 50, y));
        locationField = createTextField(150, y);
        panel.add(locationField);
        y += 50;

        panel.add(createLabel("Durum:", 50, y));
        statusCombo = new JComboBox<>(new String[]{"Aktif", "Pasif"});
        statusCombo.setBounds(150, y, 300, 25);
        panel.add(statusCombo);
        y += 50;

        panel.add(createLabel("Başlangıç Tarihi:", 50, y));
        fromPicker = createDatePicker(150, y);
        panel.add(fromPicker);
        y += 50;

        panel.add(createLabel("Bitiş Tarihi:", 50, y));
        toPicker = createDatePicker(150, y);
        panel.add(toPicker);
        y += 60;

        updateButton = new JButton("Güncelle");
        updateButton.setBounds(150, y, 140, 40);
        panel.add(updateButton);

        cancelButton = new JButton("İptal");
        cancelButton.setBounds(310, y, 140, 40);
        panel.add(cancelButton);

        updateButton.addActionListener(this::handleUpdate);
        cancelButton.addActionListener(e -> {
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

        if (houseDto.getAvailableFrom() != null) {
            fromPicker.getModel().setDate(
                    houseDto.getAvailableFrom().getYear(),
                    houseDto.getAvailableFrom().getMonthValue() - 1,
                    houseDto.getAvailableFrom().getDayOfMonth());
            fromPicker.getModel().setSelected(true);
        }

        if (houseDto.getAvailableTo() != null) {
            toPicker.getModel().setDate(
                    houseDto.getAvailableTo().getYear(),
                    houseDto.getAvailableTo().getMonthValue() - 1,
                    houseDto.getAvailableTo().getDayOfMonth());
            toPicker.getModel().setSelected(true);
        }
    }

    private void handleUpdate(ActionEvent e) {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String priceText = priceField.getText();
        String location = locationField.getText();
        String status = (String) statusCombo.getSelectedItem();

        if (title.isEmpty() || priceText.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Zorunlu alanlar boş olamaz!");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Fiyat sayısal olmalıdır!");
            return;
        }

        LocalDate fromDate = LocalDate.of(
                fromPicker.getModel().getYear(),
                fromPicker.getModel().getMonth() + 1,
                fromPicker.getModel().getDay());

        LocalDate toDate = LocalDate.of(
                toPicker.getModel().getYear(),
                toPicker.getModel().getMonth() + 1,
                toPicker.getModel().getDay());

        HouseUpdateDto dto = new HouseUpdateDto();
        dto.setId(houseDto.getId());
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setPrice(price);
        dto.setLocation(location);
        dto.setStatus(status);
        dto.setAvailableFrom(fromDate);
        dto.setAvailableTo(toDate);
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
    }

    private JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 120, 25);
        return label;
    }

    private JTextField createTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 300, 25);
        return field;
    }

    private JDatePickerImpl createDatePicker(int x, int y) {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Bugün");
        p.put("text.month", "Ay");
        p.put("text.year", "Yıl");
        JDatePanelImpl panel = new JDatePanelImpl(model, p);
        JDatePickerImpl picker = new JDatePickerImpl(panel, new DateLabelFormatter());
        picker.setBounds(x, y, 300, 30);
        return picker;
    }

    private void setNumericOnlyFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("[0-9.]*")) super.insertString(fb, offset, string, attr);
            }
            @Override public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("[0-9.]*")) super.replace(fb, offset, length, text, attrs);
            }
        });
    }
}
