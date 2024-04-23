package application.controller;

import java.time.LocalDate;
import java.util.List;

import application.dao.DaoManager;
import application.model.Configuration;
import application.model.Escrim;
import application.model.ModelInterface;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DeployController {
	private ModelInterface model;
	private DaoManager daoManager;
	
	@FXML
    private ChoiceBox<String> configuration;
    @FXML
    private TextField country;
    @FXML
    private DatePicker date;
    @FXML
    private Button deployButton;
    @FXML
    private TextArea description;
    @FXML
    private TextField duration;
    @FXML
    private Button removeButton;
    
    public void initialize() {
    	this.daoManager = DaoManager.getInstance();
    	this.model = Escrim.getInstance();
    	durationInputRestrection();
    	daoManager.getEscrim();
    	initilizeEscrim(); 	
    	
    	System.out.println("i got called again");
    }
    
    
    
    
    public void durationInputRestrection() {
    	duration.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	duration.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});

    }
    
    public void initilizeEscrim() {
    	List<Configuration> allConfig=this.daoManager.getAllConfigurations();
    	for(Configuration config:allConfig) {
    		configuration.getItems().add(config.getId() + "-" + config.getName());
    	}
    	
    	removeButton.setDisable(true);
    
    	if(this.model.getState()) {    		
    	country.setText(this.model.getCountry());
    	duration.setText(Integer.toString(this.model.getDuration()));
    	description.setText(this.model.getDescription());
    	date.setValue(model.getDate());
    	Configuration selectedConfig = allConfig.stream().filter(conf -> conf.getId()== this.model.getConfigurationId()).findFirst().orElse(null);
    	configuration.setValue(selectedConfig.getId() + "-" + selectedConfig.getName());
    	deployButton.setDisable(true);
    	removeButton.setDisable(false);
    	disableFields();

    	}
    	
    	deployButton.setOnMouseClicked(e->{
    		int selectedIndex = configuration.getSelectionModel().getSelectedIndex();
    		String countryValue = country.getText();
    		LocalDate dateValue = date.getValue();
    		String descriptionValue = description.getText();
    		if(validateInput(countryValue,descriptionValue,dateValue,selectedIndex,duration.getText())) {
    			int durationValue =Integer.parseInt(duration.getText()) ;
    			Configuration selectedConfig = allConfig.get(selectedIndex);
    			this.daoManager.deploy(dateValue, durationValue, descriptionValue, selectedConfig, countryValue);
    			
    			showSuccessDialog("Deployed","Escrim deployed successfully.");
    			deployButton.setDisable(true);
    			removeButton.setDisable(false);
    			daoManager.getEscrim();
    			disableFields();
    		}
    		
    		
    		
    	});
    	
    	removeButton.setOnMouseClicked(e->{
        	Configuration selectedConfig = allConfig.stream().filter(conf -> conf.getId()== this.model.getConfigurationId()).findFirst().orElse(null);
        	if (selectedConfig != null) {
        	    daoManager.removeDeployement(selectedConfig);
        	    daoManager.getEscrim();
        	} else {
        	    System.out.println("selectedConfig is null, cannot remove deployment");
        	}
    
    		clearForm();
    		
    		
    		
    	});
    	   
    	
    }
    
    private void disableFields() {
    	country.setEditable(false);
    	description.setEditable(false);
    	duration.setEditable(false);
    	date.setEditable(false);
    	configuration.setDisable(true);
    }
    
    
    private void clearForm() {
    	country.clear();
    	description.clear();
    	duration.clear();
    	date.setValue(null);
		configuration.getSelectionModel().clearSelection();
		deployButton.setDisable(false);
		removeButton.setDisable(true);
		country.setEditable(true);
    	description.setEditable(true);
    	duration.setEditable(true);
    	date.setEditable(true);
    	configuration.setDisable(true);
    }
    
    
    
    
    private boolean validateInput(String country,String description,LocalDate date,int selectedConfiguration,String duration) {
    
        if (country.isEmpty() || description.isEmpty() || duration.isEmpty() ) {
            showErrorDialog("Input Error", "Please fill in all text fields.");
            return false;
        }

        if (selectedConfiguration == -1) {
            showErrorDialog("Selection Error", "Please select a configuration");
            return false;
        }

        if (date == null) {
            showErrorDialog("Picker Error", "Please select a date.");
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

    
    
    
    
	
}
