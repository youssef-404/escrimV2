package application.controller;

import java.sql.SQLException;

import application.dao.DaoManager;
import application.model.Escrim;
import application.model.ModelInterface;
import application.model.Parcel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewController {

	private ModelInterface model;
	private DaoManager daoManger;
    @FXML
    private TableColumn<Parcel, Integer> id;
    @FXML
    private TableColumn<Parcel, String> assignment;
    @FXML
    private TableColumn<Parcel, String> module;
    @FXML
    private TableColumn<Parcel, String> nominal;
    @FXML
    private TableColumn<Parcel, String> type;
    @FXML
    private TableColumn<Parcel, String> sector; 
    @FXML
    private TableColumn<Parcel, String> dimension;
    @FXML
    private TableColumn<Parcel, Double> volume;
    @FXML
    private TableColumn<Parcel, Double> weight;
    @FXML
    private TableView<Parcel> tableView;
    
    public void initialize() {
 
    	this.daoManger = new DaoManager();
    	this.model = Escrim.getInstance();
    	loadDataInventory();
    }

    
    public void loadDataInventory() {
 		try {
 			id.setCellValueFactory(new PropertyValueFactory<Parcel, Integer>("id"));
 			module.setCellValueFactory(new PropertyValueFactory<Parcel, String>("model"));
 			assignment.setCellValueFactory(new PropertyValueFactory<Parcel, String>("affectaire"));
 			nominal.setCellValueFactory(new PropertyValueFactory<Parcel, String>("nominal"));
 			type.setCellValueFactory(new PropertyValueFactory<Parcel, String>("type"));
 			sector.setCellValueFactory(new PropertyValueFactory<Parcel, String>("secteur"));
 			dimension.setCellValueFactory(new PropertyValueFactory<Parcel, String>("dim"));
 			volume.setCellValueFactory(new PropertyValueFactory<Parcel, Double>("volume"));
 			weight.setCellValueFactory(new PropertyValueFactory<Parcel, Double>("weight"));

 			
 			this.model.setGlobalInventory(this.daoManger.getInventory());
 	        ObservableList<Parcel> data = FXCollections.observableArrayList(this.model.getGlobalInventory().getPackages()); 
 	       tableView.setItems(data);
 	       this.daoManger.getInventory().printItems();
 	
 			
 		} catch (SQLException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
    }

}
