package ui.admin;

import business.services.userService.IUserService;
import business.services.userService.UserService;
import core.session.UserSession;
import entites.dtos.UserListDto;
import entites.dtos.UserUpdateDto;
import entites.enums.UserRole;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UserManagementScreen extends JFrame {
    private JTable userTable;
    private DefaultTableModel model;
    private JButton editButton, deleteButton, toggleStatusButton, backButton;
    private final IUserService userService = new UserService();

    public UserManagementScreen() {
        setTitle("Kullanıcı Yönetimi");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        loadUsers();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Sistemdeki Tüm Kullanıcılar", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Ad", "Soyad", "Email", "Rol", "Durum"}, 0);
        userTable = new JTable(model);
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        editButton = new JButton("✏️ Düzenle");
        deleteButton = new JButton("🗑️ Sil");
        toggleStatusButton = new JButton("🔁 Aktif/Pasif");
        backButton = new JButton("← Geri");

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(toggleStatusButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        editButton.addActionListener(this::editUser);
        deleteButton.addActionListener(this::deleteUser);
        toggleStatusButton.addActionListener(this::toggleUserStatus);
        backButton.addActionListener(e -> {
            dispose();
            new AdminDashboard().setVisible(true);
        });
    }

    private void loadUsers() {
        model.setRowCount(0);
        userService.getAll(UserSession.currentUser.getId()).thenAccept(result -> {
            if (result.isSuccess()) {
                List<UserListDto> users = result.getData();
                SwingUtilities.invokeLater(() -> {
                    for (UserListDto user : users) {
                        model.addRow(new Object[]{
                                user.getId(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getEmail(),
                                user.getRole().name().toLowerCase(),
                                user.isActive() ? "Aktif" : "Pasif"
                        });
                    }
                });
            } else {
                JOptionPane.showMessageDialog(null, "Kullanıcılar yüklenemedi: " + result.getMessage());
            }
        });
    }

    private void editUser(ActionEvent e) {
        int row = userTable.getSelectedRow();
        if (row != -1) {
            int id = (int) userTable.getValueAt(row, 0);
            String name = userTable.getValueAt(row, 1).toString();
            String surname = userTable.getValueAt(row, 2).toString();
            String email = userTable.getValueAt(row, 3).toString();
            String roleStr = userTable.getValueAt(row, 4).toString();

            String newName = JOptionPane.showInputDialog("Yeni İsim", name);
            String newSurname = JOptionPane.showInputDialog("Yeni Soyad", surname);
            String newEmail = JOptionPane.showInputDialog("Yeni E-mail", email);
            String newRoleStr = JOptionPane.showInputDialog("Yeni Rol (admin/host/renter)", roleStr);

            if (newName != null && newSurname != null && newEmail != null && newRoleStr != null) {
                try {
                    UserRole role = UserRole.valueOf(newRoleStr.toUpperCase());

                    UserUpdateDto dto = new UserUpdateDto();
                    dto.setId(id);
                    dto.setFirstName(newName);
                    dto.setLastName(newSurname);
                    dto.setEmail(newEmail);
                    dto.setRole(role);

                    userService.update(dto, UserSession.currentUser.getId()).thenAccept(res -> {
                        if (res.isSuccess()) {
                            userTable.setValueAt(newName, row, 1);
                            userTable.setValueAt(newSurname, row, 2);
                            userTable.setValueAt(newEmail, row, 3);
                            userTable.setValueAt(newRoleStr.toLowerCase(), row, 4);
                            JOptionPane.showMessageDialog(null, "Kullanıcı başarıyla güncellendi!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Güncelleme hatası: " + res.getMessage());
                        }
                    });
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, "Geçersiz rol girdiniz!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Lütfen bir kullanıcı seçin!");
        }
    }

    private void deleteUser(ActionEvent e) {
        int row = userTable.getSelectedRow();
        if (row != -1) {
            int confirm = JOptionPane.showConfirmDialog(null, "Kullanıcıyı silmek istediğinize emin misiniz?");
            if (confirm == JOptionPane.YES_OPTION) {
                int id = (int) userTable.getValueAt(row, 0);
                userService.delete(id, UserSession.currentUser.getId()).thenAccept(res -> {
                    if (res.isSuccess()) {
                        model.removeRow(row);
                        JOptionPane.showMessageDialog(null, "Kullanıcı silindi.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Silme başarısız: " + res.getMessage());
                    }
                });
            }
        }
    }

    private void toggleUserStatus(ActionEvent e) {
        int row = userTable.getSelectedRow();
        if (row != -1) {
            String currentStatus = userTable.getValueAt(row, 5).toString();
            JOptionPane.showMessageDialog(null, "Durum değiştirme işlemi yalnızca backend'de aktif/pasif güncellemesi ile yapılabilir.");
            // Not: API'de kullanıcı aktiflik durumunu değiştiren bir endpoint yoksa sadece frontend görünümünü günceller.
        }
    }
}
