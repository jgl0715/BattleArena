package battlearena.common.states;

import java.util.HashMap;
import java.util.Map;

public abstract class State
{

	private String stateName;
	// Cartesian product of SigmaxQ (maps alphabet to FSA state)
	private Map<String, State> transitionFunction;

	public State(String stateName)
	{
		this.stateName = stateName;
		transitionFunction = new HashMap<String, State>();
	}
	
	public String getStateName()
	{
		return stateName;
	}
	
	public void registerTransition(String transition, State destination)
	{
		if(destination == null)
			throw new IllegalArgumentException("State machine must be deterministic: cannot have a null transition destination.");
		
		transitionFunction.put(transition, destination);
	}
	
	public State getTransitionDest(String transition)
	{
		return transitionFunction.get(transition);
	}
	
	public boolean hasDefinedTransition(String transition)
	{
		return transitionFunction.containsKey(transition);
	}
	
	public abstract void create();
	public abstract void dispose();
	public abstract void resized(int width, int height);
	public abstract void show(Object transitionInput);
	public abstract void hide();
	public abstract void update(float delta);
	public abstract void preUiRender();
	public abstract void postUiRender();

	

}
