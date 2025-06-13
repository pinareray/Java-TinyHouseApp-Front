package ui;

import business.services.userService.IUserService;
import business.services.userService.UserService;
import entites.dtos.UserDto;
import entites.dtos.UserLoginDto;
import entites.enums.UserRole;
import ui.admin.AdminDashboard;
import ui.host.HostDashboard;
import ui.renter.RenterDashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    IUserService userService = new UserService();


    public LoginScreen() {
        setTitle("Tiny House - Giriş Ekranı");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Ortala
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(241, 238, 238));
        add(panel);

        JLabel titleLabel = new JLabel("Tiny House Rezervasyon");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(60, 30, 300, 30);
        panel.add(titleLabel);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 100, 80, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(140, 100, 200, 25);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Şifre:");
        passwordLabel.setBounds(50, 150, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 150, 200, 25);
        panel.add(passwordField);

        loginButton = new JButton("Giriş Yap");
        loginButton.setBounds(140, 220, 200, 35);
        loginButton.setBackground(new Color(0, 153, 80));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        panel.add(loginButton);

        registerButton = new JButton("Kayıt Ol");
        registerButton.setBounds(140, 270, 200, 35);
        registerButton.setBackground(new Color(0, 102, 204));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        panel.add(registerButton);

        // Giriş butonu işlevi
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();
                String password = String.valueOf(passwordField.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Email ve şifre boş olamaz.");
                    return;
                }

                UserLoginDto loginDto = new UserLoginDto(email, password);

                userService.login(new UserLoginDto(email, password))
                        .thenAccept(result -> {
                            if (result != null && result.isSuccess()) {
                                UserDto user = result.getData();
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(null, "Hoş geldin " + user.getFirstName());

                                    dispose();
                                    switch (user.getRole()) {
                                        case ADMIN -> new AdminDashboard().setVisible(true);
                                        case HOST -> new HostDashboard().setVisible(true);
                                        case RENTER -> new RenterDashboard().setVisible(true);
                                        default -> JOptionPane.showMessageDialog(null, "Rol tanımlı değil.");
                                    }
                                });
                            } else {
                                SwingUtilities.invokeLater(() ->
                                        JOptionPane.showMessageDialog(null, "Giriş başarısız: " + (result != null ? result.getMessage() : "Hata"))
                                );
                            }
                        })
                        .exceptionally(ex -> {
                            SwingUtilities.invokeLater(() ->
                                    JOptionPane.showMessageDialog(null, "Sunucu hatası: " + ex.getMessage())
                            );
                            return null;
                        });
            }
        });

        // Kayıt butonu işlevi
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Login ekranını kapat
                IUserService userService = new UserService();
                new RegisterScreen(userService).setVisible(true); // Kayıt ekranını aç
            }
        });
    }
}


