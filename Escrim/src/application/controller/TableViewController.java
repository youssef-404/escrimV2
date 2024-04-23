package application.controller;

import java.sql.SQLException;
import java.util.List;

import application.dao.DaoManager;
import application.model.Escrim;
import application.model.Item;
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
    @FXML
    private TableView<Item> moreInfoTable;
    @FXML
    private TableColumn<Parcel, Integer> itemId;
    @FXML
    private TableColumn<Parcel, String> itemDesignation;
    @FXML
    private TableColumn<Parcel, Integer> itemQuantity;
    @FXML
    private ChoiceBox<String> typeInventory;
    @FXML
    private TextField searchField;
    @FXML
    private AnchorPane popup;
    @FXML
    private Pane popupContent;
    @FXML
    private Text moreInfoId,moreInfoVolume,moreInfoWeight,moreInfoNature,moreInfoSector,moreInfoDesignation,moreInfoDimension;
    
    
    public void initialize() {
    	this.daoManger = DaoManager.getInstance();
    	this.model = Escrim.getInstance();
    	daoManger.getEscrim();
    	
    	try {
			this.model.setGlobalInventory(this.daoManger.getInventory("Stock Global"));
			this.model.setLocalInventory(this.daoManger.getInventory("Stock Deployed"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	   	
    	initializeDropDown();
    	loadDataInventory("Stock Global",-1);
    	initializeSearch();
    	initializePopUp();
 
    }
    
    public void initializeDropDown() {
        ObservableList<String> options = FXCollections.observableArrayList("Stock Global", "Stock Deployed");
        typeInventory.setItems(options);

    
        typeInventory.setValue("Stock Global");
        
        typeInventory.setOnAction(event -> {
            String selectedType = typeInventory.getValue();
            loadDataInventory(selectedType,-1);
        });
    }
    
 
    
    public void loadDataInventory(String typeStock,int index) {
     	
 			id.setCellValueFactory(new PropertyValueFactory<Parcel, Integer>("id"));
 			module.setCellValueFactory(new PropertyValueFactory<Parcel, String>("model"));
 			assignment.setCellValueFactory(new PropertyValueFactory<Parcel, String>("affectaire"));
 			nominal.setCellValueFactory(new PropertyValueFactory<Parcel, String>("nominal"));
 			type.setCellValueFactory(new PropertyValueFactory<Parcel, String>("type"));
 			sector.setCellValueFactory(new PropertyValueFactory<Parcel, String>("secteur"));
 			dimension.setCellValueFactory(new PropertyValueFactory<Parcel, String>("dim"));
 			volume.setCellValueFactory(new PropertyValueFactory<Parcel, Double>("volume"));
 			weight.setCellValueFactory(new PropertyValueFactory<Parcel, Double>("weight"));

 			switch (typeStock) {
			case "Stock Global": {
				Parcel parcel =this.model.getGlobalInventory().getPackage(index);
				ObservableList<Parcel> data;
				if(parcel ==null) {
					 data = FXCollections.observableArrayList(this.model.getGlobalInventory().getPackages()); 
				}else {
					 data = FXCollections.observableArrayList(parcel); 
				}
	 	        tableView.setItems(data);
				break;
				
			}case "Stock Deployed": {
				Parcel parcel =this.model.getLocalInventory().getPackage(index);
				ObservableList<Parcel> data;
				if(parcel ==null) {
					 data = FXCollections.observableArrayList(this.model.getLocalInventory().getPackages()); 
				}else {
					 data = FXCollections.observableArrayList(parcel); 
				}
	 	        tableView.setItems(data);
				break;
			}
		
		} 	   
    }
    
    public void loadDataItem(List<Item> items) {
    	itemId.setCellValueFactory(new PropertyValueFactory<Parcel, Integer>("id"));
    	itemDesignation.setCellValueFactory(new PropertyValueFactory<Parcel, String>("designation"));
    	itemQuantity.setCellValueFactory(new PropertyValueFactory<Parcel, Integer>("quantity"));
    	
    	ObservableList<Item> data= FXCollections.observableArrayList(items);
    	moreInfoTable.setItems(data);
    	
    }
    
    public void initializeSearch() {
    	searchField.textProperty().addListener((observable, oldValue, newValue) -> {
    		int index ;
			try {
				index =Integer.parseInt(newValue);
				
			} catch (Exception e) {
				index=-1;
			}
			
			loadDataInventory(typeInventory.getValue(),index);

    	});
    }
    
    public void initializePopUp() {
    	tableView.setOnMouseClicked(event -> {
    	    if (event.getClickCount() == 1) {
    	        TablePosition<?, ?> selectedPosition = tableView.getSelectionModel().getSelectedCells().get(0);
    	        int selectedIndex = selectedPosition.getRow();
    	        
    	        Parcel selectedData = tableView.getItems().get(selectedIndex);
    	        
    	        moreInfoId.setText("Parcel N° "+selectedData.getId());
    	        moreInfoVolume.setText(selectedData.getVolume()+" m²");
    	        moreInfoWeight.setText(selectedData.getWeight()+" Kg");
    	        moreInfoNature.setText(selectedData.getType());
    	        moreInfoSector.setText(selectedData.getSecteur());
    	        moreInfoDesignation.setText(selectedData.getAffectaire());
    	        moreInfoDimension.setText(selectedData.getDim().toString());
    	        
    	        loadDataItem(selectedData.getItems()); 
    	        
    	        popup.setVisible(true);
    	        
    	        popup.setOnMouseClicked(eventE -> {
    	            if (!popupContent.getBoundsInLocal().contains(eventE.getX(), eventE.getY())) {
    	                popup.setVisible(false);
    	            }
    	        });
    	    }
    	});
    }

}
