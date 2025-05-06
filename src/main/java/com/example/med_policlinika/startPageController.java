package com.example.med_policlinika;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;




public class startPageController {
    @FXML
    private RadioButton doctorRadioButton; // Радиокнопка для выбора врача

    @FXML
    private RadioButton patientRadioButton; // Радиокнопка для выбора пациента

    @FXML
    private Button selectUserButton; // Кнопка для выбора пользователя

    @FXML
    private Label messageUserError; // Сообщение об ошибке выбора пользователя

    private String selectedUserType; // Выбранный тип пользователя

    /**
     * Инициализация контроллера.
     */
    public void initialize() {
        // Обработчики событий для радиокнопок
        doctorRadioButton.setOnAction(event -> {
                    if (doctorRadioButton.isSelected()) {
                        patientRadioButton.setSelected(false);
                        selectedUserType = "Врач";
                    }
                }
        );

        patientRadioButton.setOnAction(event -> {
                    if (patientRadioButton.isSelected()) {
                        doctorRadioButton.setSelected(false);
                        selectedUserType = "Пациент";
                    }
                }
        );

        // Обработчик события для кнопки выбора пользователя
        selectUserButton.setOnAction(event -> {
                    if (selectedUserType != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            if (selectedUserType.equals("Пациент")) {
                                loader.setLocation(getClass().getResource("service_selection.fxml"));
                            } else if (selectedUserType.equals("Врач")) {
                                loader.setLocation(getClass().getResource("autherization.fxml"));
                            }

                            Parent root = loader.load();
                            Scene scene = new Scene(root);
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Вывод выбранного типа пользователя в консоль
                        System.out.println("Выбран тип пользователя: " + selectedUserType);
                    }
                    else {
                        messageUserError.setText("Пожалуйста, выберите тип пользователя");
                    }
                }
        );
    }
}