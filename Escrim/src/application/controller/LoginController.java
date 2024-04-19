package application.controller;

import java.io.IOException;

import application.dao.DaoManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LoginController {
	private DaoManager daoManager;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Button submitButton;
	
    public void initialize() {
    	this.daoManager = DaoManager.getInstance();
        setupButtonActions();
    }
	
    private void setupButtonActions() {
        submitButton.setOnAction(event -> loginLogic());
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    
    private void loginLogic() {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();
        String role = daoManager.authenticate(enteredUsername, enteredPassword);
        
        if (role != null) {
            if (role.equals("Administrator")) {
                //show view.FXML;
            	loadFXML("View.fxml");
            }else if(role.equals("Medecin")){
            	System.out.println("Implement Medecien View");
            }else if(role.equals("Logisticien")){
            	System.out.println("Implement Logisticien View");
		    }else if(role.equals("Pharmacien")){
		    	System.out.println("Implement Pharmacien View");
		    }else {
                showAlert(Alert.AlertType.ERROR, "Unknown Role", "You do not have permission to access this resource.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Authentication Failed", "Invalid username or password.");
        }
    }
    
    private void loadFXML(String fxmlFileName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/view/" + fxmlFileName));
	        Scene scene = new Scene(fxmlLoader.load());
	        
	        // Get the current stage from any control within the current scene
            Stage stage = (Stage) submitButton.getScene().getWindow();
            
            stage.setScene(scene);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	
}
