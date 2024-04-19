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

public class MedicamentController {

	private ModelInterface model;
	private DaoManager daoManger;
    @FXML
    private TableColumn<Medicaments, Integer> medId;
    @FXML
    private TableColumn<Medicaments, String> medProduit;
    @FXML
    private TableColumn<Medicaments, String> medDci;
    @FXML
    private TableColumn<Medicaments, String> medDosage;
    @FXML
    private TableColumn<Medicaments, String> medDlu;
    @FXML
    private TableColumn<Medicaments, Integer> medQuantity; 
    @FXML
    private TableColumn<Medicaments, String> medClass;
    @FXML
    private TableColumn<Medicaments, Integer> medLot;
    @FXML
    private TableView<Medicaments> tableView;
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
    	   	
    	loadDataMeds(-1);
    	initializeSearch();
 
    }
     
    
    public void loadDataMeds(int index) {
     	
    		medId.setCellValueFactory(new PropertyValueFactory<Medicaments, Integer>("id"));
    		medProduit.setCellValueFactory(new PropertyValueFactory<Medicaments, String>("nom"));
 			medDci.setCellValueFactory(new PropertyValueFactory<Medicaments, String>("dci"));
 			medDosage.setCellValueFactory(new PropertyValueFactory<Medicaments, String>("dosage"));
 			medDlu.setCellValueFactory(new PropertyValueFactory<Medicaments, String>("dlu"));
 			medQuantity.setCellValueFactory(new PropertyValueFactory<Medicaments, Integer>("quantity"));
 			medClass.setCellValueFactory(new PropertyValueFactory<Medicaments, String>("medClass"));
 			medLot.setCellValueFactory(new PropertyValueFactory<Medicaments, Integer>("lot"));
 		
 			List<Medicaments> allMeds = new ArrayList<>(); 
			for(Parcel parcel:this.model.getLocalInventory().getPackages()) {
				allMeds.addAll(parcel.getMedicaments());
			}
			
			Medicaments findMed =allMeds.stream()
	        .filter(med -> med.getId()==index)
	        .findFirst()
	        .orElse(null);

			ObservableList<Medicaments> data;
			if(findMed ==null) {
				 data = FXCollections.observableArrayList(allMeds); 
			}else {
				 data = FXCollections.observableArrayList(findMed); 
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
			
			loadDataMeds(index);

    	});
    }
    
}


