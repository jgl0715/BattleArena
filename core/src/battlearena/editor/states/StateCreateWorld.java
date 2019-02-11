package battlearena.editor.states;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import battlearena.editor.WorldEditor;

public class StateCreateWorld extends State
{

	// World name
	// World width and height
	// World tileset
	
	private Label nameLabel;
	private TextField nameField;
	private Label widthLabel;
	private TextField widthField;
	private Label heightLabel;
	private TextField heightField;
	
	public StateCreateWorld()
	{
		super("Create World");
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
		Table root = WorldEditor.I.getRootComponent();
		Skin uiSkin = WorldEditor.I.getUiSkin();
		root.clear();
		root.defaults().pad(5);
		root.center();
		{
			
		}
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
		
	}

}
