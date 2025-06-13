package ui.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PaymentManagementScreen extends JFrame {
    private JTable paymentTable;
    private JButton backButton;

    public PaymentManagementScreen() {
        setTitle("Ödeme Yönetimi");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel titleLabel = new JLabel("Ödeme Geçmişi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Sahte veri
        String[] columns = {"Ödeme ID", "Rezervasyon ID", "Tutar", "Ödeme Tarihi", "Yöntem"};
        Object[][] data = {
                {201, 101, 1500.00, "2025-04-01", "Kredi Kartı"},
                {202, 102, 2500.00, "2025-05-05", "Havale"},
                {203, 103, 1800.00, "2025-06-15", "Kredi Kartı"}
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columns);
        paymentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("← Admin Paneline Dön");
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new AdminDashboard().setVisible(true);
        });
    }
}
