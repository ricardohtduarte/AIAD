package projetoAIAD;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import javax.imageio.ImageIO;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.valueLayer.GridValueLayer;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

public class MarsLauncher extends RepastSLauncher{

	private ContainerController mainContainer;
	private ContainerController agentContainer;
	private double[] startPoint = new double[]{25,25};
	private Context<Object> context = null;

	@Override
	public String getName() {
		return "Exploração de espaço desconhecido";
	}

	@Override
	protected void launchJADE() {
		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		mainContainer = rt.createMainContainer(p1);
		agentContainer = mainContainer;
		launchAgents();
	}

	private void launchAgents() {
		Parameters params = RunEnvironment.getInstance().getParameters();
		try {
			// create spotters
			int spotterCount =  (Integer) params.getValue("spotter_count");
			for (int i = 0; i < spotterCount; i++) {
				Spotter spotter = new Spotter(i);
				agentContainer.acceptNewAgent("Spotter " + i, spotter).start();
			}
			
			//create producers 
			int producerCount =  (Integer) params.getValue("producer_count");
			for (int i = 0; i < producerCount; i++) {
				Producer producer = new Producer(i);
				agentContainer.acceptNewAgent("Producer " + i, producer).start();
			}
			
			//create producers 
			int transporterCount =  (Integer) params.getValue("transporter_count");
			for (int i = 0; i < transporterCount; i++) {
				Transporter transporter = new Transporter(i);
				agentContainer.acceptNewAgent("Transporter " + i, transporter).start();
			}
			
			
			
			ContinuousSpace<Object> space = ((ContinuousSpace<Object>)this.context.getProjection("space"));
			for (Object obj : context.getObjects(Producer.class))
			{
				space.moveTo(obj, startPoint);
			}
			for (Object obj : context.getObjects(Spotter.class))
			{
				space.moveTo(obj, startPoint);
			}
			for (Object obj : context.getObjects(Transporter.class))
			{
				space.moveTo(obj, startPoint);
			}
			for (Object obj : context.getObjects(Base.class))
			{
				space.moveTo(obj, startPoint);
			}
			

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public Context build(Context<Object> context) {
		
		if (startPoint == null)
			throw new RuntimeException("Map start point not found");
		MarsBuilder marsBuilder = new MarsBuilder();
		this.context = context = marsBuilder.build(context);
		return super.build(context);
	}
}
