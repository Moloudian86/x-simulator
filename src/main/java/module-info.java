module com.example.twitter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;
    requires java.sql;


    opens com.example.twitter to javafx.fxml;
    exports com.example.twitter;
    exports UiController;
    opens UiController to javafx.fxml;
}