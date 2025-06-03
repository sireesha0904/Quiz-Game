module QuizGame {
    requires javafx.controls;
    requires java.sql;
    requires javafx.fxml;
    requires javafx.graphics;

    opens auth to javafx.fxml;
    opens services to javafx.fxml; // if you use FXML in services later
    opens controllers to javafx.fxml;  // <-- add this line
    opens application to javafx.graphics, javafx.fxml;
}
