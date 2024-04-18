package application;

import java.sql.SQLException;
import java.util.List;

import application.dao.DaoManager;
import application.model.User;
public class MainTest {
	public static void main(String[] args) throws SQLException {
		DaoManager daoManager = DaoManager.getInstance();
		
        List<User> users = daoManager.getUsers();
        for (User user : users) {
            System.out.println("User ID: " + user.getId());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Password: " + user.getPassword());
            System.out.println("Role: " + user.getRole());
            System.out.println();
        }
	}
}
