package application.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import application.dao.DaoManager;
import application.model.Escrim;
import application.model.Item;
import application.model.ModelInterface;
import application.model.Parcel;
import application.model.Patient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class PatientController {
	
	private DaoManager dao;
	
    @FXML
    private TableColumn<Patient, Integer> idCell;
    @FXML
    private TableColumn<Patient, String> nameCell;
    @FXML
    private TableColumn<Patient, Integer> ageCell;
    @FXML
    private TableColumn<Patient, String> genderCell;
    @FXML
    private TableView<Patient> tableViewPatients;
    
    @FXML
    private ChoiceBox<String> typeInventory;
    @FXML
    private TextField name;
    @FXML
    private TextField age;
    @FXML
    private TextField gender;
    
    @FXML
    private Button addPatient;
    @FXML
    private Button addObservation;
    
    @FXML
    private TextField observation;
    @FXML
    private DatePicker dateVisite;
    
    @FXML
    private AnchorPane popup;
    @FXML
    private Pane popupContent;
    @FXML
    private Text moreInfoId,moreInfoName,moreInfoAge,moreInfoGender,moreInfoNumber;
    @FXML
    private TableView<String> moreInfoTable;
    @FXML
    private TableColumn<String, String> observationCell;
    @FXML
    private TableColumn<String, String> dateVisit;


    
    //@FXML
    //private AnchorPane popup;
    //@FXML
    //private Pane popupContent;
    //@FXML
    //private Text moreInfoId,moreInfoVolume,moreInfoWeight,moreInfoNature,moreInfoSector,moreInfoDesignation,moreInfoDimension;

    
    public void initialize() {
    	this.dao = DaoManager.getInstance();
    	setupButtonActions();
    	loadDataInventory();
    	
        // Add listener to detect double-click events on TableView cells 
    	tableViewPatients.setRowFactory(tv -> {
            TableRow<Patient> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showObservations();
                }
            });
            return row;
        });
    }
    
    private void setupButtonActions() {
    	addPatient.setOnAction(event -> addNewPatient());
    	addObservation.setOnAction(event -> addNewObservation());
    }
    
    public void loadDataInventory() {
     	
	    	idCell.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("id"));
	    	nameCell.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
	    	ageCell.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("age"));
	    	genderCell.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
	    	
	    	List<Patient> patients = dao.getPatients();		
	    	
	        // Populate TableView with patients 
	        ObservableList<Patient> patientList = FXCollections.observableArrayList(patients);
	        tableViewPatients.setItems(patientList);

    }
    
    private void addNewPatient() {
        String patientName = name.getText();
        String patientAgeText = age.getText();
        String patientGender = gender.getText();

        // Check if any of the fields are empty
        if (patientName.isEmpty() || patientAgeText.isEmpty() || patientGender.isEmpty()) {
            // Show an alert indicating that all fields are required
        	showAlert(Alert.AlertType.ERROR, "Empty Field","All fields are required!");
            return;
        }

        int patientAge;
        try {
            // Try to parse the age as an integer
            patientAge = Integer.parseInt(patientAgeText);
        } catch (NumberFormatException e) {
            // Show an alert indicating that the age must be a valid integer
            showAlert(Alert.AlertType.ERROR, "Age Field","Age must be a valid integer!");
            return;
        }

        // Create a new Patient object
        Patient newPatient = new Patient(0, patientName, patientAge, patientGender);

        // Call the DAO method to add the patient
        dao.addPatient(newPatient);

        // Clear the text fields
        name.clear();
        age.clear();
        gender.clear();

        // Load data (assuming loadDataInventory() is a method to refresh data)
        loadDataInventory();
    }

    private void addNewObservation() {
        // Check if any row is selected in the TableView 
        if (tableViewPatients.getSelectionModel().getSelectedCells().isEmpty()) {
            // Show an alert indicating that no patient is selected 
            showAlert(Alert.AlertType.ERROR, "No Patient Selected", "Please select a patient first.");
            return;
        }

        // Get the selected patient ID
        Patient selectedPatient = tableViewPatients.getSelectionModel().getSelectedItem();
        int patientId = selectedPatient.getId();

        // Get the observation text and selected date
        String observationText = observation.getText();
        LocalDate selectedDate = dateVisite.getValue();

        // Check if any field is empty
        if (observationText.isEmpty() || selectedDate == null) {
            // Show an alert indicating that all fields are required
            showAlert(Alert.AlertType.ERROR, "Empty Fields", "Please enter observation text and select a date.");
            return;
        }

        // Call the DAO method to add the observation
        int newObservationId = dao.addObservation(observationText, selectedDate, patientId);
        if (newObservationId != -1) {
            // Show a success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Observation added successfully.");
            // Clear the observation text field
            observation.clear();
            // Optionally, refresh the TableView if needed
            // loadDataInventory();
        } else {
            // Show an error message if adding observation failed
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add observation.");
        }
        
    }
    
    @FXML
    private void showObservations() {
        if (tableViewPatients.getSelectionModel().getSelectedItem() != null) {
            Patient p = tableViewPatients.getSelectionModel().getSelectedItem();
            Patient selectedPatient = dao.getAllObservations(p.getId(),p.getName(),p.getAge(),p.getGender());
            // Get observations and visit dates for the selected patient
            List<String> observations = selectedPatient.getObservations();
            List<String> visitDates = selectedPatient.getVisitDates();

            // Create a list to hold the data for the TableView
            ObservableList<String> dataObs = FXCollections.observableArrayList();
            ObservableList<String> dataDate = FXCollections.observableArrayList();

         // Populate data into the TableView
            for (int i = 0; i < observations.size(); i++) {
                // Combine observation and visit date into a single string
                String observationAndDate = observations.get(i) + " - " + visitDates.get(i);
                // Add the combined string to the data list
                dataObs.add(observationAndDate);
            }

            // Set cell value factories
            observationCell.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

            // Set the data to the TableView
            moreInfoTable.setItems(dataObs);
            
            // Set additional patient information
            moreInfoId.setText("ID: " + selectedPatient.getId());
            moreInfoName.setText("Name: " + selectedPatient.getName());
            moreInfoAge.setText("Age: " + selectedPatient.getAge());
            moreInfoGender.setText("Gender: " + selectedPatient.getGender());
            moreInfoNumber.setText(String.valueOf(selectedPatient.getObservations().size()));

            // Make the popup visible
            popup.setVisible(true);
            
	        popup.setOnMouseClicked(eventE -> {
	            if (!popupContent.getBoundsInLocal().contains(eventE.getX(), eventE.getY())) {
	                popup.setVisible(false);
	            }
	        });
	            
        }
    }
 
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
