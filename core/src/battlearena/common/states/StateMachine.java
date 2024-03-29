package battlearena.common.states;

import com.badlogic.gdx.Gdx;

import java.util.HashSet;
import java.util.Set;

public class StateMachine
{

	// State transitions

	private Set<battlearena.common.states.State> states;
	private battlearena.common.states.State currentState;

	// -- States ---- s:
	// Main menu
	// Tileset Editor
	// World Editor
	// Create Tileset
	// Load Tileset
	// Create World
	// Load World

	public StateMachine()
	{
		states = new HashSet<battlearena.common.states.State>();
		currentState = null;
	}

	public battlearena.common.states.State getCurrent()
	{
		return currentState;
	}
	
	public void registerState(battlearena.common.states.State state)
	{
		registerState(state, false);
	}

	public void registerState(battlearena.common.states.State state, boolean initial)
	{
		// Create all registered states.
		state.create();

		if (initial)
		{
			currentState = state;
			currentState.show(null);
		}

		states.add(state);
	}

	public void registerTransition(battlearena.common.states.State src, battlearena.common.states.State dst, String transition)
	{
		if (!states.contains(src))
		{
			throw new IllegalArgumentException("Must register state before registering state transitions");
		}
		else
		{
			src.registerTransition(transition, dst);
		}
	}
	
	public void transition(String transition)
	{
		transition(transition, null);
	}

	public void transition(String transition, Object transitionInput)
	{
		if (!currentState.hasDefinedTransition(transition))
		{
			throw new IllegalArgumentException(currentState.getStateName() + " does not have a transition defined for \"" + transition + "\"");
		}
		else
		{
			battlearena.common.states.State transitionDst = currentState.getTransitionDest(transition);

			if (!states.contains(transitionDst))
				throw new IllegalStateException("Transition destination state has not yet been registered. ");

			// Hide the current state and show the next state.
			currentState.hide();
			currentState = transitionDst;
			currentState.resized(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			currentState.show(transitionInput);
		}
	}

	public void updateCurrent(float delta)
	{
		currentState.update(delta);
	}

	public void resizeCurrent(int width, int height)
	{
		currentState.resized(width, height);
	}

	public void renderCurrent()
	{
		currentState.render();
	}

	public void dispose()
	{
		for (battlearena.common.states.State state : states)
		{
			state.dispose();
		}
	}

}
