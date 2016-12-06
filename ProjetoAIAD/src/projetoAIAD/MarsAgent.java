package projetoAIAD;

import java.util.ArrayList;

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

                 while ((msg = receive())!=null)
                     System.out.println(msg.getContent());
             }
        });
	}

}
