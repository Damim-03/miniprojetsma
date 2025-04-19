package com.retima.ui;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

import com.retima.agents.AgentManager;

public class AddSellerAgentUI extends JFrame {
    public AddSellerAgentUI() {
        setTitle("إضافة وكيل بائع");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(7, 2));
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField statusField = new JTextField();
        JTextField minPriceField = new JTextField();
        JTextField maxPriceField = new JTextField();

        formPanel.add(new JLabel("اسم الوكيل:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("الهاتف:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("العنوان:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("العمر:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("الحالة الاجتماعية:"));
        formPanel.add(statusField);
        formPanel.add(new JLabel("السعر الأدنى:"));
        formPanel.add(minPriceField);
        formPanel.add(new JLabel("السعر الأعلى:"));
        formPanel.add(maxPriceField);

        JButton addButton = new JButton("إضافة");
        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String phone = phoneField.getText();
                String address = addressField.getText();
                int age = Integer.parseInt(ageField.getText());
                String status = statusField.getText();
                BigDecimal minPrice = new BigDecimal(minPriceField.getText());
                BigDecimal maxPrice = new BigDecimal(maxPriceField.getText());

                Object[] args = new Object[] { name, phone, address, age, status, minPrice, maxPrice };
                AgentManager.createSellerAgent(name, args);

                JOptionPane.showMessageDialog(this, "تمت إضافة وكيل البائع بنجاح!");
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "حدث خطأ: " + ex.getMessage());
            }
        });

        add(formPanel, BorderLayout.CENTER);
        add(addButton, BorderLayout.SOUTH);
    }
}
