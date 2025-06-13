package ui.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UserManagementScreen extends JFrame {
    private JTable userTable;
    private JButton editButton, deleteButton, toggleStatusButton, backButton;

    public UserManagementScreen() {
        setTitle("Kullanıcı Yönetimi");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Sistemdeki Tüm Kullanıcılar", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"ID", "Ad", "Soyad", "Email", "Rol", "Durum"};
        Object[][] data = {
                {1, "Ayşe", "Yılmaz", "ayse@mail.com", "renter", "Aktif"},
                {2, "Mehmet", "Kaya", "mehmet@mail.com", "host", "Pasif"},
                {3, "Admin", "Yetkili", "admin@admin.com", "admin", "Aktif"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        userTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);

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

        editButton.addActionListener((ActionEvent e) -> {
            int row = userTable.getSelectedRow();
            if (row != -1) {
                String name = userTable.getValueAt(row, 1).toString();
                String surname = userTable.getValueAt(row, 2).toString();
                String email = userTable.getValueAt(row, 3).toString();
                String role = userTable.getValueAt(row, 4).toString();

                // Kullanıcıyı düzenlemek için yeni bilgileri alacağız
                String newName = JOptionPane.showInputDialog("Yeni İsim", name);
                String newSurname = JOptionPane.showInputDialog("Yeni Soyad", surname);
                String newEmail = JOptionPane.showInputDialog("Yeni E-mail", email);
                String newRole = JOptionPane.showInputDialog("Yeni Rol", role);

                if (newName != null && newSurname != null && newEmail != null && newRole != null) {
                    // Güncellenmiş verileri tabloya ekliyoruz
                    userTable.setValueAt(newName, row, 1);
                    userTable.setValueAt(newSurname, row, 2);
                    userTable.setValueAt(newEmail, row, 3);
                    userTable.setValueAt(newRole, row, 4);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Düzenlemek için bir kullanıcı seçin!");
            }
        });

        //Kullanıcı silme butonu
        deleteButton.addActionListener((ActionEvent e) -> {
            int row = userTable.getSelectedRow();
            if (row != -1) {
                int confirm = JOptionPane.showConfirmDialog(null, "Kullanıcıyı silmek istediğinize emin misiniz?");
                if (confirm == JOptionPane.YES_OPTION) {
                    model.removeRow(row);
                }
            }
        });
        //Aktif, pasif yapma butonu
        toggleStatusButton.addActionListener((ActionEvent e) -> {
            int row = userTable.getSelectedRow();
            if (row != -1) {
                String status = userTable.getValueAt(row, 5).toString();
                userTable.setValueAt(status.equals("Aktif") ? "Pasif" : "Aktif", row, 5);
            }
        });

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new AdminDashboard().setVisible(true);
        });
    }
}
