package ui.host;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HostPaymentsScreen extends JFrame {
    private JTable paymentTable;
    private JButton backButton;

    public HostPaymentsScreen() {
        setTitle("Ödeme Geçmişi");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Ödeme Geçmişi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Sahte veri
        String[] columns = {"Kiracı", "Ev", "Tutar", "Tarih", "Durum"};
        Object[][] data = {
                {"Ayşe Yılmaz", "Orman Evi", "3000 TL", "2025-04-01", "Tamamlandı"},
                {"Mehmet Demir", "Dağ Evi", "4200 TL", "2025-04-12", "Bekliyor"},
                {"Zeynep Kaya", "Orman Evi", "2500 TL", "2025-04-15", "Tamamlandı"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        paymentTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("← Ana Panele Dön");
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new HostDashboard().setVisible(true);
        });
    }
}

