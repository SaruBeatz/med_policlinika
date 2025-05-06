package com.example.med_policlinika;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientAdressController {
    @FXML
    private TextField City;
    @FXML
    private TextField Street;
    @FXML
    private TextField House;
    @FXML
    private TextField Apartament;
    @FXML
    private Label BackToFirstPage;

    private int lastPatientId;

    @FXML
    private Button MakeAppointment;

    //вернуться назад в главное меню
    public void FirstPage(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start_page.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    public void MakeAppointment(MouseEvent event) throws IOException {
        try {
            Connection connection = DataBase.connectDB();

            // Получение значений из полей
            String city = City.getText().trim();
            String street = Street.getText().trim();
            String houseNumber = House.getText().trim();
            String apartment = Apartament.getText().trim();

            // Проверка, что все поля заполнены
            if (city.isEmpty() || street.isEmpty() || houseNumber.isEmpty()) {
                System.out.println("Пожалуйста, заполните все обязательные поля.");
                return;
            }

            // Получение последнего id_patient
            lastPatientId = getLastPatientId(connection);

            // Создание подготовленного оператора для вставки данных в таблицу Patients
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Patients SET City = ?, Street = ?, House = ?, Apartament = ? WHERE Id_patient = ?");
            statement.setString(1, city);
            statement.setString(2, street);
            statement.setString(3, houseNumber);
            statement.setString(4, apartment);
            statement.setInt(5, lastPatientId);

            // Выполнение запроса на обновление
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Данные пациента успешно сохранены.");
            } else {
                System.out.println("Не удалось сохранить данные пациента.");
            }

            // Закрытие соединения с базой данных
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("result_appointment.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    // получить id пациента по его имени, по последней записи
    private int getLastPatientId(Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT MAX(Id_patient) FROM Patients");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
