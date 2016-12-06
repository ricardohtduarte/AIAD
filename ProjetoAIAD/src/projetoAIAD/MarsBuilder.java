package projetoAIAD;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;
import repast.simphony.space.grid.WrapAroundBorders;

public class MarsBuilder implements ContextBuilder<Object> {
	
	final public static int WIDTH = 50;
	final public static int HEIGHT = 50;
	
	@Override
	public Context<Object> build(Context<Object> context) {
		context.setId("mars");

		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		spaceFactory.createContinuousSpace(
				"space", context, new RandomCartesianAdder<Object>(),
				new repast.simphony.space.continuous.WrapAroundBorders(),
				WIDTH, HEIGHT);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(), true, WIDTH, HEIGHT));
		
		int mineCount = 30;
		for(int i = 0; i < mineCount; i++){
			Mine mine = new Mine(i,50);
			context.add(mine);
		}
		
		return context;
	}
}
