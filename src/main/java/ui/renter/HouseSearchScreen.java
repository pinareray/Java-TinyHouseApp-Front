package ui.renter;

import business.services.houseService.HouseService;
import business.services.houseService.IHouseService;
import core.session.UserSession;
import entites.dtos.HouseListDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class HouseSearchScreen extends JFrame {
    private JTable houseTable;
    private JButton detailButton, backButton;

    public HouseSearchScreen() {
        setTitle("Ev Ara / Listele");
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

        JLabel titleLabel = new JLabel("Aktif Tiny House İlanları", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Yeni sütun başlıkları eklendi
        String[] columns = {"İlan ID", "Başlık", "Fiyat", "Konum", "Durum", "Yorum Sayısı", "Ortalama Puan"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        houseTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(houseTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        detailButton = new JButton("🔍 Detayları Gör");
        backButton = new JButton("← Ana Panele Dön");

        buttonPanel.add(detailButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        detailButton.addActionListener((ActionEvent e) -> {
            int selectedRow = houseTable.getSelectedRow();
            if (selectedRow != -1) {
                int houseId = (int) houseTable.getValueAt(selectedRow, 0);
                dispose();
                new HouseDetailScreen(houseId).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen detayını görmek için bir ev seçin!");
            }
        });

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new RenterDashboard().setVisible(true);
        });
    }

    private void loadHouseData() {
        IHouseService houseService = new HouseService();
        houseService.getAll(UserSession.currentUser.getId()).thenAccept(result -> {
            if (result.isSuccess()) {
                List<HouseListDto> houses = result.getData();

                SwingUtilities.invokeLater(() -> {
                    DefaultTableModel model = (DefaultTableModel) houseTable.getModel();
                    model.setRowCount(0); // önceki verileri temizle

                    for (HouseListDto house : houses) {
                        if ("Aktif".equalsIgnoreCase(house.getStatus())) {
                            Object[] row = {
                                    house.getId(),
                                    house.getTitle(),
                                    house.getPrice(),
                                    house.getLocation(),
                                    house.getStatus(),
                                    house.getCommentCount(),
                                    String.format("%.1f", house.getAverageRating())
                            };
                            model.addRow(row);
                        }
                    }
                });
            } else {
                JOptionPane.showMessageDialog(null, "Evler yüklenemedi: " + result.getMessage());
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Sunucu hatası: " + ex.getMessage());
            return null;
        });
    }

}
