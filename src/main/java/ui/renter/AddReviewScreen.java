package ui.renter;

import business.services.commentService.ICommentService;
import business.services.commentService.CommentService;
import business.services.reservationService.IReservationService;
import business.services.reservationService.ReservationService;
import business.services.houseService.IHouseService;
import business.services.houseService.HouseService;
import core.session.UserSession;
import entites.dtos.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AddReviewScreen extends JFrame {
    private JComboBox<HouseListDto> houseCombo;
    private JTextArea commentArea;
    private JComboBox<Integer> ratingCombo;
    private JButton submitButton, backButton;

    private final IReservationService reservationService = new ReservationService();
    private final IHouseService houseService = new HouseService();
    private final ICommentService commentService = new CommentService();

    public AddReviewScreen() {
        setTitle("Yorum Yap / Puan Ver");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadRentedHouses();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 245, 245));
        add(panel);

        JLabel titleLabel = new JLabel("Kiraladığınız Evler");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(50, 30, 200, 30);
        panel.add(titleLabel);

        houseCombo = new JComboBox<>();
        houseCombo.setBounds(50, 70, 480, 30);
        panel.add(houseCombo);

        JLabel commentLabel = new JLabel("Yorumunuz:");
        commentLabel.setBounds(50, 120, 100, 25);
        panel.add(commentLabel);

        commentArea = new JTextArea();
        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setBounds(50, 150, 480, 120);
        panel.add(commentScroll);

        JLabel ratingLabel = new JLabel("Puan (1-5):");
        ratingLabel.setBounds(50, 290, 100, 25);
        panel.add(ratingLabel);

        ratingCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        ratingCombo.setBounds(150, 290, 100, 25);
        panel.add(ratingCombo);

        submitButton = new JButton("✅ Gönder");
        submitButton.setBounds(200, 340, 180, 40);
        panel.add(submitButton);

        backButton = new JButton("← Geri Dön");
        backButton.setBounds(20, 420, 100, 30);
        panel.add(backButton);

        submitButton.addActionListener(this::submitComment);
        backButton.addActionListener(e -> {
            dispose();
            new RenterDashboard().setVisible(true);
        });
    }

    private void loadRentedHouses() {
        reservationService.getByUserId(UserSession.currentUser.getId(), UserSession.currentUser.getId())
                .thenAccept(res -> {
                    if (res.isSuccess()) {
                        List<ReservationListDto> reservations = res.getData();
                        for (ReservationListDto r : reservations) {
                            HouseListDto house = r.getHouse();
                            SwingUtilities.invokeLater(() -> houseCombo.addItem(house));
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Ev bilgileri yüklenemedi: " + res.getMessage());
                    }
                });

    }

    private void submitComment(ActionEvent e) {
        HouseListDto selectedHouse = (HouseListDto) houseCombo.getSelectedItem();
        String comment = commentArea.getText();
        int rating = (int) ratingCombo.getSelectedItem();

        if (selectedHouse == null || comment.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Lütfen tüm alanları doldurun!");
            return;
        }

        CommentCreateDto dto = new CommentCreateDto();
        dto.setHouseId(selectedHouse.getId());
        dto.setContent(comment);
        dto.setRating(rating);
        dto.setUserId(UserSession.currentUser.getId());

        commentService.add(dto).thenAccept(result -> {
            if (result.isSuccess()) {
                JOptionPane.showMessageDialog(null, "Yorum başarıyla gönderildi!");
                dispose();
                new RenterDashboard().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Yorum gönderilemedi: " + result.getMessage());
            }
        });
    }
}
