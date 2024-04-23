package application.controller;

import application.dao.DaoManager;
import application.model.Escrim;
import application.model.ModelInterface;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private ChoiceBox<?> configuration;
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
    	country.setText(this.model.getCountry());
    	duration.setText(Integer.toString(this.model.getDuration()));
    	description.setText(this.model.getDescription());
    	date.setValue(model.getDate());

    	
    }
    
	
}
