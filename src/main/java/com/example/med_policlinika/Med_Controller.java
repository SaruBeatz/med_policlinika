package com.example.med_policlinika;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Med_Controller {

    // Поле для хранения id текущего врача
    public static int currentDoctorId = -1;

    // Поля ввода для логина и пароля
    @FXML
    private TextField Login;
    @FXML
    private PasswordField Password;

    /**
     * Метод для регистрации врача.
     */
    public void registerDoctor(ActionEvent event) {
        try {
            Connection connection = DataBase.connectDB();

            // Получение логина и пароля из полей ввода
            String loginText = Login.getText();
            String passwordText = Password.getText();

            // Поиск id_doctor по логину
            int id_doctor = getDoctorIdByLogin(connection, loginText);

            if (id_doctor != -1) {
                // Обновление пароля в таблице doctors
                updateDoctorPassword(connection, id_doctor, passwordText);
                showSuccessAlert("Пароль успешно установлен.");
            } else {
                showErrorAlert("Врач с таким идентификатором не найден.");
            }

            // Закрытие соединения с базой данных
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при работе с базой данных.");
        }
    }

    /**
     * Метод для авторизации врача.
     */
    public void loginDoctor(ActionEvent event) {
        try {
            Connection connection = DataBase.connectDB();

            // Получение логина и пароля из полей ввода
            String loginText = Login.getText();
            String passwordText = Password.getText();

            // Поиск id_doctor по логину и паролю
            int id_doctor = getDoctorIdByLoginAndPassword(connection, loginText, passwordText);

            if (id_doctor != -1) {
                currentDoctorId = id_doctor;
                // Успешный вход в систему
                FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor_panel.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
                // Здесь можно добавить дополнительную логику, например, переход на другое окно
            } else {
                showErrorAlert("Неверный логин или пароль.");
            }

            // Закрытие соединения с базой данных
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при работе с базой данных.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для получения id врача по логину из базы данных.
     */
    private int getDoctorIdByLogin(Connection connection, String login) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id_doctor FROM doctors WHERE id_doctor = ?");
        statement.setString(1, login);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id_doctor");
        }
        return -1;
    }

    /**
     * Метод для обновления пароля врача в базе данных.
     */
    private void updateDoctorPassword(Connection connection, int id_doctor, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE doctors SET password = ? WHERE id_doctor = ?");
        statement.setString(1, password);
        statement.setInt(2, id_doctor);
        statement.executeUpdate();
    }

    /**
     * Метод для получения id врача по логину и паролю из базы данных.
     */
    private int getDoctorIdByLoginAndPassword(Connection connection, String login, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id_doctor FROM doctors WHERE id_doctor = ? AND password = ?");
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id_doctor");
        }
        return -1;
    }

    /**
     * Метод для отображения окна с ошибкой.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Метод для отображения окна с сообщением об успешном действии.
     */
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Метод для перехода на страницу восстановления пароля.
     */
    public void ForgotPasswordMenu(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("registration.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Метод для перехода на страницу авторизации.
     */
    public void AutherizationdMenu(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("autherization.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}