package com.retima.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.retima.database.DBConnection;

public class NegotiationUI extends JFrame {
    private int sellerAgentId;
    private JTextField nameField, phoneField, addressField, ageField, maritalStatusField;
    private JTextField offerPriceField, offerDurationField;
    private JButton startNegotiationButton, cancelButton;
    private JLabel resultLabel;

    public NegotiationUI(int sellerAgentId) {
        this.sellerAgentId = sellerAgentId;

        setTitle("واجهة التفاوض");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // تهيئة اللوحة الرئيسية
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // لوحة معلومات المشتري
        JPanel buyerInfoPanel = new JPanel();
        buyerInfoPanel.setLayout(new GridLayout(7, 2, 10, 10));
        buyerInfoPanel.setBorder(BorderFactory.createTitledBorder("معلومات المشتري"));

        // تهيئة مكونات معلومات المشتري
        JLabel nameLabel = new JLabel("الاسم:");
        nameField = new JTextField(20);

        JLabel phoneLabel = new JLabel("رقم الهاتف:");
        phoneField = new JTextField(20);

        JLabel addressLabel = new JLabel("العنوان:");
        addressField = new JTextField(20);

        JLabel ageLabel = new JLabel("العمر:");
        ageField = new JTextField(20);

        JLabel maritalStatusLabel = new JLabel("الحالة الاجتماعية:");
        maritalStatusField = new JTextField(20);

        JLabel offerPriceLabel = new JLabel("السعر المقترح:");
        offerPriceField = new JTextField(20);

        JLabel offerDurationLabel = new JLabel("المدة المقترحة (بالأشهر):");
        offerDurationField = new JTextField(20);

        // إضافة المكونات إلى لوحة معلومات المشتري
        buyerInfoPanel.add(nameLabel);
        buyerInfoPanel.add(nameField);

        buyerInfoPanel.add(phoneLabel);
        buyerInfoPanel.add(phoneField);

        buyerInfoPanel.add(addressLabel);
        buyerInfoPanel.add(addressField);

        buyerInfoPanel.add(ageLabel);
        buyerInfoPanel.add(ageField);

        buyerInfoPanel.add(maritalStatusLabel);
        buyerInfoPanel.add(maritalStatusField);

        buyerInfoPanel.add(offerPriceLabel);
        buyerInfoPanel.add(offerPriceField);

        buyerInfoPanel.add(offerDurationLabel);
        buyerInfoPanel.add(offerDurationField);

        // لوحة النتيجة
        JPanel resultPanel = new JPanel();
        resultPanel.setBorder(BorderFactory.createTitledBorder("نتيجة التفاوض"));
        resultLabel = new JLabel("لم يبدأ التفاوض بعد");
        resultPanel.add(resultLabel);

        // لوحة الأزرار
        JPanel buttonPanel = new JPanel();
        startNegotiationButton = new JButton("بدء التفاوض");
        cancelButton = new JButton("إلغاء");

        buttonPanel.add(startNegotiationButton);
        buttonPanel.add(cancelButton);

        // إضافة مستمعي الأحداث للأزرار
        startNegotiationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNegotiation();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // إضافة اللوحات إلى اللوحة الرئيسية
        mainPanel.add(buyerInfoPanel, BorderLayout.NORTH);
        mainPanel.add(resultPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // إضافة اللوحة الرئيسية إلى الإطار
        add(mainPanel);
    }

    private void startNegotiation() {
        // التحقق من إدخال جميع المعلومات المطلوبة
        if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                addressField.getText().isEmpty() || ageField.getText().isEmpty() ||
                maritalStatusField.getText().isEmpty() || offerPriceField.getText().isEmpty() ||
                offerDurationField.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "يرجى إدخال جميع المعلومات المطلوبة", "خطأ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // استخراج البيانات من الحقول
            String name = nameField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            int age = Integer.parseInt(ageField.getText());
            String maritalStatus = maritalStatusField.getText();
            BigDecimal offerPrice = new BigDecimal(offerPriceField.getText());
            int offerDuration = Integer.parseInt(offerDurationField.getText());

            // حفظ معلومات المشتري في قاعدة البيانات
            int buyerId = saveBuyerInfo(name, phone, address, age, maritalStatus);

            if (buyerId != -1) {
                // محاكاة عملية التفاوض (يمكن استبدالها بمنطق حقيقي للتفاوض باستخدام JADE)
                boolean negotiationResult = simulateNegotiation(sellerAgentId, buyerId, offerPrice, offerDuration);

                // تحديث واجهة المستخدم بنتيجة التفاوض
                if (negotiationResult) {
                    resultLabel.setText("تم قبول التفاوض بنجاح!");

                    // حذف الوكيل البائع (أو تحديث حالة العقار إلى مؤجر)
                    updatePropertyStatus(sellerAgentId, "RENTED");

                    // حفظ معلومات التفاوض
                    saveNegotiationResult(sellerAgentId, buyerId, true, offerPrice, offerDuration);

                    // تعطيل أزرار التفاوض
                    startNegotiationButton.setEnabled(false);

                    // عرض خيار نعم/لا
                    showYesNoDialog();
                } else {
                    resultLabel.setText("تم رفض التفاوض!");

                    // حفظ معلومات التفاوض
                    saveNegotiationResult(sellerAgentId, buyerId, false, offerPrice, offerDuration);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "تنسيق غير صحيح للأرقام", "خطأ", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "خطأ: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int saveBuyerInfo(String name, String phone, String address, int age, String maritalStatus) {
        try {
            Connection conn = DBConnection.getConnection();

            // إدراج معلومات المشتري
            String insertQuery = "INSERT INTO agents (name, phone, address, agent_type, age, marital_status) " +
                    "VALUES (?, ?, ?, 'BUYER', ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, address);
            stmt.setInt(4, age);
            stmt.setString(5, maritalStatus);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("فشل إنشاء المشتري، لم يتم إدراج أي صف");
            }

            // الحصول على المعرف الذي تم إنشاؤه
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            int buyerId = -1;

            if (generatedKeys.next()) {
                buyerId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("فشل إنشاء المشتري، لم يتم الحصول على المعرف");
            }

            stmt.close();
            return buyerId;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "خطأ في حفظ معلومات المشتري: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    private boolean simulateNegotiation(int sellerId, int buyerId, BigDecimal offerPrice, int offerDuration) {
        try {
            Connection conn = DBConnection.getConnection();

            // استعلام لاسترجاع معلومات العقار للبائع
            String query = "SELECT min_price, max_price, duration FROM properties WHERE agent_id = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                BigDecimal minPrice = rs.getBigDecimal("min_price");
                BigDecimal maxPrice = rs.getBigDecimal("max_price");
                int minDuration = rs.getInt("duration");

                // زيادة عدد جولات التفاوض
                String updateQuery = "UPDATE properties SET negotiation_rounds = negotiation_rounds + 1 WHERE agent_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, sellerId);
                updateStmt.executeUpdate();
                updateStmt.close();

                // محاكاة منطق التفاوض
                // للتبسيط: قبول العرض إذا كان السعر المعروض أعلى من الحد الأدنى وكانت المدة
                // مقبولة
                return offerPrice.compareTo(minPrice) >= 0 && offerDuration >= minDuration;
            }

            rs.close();
            stmt.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "خطأ أثناء التفاوض: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void updatePropertyStatus(int sellerId, String status) {
        try {
            Connection conn = DBConnection.getConnection();
            String updateQuery = "UPDATE properties SET status = ? WHERE agent_id = ?";

            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, status);
            stmt.setInt(2, sellerId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "خطأ في تحديث حالة العقار: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveNegotiationResult(int sellerId, int buyerId, boolean result, BigDecimal finalPrice,
            int finalDuration) {
        try {
            Connection conn = DBConnection.getConnection();

            // الحصول على معرف العقار
            String propertyQuery = "SELECT id FROM properties WHERE agent_id = ?";
            PreparedStatement propertyStmt = conn.prepareStatement(propertyQuery);
            propertyStmt.setInt(1, sellerId);
            ResultSet propertyRs = propertyStmt.executeQuery();

            int propertyId = -1;
            if (propertyRs.next()) {
                propertyId = propertyRs.getInt("id");
            }

            propertyRs.close();
            propertyStmt.close();

            if (propertyId != -1) {
                // حفظ نتيجة التفاوض
                String insertQuery = "INSERT INTO negotiations (seller_id, buyer_id, property_id, status, final_price, final_duration) "
                        +
                        "VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setInt(1, sellerId);
                insertStmt.setInt(2, buyerId);
                insertStmt.setInt(3, propertyId);
                insertStmt.setString(4, result ? "ACCEPTED" : "REJECTED");
                insertStmt.setBigDecimal(5, finalPrice);
                insertStmt.setInt(6, finalDuration);

                insertStmt.executeUpdate();
                insertStmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "خطأ في حفظ نتيجة التفاوض: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showYesNoDialog() {
        int option = JOptionPane.showConfirmDialog(this,
                "هل تريد الاستمرار مع هذا العقار؟",
                "تأكيد الإيجار",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "تم تأكيد الإيجار بنجاح!", "نجاح", JOptionPane.INFORMATION_MESSAGE);
            // حذف الوكيل البائع لأن العقار تم تأجيره
            deleteSellerAgent(sellerAgentId);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "تم إلغاء الإيجار", "إلغاء", JOptionPane.INFORMATION_MESSAGE);
            // إعادة حالة العقار إلى متاح
            updatePropertyStatus(sellerAgentId, "AVAILABLE");
        }
    }

    private void deleteSellerAgent(int sellerId) {
        try {
            Connection conn = DBConnection.getConnection();

            // حذف جميع السجلات المرتبطة في جدول negotiations
            String deleteNegotiationsQuery = "DELETE FROM negotiations WHERE seller_id = ?";
            PreparedStatement deleteNegotiationsStmt = conn.prepareStatement(deleteNegotiationsQuery);
            deleteNegotiationsStmt.setInt(1, sellerId);
            deleteNegotiationsStmt.executeUpdate();
            deleteNegotiationsStmt.close();

            // حذف السجلات المرتبطة في جدول properties
            String deletePropertiesQuery = "DELETE FROM properties WHERE agent_id = ?";
            PreparedStatement deletePropertiesStmt = conn.prepareStatement(deletePropertiesQuery);
            deletePropertiesStmt.setInt(1, sellerId);
            deletePropertiesStmt.executeUpdate();
            deletePropertiesStmt.close();

            // حذف الوكيل نفسه
            String deleteAgentQuery = "DELETE FROM agents WHERE id = ?";
            PreparedStatement deleteAgentStmt = conn.prepareStatement(deleteAgentQuery);
            deleteAgentStmt.setInt(1, sellerId);
            deleteAgentStmt.executeUpdate();
            deleteAgentStmt.close();

            System.out.println("تم حذف الوكيل البائع وجميع البيانات المرتبطة به بنجاح");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "خطأ في حذف الوكيل البائع: " + e.getMessage(), "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
