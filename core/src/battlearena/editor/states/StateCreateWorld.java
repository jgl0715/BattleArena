package battlearena.editor.states;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import battlearena.editor.WorldEditor;
import battlearena.editor.view.HUDCreateWorld;

public class StateCreateWorld extends battlearena.common.states.State
{

	// World name
	// World width and height
	// World tileset

	private HUDCreateWorld hudCreateWorld;

	public StateCreateWorld()
	{
		super("Create World");
	}

	@Override
	public void create()
	{
		hudCreateWorld = new HUDCreateWorld(WorldEditor.I.getUiSkin());
		hudCreateWorld.create();
	}

	@Override
	public void dispose()
	{

	}

	@Override
	public void resized(int width, int height)
	{
		hudCreateWorld.resize(width, height);
	}

	@Override
	public void show(Object transitionInput)
	{

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postUiRender()
	{
		// TODO Auto-generated method stub

		hudCreateWorld.render();
		
	}

}
