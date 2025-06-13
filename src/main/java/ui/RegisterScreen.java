package ui;

import business.services.userService.IUserService;
import business.services.userService.UserService;
import entites.dtos.UserRegisterDto;
import entites.enums.UserRole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterScreen extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<UserRole> roleCombo;
    private JButton registerButton;
    private JButton backButton;
    private final IUserService userService;

    public RegisterScreen(IUserService userService) {
        this.userService = userService;

        setTitle("Kayıt Ol");
        setSize(450, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 245));
        add(panel);

        JLabel titleLabel = new JLabel("Yeni Hesap Oluştur");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(130, 20, 250, 30);
        panel.add(titleLabel);

        JLabel nameLabel = new JLabel("Ad:");
        nameLabel.setBounds(50, 70, 100, 25);
        panel.add(nameLabel);

        firstNameField = new JTextField();
        firstNameField.setBounds(160, 70, 200, 25);
        panel.add(firstNameField);

        JLabel lastNameLabel = new JLabel("Soyad:");
        lastNameLabel.setBounds(50, 110, 100, 25);
        panel.add(lastNameLabel);

        lastNameField = new JTextField();
        lastNameField.setBounds(160, 110, 200, 25);
        panel.add(lastNameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 150, 100, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(160, 150, 200, 25);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Şifre:");
        passwordLabel.setBounds(50, 190, 100, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 190, 200, 25);
        panel.add(passwordField);

        JLabel roleLabel = new JLabel("Rol:");
        roleLabel.setBounds(50, 230, 100, 25);
        panel.add(roleLabel);

        String[] roles = {"admin", "host", "renter"};
        roleCombo = new JComboBox<>(UserRole.values());
        roleCombo.setBounds(160, 230, 200, 25);
        panel.add(roleCombo);

        registerButton = new JButton("Kayıt Ol");
        registerButton.setBounds(160, 280, 200, 35);
        registerButton.setBackground(new Color(0, 153, 76));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        panel.add(registerButton);

        backButton = new JButton("← Giriş Ekranı");
        backButton.setBounds(160, 330, 200, 30);
        backButton.setBackground(new Color(100, 100, 100));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        panel.add(backButton);

        // Kayıt butonu işlevi
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ad = firstNameField.getText();
                String soyad = lastNameField.getText();
                String email = emailField.getText();
                String sifre = String.valueOf(passwordField.getPassword());
                UserRole rol = (UserRole) roleCombo.getSelectedItem();

                UserRegisterDto dto = new UserRegisterDto(ad, soyad, email, sifre, rol);

                userService.register(dto)
                        .thenAccept(result -> {
                            if (result != null && result.isSuccess()) {
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(null, result.getMessage());
                                    dispose(); // Register ekranını kapat
                                    new LoginScreen().setVisible(true); // Login ekranını aç
                                });
                            } else {
                                SwingUtilities.invokeLater(() ->
                                        JOptionPane.showMessageDialog(null, "Kayıt başarısız: " + (result != null ? result.getMessage() : "Sunucu hatası"))
                                );
                            }
                        })
                        .exceptionally(ex -> {
                            SwingUtilities.invokeLater(() ->
                                    JOptionPane.showMessageDialog(null, "İstek hatası: " + ex.getMessage())
                            );
                            return null;
                        });
            }
        });


        // Geri dön butonu
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Bu ekranı kapat
                new LoginScreen().setVisible(true); // Login ekranını aç
            }
        });
    }
}
