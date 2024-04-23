package application.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import application.dao.DaoManager;
import application.model.HospitalizedPatient;
import application.model.Patient;
import application.model.Prescribe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrescribeController {
    private DaoManager dao;
    
    @FXML
    private TableView<Patient> tableViewPatients;
    @FXML
    private TableView<Prescribe> tableViewP;
    
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
    private TableColumn<Prescribe, Integer> idP;
    @FXML
    private TableColumn<Prescribe, String> pP;
    @FXML
    private TableColumn<Prescribe, String> medP;
    @FXML
    private TableColumn<Prescribe, LocalDate> presDateP;
    @FXML
    private TableColumn<Prescribe, String> commentP;
    
    @FXML
    private DatePicker presDatePicker;
    @FXML
    private TextField commentField;
    @FXML
    private TextField patientText;    
    @FXML
    private ComboBox<String> medComboBox;
    @FXML
    private Button prescribeButton;
    
    @FXML
    private Button expiredMeds;
    
    @FXML
    private Button validMeds;

    public void initialize() throws SQLException {
    	this.dao = DaoManager.getInstance();
    	
    	setupButtonActions();
    	initializeMeds();
    	initializeNumber();
    	loadPatients();
    	initializePatientText();
    	PrescriptionTableInit();
    }
    
	private void PrescriptionTableInit() {
        // Initialize table columns 
        idP.setCellValueFactory(new PropertyValueFactory<>("id"));
        pP.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        medP.setCellValueFactory(new PropertyValueFactory<>("medId"));
        presDateP.setCellValueFactory(new PropertyValueFactory<>("prescDate"));
        commentP.setCellValueFactory(new PropertyValueFactory<>("comment"));		
        // Populate the TableView
        try {
            List<Prescribe> prescriptions = dao.getAllPrescriptions();
            ObservableList<Prescribe> observableList = FXCollections.observableArrayList(prescriptions);
            tableViewP.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
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

    
	private void setupButtonActions() {
		prescribeButton.setOnAction(event -> {
			try {
				addPrescription();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
    
	private void addPrescription() throws SQLException {
        Patient selectedPatient = tableViewPatients.getSelectionModel().getSelectedItem();
        String selectedMed = medComboBox.getValue();
        LocalDate prescDate = presDatePicker.getValue();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String prescDateString = prescDate.format(formatter);
        
        String comment = commentField.getText();

        if (selectedPatient != null && selectedMed != null && prescDate != null && !comment.isEmpty()) {
            try {
                // Ensure that expected stay is a positive integer
                    // Call DAO method to add the patient to the hospitalized table
                    dao.addPrescription(selectedPatient.getId(), selectedMed, prescDateString, comment);
                    dao.decrementQuantityOfNearExpiry(selectedMed,1);
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
                
            } catch (NumberFormatException e) {
                // Show error alert for invalid expected stay format
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", "Expected stay should be a valid integer.");
            }
        } else {
            // Show error alert for missing or invalid input
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Input", "Please fill out all fields and select a patient and bed.");
        }
	}

	private void initializeNumber(){
		int expiredCount = dao.getExpiredMedsCount();
		int validCount = dao.getValidMedsCount();

		expiredMeds.setText(expiredCount + " Expired Medicines");
        validMeds.setText(validCount + " Valid Medicines");		
	}

	private void initializeMeds() {
        try {
            List<String> productNames = dao.getDistinctValidProductNames();
            medComboBox.getItems().addAll(productNames);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception (show error message, log, etc.)
        }		
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

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void clearInputFields() {
    	medComboBox.setValue(null);
        presDatePicker.setValue(null);
        commentField.clear();
    }

}
