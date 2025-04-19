package com.retima.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.retima.database.DBConnection;

public class AgentDetailsUI extends JFrame {
    private int agentId;
    private JLabel nameLabel, phoneLabel, addressLabel;
    private JLabel minPriceLabel, maxPriceLabel, durationLabel, roundsLabel;
    private JButton negotiateButton, backButton;

    public AgentDetailsUI(int agentId) {
        this.agentId = agentId;

        setTitle("تفاصيل الوكيل");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // تهيئة اللوحة الرئيسية
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // لوحة المعلومات
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(7, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // تهيئة المكونات
        JLabel nameTitle = new JLabel("الاسم:");
        nameLabel = new JLabel();

        JLabel phoneTitle = new JLabel("رقم الهاتف:");
        phoneLabel = new JLabel();

        JLabel addressTitle = new JLabel("العنوان:");
        addressLabel = new JLabel();

        JLabel minPriceTitle = new JLabel("أقل سعر:");
        minPriceLabel = new JLabel();

        JLabel maxPriceTitle = new JLabel("أعلى سعر:");
        maxPriceLabel = new JLabel();

        JLabel durationTitle = new JLabel("المدة:");
        durationLabel = new JLabel();

        JLabel roundsTitle = new JLabel("عدد جولات التفاوض:");
        roundsLabel = new JLabel();

        // إضافة المكونات إلى اللوحة
        infoPanel.add(nameTitle);
        infoPanel.add(nameLabel);

        infoPanel.add(phoneTitle);
        infoPanel.add(phoneLabel);

        infoPanel.add(addressTitle);
        infoPanel.add(addressLabel);

        infoPanel.add(minPriceTitle);
        infoPanel.add(minPriceLabel);

        infoPanel.add(maxPriceTitle);
        infoPanel.add(maxPriceLabel);

        infoPanel.add(durationTitle);
        infoPanel.add(durationLabel);

        infoPanel.add(roundsTitle);
        infoPanel.add(roundsLabel);

        // لوحة الأزرار
        JPanel buttonPanel = new JPanel();
        negotiateButton = new JButton("بدء التفاوض");
        backButton = new JButton("رجوع");

        buttonPanel.add(negotiateButton);
        buttonPanel.add(backButton);

        // إضافة مستمعي الأحداث للأزرار
        negotiateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // فتح واجهة التفاوض
                NegotiationUI negotiationUI = new NegotiationUI(agentId);
                negotiationUI.setVisible(true);
                dispose();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // إضافة اللوحات إلى اللوحة الرئيسية
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // إضافة اللوحة الرئيسية إلى الإطار
        add(mainPanel);

        // تحميل بيانات الوكيل
        loadAgentData();
    }

    private void loadAgentData() {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT a.name, a.phone, a.address, p.min_price, p.max_price, p.duration, p.negotiation_rounds "
                    +
                    "FROM agents a JOIN properties p ON a.id = p.agent_id " +
                    "WHERE a.id = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, agentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameLabel.setText(rs.getString("name"));
                phoneLabel.setText(rs.getString("phone"));
                addressLabel.setText(rs.getString("address"));
                minPriceLabel.setText(rs.getBigDecimal("min_price").toString());
                maxPriceLabel.setText(rs.getBigDecimal("max_price").toString());
                durationLabel.setText(rs.getInt("duration") + " شهر");
                roundsLabel.setText(String.valueOf(rs.getInt("negotiation_rounds")));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "خطأ في تحميل بيانات الوكيل: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
