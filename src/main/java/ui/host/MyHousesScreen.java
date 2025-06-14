package ui.host;

import business.services.houseService.HouseService;
import business.services.houseService.IHouseService;
import core.session.UserSession;
import entites.dtos.HouseDto;
import entites.dtos.HouseListDto;
import entites.dtos.HouseUpdateDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MyHousesScreen extends JFrame {
    private JTable houseTable;
    private JButton addButton, editButton, deleteButton, deleteRealButton, backButton;

    public MyHousesScreen() {
        setTitle("İlanlarımı Yönet");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadHouseData();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("İlanlarım", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"İlan ID", "Başlık", "Fiyat", "Konum", "Durum"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        houseTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(houseTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        addButton = new JButton("➕ İlan Ekle");
        editButton = new JButton("✏️ İlan Düzenle");
        deleteButton = new JButton("🗑️ İlanı Pasif Yap");
        deleteRealButton = new JButton("❌ İlanı Sil");
        backButton = new JButton("← Ana Panele Dön");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(deleteRealButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Buton İşlevleri
        addButton.addActionListener((ActionEvent e) -> {
            dispose();
            new AddHouseScreen().setVisible(true);
        });

        editButton.addActionListener((ActionEvent e) -> {
            int selectedRow = houseTable.getSelectedRow();
            if (selectedRow != -1) {
                int houseId = (int) houseTable.getValueAt(selectedRow, 0);
                IHouseService houseService = new HouseService();
                houseService.getById(houseId, UserSession.currentUser.getId()).thenAccept(result -> {
                    if (result.isSuccess()) {
                        SwingUtilities.invokeLater(() -> {
                            dispose();
                            new EditHouseScreen(result.getData()).setVisible(true);
                        });
                    } else {
                        JOptionPane.showMessageDialog(null, "Hata: " + result.getMessage());
                    }
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Sunucu hatası: " + ex.getMessage());
                    return null;
                });
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen düzenlemek için bir ilan seçin!");
            }
        });

        deleteButton.addActionListener((ActionEvent e) -> {
            int selectedRow = houseTable.getSelectedRow();
            if (selectedRow != -1) {
                int houseId = (int) houseTable.getValueAt(selectedRow, 0);
                IHouseService houseService = new HouseService();
                houseService.getById(houseId, UserSession.currentUser.getId()).thenAccept(result -> {
                    if (result.isSuccess()) {
                        HouseDto house = result.getData();

                        HouseUpdateDto updateDto = new HouseUpdateDto();
                        updateDto.setId(house.getId());
                        updateDto.setTitle(house.getTitle());
                        updateDto.setDescription(house.getDescription());
                        updateDto.setPrice(house.getPrice());
                        updateDto.setLocation(house.getLocation());
                        updateDto.setStatus("Pasif");
                        updateDto.setAvailableFrom(house.getAvailableFrom());
                        updateDto.setAvailableTo(house.getAvailableTo());
                        updateDto.setRequesterId(UserSession.currentUser.getId());

                        houseService.update(updateDto).thenAccept(updateResult -> {
                            if (updateResult.isSuccess()) {
                                JOptionPane.showMessageDialog(null, "İlan pasif hale getirildi.");
                                loadHouseData();
                            } else {
                                JOptionPane.showMessageDialog(null, "Hata: " + updateResult.getMessage());
                            }
                        }).exceptionally(ex -> {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Sunucu hatası: " + ex.getMessage());
                            return null;
                        });
                    }
                });
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen pasif yapmak için bir ilan seçin!");
            }
        });

        deleteRealButton.addActionListener((ActionEvent e) -> {
            int selectedRow = houseTable.getSelectedRow();
            if (selectedRow != -1) {
                int houseId = (int) houseTable.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Bu ilanı kalıcı olarak silmek istediğinize emin misiniz?",
                        "Silme Onayı", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    IHouseService houseService = new HouseService();
                    houseService.delete(houseId, UserSession.currentUser.getId())
                            .thenAccept(result -> {
                                if (result.isSuccess()) {
                                    JOptionPane.showMessageDialog(null, "İlan kalıcı olarak silindi.");
                                    loadHouseData();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Silme hatası: " + result.getMessage());
                                }
                            }).exceptionally(ex -> {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Sunucu hatası: " + ex.getMessage());
                                return null;
                            });
                }
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen silmek için bir ilan seçin!");
            }
        });

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HostDashboard().setVisible(true);
        });
    }

    private void loadHouseData() {
        IHouseService houseService = new HouseService();
        houseService.getByHostId(UserSession.currentUser.getId(), UserSession.currentUser.getId())
                .thenAccept(result -> {
                    if (result.isSuccess()) {
                        SwingUtilities.invokeLater(() -> {
                            String[] columns = {"ID", "Başlık", "Fiyat", "Konum", "Durum"};
                            DefaultTableModel model = new DefaultTableModel(columns, 0);

                            for (HouseListDto house : result.getData()) {
                                Object[] row = {
                                        house.getId(),
                                        house.getTitle(),
                                        house.getPrice(),
                                        house.getLocation(),
                                        house.getStatus()
                                };
                                model.addRow(row);
                            }

                            houseTable.setModel(model);
                        });
                    } else {
                        JOptionPane.showMessageDialog(null, "İlanlar yüklenemedi: " + result.getMessage());
                    }
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Sunucu hatası: " + ex.getMessage());
                    return null;
                });
    }
}
