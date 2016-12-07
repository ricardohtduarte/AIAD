package projetoAIAD;

public class Base {
	
	private int coordX;
	private int coordY;
	private int maxStore=100000;
	
	private int currentStore=0;
	
	int id;
	
	public Base(int id){
		this.id=id;
		
	}
	

	
	public int getStoredQuantity(){
		return currentStore;
	}
	public void setStoredQuantity(int quantity){
		this.currentStore+=quantity;
	}

}
