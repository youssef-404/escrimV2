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
    	this.escrim = Escrim.getInstance();
        setupButtonActions();
        setupLogoutButtonAction();
        inventoryButton.fire();
//        handleEscrimState(escrim);
        deployed.visibleProperty().bind(escrim.State());
        notDeployed.visibleProperty().bind(escrim.State().not());
    }

    private void setupButtonActions() {
        inventoryButton.setOnAction(event -> switchPane("/application/view/inventories.fxml"));
        deployButton.setOnAction(event -> switchPane("/application/view/deploy.fxml"));
        configurationButton.setOnAction(event -> switchPane("/application/view/configuration.fxml"));
        statisticsButton.setOnAction(event -> switchPane("/application/view/statistics.fxml"));
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
	
//	private void handleEscrimState(Escrim escrim) {
//		if (escrim.isState()) {
//			deployed.setVisible(true);
//			notDeployed.setVisible(false);
//		} else {
//			deployed.setVisible(false);
//			notDeployed.setVisible(true);
//		}
//	}
	
	

}

    
    
