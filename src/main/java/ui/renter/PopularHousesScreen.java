package ui.renter;

import business.services.houseService.HouseService;
import business.services.houseService.IHouseService;
import core.session.UserSession;
import entites.dtos.HouseListDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Comparator;
import java.util.List;

public class PopularHousesScreen extends JFrame {
    private JTable popularTable;
    private DefaultTableModel model;
    private JButton backButton;

    private final IHouseService houseService = new HouseService();

    public PopularHousesScreen() {
        setTitle("Popüler Evler");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadPopularHouses();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("En Yüksek Puanlı Tiny House İlanları", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Ev Başlığı", "Konum", "Fiyat", "Puan"};
        model = new DefaultTableModel(columns, 0);
        popularTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(popularTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("← Ana Panele Dön");
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new RenterDashboard().setVisible(true);
        });
    }

    private void loadPopularHouses() {
        houseService.getAll(UserSession.currentUser.getId())
                .thenAccept(result -> {
                    if (result.isSuccess()) {
                        List<HouseListDto> houses = result.getData();
                        List<HouseListDto> popularHouses = houses.stream()
                                .filter(h -> h.getAverageRating() >= 4.5)
                                .sorted(Comparator.comparingDouble(HouseListDto::getAverageRating).reversed())
                                .toList();

                        SwingUtilities.invokeLater(() -> {
                            model.setRowCount(0);
                            for (HouseListDto house : popularHouses) {
                                model.addRow(new Object[]{
                                        house.getTitle(),
                                        house.getLocation(),
                                        house.getPrice(),
                                        house.getAverageRating()
                                });
                            }
                        });
                    } else {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                                this,
                                "Popüler evler yüklenemedi: " + result.getMessage()
                        ));
                    }
                });
    }
}
