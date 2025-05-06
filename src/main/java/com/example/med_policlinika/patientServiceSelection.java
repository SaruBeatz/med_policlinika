package com.example.med_policlinika;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;





/**
 * Класс для выбора услуги пациентом: запись на прием или просмотр списка врачей.
 */
public class patientServiceSelection {

    @FXML
    private RadioButton DoctorListRadioButton; // Радиокнопка для выбора просмотра списка врачей

    @FXML
    private RadioButton MakeAppointmentRadioButton; // Радиокнопка для выбора записи на прием

    @FXML
    private Button ServiceChoiseButton; // Кнопка для подтверждения выбора услуги

    @FXML
    private Label messageErrorChoice; // Сообщение об ошибке выбора услуги

    private String selectedRadioButton; // Выбранная радиокнопка

    /**
     * Метод инициализации контроллера.
     */
    public void initialize() {
        // Обработчики событий для радиокнопок выбора услуги
        MakeAppointmentRadioButton.setOnAction(event -> {
                    if (MakeAppointmentRadioButton.isSelected()) {
                        DoctorListRadioButton.setSelected(false);
                        selectedRadioButton = "Визит";
                    }
                }
        );

        DoctorListRadioButton.setOnAction(event -> {
                    if (DoctorListRadioButton.isSelected()) {
                        MakeAppointmentRadioButton.setSelected(false);
                        selectedRadioButton = "Врачи";
                    }
                }
        );

        // Обработчик события для кнопки подтверждения выбора услуги
        ServiceChoiseButton.setOnAction(event -> {
                    if (selectedRadioButton != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            if (selectedRadioButton.equals("Визит")) {
                                loader.setLocation(getClass().getResource("make_appointment.fxml"));
                            } else if (selectedRadioButton.equals("Врачи")) {
                                loader.setLocation(getClass().getResource("doctor_list.fxml"));
                            }

                            Parent root = loader.load();
                            Scene scene = new Scene(root);
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        messageErrorChoice.setText("Пожалуйста, выберите одну из функций");
                    }
                }
        );
    }
}