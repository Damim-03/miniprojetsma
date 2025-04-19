package com.retima.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.retima.database.DBConnection;

public class AgentListUI extends JFrame {
    private JTable agentsTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton backButton;

    public AgentListUI() {
        setTitle("قائمة الوكلاء المتاحين");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // تهيئة نموذج الجدول
        String[] columnNames = { "الرقم", "اسم الوكيل", "رقم الهاتف", "العنوان", "أقل سعر", "أعلى سعر", "المدة",
                "عدد جولات التفاوض" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // جعل الجدول غير قابل للتحرير
            }
        };

        // إنشاء الجدول
        agentsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(agentsTable);

        // إضافة مستمع أحداث للنقر المزدوج على الجدول
        agentsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = agentsTable.getSelectedRow();
                    int agentId = (int) tableModel.getValueAt(row, 0);

                    // فتح نافذة تفاصيل الوكيل
                    AgentDetailsUI detailsUI = new AgentDetailsUI(agentId);
                    detailsUI.setVisible(true);
                }
            }
        });

        // إنشاء لوحة الأزرار
        JPanel buttonPanel = new JPanel();
        refreshButton = new JButton("تحديث");
        backButton = new JButton("رجوع");

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        // إضافة مستمعي الأحداث للأزرار
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAgentsData();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // إضافة المكونات إلى الإطار
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // تحميل بيانات الوكلاء
        loadAgentsData();
    }

    private void loadAgentsData() {
        // مسح البيانات الحالية في الجدول
        tableModel.setRowCount(0);

        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT a.id, a.name, a.phone, a.address, p.min_price, p.max_price, p.duration, p.negotiation_rounds "
                    +
                    "FROM agents a JOIN properties p ON a.id = p.agent_id " +
                    "WHERE a.agent_type = 'SELLER' AND p.status = 'AVAILABLE'";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBigDecimal("min_price"),
                        rs.getBigDecimal("max_price"),
                        rs.getInt("duration"),
                        rs.getInt("negotiation_rounds")
                };
                tableModel.addRow(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "خطأ في تحميل بيانات الوكلاء: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
