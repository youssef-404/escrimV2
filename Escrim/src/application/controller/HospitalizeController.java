package application.controller;

import java.time.LocalDate;
import java.util.List;

import application.dao.DaoManager;
import application.model.Bed;
import application.model.HospitalizedPatient;
import application.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class HospitalizeController {

    private DaoManager dao;
    
    @FXML
    private TableView<Patient> tableViewPatients;
    @FXML
    private TableView<HospitalizedPatient> tableViewHospitalized;
    
    @FXML
    private TableColumn<Patient, Integer> idCell;
    @FXML
    private TableColumn<Patient, String> nameCell;
    @FXML
    private TableColumn<Patient, Integer> ageCell;
    @FXML
    private TableColumn<Patient, String> genderCell;
    @FXML
    private TableColumn<Patient, String> nameCell1;
    
    @FXML
    private TableColumn<Patient, Integer> idH;
    @FXML
    private TableColumn<Patient, String> nameH;
    @FXML
    private TableColumn<Patient, Integer> bedH;
    @FXML
    private TableColumn<Patient, LocalDate> entryDateH;
    @FXML
    private TableColumn<Patient, Integer> expectedStayH;
    
    @FXML
    private ComboBox<Integer> bedComboBox;
    @FXML
    private DatePicker entryDatePicker;
    
    @FXML
    private TextField expectedStayField;
    
    @FXML
    private TextField patientText;
    
    @FXML
    private Button finishButton;
    @FXML
    private Button hospitalizeButton;
    
    @FXML 
    private Button unusedBeds;
    @FXML 
    private Button usedBeds;

    public void initialize() {
    	this.dao = DaoManager.getInstance();
    	
    	setupButtonActions();
    	initializeBeds();
    	initializePatientText();
    	loadPatients();
    	loadHospitalizedPatients();
    }

	private void initializePatientText() {
	    // Add listener for selection changes in tableViewPatients
	    tableViewPatients.getSelectionModel().selectedItemProperty().addListener((obs, oldPatient, newPatient) -> {
	        if (newPatient != null) {
	            patientText.setText(String.valueOf(newPatient.getId()));
	        } else {
	            patientText.clear();
	        }
	    });		
	}

	private void initializeBeds() {
	    List<Bed> beds = dao.getAllDeployedBeds();
	    ObservableList<Integer> bedIds = FXCollections.observableArrayList();
	    for (Bed bed : beds) {
	        bedIds.add(bed.getId());
	    }
	    bedComboBox.setItems(bedIds);
	    
        int usedBedsCount = dao.getUsedBedsNumber();
        int unusedBedsCount = dao.getUnusedBedsNumber();

        usedBeds.setText(usedBedsCount + " Used Beds");
        unusedBeds.setText(unusedBedsCount + " Unused Beds");

	   }

	private void setupButtonActions() {
		finishButton.setOnAction(event -> finishHospitalization());
        hospitalizeButton.setOnAction(event -> hospitalizePatient());
	}

	private void loadPatients() {
    	idCell.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("id"));
    	nameCell.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
    	ageCell.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("age"));
    	genderCell.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
    	List<Patient> patients = dao.getPatients();		
        // Populate TableView with patients 
        ObservableList<Patient> patientList = FXCollections.observableArrayList(patients);
        tableViewPatients.setItems(patientList);		
	}
	
	private void loadHospitalizedPatients() {
	    idH.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("patientId"));
	    nameH.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
	    bedH.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("bedId")); // Assuming bed is a String property in Patient class
	    entryDateH.setCellValueFactory(new PropertyValueFactory<Patient, LocalDate>("entryDate"));
	    expectedStayH.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("expectedStay"));
	    
	    List<HospitalizedPatient> hospitalizedPatients = dao.getAllHospitalized();
	    ObservableList<HospitalizedPatient> hospitalizedPatientList = FXCollections.observableArrayList(hospitalizedPatients);
	    tableViewHospitalized.setItems(hospitalizedPatientList);
	}

	
    private void hospitalizePatient() {
        Patient selectedPatient = tableViewPatients.getSelectionModel().getSelectedItem();
        Integer selectedBedId = bedComboBox.getValue();
        LocalDate entryDate = entryDatePicker.getValue();
        String expectedStayText = expectedStayField.getText();

        if (selectedPatient != null && selectedBedId != null && entryDate != null && !expectedStayText.isEmpty()) {
            try {
                int expectedStay = Integer.parseInt(expectedStayText);
                // Ensure that expected stay is a positive integer
                if (expectedStay > 0) {
                    // Call DAO method to add the patient to the hospitalized table
                    dao.addHospitalizedEntry(selectedPatient.getId(), selectedPatient.getName(), selectedBedId, entryDate.toString(), expectedStay);
                    dao.toggleBedInUseState(selectedBedId);
                    initialize();
                    // Show success alert
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Patient Hospitalized");
                    successAlert.setContentText("The patient has been successfully hospitalized.");
                    successAlert.showAndWait();
                    
                    // Optionally, you can update the UI or perform any other actions after adding the patient
                    // For example, clear the input fields or refresh the table views
                    clearInputFields();
                } else {
                    // Show error alert for invalid expected stay
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", "Expected stay should be a positive integer.");
                }
            } catch (NumberFormatException e) {
                // Show error alert for invalid expected stay format
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", "Expected stay should be a valid integer.");
            }
        } else {
            // Show error alert for missing or invalid input
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Input", "Please fill out all fields and select a patient and bed.");
        }
    }
    
    @FXML
    private void finishHospitalization() {
        // Get the selected hospitalized patient from the table view
    	HospitalizedPatient selectedHospitalizedPatient = tableViewHospitalized.getSelectionModel().getSelectedItem();
        
        if (selectedHospitalizedPatient != null) {
            // Get the necessary information from the selected patient
            String patientName = selectedHospitalizedPatient.getName();
            int bedId = selectedHospitalizedPatient.getBedId();
            LocalDate entryDate = selectedHospitalizedPatient.getEntryDate();
            int expectedStay = selectedHospitalizedPatient.getExpectedStay();

            // Call the DAO method to delete the hospitalized entry based on the information
            boolean success = dao.deleteHospitalizedEntry(patientName, bedId, entryDate, expectedStay);
            dao.toggleBedInUseState(bedId);
            
            if (success) {
                initialize();
                // Show success alert
                showAlert(Alert.AlertType.INFORMATION, "Success", "Patient Discharged", "The patient has been successfully discharged.");
            } else {
                // Show error alert if the entry is not found
                showAlert(Alert.AlertType.ERROR, "Error", "Entry Not Found", "The selected hospitalized entry could not be found.");
            }
        } else {
            // Show error alert if no entry is selected
            showAlert(Alert.AlertType.ERROR, "Error", "No Selection", "Please select a patient to discharge.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void clearInputFields() {
        bedComboBox.setValue(null);
        entryDatePicker.setValue(null);
        expectedStayField.clear();
    }

}
