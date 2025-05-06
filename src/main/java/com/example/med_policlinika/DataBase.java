package com.example.med_policlinika;
import java.sql.Connection;
import java.sql.*;
import java.sql.DriverManager;

public class DataBase {
    // Метод для установления соединения с базой данных
    public static Connection connectDB() {
        String url = "jdbc:mysql://localhost:3306/med_policlinika"; // URL базы данных
        String user = "root"; // Имя пользователя базы данных
        String password = "dimaklass1234"; // Пароль для доступа к базе данных
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Загрузка драйвера базы данных
            connection = DriverManager.getConnection(url, user, password); // Установление соединения с базой данных
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // Вывод информации об ошибке в случае возникновения исключения
        }

        return connection; // Возвращаем установленное соединение
    }
}