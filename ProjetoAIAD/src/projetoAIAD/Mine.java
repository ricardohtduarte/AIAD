package projetoAIAD;

public class Mine {
	
	private int mine_quantity;
	private int coordX;
	private int coordY;
	
	public Mine(int mine_quantity){
		this.mine_quantity = mine_quantity;
		
	}
	
	public int getQuantity(){
		return mine_quantity;
	}
}
