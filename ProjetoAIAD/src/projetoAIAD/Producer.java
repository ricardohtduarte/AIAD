package projetoAIAD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jade.lang.acl.ACLMessage;

import repast.simphony.context.Context;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;

import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;

import sajas.core.AID;


public class Producer extends Agent{
	private int id;
	private Random random = new Random();
	private Double angle = null;
	private final double randomness = 0.05;
	
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;

	
	Producer(int id)
	{
		super();
		this.id = id;
	}
	
	int getId(){
		return id;
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

                 while ((msg = receive())!=null)
                     System.out.println(msg.getContent());
                 
                
                 
             }
        });
	}
	
	
	@ScheduledMethod(start = 2, interval = 10000)
	public void stepProducer() {
	
		
		normalMovement();
		
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
