package application.controller;


import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SideMenuControllerProfile3 {

	@FXML
    private Pane contentPane;

	@FXML
    private Button inventoryButton;

	@FXML
    private Button medicamentButton;
	@FXML
    private Button medicalSupplyButton;
	@FXML
    private Button prescribeButton;
	@FXML
	private Button logoutButton;


    public void initialize() {
        setupButtonActions();
        setupLogoutButtonAction();
        inventoryButton.fire();
    }

    private void setupButtonActions() {
        inventoryButton.setOnAction(event -> switchPane("/application/view/inventoriesProfile2.fxml"));
        medicamentButton.setOnAction(event -> switchPane("/application/view/medicaments.fxml"));
        medicalSupplyButton.setOnAction(event -> switchPane("/application/view/medicalSupply.fxml"));
        prescribeButton.setOnAction(event -> switchPane("/application/view/prescribe.fxml"));
    }

    private void switchPane(String fxmlFile) {
        try {
            Node pane = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupLogoutButtonAction() {
    	logoutButton.setOnAction(event -> logout());
    }
	
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

    
    
