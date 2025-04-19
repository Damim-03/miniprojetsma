package com.retima.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    private JButton viewAgentsButton;
    private JButton addSellerAgentButton;
    private JButton addBuyerAgentButton;
    private JButton exitButton;

    public MainMenu() {
        setTitle("نظام وكلاء العقارات");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // تهيئة المكونات
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        viewAgentsButton = new JButton("عرض الوكلاء المتاحين");
        addSellerAgentButton = new JButton("إضافة وكيل بائع جديد");
        addBuyerAgentButton = new JButton("إضافة وكيل مشتري جديد");
        exitButton = new JButton("خروج");

        // إضافة المكونات إلى اللوحة
        panel.add(viewAgentsButton);
        panel.add(addSellerAgentButton);
        panel.add(addBuyerAgentButton);
        panel.add(exitButton);

        // إضافة اللوحة إلى الإطار
        add(panel);

        // أحداث الأزرار
        viewAgentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AgentListUI agentListUI = new AgentListUI();
                agentListUI.setVisible(true);
            }
        });

        addSellerAgentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSellerAgentUI sellerForm = new AddSellerAgentUI();
                sellerForm.setVisible(true);
            }
        });

        addBuyerAgentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddBuyerAgentUI buyerForm = new AddBuyerAgentUI();
                buyerForm.setVisible(true);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
