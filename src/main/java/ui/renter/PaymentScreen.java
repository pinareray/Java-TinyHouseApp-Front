package ui.renter;

import business.services.paymentService.IPaymentService;
import business.services.paymentService.PaymentService;
import business.services.reservationService.IReservationService;
import business.services.reservationService.ReservationService;
import core.session.UserSession;
import entites.dtos.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import core.utilities.helpers.DateLabelFormatter;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.Properties;

public class PaymentScreen extends JFrame {

    private JDatePickerImpl startDatePicker, endDatePicker;
    private JTextField cardNumberField, cardNameField;
    private JButton payButton, backButton;
    private JLabel houseTitleLabel, priceInfoLabel;

    private final HouseDto house;

    public PaymentScreen(HouseDto house) {
        this.house = house;
        setTitle("Rezervasyon ve Ödeme");
        setSize(600, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        add(panel);

        JLabel titleLabel = new JLabel("Rezervasyon Bilgileri");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(180, 20, 300, 30);
        panel.add(titleLabel);

        houseTitleLabel = new JLabel("Ev: " + house.getTitle());
        houseTitleLabel.setBounds(50, 60, 400, 25);
        panel.add(houseTitleLabel);

        priceInfoLabel = new JLabel("Fiyat: " + house.getPrice() + " TL / Gece");
        priceInfoLabel.setBounds(50, 90, 300, 25);
        panel.add(priceInfoLabel);

        panel.add(createLabel("Başlangıç Tarihi:", 50, 130));
        startDatePicker = createDatePicker(300, 130);
        panel.add(startDatePicker);

        panel.add(createLabel("Bitiş Tarihi:", 50, 170));
        endDatePicker = createDatePicker(300, 170);
        panel.add(endDatePicker);

        panel.add(createLabel("Kart Numarası:", 50, 230));
        cardNumberField = createTextField(300, 230);
        setNumericOnlyFilter(cardNumberField);
        panel.add(cardNumberField);

        panel.add(createLabel("Kart Üzerindeki İsim:", 50, 270));
        cardNameField = createTextField(300, 270);
        panel.add(cardNameField);

        payButton = new JButton("💳 Ödeme Yap");
        payButton.setBounds(200, 350, 180, 40);
        panel.add(payButton);

        backButton = new JButton("← Geri Dön");
        backButton.setBounds(20, 450, 100, 30);
        panel.add(backButton);

        payButton.addActionListener(this::handlePayment);
        backButton.addActionListener(e -> {
            dispose();
            new HouseDetailScreen(house.getId()).setVisible(true);
        });
    }

    private void handlePayment(ActionEvent e) {
        // Tarihleri DatePicker'dan alıyoruz
        LocalDate startDate = getPickerDate(startDatePicker);
        LocalDate endDate   = getPickerDate(endDatePicker);

        if (startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(null, "Lütfen tüm tarihleri seçin!");
            return;
        }
        if (startDate.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(null, "Başlangıç tarihi bugünden önce olamaz!");
            return;
        }
        if (!endDate.isAfter(startDate)) {
            JOptionPane.showMessageDialog(null, "Bitiş tarihi, başlangıç tarihinden sonra olmalıdır!");
            return;
        }

        String cardNumber = cardNumberField.getText().trim();
        String cardName   = cardNameField.getText().trim();

        if (cardNumber.length() != 16) {
            JOptionPane.showMessageDialog(null, "Kart numarası 16 haneli olmalıdır!");
            return;
        }
        if (cardName.length() < 3) {
            JOptionPane.showMessageDialog(null, "Kart üzerindeki isim en az 3 karakter olmalıdır!");
            return;
        }

        payButton.setEnabled(false);
        backButton.setEnabled(false);

        // Rezervasyon oluştur
        ReservationCreateDto reservationDto = new ReservationCreateDto();
        reservationDto.setStartDate(startDate);
        reservationDto.setEndDate(endDate);
        reservationDto.setHouseId(house.getId());
        reservationDto.setRenterId(UserSession.currentUser.getId());

        IReservationService reservationService = new ReservationService();
        reservationService.add(reservationDto).thenAccept(res -> {
            if (res.isSuccess()) {
                int reservationId = res.getData().getId();
                // Ödeme DTO
                PaymentCreateDto paymentDto = new PaymentCreateDto();
                paymentDto.setAmount(house.getPrice());
                paymentDto.setUserId(UserSession.currentUser.getId());
                paymentDto.setReservationId(reservationId);

                IPaymentService paymentService = new PaymentService();
                paymentService.add(paymentDto).thenAccept(paymentRes -> {
                    SwingUtilities.invokeLater(() -> {
                        if (paymentRes.isSuccess()) {
                            JOptionPane.showMessageDialog(null, "Rezervasyon ve ödeme tamamlandı!");
                            dispose();
                            new RenterDashboard().setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Ödeme başarısız: " + paymentRes.getMessage());
                            enableButtons();
                        }
                    });
                }).exceptionally(ex -> {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, "Ödeme hatası: " + ex.getMessage()));
                    enableButtons();
                    return null;
                });
            } else {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(null, "Rezervasyon başarısız: " + res.getMessage()));
                enableButtons();
            }
        }).exceptionally(ex -> {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null, "Rezervasyon hatası: " + ex.getMessage()));
            enableButtons();
            return null;
        });
    }

    private LocalDate getPickerDate(JDatePickerImpl picker) {
        if (picker.getModel().getValue() != null) {
            java.util.Date d = (java.util.Date) picker.getModel().getValue();
            return LocalDate.of(d.getYear() + 1900, d.getMonth() + 1, d.getDate());
        }
        return null;
    }

    private void enableButtons() {
        SwingUtilities.invokeLater(() -> {
            payButton.setEnabled(true);
            backButton.setEnabled(true);
        });
    }

    private JLabel createLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 220, 25);
        return lbl;
    }

    private JTextField createTextField(int x, int y) {
        JTextField fld = new JTextField();
        fld.setBounds(x, y, 200, 25);
        return fld;
    }

    private void setNumericOnlyFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override public void insertString(FilterBypass fb, int off, String str, AttributeSet attr) throws BadLocationException {
                if (str.matches("\\d*")) super.insertString(fb, off, str, attr);
            }
            @Override public void replace(FilterBypass fb, int off, int len, String txt, AttributeSet attrs) throws BadLocationException {
                if (txt.matches("\\d*")) super.replace(fb, off, len, txt, attrs);
            }
        });
    }

    private JDatePickerImpl createDatePicker(int x, int y) {
        UtilDateModel model = new UtilDateModel();
        model.setSelected(false);
        Properties p = new Properties();
        p.put("text.today", "Bugün");
        p.put("text.month", "Ay");
        p.put("text.year", "Yıl");
        JDatePanelImpl panel = new JDatePanelImpl(model, p);
        JDatePickerImpl picker = new JDatePickerImpl(panel, new DateLabelFormatter());
        picker.setBounds(x, y, 200, 30);
        return picker;
    }
}
