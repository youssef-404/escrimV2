package application.model;

public interface ModelInterface {
	public Inventory getGlobalInventory();
	public void setGlobalInventory(Inventory globalInventory) ;
	public Inventory getLocalInventory() ;

	public void setLocalInventory(Inventory localInventory);
}
