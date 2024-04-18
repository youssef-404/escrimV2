package application.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class SideMenuController {

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


    public void initialize() {
        setupButtonActions();
        inventoryButton.fire();
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
    


}

    
    
