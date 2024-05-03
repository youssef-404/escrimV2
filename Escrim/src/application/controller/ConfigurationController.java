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

	private DaoManager daoManager;   // DAO Manager to handle database operations
	
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
    
    private Configuration lastSelectedConfiguration = null;  // Stores the last selected configuration
	
    // Initializes the controller, sets up the environment
	public void initialize() {
		
		this.daoManager = DaoManager.getInstance();
		
		loadConfigurations();  // Load existing configurations into the table
		
		// Set the selection mode of the list views to multiple selections
		parcelsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		selectedParcelsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		aircraftListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		selectedAircraftsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		populateParcelsListView();  // Populate the list of parcels
        populateAircraftListView();  // Populate the list of aircrafts
        
        populateSelectedParcelsListView();
        populateSelectedAircraftsListView();
        
        
        // Add a mouse click listener to the configuration table view.
        // Loads data of the selected configuration into the form fields.
        tableViewConfig.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && tableViewConfig.getSelectionModel().getSelectedItem() != null) {
                Configuration selectedConfig = tableViewConfig.getSelectionModel().getSelectedItem();
                if (selectedConfig.equals(lastSelectedConfiguration)) {
                    tableViewConfig.getSelectionModel().clearSelection();
                    clearForm();
                    addConfig.setDisable(false);
                    updateConfig.setDisable(true);
                    deleteConfig.setDisable(true);
                    List<Parcel> allParcels = daoManager.getAllParcels();
                    parcelsListView.setItems(FXCollections.observableArrayList(allParcels));
                    List<Plane> allAircrafts = daoManager.getAllAircraft();
                    aircraftListView.setItems(FXCollections.observableArrayList(allAircrafts));
                    lastSelectedConfiguration = null;
                } else {
                    lastSelectedConfiguration = selectedConfig;
                    addConfig.setDisable(true);
                    updateConfig.setDisable(false);
                    deleteConfig.setDisable(false);
                    loadConfigData(selectedConfig);
                }
            }
        });

        
	}
	
	
	
	private void loadConfigurations() {
		configID.setCellValueFactory(new PropertyValueFactory<Configuration, Integer>("id"));
		configName.setCellValueFactory(new PropertyValueFactory<Configuration, String>("name"));
		configDisaster.setCellValueFactory(new PropertyValueFactory<Configuration, String>("catastrophe"));	
		
		// Retrieves all configurations from the database and sets them as items in the table view.
	    ObservableList<Configuration> configurationList = FXCollections.observableArrayList(daoManager.getAllConfigurations());
	    tableViewConfig.setItems(configurationList);
	    tableViewConfig.refresh();  // Refreshes the table view to display the latest loaded data.

	}
	
	
	// Retrieves all parcels from the database and sets them as items in the parcels list view.
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
	
	
	// Retrieves all aircraft from the database and sets them as items in the aircraft list view.
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
		// Validates the input fields before processing the addition of a new configuration.
	    if (!validateInput()) {
	        return; // Stop the addition process if validation fails
	    }
	    
	    try {
	    	// Creates a new Configuration object and sets its properties from the form fields.
	        Configuration newConfig = new Configuration();
	        newConfig.setName(nameTextField.getText().trim());
	        newConfig.setCatastrophe(disasterTextField.getText().trim());

	        // Collects selected parcels and aircraft from their respective list views.
	        List<Parcel> selectedParcels = new ArrayList<>(selectedParcelsListView.getItems());
	        List<Plane> selectedPlanes = new ArrayList<>(selectedAircraftsListView.getItems());

	        // Calls the DaoManager to add the new configuration to the database along with its associated parcels and planes.
	        daoManager.addConfiguration(newConfig, selectedParcels, selectedPlanes);

	        clearForm(); // Clears the form fields after successful addition.
	        loadConfigurations();  // Reloads the configurations to reflect the new addition in the table view.
	    } catch (Exception e) {
	        e.printStackTrace();
	        showErrorDialog("Addition Error", "Failed to add the configuration: " + e.getMessage());
	    }
	}


	// Clears the form fields and selections, resetting the form to a blank state.
    private void clearForm() {
        nameTextField.clear();
        disasterTextField.clear();
        selectedParcelsListView.getItems().clear();
        selectedAircraftsListView.getItems().clear();
    }
    
    
    // Validate that the input fields are not empty and that there is at least one parcel and one aircraft selected.
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
    
    // Shows an error dialog with the specified title and content
    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR); 
        alert.setTitle(title);                         
        alert.setHeaderText(null);                 
        alert.setContentText(content);                
        alert.showAndWait();                           
    }
    
    // Shows a success dialog with the specified title and content
    private void showSuccessDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION); 
        alert.setTitle(title);                         
        alert.setHeaderText(null);                 
        alert.setContentText(content);                
        alert.showAndWait();                           
    }

    
    // Loads the data of the selected configuration into the form fields
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

        }
    }

    
    // Moves selected parcels from the left list view (available parcels) to the right list view (selected parcels).
    @FXML
    private void moveSelectedParcelsToRight() {
        List<Parcel> selectedItems = new ArrayList<>(parcelsListView.getSelectionModel().getSelectedItems());
        selectedParcelsListView.getItems().addAll(selectedItems);
        parcelsListView.getItems().removeAll(selectedItems);
    }

    // Moves selected parcels from the right list view (selected parcels) to the left list view (available parcels).
    @FXML
    private void moveSelectedParcelsToLeft() {
        List<Parcel> selectedItems = new ArrayList<>(selectedParcelsListView.getSelectionModel().getSelectedItems());
        parcelsListView.getItems().addAll(selectedItems);
        selectedParcelsListView.getItems().removeAll(selectedItems);
    }
    

    // Populates parcels of the right ListView (selected parcels) 
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
    
    // Moves selected aircraft from the left list view (available aircraft) to the right list view (selected aircraft).
    @FXML
    private void moveSelectedAircraftsToRight() {
        List<Plane> selectedItems = new ArrayList<>(aircraftListView.getSelectionModel().getSelectedItems());
        selectedAircraftsListView.getItems().addAll(selectedItems);
        aircraftListView.getItems().removeAll(selectedItems);
    }

    // Moves selected aircraft from the right list view (selected aircraft) to the left list view (available aircraft).
    @FXML
    private void moveSelectedAircraftsToLeft() {
        List<Plane> selectedItems = new ArrayList<>(selectedAircraftsListView.getSelectionModel().getSelectedItems());
        aircraftListView.getItems().addAll(selectedItems);
        selectedAircraftsListView.getItems().removeAll(selectedItems);
    }
    

    // Populates aircrafts of the right ListView (selected aircrafts)
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
    
 
    // Handles the update action when the "Update" button is clicked.
    @FXML
    private void handleUpdateAction() {
        Configuration selectedConfig = tableViewConfig.getSelectionModel().getSelectedItem();
     
        // Shows an error if no configuration is selected.
        if (selectedConfig == null) {
            showErrorDialog("Update Error", "No configuration selected.");
            return;
        }

        if (!validateInput()) {
            return; // Stops the update process if validation fails.
        }

        // Updates the selected configuration with new values from the form fields.
        selectedConfig.setName(nameTextField.getText().trim());
        selectedConfig.setCatastrophe(disasterTextField.getText().trim());
        
        List<Parcel> selectedParcels = new ArrayList<>(selectedParcelsListView.getItems());
        List<Plane> selectedPlanes = new ArrayList<>(selectedAircraftsListView.getItems());

        try {
            daoManager.updateConfiguration(selectedConfig, selectedParcels, selectedPlanes); // Calls the DAO to update the configuration in the database.
            showSuccessDialog("Success", "Configuration updated successfully.");
            clearForm();  // Clears the form after successful update.
            deleteConfig.setDisable(true);
            updateConfig.setDisable(true);
            addConfig.setDisable(false);
            loadConfigurations();  // Reloads the configurations to reflect the update in the table view.
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Update Error", "Failed to update the configuration: " + e.getMessage());
        }
    }

    
    // Handles the delete action when the "Delete" button is clicked.
    @FXML
    private void handleDeleteAction() {
        Configuration selectedConfig = tableViewConfig.getSelectionModel().getSelectedItem();
        
        // Shows an error if no configuration is selected.
        if (selectedConfig == null) {
            showErrorDialog("Delete Error", "No configuration selected.");
            return;
        }

        // Confirmation dialog to ensure the user wants to proceed with deletion.
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this configuration?");
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                daoManager.deleteConfiguration(selectedConfig.getId());  // Calls the DAO to delete the configuration from the database.
                showSuccessDialog("Success", "Configuration deleted successfully.");
                clearForm();
                deleteConfig.setDisable(true);
                updateConfig.setDisable(true);
                addConfig.setDisable(false);
                loadConfigurations();
            } catch (Exception e) {
                showErrorDialog("Deletion Error", "Failed to delete the configuration: " + e.getMessage());
            }
        }
    }

    
    
}
