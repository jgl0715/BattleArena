package battlearena.editor.states;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import battlearena.editor.WorldEditor;

public class StateMainMenu extends State
{

	private TextButton createWorld;
	private TextButton loadWorld;
	private TextButton createTileset;
	private TextButton loadTileset;
	private TextButton quit;

	public StateMainMenu()
	{
		super("Main Menu");
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
			createWorld = new TextButton("Create World", uiSkin);
			loadWorld = new TextButton("Load World", uiSkin);
			createTileset = new TextButton("Create Tileset", uiSkin);
			loadTileset = new TextButton("Load Tileset", uiSkin);
			quit = new TextButton("Quit", uiSkin);

			createWorld.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					super.clicked(event, x, y);

					WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_CREATE_WORLD);
				}
			});

			loadWorld.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					super.clicked(event, x, y);
					
					WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_LOAD_WORLD);
				}
			});

			createTileset.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					super.clicked(event, x, y);
					
					WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_CREATE_TILESET);
				}
			});

			loadTileset.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					super.clicked(event, x, y);
					
					WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_LOAD_TILESET);
				}
			});

			quit.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					super.clicked(event, x, y);
					
					WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_QUIT);
				}
			});

			root.add(createWorld).width(300).row();
			root.add(loadWorld).width(300).row();
			root.add(createTileset).width(300).row();
			root.add(loadTileset).width(300).row();
			root.add(quit).width(300).row();
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
		
	}

	@Override
	public void postUiRender()
	{
		
	}

}
