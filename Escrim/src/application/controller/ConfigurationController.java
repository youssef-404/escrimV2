package application.controller;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import javafx.scene.control.ButtonType;
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
    private ListView<Parcel> selectedParcelsListView;
    
    @FXML
    private ListView<Plane> aircraftListView;
    
    @FXML
    private ListView<Plane> selectedAircraftsListView;
    
    @FXML 
    private Button addConfig;
    
    @FXML 
    private Button updateConfig;
    
    @FXML 
    private Button deleteConfig;
    
    private Configuration lastSelectedConfiguration = null;
	
	public void initialize() {
		
		this.daoManager = DaoManager.getInstance();
		
		loadConfigurations();
		
		parcelsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		selectedParcelsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		aircraftListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		selectedAircraftsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		populateParcelsListView();
        populateAircraftListView();
        
        populateSelectedParcelsListView();
        populateSelectedAircraftsListView();
        
        updateConfig.setDisable(true);
        deleteConfig.setDisable(true);
        
        tableViewConfig.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && tableViewConfig.getSelectionModel().getSelectedItem() != null) {
                Configuration selectedConfig = tableViewConfig.getSelectionModel().getSelectedItem();
                if (selectedConfig.equals(lastSelectedConfiguration)) {
                    tableViewConfig.getSelectionModel().clearSelection();
                    clearForm();
                    lastSelectedConfiguration = null;
                } else {
                    lastSelectedConfiguration = selectedConfig;
                    loadConfigData(selectedConfig);
                }
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
	                setText(plane.getName());
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
        selectedParcelsListView.getItems().clear();
        selectedAircraftsListView.getItems().clear();
    }
    
    private boolean validateInput() {
        String name = nameTextField.getText().trim();
        String disaster = disasterTextField.getText().trim();
        ObservableList<Parcel> parcels = selectedParcelsListView.getItems();
        ObservableList<Plane> aircrafts = selectedAircraftsListView.getItems();

        if (name.isEmpty() || disaster.isEmpty()) {
            showErrorDialog("Input Error", "Please fill in all text fields.");
            return false;
        }

        if (parcels.isEmpty()) {
            showErrorDialog("Selection Error", "Please select at least one parcel.");
            return false;
        }

        if (aircrafts.isEmpty()) {
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
    
    private void showSuccessDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION); 
        alert.setTitle(title);                         
        alert.setHeaderText(null);                 
        alert.setContentText(content);                
        alert.showAndWait();                           
    }

    
    private void loadConfigData(Configuration config) {
        if (config == null) {
            clearForm();
        } else {
            nameTextField.setText(config.getName());
            disasterTextField.setText(config.getCatastrophe());

            ObservableList<Parcel> selectedParcelsList = FXCollections.observableArrayList(config.getColis());
            selectedParcelsListView.setItems(selectedParcelsList);  

            List<Parcel> allParcels = daoManager.getAllParcels();
            allParcels.removeAll(selectedParcelsList); 
            parcelsListView.setItems(FXCollections.observableArrayList(allParcels));
            
            ObservableList<Plane> selectedAircraftsList = FXCollections.observableArrayList(config.getAvions());
            selectedAircraftsListView.setItems(selectedAircraftsList);
            
            List<Plane> allAircrafts = daoManager.getAllAircraft();
            allAircrafts.removeAll(selectedAircraftsList); 
            aircraftListView.setItems(FXCollections.observableArrayList(allAircrafts));
            
            addConfig.setDisable(true);

        }
    }

    
    @FXML
    private void moveSelectedParcelsToRight() {
        List<Parcel> selectedItems = new ArrayList<>(parcelsListView.getSelectionModel().getSelectedItems());
        selectedParcelsListView.getItems().addAll(selectedItems);
        parcelsListView.getItems().removeAll(selectedItems);
    }

    @FXML
    private void moveSelectedParcelsToLeft() {
        List<Parcel> selectedItems = new ArrayList<>(selectedParcelsListView.getSelectionModel().getSelectedItems());
        parcelsListView.getItems().addAll(selectedItems);
        selectedParcelsListView.getItems().removeAll(selectedItems);
    }
    

    
    private void populateSelectedParcelsListView() {
        selectedParcelsListView.setItems(FXCollections.observableArrayList());
        selectedParcelsListView.setCellFactory(listView -> new ListCell<Parcel>() {
            @Override
            protected void updateItem(Parcel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + " - " + item.getModel());
                }
            }
        });
    }
    
    @FXML
    private void moveSelectedAircraftsToRight() {
        List<Plane> selectedItems = new ArrayList<>(aircraftListView.getSelectionModel().getSelectedItems());
        selectedAircraftsListView.getItems().addAll(selectedItems);
        aircraftListView.getItems().removeAll(selectedItems);
    }

    @FXML
    private void moveSelectedAircraftsToLeft() {
        List<Plane> selectedItems = new ArrayList<>(selectedAircraftsListView.getSelectionModel().getSelectedItems());
        aircraftListView.getItems().addAll(selectedItems);
        selectedAircraftsListView.getItems().removeAll(selectedItems);
    }
    

    
    private void populateSelectedAircraftsListView() {
    	selectedAircraftsListView.setItems(FXCollections.observableArrayList());
    	selectedAircraftsListView.setCellFactory(listView -> new ListCell<Plane>() {
            @Override
            protected void updateItem(Plane item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + " - " + item.getName());
                }
            }
        });
    }
    
 
    @FXML
    private void handleUpdateAction() {
        Configuration selectedConfig = tableViewConfig.getSelectionModel().getSelectedItem();
        if (selectedConfig == null) {
            showErrorDialog("Update Error", "No configuration selected.");
            return;
        }

        if (!validateInput()) {
            return; 
        }

        selectedConfig.setName(nameTextField.getText().trim());
        selectedConfig.setCatastrophe(disasterTextField.getText().trim());
        
        List<Parcel> selectedParcels = new ArrayList<>(selectedParcelsListView.getItems());
        List<Plane> selectedPlanes = new ArrayList<>(aircraftListView.getSelectionModel().getSelectedItems());

        try {
            daoManager.updateConfiguration(selectedConfig, selectedParcels, selectedPlanes);
            showSuccessDialog("Success", "Configuration updated successfully.");
            loadConfigurations(); 
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Update Error", "Failed to update the configuration: " + e.getMessage());
        }
    }

    
    @FXML
    private void handleDeleteAction() {
        Configuration selectedConfig = tableViewConfig.getSelectionModel().getSelectedItem();
        if (selectedConfig == null) {
            showErrorDialog("Delete Error", "No configuration selected.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this configuration?");
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                daoManager.deleteConfiguration(selectedConfig.getId());
                showSuccessDialog("Success", "Configuration deleted successfully.");
                loadConfigurations(); 
            } catch (Exception e) {
                showErrorDialog("Deletion Error", "Failed to delete the configuration: " + e.getMessage());
            }
        }
    }

    
    
}
