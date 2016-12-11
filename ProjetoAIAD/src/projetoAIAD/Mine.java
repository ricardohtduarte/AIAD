package projetoAIAD;

public class Mine {
	
	private int mine_quantity;
	private int quantidadeMinada;
	boolean explored=false;
	
	int id;
	
	public Mine(int id,int mine_quantity){
		this.mine_quantity = mine_quantity;
		this.id=id;
		
	}
	
	public int getID(){
		return id;
	}
	
	public int getQuantidadeMinada(){
		return quantidadeMinada;
	}
	
	public void incrementQuantidadeMinada(){
		quantidadeMinada++;
		mine_quantity--;
	}
		
	public int getQuantity(){
		return mine_quantity;
	}
}
