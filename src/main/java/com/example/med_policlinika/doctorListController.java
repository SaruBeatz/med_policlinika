package com.example.med_policlinika;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class doctorListController {

    @FXML
    private Hyperlink LastPage; // Гиперссылка для возвращения на предыдущую страницу

    @FXML
    private TableView<Doctor> doctorsTable; // Таблица для отображения списка врачей

    /**
     * Метод для заполнения таблицы данными из базы данных.
     */
    public void initialize() {
        try {
            Connection connection = DataBase.connectDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT doctors.Id_doctor, doctors.Doc_name, doctors.Surname, doctors.Patrnomic, " +
                    "doctors.Telephone, specializations.Name_specialization, doctor_specialization.Date_assigment " +
                    "FROM doctors " +
                    "JOIN doctor_specialization ON doctor_specialization.Id_doctor = doctors.Id_doctor " +
                    "JOIN specializations ON doctor_specialization.Id_specialization = specializations.id_specialization");

            while (resultSet.next()) {
                int idDoctor = resultSet.getInt("Id_doctor");
                String docName = resultSet.getString("Doc_name");
                String surname = resultSet.getString("Surname");
                String patronymic = resultSet.getString("Patrnomic");
                String telephone = resultSet.getString("Telephone");
                String specialization = resultSet.getString("Name_specialization");
                String dateAssignment = resultSet.getString("Date_assigment");

                Doctor doctor = new Doctor(idDoctor, docName, surname, patronymic, telephone, specialization, dateAssignment);
                doctorsTable.getItems().add(doctor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Обработчик события для гиперссылки "Назад"
        LastPage.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("service_selection.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) LastPage.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}