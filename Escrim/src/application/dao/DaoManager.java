package application.dao;

import application.model.*;
import application.model.util.Dim3D;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DaoManager {

	protected static DaoManager instance;
	protected static final String URL = "jdbc:sqlite:src/application/resources/db/db.db"; 
  
	protected Connection connection;

	protected DaoManager(){
		connect();    		
    }
    
    public static DaoManager getInstance() {
    	if(instance==null) {
    		instance =new DaoManager();
    	}
    	return instance;
    }

    protected void connect(){
        try {
			connection = DriverManager.getConnection(URL);
			if (connection == null) {
			    // Connection failed
			    System.err.println("Failed to connect to the database.");
			} else {
			    // Connection successful
			    System.out.println("Connected to the database successfully.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    public Inventory getInventory(String typeStock) throws SQLException {
    	String sql =null;
    	switch (typeStock) {
			case "Stock Global": {
				sql = "SELECT p.* FROM stockGlobal sg INNER JOIN parcel p ON sg.parcel = p.id;";
				break;
				
			}case "Stock Deployed": {
				sql = "SELECT p.* FROM stockDeploye sd INNER JOIN parcel p ON sd.parcel = p.id;";
				break;
			}
		
		}
        
        Inventory parcels = new Inventory();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
            	Parcel parcel = new Parcel();
            	parcel.setId(rs.getInt("id"));
            	parcel.setAffectaire(rs.getString("assignment"));
            	parcel.setModel(rs.getString("module"));
            	parcel.setNominal(rs.getString("nominal"));
            	parcel.setType(rs.getString("type"));
            	parcel.setSecteur(rs.getString("sector"));
            	String[] dim = (rs.getString("dimension")).split("X");
            	parcel.setDim(new Dim3D(Double.parseDouble(dim[0]),Double.parseDouble(dim[1]),Double.parseDouble(dim[2])));
            	parcel.setWeight(rs.getFloat("weight"));
            	parcel.setVolume(rs.getFloat("volume"));
            	
            	getParcelItems(parcel);
            	
            	parcels.addItem(parcel);
            }
        }
        return parcels;
    }
    
    void getParcelItems(Parcel parcel) {
    	try {
    		String sql = "SELECT * FROM autreItem WHERE parcel = ?;";
    		PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, parcel.getId());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Item item = new Item(rs.getInt("id"),rs.getInt("quantity"));
				item.setDesignation(rs.getString("designation"));
				parcel.addItem(item);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
 
   
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // User DAO :
    
    public List<User> getUsers() throws SQLException {
    	String sql =null;
		sql = "SELECT * FROM user";
        List<User> Users = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
            	User user = new User(rs.getInt("id_user"),rs.getString("username"),rs.getString("password"),rs.getString("role"));
            	Users.add(user);
            }
        }
        return Users;
    }
    
    public List<User> getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ?";
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs.getInt("id_user"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
                    users.add(user);
                }
            }
        }
        return users;
    }
}
