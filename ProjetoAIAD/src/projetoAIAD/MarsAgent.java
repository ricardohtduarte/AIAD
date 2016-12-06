package projetoAIAD;

import java.util.ArrayList;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;
import sajas.core.Agent;
import sajas.core.behaviours.*;

public class MarsAgent extends Agent{
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	protected boolean recebeu=true;
	
	protected AID sender=null;
	
	public MarsAgent() {
		super();	
	}

	@Override
	public void setup(){
		super.setup();
		Context context = ContextUtils.getContext(this);
		space = (ContinuousSpace<Object>)context.getProjection("space");
		NdPoint pt = space.getLocation(this);
		grid = (Grid<Object>)context.getProjection("grid");
		grid.moveTo(this, (int) pt.getX(), (int) pt.getY());
		
		/*
		if(this.getClass().isAssignableFrom(Spotter.class)){
			
		}
		*/
		
		addBehaviour(new CyclicBehaviour(this) 
        {
             public void action() 
             {
            	 ACLMessage msg;
            	 
                 while ((msg = receive())!=null){
                	 
                	 if(Producer.class.isAssignableFrom(myAgent.getClass())){
                        	 sender=msg.getSender();
                             System.out.println("Producer received: "+msg.getContent());
                       
              		}else if(Spotter.class.isAssignableFrom(myAgent.getClass())){
              			System.out.println("Spotter received:"+msg.getContent());
              		}
                	
                 }
                 
                 
                
             }
        });
	}

}
