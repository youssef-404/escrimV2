package application.dao;

import application.model.*;
import application.model.util.Dim2D;
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
    
    public List<User> getUsers(){
    	String sql =null;
		sql = "SELECT * FROM user";
        List<User> Users = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
            	User user = new User(rs.getInt("id_user"),rs.getString("username"),rs.getString("password"),rs.getString("role"));
            	Users.add(user);
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return Users;
    }
    
    public User getUserByUsername(String username){
        String sql = "SELECT * FROM user WHERE username = ?";
        User user = new User(0,null,null,null);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    user = new User(rs.getInt("id_user"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
                }
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return user;
    }
    
    public String authenticate(String username, String password) {
    	User user = getUserByUsername(username);
    	if(user.checkPassword(password)) return user.getRole(); 
    	else return null;
    }
    
    //Config DAO
    
    public List<Configuration> getAllConfigurations() {
        List<Configuration> configurations = new ArrayList<>();
        String sql = "SELECT * FROM configuration";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Configuration config = new Configuration();
                config.setId(rs.getInt("id"));
                config.setName(rs.getString("name"));
                config.setCatastrophe(rs.getString("disaster"));
                config.setAvions(fetchPlanesForConfiguration(config.getId()));
                configurations.add(config);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return configurations;
    }
    
    private List<Plane> fetchPlanesForConfiguration(int configurationId) {
        List<Plane> planes = new ArrayList<>();
        String sql = "SELECT aircraft.* FROM aircraft "
                + "JOIN configuration_aircraft ON aircraft.id = configuration_aircraft.aircraftId "
                + "WHERE configuration_aircraft.configurationId = ?";

                   
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setInt(1, configurationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Plane plane = new Plane();
                    plane.setId(rs.getInt("id"));
                    plane.setName(rs.getString("name"));
                    plane.setMaxLoad(rs.getFloat("maxload"));
                    plane.setDoorSize(parseDim2D(rs.getString("doorSize")));
                    plane.setCargoHold(parseDim3D(rs.getString("cargoHold")));
                    plane.setLoadRange(rs.getFloat("loadRange"));
                    planes.add(plane);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return planes;
    }
    
    private Dim2D parseDim2D(String dimension) {
        String[] parts = dimension.split("x");
        if (parts.length == 2) {
            try {
                double length = Double.parseDouble(parts[0]);
                double width = Double.parseDouble(parts[1]);
                return new Dim2D(length, width);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null; 
    }

    private Dim3D parseDim3D(String dimension) {
        String[] parts = dimension.split("x");
        if (parts.length == 3) {
            try {
                double length = Double.parseDouble(parts[0]);
                double width = Double.parseDouble(parts[1]);
                double height = Double.parseDouble(parts[2]);
                return new Dim3D(length, width, height);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null; 
    }
    
    public void addConfiguration(Configuration config, List<Parcel> selectedParcels, List<Plane> selectedPlanes) throws SQLException {
        String sqlConfig = "INSERT INTO configuration (name, disaster) VALUES (?, ?)";

        try (PreparedStatement pstmtConfig = connection.prepareStatement(sqlConfig, Statement.RETURN_GENERATED_KEYS)) {
            pstmtConfig.setString(1, config.getName());
            pstmtConfig.setString(2, config.getCatastrophe());
            int affectedRows = pstmtConfig.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating configuration failed, no rows affected.");
            }

            int configurationId;
            try (ResultSet generatedKeys = pstmtConfig.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    configurationId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating configuration failed, no ID obtained.");
                }
            }

            associateParcelsWithConfiguration(configurationId, selectedParcels);

            for (Plane plane : selectedPlanes) {
                associatePlaneWithConfiguration(configurationId, plane.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; 
        }
    }

    public void associatePlaneWithConfiguration(int configurationId, int planeId) throws SQLException {
        String sql = "INSERT INTO configuration_aircraft (configurationId, aircraftId) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, configurationId);
            pstmt.setInt(2, planeId);
            pstmt.executeUpdate();
        }
    }

    
    public void associateParcelsWithConfiguration(int configurationId, List<Parcel> selectedParcels) throws SQLException {
        String sql = "INSERT INTO configuration_parcel (configurationId, parcelId) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Parcel parcel : selectedParcels) {
                pstmt.setInt(1, configurationId);
                pstmt.setInt(2, parcel.getId());
                pstmt.executeUpdate();
            }
        }
    }


    
    public List<Parcel> getAllParcels() {
        List<Parcel> parcels = new ArrayList<>();
        String sql = "SELECT * FROM parcel";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Parcel parcel = new Parcel();
                parcel.setId(rs.getInt("id"));
                parcel.setModel(rs.getString("module"));
                parcels.add(parcel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parcels;
    }
    
    public List<Plane> getAllAircraft() {
        List<Plane> aircraft = new ArrayList<>();
        String sql = "SELECT * FROM aircraft";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Plane plane = new Plane();
                plane.setId(rs.getInt("id")); 
                plane.setName(rs.getString("name"));
                plane.setMaxLoad(rs.getFloat("maxload"));
                plane.setDoorSize(parseDim2D(rs.getString("doorSize")));
                plane.setCargoHold(parseDim3D(rs.getString("cargoHold")));
                aircraft.add(plane);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aircraft;
    }



}
