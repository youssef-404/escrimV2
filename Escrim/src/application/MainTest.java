package application;

import java.sql.SQLException;
import java.util.List;

import application.dao.DaoManager;
import application.model.Patient;
import application.model.User;
public class MainTest {
	public static void main(String[] args) throws SQLException {
		DaoManager daoManager = DaoManager.getInstance();
		
		List<Patient> myList = daoManager.getPatients();
		
		for (int i = 0; i < daoManager.getPatients().size(); i++) {
		    Patient element = myList.get(i);
		    // Do something with the element 
		    System.out.println("Element at index " + i + ": " + element);
		}
            
        }
}

