package battlearena.editor.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import javax.swing.JFileChooser;

import battlearena.common.file.TilesetImporter;
import battlearena.common.tile.Tileset;
import battlearena.common.world.TiledWorld;
import battlearena.editor.WorldEditor;
import battlearena.editor.view.HUDCreateWorld;

public class StateCreateWorld extends battlearena.common.states.State
{

	private HUDCreateWorld hudCreateWorld;
	private TilesetImporter importer;
	private Tileset worldTileset;

	public StateCreateWorld()
	{
		super("Create World");
	}

	@Override
	public void create()
	{
		hudCreateWorld = new HUDCreateWorld(WorldEditor.I.getUiSkin());
		importer = new TilesetImporter();

		hudCreateWorld.buttonTilesetPath.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				JFileChooser chooser = new JFileChooser();
				int res = chooser.showOpenDialog(null);
				if (res == JFileChooser.APPROVE_OPTION)
				{
					importer.setImportLocation(chooser.getSelectedFile().getAbsolutePath());
					worldTileset = importer.imp();
				}

				hudCreateWorld.fieldTilesetPath.setText(chooser.getSelectedFile().getAbsolutePath());

				if(worldTileset != null)
				{
					hudCreateWorld.fieldTilesetPath.setColor(Color.GREEN);
				}
				else
				{
					hudCreateWorld.fieldTilesetPath.setColor(Color.RED);
				}
			}
		});

		hudCreateWorld.confirmButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				if(confirmInput())
				{
					String worldName = hudCreateWorld.fieldWorldName.getText();
					int width = Integer.parseInt(hudCreateWorld.fieldWidth.getText());
					int height = Integer.parseInt(hudCreateWorld.fieldWidth.getText());

					if(worldTileset != null)
					{
						TiledWorld worldObject = new TiledWorld(worldName, worldTileset, width, height);
						WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_EDIT_WORLD, worldObject);
					}

				}

			}
		});

		hudCreateWorld.cancelButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_MAIN_MENU);
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
		hudCreateWorld.resize(width, height);
	}

	@Override
	public void show(Object transitionInput)
	{
		hudCreateWorld.setAsInput();
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
		hudCreateWorld.render();
	}

	public boolean confirmInput()
	{
		String path = hudCreateWorld.fieldTilesetPath.getText();
		FileHandle file = Gdx.files.absolute(path);
		String ext = file.extension();
		String[] supportedExtensions =
				{ "ts" };

		if (path.equalsIgnoreCase(""))
			return false;

		if (!file.exists())
			return false;

		try
		{
			Integer.parseInt(hudCreateWorld.fieldWidth.getText());
			Integer.parseInt(hudCreateWorld.fieldHeight.getText());
		} catch (NumberFormatException e)
		{
			return false;
		}

		for (String x : supportedExtensions)
			if (x.equalsIgnoreCase(ext))
				return true;

		return false;
	}

}
