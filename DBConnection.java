package com.retima.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // ✅ تم تصحيح اسم قاعدة البيانات هنا من real_estate_agents إلى house_rentals
    private static final String URL = "jdbc:mysql://localhost:3306/house_rentals";
    private static final String USER = "root"; // اسم المستخدم
    private static final String PASSWORD = "chiraz2003"; // كلمة المرور

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ تم الاتصال بقاعدة البيانات بنجاح!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ خطأ في الاتصال بقاعدة البيانات: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("✅ تم إغلاق الاتصال بقاعدة البيانات!");
            } catch (SQLException e) {
                System.err.println("❌ خطأ في إغلاق الاتصال بقاعدة البيانات: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
