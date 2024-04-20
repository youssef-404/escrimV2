package application.controller;



import java.util.ArrayList;
import java.util.List;

import application.dao.DaoManager;
import application.model.Configuration;
import application.model.Escrim;
import application.model.ModelInterface;
import application.model.Parcel;
import application.model.Plane;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;

public class ConfigurationController {

	private DaoManager daoManager;
	
	@FXML
    private TableView<Configuration> tableViewConfig;
	
	@FXML
    private TableColumn<Configuration, Integer> configID;
	
	@FXML
    private TableColumn<Configuration, String> configName;
	
	@FXML
    private TableColumn<Configuration, String> configDisaster;
	
	@FXML
    private TextField nameTextField;
	
    @FXML
    private TextField disasterTextField;
    
    @FXML
    private ListView<Parcel> parcelsListView;
    
    @FXML
    private ListView<Plane> aircraftListView;
    
    @FXML 
    private Button addConfig;
	
	public void initialize() {
		
		this.daoManager = DaoManager.getInstance();
		
		loadConfigurations();
		
		parcelsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        aircraftListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		populateParcelsListView();
        populateAircraftListView();
        
        tableViewConfig.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateFormFields(newSelection);
            }
        });
        
        
	}
	
	private void loadConfigurations() {
		configID.setCellValueFactory(new PropertyValueFactory<Configuration, Integer>("id"));
		configName.setCellValueFactory(new PropertyValueFactory<Configuration, String>("name"));
		configDisaster.setCellValueFactory(new PropertyValueFactory<Configuration, String>("catastrophe"));	
		
	    ObservableList<Configuration> configurationList = FXCollections.observableArrayList(daoManager.getAllConfigurations());
	    tableViewConfig.setItems(configurationList);
	    
	}
	
	private void populateParcelsListView() {
	    ObservableList<Parcel> parcels = FXCollections.observableArrayList(daoManager.getAllParcels());

	    parcelsListView.setItems(parcels);

	    parcelsListView.setCellFactory(listView -> new ListCell<Parcel>() {
	        @Override
	        protected void updateItem(Parcel parcel, boolean empty) {
	            super.updateItem(parcel, empty);
	            if (empty || parcel == null) {
	                setText(null);
	            } else {
	                setText(parcel.getId() + " - " + parcel.getModel());
	            }
	        }
	    });
	}
	
	private void populateAircraftListView() {
	    ObservableList<Plane> aircraft = FXCollections.observableArrayList(daoManager.getAllAircraft());
	    aircraftListView.setItems(aircraft);

	    aircraftListView.setCellFactory(listView -> new ListCell<Plane>() {
	        @Override
	        protected void updateItem(Plane plane, boolean empty) {
	            super.updateItem(plane, empty);
	            if (empty || plane == null) {
	                setText(null);
	            } else {
	                setText(plane.getName() + " - Max Load: " + plane.getMaxLoad() + " Kg");
	            }
	        }
	    });
	}

	

	@FXML
	private void handleAddAction() {
	    if (!validateInput()) {
	        return; // Stop the addition process if validation fails
	    }

	    try {
	        Configuration newConfig = new Configuration();
	        newConfig.setName(nameTextField.getText().trim());
	        newConfig.setCatastrophe(disasterTextField.getText().trim());

	        List<Parcel> selectedParcels = new ArrayList<>(parcelsListView.getSelectionModel().getSelectedItems());
	        List<Plane> selectedPlanes = new ArrayList<>(aircraftListView.getSelectionModel().getSelectedItems());

	        daoManager.addConfiguration(newConfig, selectedParcels, selectedPlanes);

	        clearForm();
	        loadConfigurations();
	    } catch (Exception e) {
	        e.printStackTrace();
	        showErrorDialog("Addition Error", "Failed to add the configuration: " + e.getMessage());
	    }
	}


	
    private void clearForm() {
        nameTextField.clear();
        disasterTextField.clear();
        parcelsListView.getSelectionModel().clearSelection();
        aircraftListView.getSelectionModel().clearSelection();
    }
    
    private boolean validateInput() {
        String name = nameTextField.getText().trim();
        String disaster = disasterTextField.getText().trim();

        if (name.isEmpty() || disaster.isEmpty()) {
            showErrorDialog("Input Error", "Please fill in all text fields.");
            return false;
        }

        if (parcelsListView.getSelectionModel().getSelectedItems().isEmpty()) {
            showErrorDialog("Selection Error", "Please select at least one parcel.");
            return false;
        }

        if (aircraftListView.getSelectionModel().getSelectedItems().isEmpty()) {
            showErrorDialog("Selection Error", "Please select at least one aircraft.");
            return false;
        }

        return true;
    }
    
    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR); 
        alert.setTitle(title);                         
        alert.setHeaderText(null);                 
        alert.setContentText(content);                
        alert.showAndWait();                           
    }

    
    private void updateFormFields(Configuration config) {
        if (config == null) {
            nameTextField.clear();
            disasterTextField.clear();
            parcelsListView.setItems(FXCollections.observableArrayList());
            aircraftListView.setItems(FXCollections.observableArrayList());
        } else {
            nameTextField.setText(config.getName() != null ? config.getName() : "");
            disasterTextField.setText(config.getCatastrophe() != null ? config.getCatastrophe() : "");

            ObservableList<Parcel> parcelList = config.getColis() != null ?
                FXCollections.observableArrayList(config.getColis()) : FXCollections.observableArrayList();
            parcelsListView.setItems(parcelList);

            ObservableList<Plane> planeList = config.getAvions() != null ?
                FXCollections.observableArrayList(config.getAvions()) : FXCollections.observableArrayList();
            aircraftListView.setItems(planeList);
        }
    }
    
}
