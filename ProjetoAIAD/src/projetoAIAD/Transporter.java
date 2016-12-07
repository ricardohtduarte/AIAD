package projetoAIAD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.poi.util.SystemOutLogger;

import jade.lang.acl.ACLMessage;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;
import sajas.core.AID;
import sajas.core.Agent;
import sajas.core.behaviours.*;

public class Transporter extends Agent{

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	private Random random = new Random();
	private Double angle = null;
	private final double randomness = 0.05;
	private int id;
	private int quantidadeTransportada;
	
	
	Object minaObj =null;
	Object baseObj =null;
	private boolean transporting = false;
	
	private double mineX;
	private double mineY;
	private int mineId;

	private boolean stopped = false;
	private boolean waiting = false;
	private boolean movingToBase=false;


	Transporter(int id)
	{
		super();
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public int getquantidade(){
		return quantidadeTransportada;
	}
	
	@Override
	public void setup(){
		super.setup();
		Context context = ContextUtils.getContext(this);
		space = (ContinuousSpace<Object>)context.getProjection("space");
		NdPoint pt = space.getLocation(this);
		grid = (Grid<Object>)context.getProjection("grid");
		grid.moveTo(this, (int) pt.getX(), (int) pt.getY());
		
		
		addBehaviour(new CyclicBehaviour(this) 
        {
             public void action() 
             {
            	 ACLMessage msg;

                 while ((msg = receive())!=null){
                	// System.out.println("ID " + id+"   Transporter RECEIVED:" + msg.getContent()+ "- SENDER "+ msg.getSender());
                      
                     if(msg.getContent().equals("yes")){
                    	 transporting=true;
                     }
                     else if(msg.getContent().equals("no")){
                    	 waiting=false;
                     }
                     else if(transporting ||waiting||stopped || movingToBase){
                    	 ACLMessage res = new ACLMessage(ACLMessage.INFORM);
                    	 res.setContent("no producer");
                    	 res.addReceiver( msg.getSender() );
            	 	     send(res);
                     }else{
                    	 String[] splited = msg.getContent().split(" ");
                    	 if(isNumeric(splited[0])&&isNumeric(splited[1]) && !transporting){
                    		 mineX=Double.parseDouble(splited[0]);
                    		 mineY=Double.parseDouble(splited[1]);
                    		 mineId=Integer.parseInt(splited[2]);
                    		 
                    		 
                    		 ACLMessage res = new ACLMessage(ACLMessage.INFORM);
                        	 res.setContent("ready producer");
                        	 res.addReceiver( msg.getSender() );
                	 	     send(res);
                	 	     
                    		 waiting = true;
                    	 }
                    	 
                     }
                 }
                 
             }
        });
	}

	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}

	@ScheduledMethod(start = 2, interval = 1)
	public void stepTransporter() {
		NdPoint myPoint = space.getLocation(this);
		ContinuousWithin<Object> t = new ContinuousWithin<Object>(space, (Object)this, 8.0);
		Iterator<Object> iterador = t.query().iterator();
		NdPoint minepoint = null;
		Object elemento=null;
		
		while(iterador.hasNext())
		{
			 elemento = iterador.next();
			if(elemento instanceof Mine ){
	
				if(minaObj!=null){
				}
				
				else if(((Mine)elemento).getID()==mineId){	
					minaObj=elemento;
				}		
			}else if(elemento instanceof Base){
				if(baseObj!=null){				
				}
				else{
					baseObj=elemento;
				}
			}
		}

		if(transporting){
			NdPoint mina= new NdPoint(mineX,mineY);
	
			if(movingToBase){
				NdPoint base= new NdPoint(25,25);
				if(isOnTopMine(base,space.getLocation(this))){	
					((Base) baseObj).setStoredQuantity(this.quantidadeTransportada);	
					transporting=false;
					movingToBase=false;
					minaObj=null;
		 	 	     this.quantidadeTransportada=0;
					System.out.println("Na base : "+((Base) baseObj).getStoredQuantity());
					
				}else{
					moveTowards(base);
				}
				
			}
			else if(!isOnTopMine(mina,space.getLocation(this))){
				moveTowards(mina);	
			}else if(minaObj!=null){
				 if(((Mine)minaObj).getQuantity()>0){
					//System.out.println("ID:"+id+"  Estou à espera faltam  "+ ((Mine)minaObj).getQuantity()+" na mina "+((Mine)minaObj).id);
				}else{
					this.quantidadeTransportada=((Mine)minaObj).getQuantidadeMinada();
					movingToBase=true;
		 	 	     waiting=false;
		 	 	     
				}
			}	
		}else if(waiting){
			
		}
		else{
			normalMovement();
		}
		 
	}

	public boolean isOnTopMine(NdPoint mine, NdPoint mypoint){
		
		return (Math.abs((int)mine.getX()-(int)mypoint.getX())<2 && Math.abs((int)mine.getY()-(int)mypoint.getY())<2);
		
	}

	public void normalMovement(){
		double rand = random.nextDouble();
		if (angle != null && rand > randomness )
			moveByAngle(this.angle);
		else if (angle != null && rand > randomness )
			moveByAngle(this.angle + 0.2);
		else if (angle != null && rand > randomness)
			moveByAngle(this.angle - 0.2);
		else
		{
			List<NdPoint> sites = findEmptySites();
			if (!sites.isEmpty())
			{
				moveTowards(sites.get(0));
			}
		}
	}

	public void moveTowards(NdPoint pt) {
		NdPoint myPoint = space.getLocation(this);
		if (!pt.equals(myPoint)) {
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			moveByAngle(SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint));
		}
	}

	public void moveByAngle(double angle)
	{
		this.angle = angle;
		space.moveByVector(this, 1, angle, 0);
		NdPoint myPoint = space.getLocation(this);
		grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
	}



	private List<NdPoint> findEmptySites(){
		List<NdPoint> emptySites = new ArrayList<NdPoint>();
		NdPoint pt = space.getLocation(this);
		double height = space.getDimensions().getHeight();
		double width = space.getDimensions().getWidth();

		for (int difx = -1; difx <= 1; difx++)
		{
			for (int dify = -1; dify <= 1; dify++)
			{
				if (difx == 0 && dify == 0) continue;
				double newx = pt.getX() + difx;
				double newy = pt.getY() + dify;

				emptySites.add(new NdPoint(newx, newy));
			}
		}

		Collections.shuffle(emptySites);
		return emptySites;
	}
}
