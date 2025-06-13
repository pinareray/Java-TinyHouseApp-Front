package ui.renter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddReviewScreen extends JFrame {
    private JComboBox<String> houseCombo;
    private JTextArea commentArea;
    private JComboBox<Integer> ratingCombo;
    private JButton submitButton, backButton;

    public AddReviewScreen() {
        setTitle("Yorum Yap / Puan Ver");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 245, 245));
        add(panel);

        JLabel titleLabel = new JLabel("Kiraladığınız Evler");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(50, 30, 200, 30);
        panel.add(titleLabel);

        // Sahte kiralanan evler (ileride veritabanından gelir)
        String[] rentedHouses = {"Orman Evi", "Dağ Evi", "Bungalov Ev"};
        houseCombo = new JComboBox<>(rentedHouses);
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

        // Buton İşlevleri
        submitButton.addActionListener((ActionEvent e) -> {
            String selectedHouse = (String) houseCombo.getSelectedItem();
            String comment = commentArea.getText();
            int rating = (int) ratingCombo.getSelectedItem();

            if (comment.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Lütfen yorum alanını doldurun!");
                return;
            }

            JOptionPane.showMessageDialog(null, "Yorumunuz gönderildi!\nEv: " + selectedHouse +
                    "\nPuan: " + rating + "\nTeşekkürler!");
            dispose();
            new RenterDashboard().setVisible(true);
        });

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new RenterDashboard().setVisible(true);
        });
    }
}

