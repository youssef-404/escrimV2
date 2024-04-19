package application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import application.dao.DaoManager;
import application.model.Escrim;
import application.model.Item;
import application.model.Medicaments;
import application.model.ModelInterface;
import application.model.Parcel;
import application.model.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MedicalSupplyController {

	private ModelInterface model;
	private DaoManager daoManger;
    @FXML
    private TableColumn<Utility, Integer> utilityId;
    @FXML
    private TableColumn<Utility, String> designation;
    @FXML
    private TableColumn<Utility, String> utilityDlu;
    @FXML
    private TableColumn<Utility, Integer> utilityQuantity; 
    @FXML
    private TableColumn<Utility, Integer> utilityLot;
    @FXML
    private TableView<Utility> tableView;
    @FXML
    private TextField searchField;
    
    public void initialize() {
    	this.daoManger = DaoManager.getInstance();
    	this.model = Escrim.getInstance();
    	
    	try {
			this.model.setLocalInventory(this.daoManger.getInventory("Stock Deployed"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	loadDataUtilities(-1);
    	initializeSearch();
 
    }
     
    
    public void loadDataUtilities(int index) {
     	utilityId.setCellValueFactory(new PropertyValueFactory<Utility, Integer>("id"));
    	designation.setCellValueFactory(new PropertyValueFactory<Utility, String>("designation"));
		utilityDlu.setCellValueFactory(new PropertyValueFactory<Utility, String>("dlu"));
		utilityQuantity.setCellValueFactory(new PropertyValueFactory<Utility, Integer>("quantity"));
		utilityLot.setCellValueFactory(new PropertyValueFactory<Utility, Integer>("parcel"));
 		
		List<Utility> allUtilities = new ArrayList<>(); 
		for(Parcel parcel:this.model.getLocalInventory().getPackages()) {
			allUtilities.addAll(parcel.getUtilities());
		}
		
		Utility findUtility =allUtilities.stream()
        .filter(util -> util.getId()==index)
        .findFirst()
        .orElse(null);

		ObservableList<Utility> data ;
		if(findUtility ==null) {
			 data = FXCollections.observableArrayList(allUtilities); 
		}else {
			 data = FXCollections.observableArrayList(findUtility); 
		}
        tableView.setItems(data);

			
		
		} 	   
   
 
    public void initializeSearch() {
    	searchField.textProperty().addListener((observable, oldValue, newValue) -> {
    		int index ;
			try {
				index =Integer.parseInt(newValue);
				
			} catch (Exception e) {
				index=-1;
			}
			
			loadDataUtilities(index);

    	});
    }
}