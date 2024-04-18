//package application.controller;
//
//import java.sql.SQLException;
//
//import application.dao.*;
//import application.model.*;
//import application.view.*;
//
//public class Controller {
//	private ModelInterface model;
//	private DaoManager daoManager;
//	
//	public Controller(ViewInterface view, ModelInterface model) {
//		super();
//		this.model = model;
//		this.daoManager = new DaoManager();
//		
//	}
//	
//	
////	public void changePage(String page) {
//////		view.loadPage(page);
////		
////	}
//	
////	public Inventory getInventory() {
////		try {
////			this.model.setGlobalInventory(daoManager.getInventory());
////			
////			
////			return this.model.getGlobalInventory();
////			
////		} catch (SQLException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		return null;
////	}
////	
//}
package application.controller;

import java.sql.SQLException;

import application.dao.DaoManager;
import application.model.Escrim;
import application.model.ModelInterface;
import application.model.Parcel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    
    
