package battlearena.editor.states;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import battlearena.editor.WorldEditor;
import battlearena.editor.view.HUDMainMenu;

public class StateMainMenu extends battlearena.common.states.State
{

	private HUDMainMenu hudMainMenu;

	public StateMainMenu()
	{
		super("Main Menu");
	}

	@Override
	public void create()
	{

		hudMainMenu = new HUDMainMenu(WorldEditor.I.getUiSkin());

		hudMainMenu.createWorld.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_CREATE_WORLD);
			}
		});

		hudMainMenu.loadWorld.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_LOAD_WORLD);
			}
		});

		hudMainMenu.createTileset.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_CREATE_TILESET);
			}
		});

		hudMainMenu.loadTileset.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_LOAD_TILESET);
			}
		});

		hudMainMenu.quit.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_QUIT);
			}
		});

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
		hudMainMenu.setAsInput();
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
		hudMainMenu.render();
	}

}
