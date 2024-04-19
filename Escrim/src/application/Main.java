package application;
	
import java.io.IOException;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;



public class Main extends Application {
	@Override
	public void start(Stage stage) {		
		try {
				
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/Login.fxml"));
	        Scene scene = new Scene(fxmlLoader.load());
	        stage.setScene(scene);
	        stage.setTitle("Hello!");
	        stage.show();
	        stage.setMinWidth(stage.getWidth());
	        stage.setMaxWidth(stage.getWidth());
	        stage.setMinHeight(stage.getHeight());
	        stage.setMaxHeight(stage.getHeight());
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
