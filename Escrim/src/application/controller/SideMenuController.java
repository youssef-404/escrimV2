package application.controller;


import java.io.IOException;

import application.model.Escrim;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class SideMenuController {
	
	private Escrim escrim;

	@FXML
    private Pane contentPane;

	@FXML
    private Button inventoryButton;

	@FXML
    private Button deployButton;

	@FXML
    private Button configurationButton;

	@FXML
    private Button statisticsButton;
	
	@FXML
	private Button logoutButton;
	
	@FXML
	private TextField deployed;

	@FXML
	private TextField notDeployed;
	
    public void initialize() {
    	this.escrim = Escrim.getInstance();  // Singleton instance of the Escrim
        setupButtonActions();  // Setup the actions for menu buttons.
        setupLogoutButtonAction();
        inventoryButton.fire();  // Automatically triggers the inventory button to load the default view.
        deployed.visibleProperty().bind(escrim.State());  // Binds visibility of the deployed TextField to the Escrim state.
        notDeployed.visibleProperty().bind(escrim.State().not());  // Binds visibility of the notDeployed TextField to the inverse of the Escrim state.
    }

    // Configures the action events for each navigation button.
    private void setupButtonActions() {
        inventoryButton.setOnAction(event -> switchPane("/application/view/inventories.fxml"));
        deployButton.setOnAction(event -> switchPane("/application/view/deploy.fxml"));
        configurationButton.setOnAction(event -> switchPane("/application/view/configuration.fxml"));
        statisticsButton.setOnAction(event -> switchPane("/application/view/statistics.fxml"));
    }

    // Loads and switches the content of the main panel to the specified FXML view.
    private void switchPane(String fxmlFile) {
        try {
            Node pane = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Sets up the action for the logout button to call the logout method.
    private void setupLogoutButtonAction() {
    	logoutButton.setOnAction(event -> logout());
    }
	
    // Handles user logout, switching from the current scene to the login scene.
	private void logout() {
		try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/view/login.fxml"));
	        Scene scene = new Scene(fxmlLoader.load());
	        
	        Stage newStage = new Stage();
	        newStage.setScene(scene);

	        Stage oldStage = (Stage) logoutButton.getScene().getWindow();
	        oldStage.close();
            
	        newStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	

}

    
    
