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

public class StateCreateTileset extends battlearena.common.states.State
{

	private Label nameLabel;
	private TextField nameField;
	private Label widthLabel;
	private TextField widthField;
	private Label heightLabel;
	private TextField heightField;
	private Label tilesheetLabel;
	private TextField tilesheetField;
	private TextButton chooseFileButton;
	private TextButton confirmButton;
	private TextButton cancelButton;

	public StateCreateTileset()
	{
		super("Create Tileset");
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
		Table nameRow = new Table();
		Table sizeRow = new Table();
		Table tilesheetRow = new Table();
		Table buttonsRow = new Table();

		Table tilesheetColumn = new Table();
		Skin uiSkin = WorldEditor.I.getUiSkin();
		root.clear();
		root.defaults().pad(5).left();

		nameRow.defaults().pad(0).left();
		sizeRow.defaults().pad(0).left();
		tilesheetRow.defaults().padRight(0).left();
		buttonsRow.defaults().pad(0).left();

		root.center();
		{
			nameLabel = new Label("Name", uiSkin);
			widthLabel = new Label("Width", uiSkin);
			heightLabel = new Label("Height", uiSkin);
			tilesheetLabel = new Label("Tilesheet", uiSkin);

			nameField = new TextField("", uiSkin);
			widthField = new TextField("", uiSkin);
			heightField = new TextField("", uiSkin);
			tilesheetField = new TextField("", uiSkin);
			chooseFileButton = new TextButton("...", uiSkin);

			confirmButton = new TextButton("Confirm", uiSkin);
			cancelButton = new TextButton("Cancel", uiSkin);

			nameRow.add(nameLabel).width(150).padRight(5);
			nameRow.add(nameField).width(150);

			sizeRow.add(widthLabel).width(100);
			sizeRow.add(widthField).width(50).padRight(5);
			sizeRow.add(heightLabel).width(100);
			sizeRow.add(heightField).width(50);

			tilesheetRow.add(tilesheetLabel).width(150).padRight(5);
			tilesheetColumn.add(tilesheetField).width(120);
			tilesheetColumn.add(chooseFileButton).width(30);
			tilesheetRow.add(tilesheetColumn);

			buttonsRow.add(confirmButton).width(152.5f);
			buttonsRow.add(cancelButton).width(152.5f).row();

			root.add(nameRow).row();
			root.add(sizeRow).row();
			root.add(tilesheetRow).row();
			root.add(buttonsRow).row();

			chooseFileButton.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					super.clicked(event, x, y);

					JFileChooser chooser = new JFileChooser();
					int res = chooser.showOpenDialog(null);
					if (res == JFileChooser.APPROVE_OPTION)
					{
						tilesheetField.setText(chooser.getSelectedFile().getAbsolutePath());
					}
				}
			});

			confirmButton.addListener(new ClickListener()
			{

				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					super.clicked(event, x, y);

					if (confirmInput())
					{
						// Make new tileset
						String name = nameField.getText();
						String path = tilesheetField.getText();
						int width = Integer.parseInt(widthField.getText());
						int height = Integer.parseInt(widthField.getText());
						Tileset newTileset = new Tileset(name, path, width, height);
						
						WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_EDIT_TILESET, newTileset);
					}
				}
			});

			cancelButton.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					super.clicked(event, x, y);

					WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_MAIN_MENU);
				}
			});
		}

	}

	public boolean confirmInput()
	{
		String path = tilesheetField.getText();
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
			Integer.parseInt(widthField.getText());
			Integer.parseInt(heightField.getText());
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
	public void preUiRender()
	{
		
	}

	@Override
	public void postUiRender()
	{
		
	}

}
