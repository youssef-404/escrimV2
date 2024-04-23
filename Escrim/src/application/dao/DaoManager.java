package application.dao;

import application.model.*;
import application.model.util.Dim2D;
import application.model.util.Dim3D;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DaoManager {

	protected static DaoManager instance;

protected static final String URL = "jdbc:sqlite:src/application/resources/db/db.db"; 
private ModelInterface model = Escrim.getInstance();

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
    
    
    public void getEscrim() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    	String sql ="SELECT * from Escrim where state=1";
    	try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
    		model.setState(rs.getBoolean("state"));
    		model.setDate(LocalDate.parse(rs.getString("date"),formatter));
    		model.setDescription(rs.getString("description"));
    		model.setDuration(rs.getInt("duration"));
    		model.setConfigurationId(rs.getInt("configuration"));
    		model.setCountry(rs.getString("country"));
 
    		
    	}catch(SQLException e){
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
            	getParcelMeds(parcel);
            	getParcelMedsSupply(parcel);
            	
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
    
    void getParcelMeds(Parcel parcel) {
    	try {
    		String sql = "SELECT * FROM medication WHERE parcel = ?;";
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    		PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, parcel.getId());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Medicaments medicament = new Medicaments(rs.getInt("id_med"),rs.getString("produit"),rs.getString("dci"),rs.getString("dosage"),LocalDate.parse(rs.getString("dlu"),formatter),rs.getInt("quantity"),rs.getString("medClass"),rs.getInt("parcel"));
				parcel.addMedicament(medicament);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    void getParcelMedsSupply(Parcel parcel) {
    	try {
    		String sql = "SELECT * FROM medicalSupply WHERE parcel = ?;";
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    		PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, parcel.getId());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Utility utility= new Utility(rs.getInt("id"),rs.getString("designation"),LocalDate.parse(rs.getString("dlu"),formatter),rs.getInt("quantity"),rs.getInt("parcel"));
				parcel.addutility(utility);
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
    

    // Patient Dao
    
    public List<Patient> getPatients(){
    	String sql =null;
		sql = "SELECT * FROM patient";
        List<Patient> Patients = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
            	Patient Patient = new Patient(rs.getInt("id_patient "),rs.getString("name"),rs.getInt("age"),rs.getString("gender"));
            	Patients.add(Patient);
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
        return Patients;
    }
    
    public int addPatient(Patient p) {
        String sql = "INSERT INTO patient (name, age, gender) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, p.getName());
            stmt.setInt(2, p.getAge());
            stmt.setString(3, p.getGender());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 1) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    p.setId(generatedId);
                } else {
                    throw new SQLException("Failed to retrieve auto-generated ID for new patient.");
                }
            } else {
                throw new SQLException("Failed to add new patient.");
            }
            
            return p.getId(); // Return the ID of the newly added patient
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate failure
        }
    }
    
    public int addObservation(String observationText, LocalDate selectedDate, int patient) {
        String sql = "INSERT INTO visit (observation, dateOfVist, patient) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, observationText);
            stmt.setString(2, selectedDate.toString());
            stmt.setInt(3, patient);
            
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 1) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Return the ID of the newly added observation
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve auto-generated ID for new observation.");
                }
            } else {
                throw new SQLException("Failed to add new observation.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate failure
        }
    }
    
    public Patient getAllObservations(int id,String Name, int age, String gender) {
        List<String> observations = new ArrayList<>();
        List<String> visitDates = new ArrayList<>();
        String sql = "SELECT * FROM visit WHERE patient = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int observationId = rs.getInt("id");
                    String observationText = rs.getString("observation");
                    LocalDate dateOfVisit = LocalDate.parse(rs.getString("dateOfVist"));
                    observations.add(observationText);
                    visitDates.add(dateOfVisit.toString());
                }
            }
            // Set observations and visitDates lists for the patient 
            Patient patient = new Patient(id,Name,age,gender);
            patient.setObservations(observations);
            patient.setVisitDates(visitDates);
            return patient;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Patient(0,null,0,null);
        }
		
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
                config.setColis(fetchColisForConfiguration(config.getId()));
                configurations.add(config);
                
                System.out.println(fetchColisForConfiguration(config.getId()));
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
    
    private List<Parcel> fetchColisForConfiguration(int configId){
    	List<Parcel> Allparcels = getAllParcels();
    	List<Parcel> configParcels = new ArrayList<>();
    	String sql= "select parcelId from Configuration_parcel where configurationId=?;";
    	try {
    		PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, configId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				int parcelId= rs.getInt("parcelId");
				Parcel parcel =Allparcels.stream()
				        .filter(pcl -> pcl.getId()== parcelId)
				        .findFirst()
				        .orElse(null);
				
				if(parcel != null) {
					configParcels.add(parcel);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return configParcels;
    	
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
            	parcel.setAffectaire(rs.getString("assignment"));
            	parcel.setModel(rs.getString("module"));
            	parcel.setNominal(rs.getString("nominal"));
            	parcel.setType(rs.getString("type"));
            	parcel.setSecteur(rs.getString("sector"));
            	String[] dim = (rs.getString("dimension")).split("X");
            	parcel.setDim(new Dim3D(Double.parseDouble(dim[0]),Double.parseDouble(dim[1]),Double.parseDouble(dim[2])));
            	parcel.setWeight(rs.getFloat("weight"));
            	parcel.setVolume(rs.getFloat("volume"));
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

    public void updateConfiguration(Configuration config, List<Parcel> selectedParcels, List<Plane> selectedPlanes) throws SQLException {
        String sqlConfig = "UPDATE configuration SET name = ?, disaster = ? WHERE id = ?";

        try (PreparedStatement pstmtConfig = connection.prepareStatement(sqlConfig)) {
            pstmtConfig.setString(1, config.getName());
            pstmtConfig.setString(2, config.getCatastrophe());
            pstmtConfig.setInt(3, config.getId());
            int affectedRows = pstmtConfig.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating configuration failed, no rows affected.");
            }

            updateParcelsForConfiguration(config.getId(), selectedParcels);
            updatePlanesForConfiguration(config.getId(), selectedPlanes);

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } 
    }

    private void updateParcelsForConfiguration(int configId, List<Parcel> parcels) throws SQLException {
        String sqlDelete = "DELETE FROM configuration_parcel WHERE configurationId = ?";
        try (PreparedStatement pstmtDelete = connection.prepareStatement(sqlDelete)) {
            pstmtDelete.setInt(1, configId);
            pstmtDelete.executeUpdate();
        }

        String sqlInsert = "INSERT INTO configuration_parcel (configurationId, parcelId) VALUES (?, ?)";
        try (PreparedStatement pstmtInsert = connection.prepareStatement(sqlInsert)) {
            for (Parcel parcel : parcels) {
                pstmtInsert.setInt(1, configId);
                pstmtInsert.setInt(2, parcel.getId());
                pstmtInsert.executeUpdate();
            }
        }
    }

    private void updatePlanesForConfiguration(int configId, List<Plane> planes) throws SQLException {
        String sqlDelete = "DELETE FROM configuration_aircraft WHERE configurationId = ?";
        try (PreparedStatement pstmtDelete = connection.prepareStatement(sqlDelete)) {
            pstmtDelete.setInt(1, configId);
            pstmtDelete.executeUpdate();
        }

        String sqlInsert = "INSERT INTO configuration_aircraft (configurationId, aircraftId) VALUES (?, ?)";
        try (PreparedStatement pstmtInsert = connection.prepareStatement(sqlInsert)) {
            for (Plane plane : planes) {
                pstmtInsert.setInt(1, configId);
                pstmtInsert.setInt(2, plane.getId());
                pstmtInsert.executeUpdate();
            }
        }
    }

    
    
    public void deleteConfiguration(int configId) throws SQLException {

    	deleteParcelsForConfiguration(configId);
    	deletePlanesForConfiguration(configId);

    	String sqlConfig = "DELETE FROM configuration WHERE id = ?";
    	try (PreparedStatement pstmtConfig = connection.prepareStatement(sqlConfig)) {
    		pstmtConfig.setInt(1, configId);
    		int affectedRows = pstmtConfig.executeUpdate();
    		if (affectedRows == 0) {
    			throw new SQLException("Deleting configuration failed, no rows affected.");
    		}
        } catch (SQLException e) {
        	e.printStackTrace();
            throw e;
        } 
    }
    
    
    private void deleteParcelsForConfiguration(int configId) throws SQLException {
        String sqlDeleteParcels = "DELETE FROM configuration_parcel WHERE configurationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sqlDeleteParcels)) {
            pstmt.setInt(1, configId);
            pstmt.executeUpdate();
        }
    }

    private void deletePlanesForConfiguration(int configId) throws SQLException {
        String sqlDeletePlanes = "DELETE FROM configuration_aircraft WHERE configurationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sqlDeletePlanes)) {
            pstmt.setInt(1, configId);
            pstmt.executeUpdate();
        }
    }
    
    // Hospitaliser DAO
    
    public List<Bed> getAllDeployedBeds() {
        List<Bed> beds = new ArrayList<>();
        String sql = "SELECT b.id, b.inUse, b.parcel FROM bed b JOIN parcel p ON b.parcel = p.id JOIN stockDeploye sd ON p.id = sd.parcel WHERE b.inUse = 0";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Bed bed = new Bed(rs.getInt("id"),1);
                beds.add(bed);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beds;
    }
    
    public void toggleBedInUseState(int bedId) {
        String sql = "UPDATE bed SET inUse = CASE WHEN inUse = 0 THEN 1 ELSE 0 END WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bedId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void addHospitalizedEntry(int patientId, String name, int bedId, String entryDate, int expectedStay) {
        String sql = "INSERT INTO hospitalized (patientId, name, bedId, entryDate, expectedStay) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            stmt.setString(2, name);
            stmt.setInt(3, bedId);
            stmt.setString(4, entryDate);
            stmt.setInt(5, expectedStay);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<HospitalizedPatient> getAllHospitalized() {
        List<HospitalizedPatient> hospitalizedPatients = new ArrayList<>();
        String sql = "SELECT * FROM hospitalized";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                HospitalizedPatient patient = new HospitalizedPatient(
                    rs.getInt("id"),
                    rs.getInt("patientId"),
                    rs.getString("name"),
                    rs.getInt("bedId"),
                    LocalDate.parse(rs.getString("entryDate")),
                    rs.getInt("expectedStay")
                );
                hospitalizedPatients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hospitalizedPatients;
    }

    public boolean deleteHospitalizedEntry(String patientName, int bedId, LocalDate entryDate, int expectedStay) {
        String sql = "DELETE FROM hospitalized WHERE name = ? AND bedId = ? AND entryDate = ? AND expectedStay = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the parameters
            pstmt.setString(1, patientName);
            pstmt.setInt(2, bedId);
            pstmt.setString(3, entryDate.toString()); // Assuming entryDate is stored as a string in the database
            pstmt.setInt(4, expectedStay);
            
            // Execute the deletion
            int affectedRows = pstmt.executeUpdate();
            
            // Check if any rows were affected
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getUsedBedsNumber() {
        String sql = "SELECT COUNT(*) AS usedBeds FROM bed WHERE inUse = 1";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("usedBeds");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Return 0 if no data is found or an exception occurs
        return 0;
    }

    public int getUnusedBedsNumber() {
        String sql = "SELECT COUNT(*) AS unusedBeds FROM bed WHERE inUse = 0";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("unusedBeds");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Return 0 if no data is found or an exception occurs
        return 0;
    }


}
