module Escrim {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.base;

	opens application to javafx.graphics, javafx.fxml;
	opens application.controller to javafx.graphics, javafx.fxml;
    opens application.model to javafx.base;

}
