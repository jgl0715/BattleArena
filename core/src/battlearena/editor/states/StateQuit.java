package battlearena.editor.states;

import com.badlogic.gdx.Gdx;

public class StateQuit extends battlearena.common.states.State
{

	public StateQuit()
	{
		super("Quit");
	}

	@Override
	public void create()
	{
		
	}

	@Override
	public void dispose()
	{
		
	}

	@Override
	public void resized(int width, int height)
	{
		
	}

	@Override
	public void show(Object transitionInput)
	{
		Gdx.app.exit();
	}

	@Override
	public void hide()
	{
		
	}

	@Override
	public void update(float delta)
	{
		
	}

	@Override
	public void preUiRender()
	{
		
	}

	@Override
	public void postUiRender()
	{
		
	}

}
