package projetoAIAD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import jade.lang.acl.ACLMessage;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import sajas.core.AID;
import sajas.core.Agent;
import sajas.core.behaviours.*;

public class Spotter extends MarsAgent{
	
	private Random random = new Random();
	private Double angle = null;
	private final double randomness = 0.05;
	private int id;
	
	Spotter(int id)
	{
		super();
		this.id = id;
	}
	

	@ScheduledMethod(start = 2, interval = 10000)
	public void stepSpotter() {
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    
    	 NdPoint myPoint = space.getLocation(new AID( "Spotter 1" , AID.ISLOCALNAME));
    	 ContinuousWithin<Object> t = new ContinuousWithin<Object>(space, (Object)this, 2.0);
    	 Iterator<Object> iterador = t.query().iterator();
    	 
    	 while(iterador.hasNext())
    	 {
    		 Object elemento = iterador.next();
    		 if(elemento instanceof Spotter && ((Spotter) elemento).id != this.id)
    		 {
    			 msg.setContent( ((Spotter) elemento).id + " estas perto de mim " + this.id );
    			 msg.addReceiver( new AID( "Spotter " + ((Spotter) elemento).id, AID.ISLOCALNAME) );
    	 	     send(msg);
    		 } 
    	 }
	     
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
