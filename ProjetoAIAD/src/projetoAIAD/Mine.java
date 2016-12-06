package projetoAIAD;

public class Mine {
	
	private int mine_quantity;
	private int coordX;
	private int coordY;
	boolean alreadyExplored=false;
	int id;
	
	public Mine(int id,int mine_quantity){
		this.mine_quantity = mine_quantity;
		this.id=id;
		
	}
	
	
	
	public int getQuantity(){
		return mine_quantity;
	}
}
