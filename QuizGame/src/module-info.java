module QuizGame {
	requires javafx.controls;
	requires java.sql;
	requires javafx.fxml;

    opens auth to javafx.fxml;
    opens services to javafx.fxml; // if you use FXML in services later

	opens application to javafx.graphics, javafx.fxml;
}
