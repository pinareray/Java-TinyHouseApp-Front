package ui.admin;

import business.services.houseService.HouseService;
import business.services.houseService.IHouseService;
import core.session.UserSession;
import entites.dtos.HouseCreateDto;
import entites.dtos.HouseListDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class HouseManagementScreen extends JFrame {
    private JTable houseTable;
    private DefaultTableModel model;
    private JButton backButton, editHouseButton, deleteHouseButton, addHouseButton;
    private final IHouseService houseService = new HouseService();

    public HouseManagementScreen() {
        setTitle("İlan Yönetimi");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        loadHouses();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Tüm Tiny House İlanları", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"İlan ID", "Ev Sahibi ID", "Başlık", "Fiyat", "Konum", "Durum"}, 0);
        houseTable = new JTable(model);
        panel.add(new JScrollPane(houseTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        editHouseButton = new JButton("✏️ Düzenle");
        deleteHouseButton = new JButton("🗑️ Sil");
        addHouseButton = new JButton("➕ İlan Ekle");
        backButton = new JButton("← Admin Paneline Dön");

        buttonPanel.add(editHouseButton);
        buttonPanel.add(deleteHouseButton);
        buttonPanel.add(addHouseButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        editHouseButton.addActionListener(this::editHouse);
        deleteHouseButton.addActionListener(this::deleteHouse);
        addHouseButton.addActionListener(this::addHouse);
        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new AdminDashboard().setVisible(true);
        });
    }

    private void loadHouses() {
        model.setRowCount(0);
        houseService.getAll(UserSession.currentUser.getId()).thenAccept(result -> {
            if (result.isSuccess()) {
                List<HouseListDto> houses = result.getData();
                SwingUtilities.invokeLater(() -> {
                    for (HouseListDto h : houses) {
                        model.addRow(new Object[]{
                                h.getId(),
                                h.getHost().getId(),
                                h.getTitle(),
                                h.getPrice(),
                                h.getLocation(),
                                h.getStatus()
                        });
                    }
                });
            } else {
                JOptionPane.showMessageDialog(null, "İlanlar yüklenemedi: " + result.getMessage());
            }
        });
    }

    private void editHouse(ActionEvent e) {
        int row = houseTable.getSelectedRow();
        if (row != -1) {
            String title = houseTable.getValueAt(row, 2).toString();
            String price = houseTable.getValueAt(row, 3).toString();
            String location = houseTable.getValueAt(row, 4).toString();

            String newTitle = JOptionPane.showInputDialog("Yeni Başlık", title);
            String newPrice = JOptionPane.showInputDialog("Yeni Fiyat", price);
            String newLocation = JOptionPane.showInputDialog("Yeni Konum", location);

            if (newTitle != null && newPrice != null && newLocation != null) {
                houseTable.setValueAt(newTitle, row, 2);
                houseTable.setValueAt(Double.parseDouble(newPrice), row, 3);
                houseTable.setValueAt(newLocation, row, 4);
                // ❗ İsteğe bağlı olarak burada backend'e güncelleme gönderilebilir
            }
        } else {
            JOptionPane.showMessageDialog(null, "Lütfen bir ilan seçin!");
        }
    }

    private void deleteHouse(ActionEvent e) {
        int row = houseTable.getSelectedRow();
        if (row != -1) {
            int id = (int) houseTable.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "İlanı silmek istiyor musunuz?");
            if (confirm == JOptionPane.YES_OPTION) {
                houseService.delete(id, UserSession.currentUser.getId()).thenAccept(res -> {
                    if (res.isSuccess()) {
                        SwingUtilities.invokeLater(() -> model.removeRow(row));
                        JOptionPane.showMessageDialog(null, "İlan silindi.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Silme başarısız: " + res.getMessage());
                    }
                });
            }
        }
    }

    private void addHouse(ActionEvent e) {
        String title = JOptionPane.showInputDialog("Başlık");
        String priceStr = JOptionPane.showInputDialog("Fiyat");
        String location = JOptionPane.showInputDialog("Konum");

        if (title != null && priceStr != null && location != null) {
            try {
                double price = Double.parseDouble(priceStr);
                // Yeni ev DTO'su oluştur
                HouseCreateDto dto = new HouseCreateDto();
                dto.setTitle(title);
                dto.setPrice(price);
                dto.setLocation(location);
                dto.setStatus("Aktif");
                dto.setRequesterId(UserSession.currentUser.getId());
                dto.setHostId(UserSession.currentUser.getId());

                houseService.add(dto).thenAccept(result -> {
                    if (result.isSuccess()) {
                        // Başarılıysa tabloyu güncelle
                        SwingUtilities.invokeLater(() -> {
                            DefaultTableModel m = (DefaultTableModel) houseTable.getModel();
                            m.addRow(new Object[]{
                                    result.getData().getId(),
                                    result.getData().getHost().getId(),
                                    result.getData().getTitle(),
                                    result.getData().getPrice(),
                                    result.getData().getLocation(),
                                    result.getData().getStatus()
                            });
                        });
                        JOptionPane.showMessageDialog(null, "İlan başarıyla eklendi.");
                    } else {
                        JOptionPane.showMessageDialog(null, "İlan eklenemedi: " + result.getMessage());
                    }
                });
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Fiyat sayısal olmalı!");
            }
        }
    }
}
