package ui.host;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HostCommentsScreen extends JFrame {
    private JTable commentsTable;
    private JButton backButton;

    public HostCommentsScreen() {
        setTitle("Kiracı Yorumları");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Kullanıcı Yorumları", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Sahte veri
        String[] columns = {"Ev Başlığı", "Kiracı", "Yorum", "Puan"};
        Object[][] data = {
                {"Orman Evi", "Ayşe Yılmaz", "Harika bir deneyimdi, teşekkürler!", 5},
                {"Dağ Evi", "Mehmet Demir", "Manzara güzeldi ama ulaşım zordu.", 3},
                {"Orman Evi", "Zeynep Kaya", "Ev çok temizdi, tavsiye ederim.", 4}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        commentsTable = new JTable(model);

        // 🔍 Yorum tıklanınca tam metni göster
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
}


