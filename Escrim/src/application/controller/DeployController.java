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

    	}
    	
    	deployButton.setOnMouseClicked(e->{
    		int selectedIndex = configuration.getSelectionModel().getSelectedIndex();
    		Configuration selectedConfig = allConfig.get(selectedIndex);
    		String countryValue = country.getText();
    		int durationValue =Integer.parseInt(duration.getText()) ;
    		LocalDate dateValue = date.getValue();
    		String descriptionValue = description.getText();
    		
    		this.daoManager.deploy(dateValue, durationValue, descriptionValue, selectedConfig, countryValue);
    		deployButton.setDisable(true);
    		removeButton.setDisable(false);
    		
    		
    		
    	});
    	
    	removeButton.setOnMouseClicked(e->{
        	Configuration selectedConfig = allConfig.stream().filter(conf -> conf.getId()== this.model.getConfigurationId()).findFirst().orElse(null);
    		this.daoManager.removeDeployement(selectedConfig);
    		country.clear();
    		description.clear();
    		duration.clear();
    		date.setValue(null);
    		configuration.getSelectionModel().clearSelection();
    		deployButton.setDisable(false);
    		removeButton.setDisable(true);
    		
    		
    	});
    	   
    	
    }
    
    
    
    
	
}
