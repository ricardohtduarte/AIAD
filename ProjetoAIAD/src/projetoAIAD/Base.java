package projetoAIAD;

public class Base {
	private int currentStore=0;
	
	int id;
	
	public Base(int id){
		this.id=id;
		
	}
	

	
	public synchronized int getStoredQuantity(){
		return currentStore;
	}
	public synchronized void setStoredQuantity(int quantity){
		this.currentStore+=quantity;
	}

}
