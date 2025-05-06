package com.example.med_policlinika;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class makeDiagnosisController {

    @FXML
    private ChoiceBox<String> DiagnosisChoiceBox;
    @FXML
    private ChoiceBox<String> patientsChoiceBox;
    @FXML
    private Button makeDiagnosis;
    Connection connection = DataBase.connectDB();
    private Stage stage;



    public void initialize() {
        connection = DataBase.connectDB();
        updateDoctorName();
        loadDiagnosisOptions();
        loadPatientOptions();


        // Добавляем обработчик события для кнопки "Поставить диагноз"
        makeDiagnosis.setOnAction(event -> {
            stage = (Stage) makeDiagnosis.getScene().getWindow();
            saveVisitResult();
            stage.close();
        });
    }

    private void updateDoctorName() {
        try {
            // Получение имени врача по id_doctor
            String doctorName = getDoctorNameById(connection, Med_Controller.currentDoctorId);

            // Закрытие соединения с базой данных
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при работе с базой данных.");
        }
    }

    private String getDoctorNameById(Connection connection, int id_doctor) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT doc_name FROM doctors WHERE id_doctor = ?");
        statement.setInt(1, id_doctor);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("doc_name");
        }
        return "Неизвестно";
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void loadDiagnosisOptions() {
        try {
            Connection connection = DataBase.connectDB();

            // Получение списка диагнозов из таблицы diagnosis
            List<String> diagnosisList = getDiagnosisNames(connection);

            // Заполнение ChoiceBox диагнозами
            DiagnosisChoiceBox.getItems().addAll(diagnosisList);

            // Закрытие соединения с базой данных
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при работе с базой данных.");
        }
    }

    private void loadPatientOptions() {
        try {
            // Получение списка пациентов, доступных текущему врачу
            List<String> patientsList = getPatientNamesForDoctor(connection, Med_Controller.currentDoctorId);

            // Заполнение ChoiceBox пациентами
            patientsChoiceBox.getItems().addAll(patientsList);

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при работе с базой данных.");
        }
    }
    private int getPatientIdByName(String patientName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id_patient FROM patients WHERE CONCAT(Surname, ' ', LEFT(Pat_name, 1), '.', LEFT(Patrnomic, 1)) = ?");
        statement.setString(1, patientName);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id_patient");
        }
        throw new SQLException("Пациент не найден.");
    }

    private int getDiagnosisIdByName(String diagnosisName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id_diagnosis FROM diagnosis WHERE name_diagnosis = ?");
        statement.setString(1, diagnosisName);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id_diagnosis");
        }
        throw new SQLException("Диагноз не найден.");
    }

    private int getVisitIdByPatientId(int id_patient) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id_visit FROM visit WHERE id_patient = ?");
        statement.setInt(1, id_patient);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id_visit");
        }
        throw new SQLException("Визит не найден.");
    }
    private void saveVisitResultData(int id_visit, int id_diagnosis) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO visit_result (Id_visit, Id_diagnosis) VALUES (?, ?)");
        statement.setInt(1, id_visit);
        statement.setInt(2, id_diagnosis);
        statement.executeUpdate();
    }

    private void saveVisitResult() {
        try {
            // Получаем выбранные значения из ChoiceBox
            String selectedPatient = patientsChoiceBox.getSelectionModel().getSelectedItem();
            String selectedDiagnosis = DiagnosisChoiceBox.getSelectionModel().getSelectedItem();

            // Находим id_patient по выбранному пациенту
            int id_patient = getPatientIdByName(selectedPatient);

            // Находим id_diagnosis по выбранному диагнозу
            int id_diagnosis = getDiagnosisIdByName(selectedDiagnosis);

            // Находим id_visit по id_patient
            int id_visit = getVisitIdByPatientId(id_patient);

            // Сохраняем данные в таблицу visit_result
            saveVisitResultData(id_visit, id_diagnosis);

            // Обновляем интерфейс или выводим сообщение об успешном сохранении
            showSuccessAlert("Диагноз успешно сохранен.");
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при сохранении данных.");
        }
    }
    private List<String> getDiagnosisNames(Connection connection) throws SQLException {
        List<String> diagnosisList = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT name_diagnosis FROM diagnosis");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            diagnosisList.add(resultSet.getString("name_diagnosis"));
        }
        return diagnosisList;
    }

    private List<String> getPatientNamesForDoctor(Connection connection, int id_doctor) throws SQLException {
        List<String> patientsList = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("Select DISTINCT p.id_patient,CONCAT(p.Surname, ' ', LEFT(p.Pat_name, 1), '.', LEFT(p.Patrnomic, 1)) AS pat_fio from visit v\n" +
                "join patients p  ON v.Id_patient = p.Id_patient\n" +
                "join doctors d on v.id_doctor = d.id_doctor\n" +
                "Where d.id_doctor = ?");
        statement.setInt(1, id_doctor);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            patientsList.add(resultSet.getString("pat_fio"));
        }
        return patientsList;
    }
}
