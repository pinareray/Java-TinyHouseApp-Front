package ui.host;

import business.services.commentService.CommentService;
import business.services.commentService.ICommentService;
import business.services.houseService.HouseService;
import business.services.houseService.IHouseService;
import core.session.UserSession;
import entites.dtos.CommentListDto;
import entites.dtos.HouseListDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HostCommentsScreen extends JFrame {
    private JTable commentsTable;
    private DefaultTableModel model;
    private JButton backButton;

    private final ICommentService commentService = new CommentService();
    private final IHouseService houseService = new HouseService();

    public HostCommentsScreen() {
        setTitle("Kiracı Yorumları");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadComments();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Kullanıcı Yorumları", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Ev Başlığı", "Kiracı", "Yorum", "Puan"};
        model = new DefaultTableModel(columns, 0);
        commentsTable = new JTable(model);

        commentsTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = commentsTable.getSelectedRow();
            if (!e.getValueIsAdjusting() && selectedRow != -1) {
                String yorum = commentsTable.getValueAt(selectedRow, 2).toString();
                JOptionPane.showMessageDialog(null, yorum, "Tam Yorum", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JScrollPane scrollPane = new JScrollPane(commentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("← Ana Panele Dön");
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HostDashboard().setVisible(true);
        });
    }

    private void loadComments() {
        int hostId = UserSession.currentUser.getId();

        houseService.getByHostId(hostId, hostId).thenAccept(houseRes -> {
            if (houseRes.isSuccess()) {
                List<HouseListDto> hostHouses = houseRes.getData();
                List<Integer> hostHouseIds = hostHouses.stream().map(HouseListDto::getId).toList();

                // ID → Başlık eşlemesi (ev başlığı için)
                Map<Integer, String> houseTitleMap = hostHouses.stream()
                        .collect(Collectors.toMap(HouseListDto::getId, HouseListDto::getTitle));

                commentService.getAll().thenAccept(commentRes -> {
                    if (commentRes.isSuccess()) {
                        List<CommentListDto> allComments = commentRes.getData();

                        List<CommentListDto> hostComments = allComments.stream()
                                .filter(c -> hostHouseIds.contains(c.getHouseId()))
                                .toList();

                        SwingUtilities.invokeLater(() -> {
                            model.setRowCount(0);
                            for (CommentListDto comment : hostComments) {
                                model.addRow(new Object[]{
                                        houseTitleMap.get(comment.getHouseId()),
                                        comment.getUserFullName(),
                                        comment.getContent(),
                                        comment.getRating()
                                });
                            }
                        });
                    } else {
                        showError("Yorumlar yüklenemedi: " + commentRes.getMessage());
                    }
                });
            } else {
                showError("Evler yüklenemedi: " + houseRes.getMessage());
            }
        });
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, message, "Hata", JOptionPane.ERROR_MESSAGE)
        );
    }
}
