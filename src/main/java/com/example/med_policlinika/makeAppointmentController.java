package com.example.med_policlinika;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;


/**
 * Класс для управления страницей записи на прием.
 */
public class makeAppointmentController {

    @FXML
    private ChoiceBox<String> Doctors_list; // Выбор врача из списка

    @FXML
    private TextField Surname; // Поле для ввода фамилии пациента

    @FXML
    private TextField Pat_name; // Поле для ввода имени пациента

    @FXML
    private TextField Patrnomic; // Поле для ввода отчества пациента

    @FXML
    private RadioButton male_gender; // Радиокнопка для выбора мужского пола

    @FXML
    private RadioButton female_gender; // Радиокнопка для выбора женского пола

    @FXML
    private TextField Telephone; // Поле для ввода номера телефона пациента

    @FXML
    private DatePicker Birthday; // Поле для выбора даты рождения пациента

    @FXML
    private RadioButton policlinicRadioButton; // Радиокнопка для выбора приема в поликлинике

    @FXML
    private RadioButton HomeRadioButton; // Радиокнопка для выбора приема на дому

    @FXML
    private Label VisitTypeError; // Сообщение об ошибке выбора типа приема

    @FXML
    private DatePicker Date_visit; // Поле для выбора даты приема

    private int VisitType; // Тип приема: поликлиника (1) или дом (2)

    private String Gender; // Пол пациента: мужской или женский


    /**
     * Метод инициализации контроллера.
     */
    public void initialize() {

        // Обработчики событий для радиокнопок выбора пола пациента
        male_gender.setOnAction(event -> {
                    if (male_gender.isSelected()) {
                        female_gender.setSelected(false);
                        Gender = "male";
                    }
                }
        );

        female_gender.setOnAction(event -> {
                    if (female_gender.isSelected()) {
                        male_gender.setSelected(false);
                        Gender = "female";
                    }
                }
        );

        // Обработчики событий для радиокнопок выбора типа приема
        policlinicRadioButton.setOnAction(event -> {
                    if (policlinicRadioButton.isSelected()) {
                        HomeRadioButton.setSelected(false);
                        VisitType = 1;
                    }
                }
        );

        HomeRadioButton.setOnAction(event -> {
                    if (HomeRadioButton.isSelected()) {
                        policlinicRadioButton.setSelected(false);
                        VisitType = 2;
                    }
                }
        );

        try {
            // Получение списка врачей из базы данных
            Connection connection = DataBase.connectDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT CONCAT(Doctors.Surname, ' ', Doctors.Doc_name, ' ', Doctors.Patrnomic) as Full_name from doctors");
            ObservableList<String> patientList = FXCollections.observableArrayList(); // Создаем список для хранения значений из базы данных

            while (resultSet.next()) {
                String patientName = resultSet.getString("Full_name");
                patientList.add(patientName); // Добавляем значение из базы данных в список
            }

            Doctors_list.setItems(patientList); // Устанавливаем значения из списка в ChoiceBox
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для создания записи на прием.
     */
    public void MakeAppointment(MouseEvent event) throws IOException {
        if (policlinicRadioButton.isSelected()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("result_appointment.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else if (HomeRadioButton.isSelected()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("patient_address.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            // Обработка случая, когда ни одна радиокнопка не выбрана
            VisitTypeError.setText("Выберите тип приема!");
        }

        try {

            String pat_name = Pat_name.getText();
            String pat_surname = Surname.getText();
            String pat_patronomic = Patrnomic.getText();
            String pat_telephone = Telephone.getText();
            String selectedDoctor = Doctors_list.getSelectionModel().getSelectedItem();


            // Разделение полного ФИО на отдельные компоненты
            String[] nameParts = selectedDoctor.split(" ");

            String surname = nameParts[0];
            String name = nameParts[1];
            String patronymic = nameParts[2];
            int id_doctor = findDoctorId(surname, name, patronymic);


            Connection connection = DataBase.connectDB();


            // Проверка наличия данных о человеке в таблице
            PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM Patients WHERE Pat_Name = ? AND Surname = ? AND Patrnomic = ?");
            checkStatement.setString(1, pat_name);
            checkStatement.setString(2, pat_surname);
            checkStatement.setString(3, pat_patronomic);
            ResultSet checkResult = checkStatement.executeQuery();
            checkResult.next();
            int count = checkResult.getInt(1);
            int id_patient = -1;
            LocalDate visitDate = Date_visit.getValue();

            if (count == 0) {
                // Если пациент не найден в базе данных, добавляем его
                PreparedStatement statement = connection.prepareStatement("INSERT INTO Patients (Pat_Name, Surname, Patrnomic, Telephone, Gender, Birthday) VALUES (?, ?, ?, ?, ?, ?)");
                statement.setString(1, pat_name);
                statement.setString(2, pat_surname);
                statement.setString(3, pat_patronomic);
                statement.setString(4, pat_telephone);
                statement.setString(5, Gender);
                statement.setDate(6, Date.valueOf(Birthday.getValue()));
                statement.executeUpdate();
                statement.close();
                System.out.println("Новый пациент добавлен в базу данных.");
            } else {
                System.out.println("Пациент уже существует в базе данных.");
            }


            // Получение ID пациента
            PreparedStatement FindIdPatient = connection.prepareStatement("SELECT Id_patient FROM Patients ORDER BY Id_patient DESC LIMIT 1");
            ResultSet resultSet = FindIdPatient.executeQuery();

            if (resultSet.next()) {
                id_patient = resultSet.getInt("Id_patient");
            } else {
                System.out.println("Нет данных о пациентах в базе.");
            }

            // Вставка данных о записи на прием
            PreparedStatement statement = connection.prepareStatement("INSERT INTO visit (id_doctor, id_patient, date_visit, visit_type) VALUES (?, ?, ?, ?)");
            statement.setInt(1, id_doctor);
            statement.setInt(2, id_patient);
            statement.setDate(3, java.sql.Date.valueOf(visitDate));
            statement.setInt(4, VisitType);

            // Выполнение запроса на вставку
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Данные о посещении успешно сохранены.");
            } else {
                System.out.println("Не удалось сохранить данные о посещении.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Метод для поиска ID врача по его ФИО.
     */
    private int findDoctorId(String surname, String name, String patronymic) {
        int id_doctor = -1; // Значение по умолчанию, если врач не найден

        try {
            Connection connection = DataBase.connectDB();
            PreparedStatement statement = connection.prepareStatement("SELECT id_doctor FROM Doctors WHERE Surname = ? AND Doc_name = ? AND Patrnomic = ?");
            statement.setString(1, surname);
            statement.setString(2, name);
            statement.setString(3, patronymic);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id_doctor = resultSet.getInt("id_doctor");
            }

            // Закрытие соединения с базой данных
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id_doctor;
    }
}