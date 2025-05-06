module com.example.med_policlinika {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.med_policlinika to javafx.fxml;
    exports com.example.med_policlinika;
}