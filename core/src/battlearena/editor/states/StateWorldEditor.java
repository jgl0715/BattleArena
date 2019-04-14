package battlearena.editor.states;

import battlearena.editor.WorldEditor;
import battlearena.editor.view.HUDWorldEditor;

public class StateWorldEditor extends battlearena.common.states.State
{

	private HUDWorldEditor hudWorldEditor;

	public StateWorldEditor()
	{
		super("World Editor");
	}

	@Override
	public void create()
	{
		hudWorldEditor = new HUDWorldEditor(WorldEditor.I.getUiSkin());
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
		hudWorldEditor.setAsInput();
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
	public void render()
	{
		
	}


}
