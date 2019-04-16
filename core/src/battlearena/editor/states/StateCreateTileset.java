package battlearena.editor.states;

import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import battlearena.common.tile.Tileset;
import battlearena.editor.WorldEditor;
import battlearena.editor.view.HUDCreateTileset;

public class StateCreateTileset extends battlearena.common.states.State
{

	private HUDCreateTileset hudCreateTileset;

	public StateCreateTileset()
	{
		super("Create Tileset");
	}

	@Override
	public void create()
	{
		hudCreateTileset = new HUDCreateTileset(WorldEditor.I.getUiSkin());

		hudCreateTileset.chooseFileButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				JFileChooser chooser = new JFileChooser();
				int res = chooser.showOpenDialog(null);
				if (res == JFileChooser.APPROVE_OPTION)
				{
					hudCreateTileset.tilesheetField.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		hudCreateTileset.confirmButton.addListener(new ClickListener()
		{

			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				if (confirmInput())
				{
					// Make new tileset
					String name = hudCreateTileset.nameField.getText();
					String path = hudCreateTileset.tilesheetField.getText();
					int width = Integer.parseInt(hudCreateTileset.widthField.getText());
					int height = Integer.parseInt(hudCreateTileset.widthField.getText());
					Tileset newTileset = new Tileset(name, path, width, height);

					WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_EDIT_TILESET, newTileset);
				}
			}
		});

		hudCreateTileset.cancelButton.addListener(new ClickListener()
		{
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
		hudCreateTileset.resize(width, height);
	}

	@Override
	public void show(Object transitionInput)
	{
		hudCreateTileset.setAsInput();
	}

	public boolean confirmInput()
	{
		String path = hudCreateTileset.tilesheetField.getText();
		FileHandle file = Gdx.files.absolute(path);
		String ext = file.extension();
		String[] supportedExtensions =
		{ "jpg", "png", "bmp" };

		if (path.equalsIgnoreCase(""))
			return false;

		if (!file.exists())
			return false;

		try
		{
			Integer.parseInt(hudCreateTileset.widthField.getText());
			Integer.parseInt(hudCreateTileset.heightField.getText());
		} catch (NumberFormatException e)
		{
			return false;
		}

		for (String x : supportedExtensions)
			if (x.equalsIgnoreCase(ext))
				return true;

		return false;
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
		hudCreateTileset.render();
	}

}
