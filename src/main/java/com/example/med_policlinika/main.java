package com.example.med_policlinika;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Загрузка разметки из файла "razmetka.fxml"
            Parent root = FXMLLoader.load(getClass().getResource("start_page.fxml"));

            // Создание сцены с указанными размерами
            Scene scene = new Scene(root, 800, 500);


            // Установка заголовка окна
            primaryStage.setTitle("Приложение Медицинского центра 'ШАХ' ");

            // Установка сцены в окне и отображение окна
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Запуск приложения
        launch(args);
    }
}
