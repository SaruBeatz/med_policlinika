package com.example.med_policlinika;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Контроллер для отображения результата записи на прием.
 */
public class resultAppointmentController {

    @FXML
    private Text patientNameText; // Текстовое поле для имени пациента

    @FXML
    private Label visitDateLabel; // Метка для даты визита

    @FXML
    private Hyperlink backToFirstPageLink; // Гиперссылка для возврата на главную страницу

    /**
     * Метод инициализации контроллера.
     */
    public void initialize() {
        try {
            // Установка соединения с базой данных
            Connection connection = DataBase.connectDB();
            int lastPatientId = -1;
            PreparedStatement statement = connection.prepareStatement("SELECT MAX(Id_patient) FROM Patients");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                lastPatientId = resultSet.getInt(1);
            }

            // Получение имени пациента и даты визита
            String patientName = getPatientName(connection, lastPatientId);
            LocalDate visitDate = getVisitDate(connection, lastPatientId);

            // Установка текста в текстовых полях
            patientNameText.setText("Дорогой " + patientName + " !");
            visitDateLabel.setText(visitDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

            // Закрытие соединения с базой данных
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для получения имени пациента из базы данных.
     */
    private String getPatientName(Connection connection, int patientId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT Pat_Name, Surname FROM Patients WHERE Id_patient = ?");
        statement.setInt(1, patientId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String name = resultSet.getString("Pat_Name");
            String surname = resultSet.getString("Surname");
            return surname + " " + name;
        }
        return "Пациент";
    }

    /**
     * Метод для получения даты визита из базы данных.
     */
    private LocalDate getVisitDate(Connection connection, int patientId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT Date_visit FROM visit WHERE id_patient = ? ORDER BY id_visit DESC LIMIT 1");
        statement.setInt(1, patientId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getDate("Date_visit").toLocalDate();
        }
        return LocalDate.now();
    }

    /**
     * Метод для возврата на главную страницу при клике на гиперссылку.
     */
    public void backToFirstPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start_page.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}



