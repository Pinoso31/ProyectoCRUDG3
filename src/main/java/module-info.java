module com.example.proyectocrudg3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.proyectocrudg3 to javafx.fxml;
    exports com.example.proyectocrudg3;

    exports  com.example.proyectocrudg3.model to javafx.base; // model

    opens com.example.proyectocrudg3.controlller to javafx.fxml;
    exports com.example.proyectocrudg3.Application;
    opens com.example.proyectocrudg3.Application to javafx.fxml; // controller
}