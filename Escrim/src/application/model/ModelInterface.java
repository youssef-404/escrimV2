package application.model;

import java.time.LocalDate;

public interface ModelInterface {
	public Inventory getGlobalInventory();
	public void setGlobalInventory(Inventory globalInventory) ;
	public Inventory getLocalInventory() ;
	public void setLocalInventory(Inventory localInventory);
	public LocalDate getDate();
	public void setDate(LocalDate date);
	public int getDuration();
	public void setDuration(int duration);
	public String getDescription();
	public void setDescription(String description) ;
	public int getConfigurationId();
	public void setConfigurationId(int configurationId);
	public void setState(boolean state);
	public String getCountry();
	public void setCountry(String country);
	public boolean getState();
}
