package com.example.med_policlinika;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class doctorPanelController {
    @FXML
    private Label UserName;
    @FXML
    private TableView<Doctor> doctorsTable;

    @FXML
    private TableColumn<Doctor, Integer> idDoctorColumn;
    @FXML
    private TableColumn<Doctor, String> fullNameColumn;
    @FXML
    private TableColumn<Doctor, String> telephoneColumn;
    @FXML
    private TableColumn<Doctor, String> dateAppointmentColumn;
    @FXML
    private TableColumn<Doctor, String> specializationColumn;
    @FXML
    private TableColumn<Doctor, String> dateAssignmentColumn;
    @FXML
    private TableView<Medicine> medicineTable;
    @FXML
    private TableColumn<Medicine, String> DrugNameColumn;
    @FXML
    private TableColumn<Medicine, String> MedDescriptionColumn;
    @FXML
    private TableColumn<Medicine, Integer> MedPriceColumn;
    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, Integer> PatientIdColumn;
    @FXML
    private TableColumn<Patient, String> patientFullNameColumn;
    @FXML
    private TableColumn<Patient, String> patientTelephoneColumn;
    @FXML
    private TableColumn<Patient, String> patientGenderColumn;
    @FXML
    private TableColumn<Patient, String> patientBirthdayColumn;
    @FXML
    private TableColumn<Patient, String> patientAddressColumn;
    @FXML
    private TableView<Visit> visitTable;
    @FXML
    private TableColumn<Visit, String> visitPatientFullNameColumn;
    @FXML
    private TableColumn<Visit, String> visitDoctorFullNameColumn;
    @FXML
    private TableColumn<Visit, String> visitDateColumn;
    @FXML
    private TableColumn<Visit, String> visitTypeColumn;
    @FXML
    private TableColumn<Visit, String> visitSymptomsColumn;
    @FXML
    private ChoiceBox<String> symptomsChoiceBox;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIDColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentFullNameColumn;
    @FXML
    private TableColumn<Appointment, String> MedicineNameColumn;
    @FXML
    private TableColumn<Appointment, Integer> appointmentDrugNumberColumn;

    @FXML
    private AnchorPane DoctorPane;
    @FXML
    private AnchorPane PatientsPane;
    @FXML
    private AnchorPane MedicinePane;
    @FXML
    private AnchorPane VisitPane;
    @FXML
    private AnchorPane AppointmentPane;

    @FXML
    private AnchorPane StartPane;
    @FXML
    private Button showDoctorsButton;
    @FXML
    private Button showPatientsButton;
    @FXML
    private Button showMedicineButton;
    @FXML
    private Button showVisitButton;
    @FXML
    private Button showAppointmentButton;

    @FXML
    private Text visitCount;
    @FXML
    private Button updateButton;
    @FXML
    private PieChart ageChart;
    @FXML
    private DatePicker visitDateField;
    private Visit selectedVisit;
    private Connection connection = DataBase.connectDB();

    public void initialize() {
        updateDoctorName();
        loadPieChartData();
        updateVisitCount();

        fetchDoctorsData(connection,doctorsTable);
        configureDoctorTableColumns();

        fetchMedicineData(connection,medicineTable);
        configureMedicineTableColumns();

        fetchPatientData(connection,patientTable);
        configurePatientsTableColumns();

        fetchVisitData(connection,visitTable);
        configureVisitTableColumns();
        setupVisitTableSelection();

        fetchAppointmentData(connection,appointmentTable);
        configureAppointmentTableColumns();

    }

    public void fetchDoctorsData(Connection connection, TableView<Doctor> doctorsTable) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT doctors.Id_doctor, CONCAT(doctors.Surname, ' ', doctors.Doc_name, ' ', doctors.Patrnomic) AS Full_Name, " +
                            "       doctors.Telephone, doctors.Date_appointment,  specializations.Name_specialization, doctor_specialization.Date_assigment " +
                            "FROM doctors " +
                            "JOIN doctor_specialization ON doctor_specialization.Id_doctor = doctors.Id_doctor " +
                            "JOIN specializations ON doctor_specialization.Id_specialization = specializations.id_specialization"
            );

            while (resultSet.next()) {
                int id = resultSet.getInt("Id_doctor");
                String fullName = resultSet.getString("Full_name");
                String telephone = resultSet.getString("Telephone");
                String dateAppointment = resultSet.getString("Date_appointment");
                String specialization = resultSet.getString("Name_specialization");
                String dateAssignment = resultSet.getString("Date_assigment");
                Doctor doctor = new Doctor(id, fullName, telephone, dateAppointment, specialization, dateAssignment);
                doctorsTable.getItems().add(doctor);
            }
        }

        catch (Exception e) {
                e.printStackTrace();
            }
        }

    private void configureDoctorTableColumns() {
        idDoctorColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        dateAppointmentColumn.setCellValueFactory(new PropertyValueFactory<>("dateAppointment"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        dateAssignmentColumn.setCellValueFactory(new PropertyValueFactory<>("dateAssignment"));
    }

    public void fetchMedicineData(Connection connection, TableView<Medicine> medicineTable) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT distinct Drug_name, med_description, med_price FROM medicine;"
            );

            while (resultSet.next()) {
                String drugName = resultSet.getString("Drug_name");
                String medDescription  = resultSet.getString("med_description");
                int medPrice = resultSet.getInt("med_price");
                Medicine medicine = new Medicine(drugName,medDescription,medPrice);
                medicine.printMedicine();
                medicineTable.getItems().add(medicine);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureMedicineTableColumns() {
        DrugNameColumn.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        MedDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("medDescription"));
        MedPriceColumn.setCellValueFactory(new PropertyValueFactory<>("medPrice"));
    }

    public void fetchPatientData(Connection connection, TableView<Patient> patientTable) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT Id_patient, CONCAT(Surname, ' ', Pat_name, ' ', Patrnomic) AS full_name, Telephone,Gender,Birthday, " +
                            "CONCAT(city, ',', street, ', д', house, ' кв.',apartament) AS address " +
                            "FROM patients ;"
            );

            while (resultSet.next()) {
                int id = resultSet.getInt("Id_patient");
                String patientFullName  = resultSet.getString("full_name");
                String patientTelephone = resultSet.getString("Telephone");
                String patientGender  = resultSet.getString("Gender");
                String patientBirthday  = resultSet.getString("Birthday");
                String patientAddress  = resultSet.getString("address");
                Patient patient = new Patient(id,patientFullName,patientTelephone,patientGender,patientBirthday,patientAddress);
                patientTable.getItems().add(patient);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurePatientsTableColumns() {
        PatientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        patientTelephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        patientGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        patientBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        patientAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    }
    private void setupVisitTableSelection() {
        visitTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedVisit = newValue;
            if (newValue != null) {
                updateSelectedVisitFields(newValue);
            }
        });
    }
    private void updateSelectedVisitFields(Visit visit) {
        String[] patientFullNameParts = visit.getPatientFullName().split(" ");
        String[] doctorFullNameParts = visit.getDocFullName().split(" ");
        visitDateField.setValue(LocalDate.parse(visit.getDateVisit()));
    }
    private int getVisitIdByFields(Connection connection, String patientFullName, String doctorFullName, String dateVisit, String visitType) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT Id_visit " +
                            "FROM Visit " +
                            "WHERE Id_patient = (SELECT Id_patient FROM patients WHERE CONCAT(Surname, ' ', LEFT(Pat_name, 1), '.', LEFT(Patrnomic, 1)) = ?) " +
                            "AND Id_doctor = (SELECT Id_doctor FROM doctors WHERE CONCAT(Surname, ' ', LEFT(Doc_Name, 1), '.', LEFT(Patrnomic, 1)) = ?) " +
                            "AND date_visit = ? " +
                            "AND visit_type = (SELECT visit_type FROM visit_type WHERE visit_desc = ?)");
            statement.setString(1, patientFullName);
            statement.setString(2, doctorFullName);
            statement.setString(3, dateVisit);
            statement.setString(4, visitType);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Id_visit");
            } else {
                System.out.println("Запись не найдена");
                return -1; // Возвращаем -1, если запись не найдена
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    @FXML
    private void updateVisit(ActionEvent event) {
        if (selectedVisit != null) {
            try {
                int visitId = getVisitIdByFields(connection, selectedVisit.getPatientFullName(), selectedVisit.getDocFullName(), selectedVisit.getDateVisit(), selectedVisit.getVisitType());
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE Visit SET " +
                                "Id_patient = (SELECT Id_patient FROM patients WHERE CONCAT(Surname, ' ', LEFT(Pat_name, 1), '.', LEFT(Patrnomic, 1)) = ?), " +
                                "Id_doctor = (SELECT Id_doctor FROM doctors WHERE CONCAT(Surname, ' ', LEFT(Doc_Name, 1), '.', LEFT(Patrnomic, 1)) = ?), " +
                                "date_visit = ?, " +
                                "Id_Symptoms = (SELECT Id_symptoms FROM Symptoms WHERE Name_symptoms = ?) " +
                                "WHERE Id_visit = ?");
                statement.setString(1, selectedVisit.getPatientFullName());
                statement.setString(2, selectedVisit.getDocFullName());
                if (visitDateField.getValue() != null) {
                    String newDateVisit = visitDateField.getValue().toString();
                    statement.setString(3, newDateVisit);
                    selectedVisit.setDateVisit(newDateVisit); // Обновляем объект Visit
                } else {
                    statement.setString(3, selectedVisit.getDateVisit());
                }
                String newSymptoms = symptomsChoiceBox.getSelectionModel().getSelectedItem();
                statement.setString(4, newSymptoms);
                selectedVisit.setSymptoms(newSymptoms); // Обновляем объект Visit

                if (visitId != -1) {
                    statement.setInt(5, visitId);
                    statement.executeUpdate();
                    visitTable.refresh();
                } else {
                    System.out.println(visitId);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void fetchVisitData(Connection connection, TableView<Visit> visitTable) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT \n" +
                            "    CONCAT(p.Surname, ' ', LEFT(p.Pat_name, 1), '.', LEFT(p.Patrnomic, 1)) AS pat_fio,\n" +
                            "    CONCAT(d.Surname, ' ', LEFT(d.Doc_Name, 1), '.', LEFT(d.Patrnomic, 1)) AS doc_fio,\n" +
                            "    v.date_visit,\n" +
                            "    vt.visit_desc,\n" +
                            "    COALESCE(GROUP_CONCAT(s.Name_symptoms), 'Симптомы не назначены') AS symptoms\n" +
                            "FROM \n" +
                            "    Visit v\n" +
                            "JOIN \n" +
                            "    patients p ON v.Id_patient = p.Id_patient\n" +
                            "JOIN \n" +
                            "    doctors d ON v.Id_doctor = d.Id_doctor\n" +
                            "LEFT JOIN \n" +
                            "    Symptoms s ON v.Id_Symptoms = s.Id_symptoms\n" +
                            "JOIN \n" +
                            "    visit_type vt ON v.visit_type = vt.visit_type\n" +
                            "GROUP BY \n" +
                            "    v.Id_patient, v.Id_doctor, v.Date_visit, v.visit_type; ;"
            );

            while (resultSet.next()) {
                String PatFullName  = resultSet.getString("pat_fio");
                String DocFullName = resultSet.getString("doc_fio");
                String dateVisit  = resultSet.getString("v.date_visit");
                String visitType  = resultSet.getString("vt.visit_desc");
                String symptoms  = resultSet.getString("symptoms");
                Visit visit = new Visit(PatFullName,DocFullName,dateVisit,visitType,symptoms);
                visitTable.getItems().add(visit);
            }
            ResultSet symptomsResultSet = statement.executeQuery("SELECT Name_symptoms FROM Symptoms");
            while (symptomsResultSet.next()) {
                symptomsChoiceBox.getItems().add(symptomsResultSet.getString("Name_symptoms"));
            }
        }


        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void configureVisitTableColumns() {
        visitPatientFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientFullName"));
        visitDoctorFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("docFullName"));
        visitDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateVisit"));
        visitTypeColumn.setCellValueFactory(new PropertyValueFactory<>("visitType"));
        visitSymptomsColumn.setCellValueFactory(new PropertyValueFactory<>("symptoms"));
    }
    public void fetchAppointmentData(Connection connection, TableView<Appointment> appointmentTable) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT \n" +
                            "\t a.id_nazn,\n" +
                            "    CONCAT(p.Surname, ' ', LEFT(p.Pat_name, 1), '.', LEFT(p.Patrnomic, 1), '.') AS patient_name,\n" +
                            "    m.Drug_name AS medicine_name,\n" +
                            "    a.Number_drugs\n" +
                            "FROM \n" +
                            "    Appointment a\n" +
                            "JOIN \n" +
                            "    Visit v ON a.Id_rez_vizit = v.Id_visit\n" +
                            "JOIN \n" +
                            "    patients p ON v.Id_patient = p.Id_patient\n" +
                            "JOIN \n" +
                            "    medicine m ON a.Id_medicine = m.Id_medicine;"
            );

            while (resultSet.next()) {
                int id = resultSet.getInt("a.id_nazn");
                String appointmentPatientName  = resultSet.getString("patient_name");
                String appointmentMedicineName = resultSet.getString("medicine_name");
                int drugsNumber  = resultSet.getInt("a.Number_drugs");

                Appointment appointment = new Appointment(id,appointmentPatientName,appointmentMedicineName,drugsNumber);
                appointmentTable.getItems().add(appointment);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void MakeDiagnosis(ActionEvent event) {
        try {
            // Загружаем FXML файл для окна "Назначить диагноз"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("make_diagnosis.fxml"));
            Parent root = loader.load();

            // Создаем новую сцену и новое окно
            Scene scene = new Scene(root);
            Stage diagnosisStage = new Stage();
            diagnosisStage.setScene(scene);
            diagnosisStage.setTitle("Назначить диагноз");
            diagnosisStage.initModality(Modality.APPLICATION_MODAL);
            diagnosisStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void configureAppointmentTableColumns() {
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointmentFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        MedicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        appointmentDrugNumberColumn.setCellValueFactory(new PropertyValueFactory<>("drugsNumber"));
    }
    private void loadPieChartData() {
        try {
            String query = "SELECT Gender as Пол, AVG(YEAR(CURDATE()) - YEAR(birthday)) AS Средний_возраст " +
                    "FROM Patients " +
                    "GROUP BY Gender";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String gender = resultSet.getString("Пол");
                double averageAge = resultSet.getDouble("Средний_возраст");
                pieChartData.add(new PieChart.Data(gender + " (" + averageAge + ")", averageAge));
            }

            ageChart.setData(pieChartData);
            ageChart.setTitle("Средний возраст пациентов");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateDoctorName() {
        try {
            Connection connection = DataBase.connectDB();

            // Получение имени врача по id_doctor
            String doctorName = getDoctorNameById(connection, Med_Controller.currentDoctorId);

            // Обновление текста Label
            UserName.setText(doctorName);

            // Закрытие соединения с базой данных
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при работе с базой данных.");
        }
    }

    private String getDoctorNameById(Connection connection, int id_doctor) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT CONCAT(doctors.Surname, ' ', LEFT(doctors.Doc_name, 1), '.', LEFT(doctors.Patrnomic, 1), '.') AS doc_name FROM doctors WHERE id_doctor = ?");
        statement.setInt(1, id_doctor);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("doc_name");
        }
        return "Неизвестно";
    }
    private void updateVisitCount() {
        try {
            String query = "SELECT COUNT(DISTINCT id_patient) as Количество_записей " +
                    "FROM visit " +
                    "WHERE date_visit BETWEEN DATE_FORMAT(CURRENT_DATE, '%Y-%m-01') AND LAST_DAY(CURRENT_DATE) " +
                    "AND id_doctor = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Med_Controller.currentDoctorId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("Количество_записей");
                visitCount.setText(String.valueOf(count));
            } else {
                visitCount.setText("0");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            visitCount.setText("Error");
        }
    }




    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void ShowDoctors(ActionEvent event) {
        DoctorPane.setManaged(true);
        DoctorPane.setVisible(true);

        PatientsPane.setManaged(false);
        PatientsPane.setVisible(false);
        MedicinePane.setManaged(false);
        MedicinePane.setVisible(false);
        VisitPane.setManaged(false);
        VisitPane.setVisible(false);
        AppointmentPane.setManaged(false);
        AppointmentPane.setVisible(false);
        StartPane.setManaged(false);
        StartPane.setVisible(false);
    }

    public void ShowPatients(ActionEvent event) {
        DoctorPane.setManaged(false);
        DoctorPane.setVisible(false);
        PatientsPane.setManaged(true);
        PatientsPane.setVisible(true);
        MedicinePane.setManaged(false);
        MedicinePane.setVisible(false);
        VisitPane.setManaged(false);
        VisitPane.setVisible(false);
        AppointmentPane.setManaged(false);
        AppointmentPane.setVisible(false);
        StartPane.setManaged(false);
        StartPane.setVisible(false);
    }

    public void ShowMedicine(ActionEvent event) {
        DoctorPane.setManaged(false);
        DoctorPane.setVisible(false);
        PatientsPane.setManaged(false);
        PatientsPane.setVisible(false);
        MedicinePane.setManaged(true);
        MedicinePane.setVisible(true);
        VisitPane.setManaged(false);
        VisitPane.setVisible(false);
        AppointmentPane.setManaged(false);
        AppointmentPane.setVisible(false);
        StartPane.setManaged(false);
        StartPane.setVisible(false);
    }

    public void ShowVisit(ActionEvent event) {
        DoctorPane.setManaged(false);
        DoctorPane.setVisible(false);
        PatientsPane.setManaged(false);
        PatientsPane.setVisible(false);
        MedicinePane.setManaged(false);
        MedicinePane.setVisible(false);
        VisitPane.setManaged(true);
        VisitPane.setVisible(true);
        AppointmentPane.setManaged(false);
        AppointmentPane.setVisible(false);
        StartPane.setManaged(false);
        StartPane.setVisible(false);
    }

    public void ShowAppointment(ActionEvent event) {
        DoctorPane.setManaged(false);
        DoctorPane.setVisible(false);
        PatientsPane.setManaged(true);
        PatientsPane.setVisible(true);
        MedicinePane.setManaged(false);
        MedicinePane.setVisible(false);
        VisitPane.setManaged(false);
        VisitPane.setVisible(false);
        AppointmentPane.setManaged(true);
        AppointmentPane.setVisible(true);
        StartPane.setManaged(false);
        StartPane.setVisible(false);
    }
}

