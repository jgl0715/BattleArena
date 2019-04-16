package battlearena.editor.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import battlearena.common.gui.HUD;

public class HUDCreateWorld extends HUD
{

    public Table tableName;
    public Table tableSize;
    public Table tableTileset;
    public Table tableButtons;
    public Label labelWorldName;
    public TextField fieldWorldName;
    public Label labelWidth;
    public TextField fieldWidth;
    public Label labelHeight;
    public TextField fieldHeight;
    public Label labelTilesetPath;
    public TextField fieldTilesetPath;
    public TextButton buttonTilesetPath;
    public TextButton confirmButton;
    public TextButton cancelButton;

    public HUDCreateWorld(Skin skin)
    {
        super(skin);

        labelWorldName = new Label("Name", skin);
        fieldWorldName = new TextField("", skin);

        labelWidth = new Label("Width", skin);
        fieldWidth = new TextField("", skin);
        labelHeight = new Label("Height", skin);
        fieldHeight = new TextField("", skin);

        labelTilesetPath = new Label("Tileset", skin);
        fieldTilesetPath = new TextField("", skin);
        buttonTilesetPath = new TextButton("...", skin);

        confirmButton = new TextButton("Confirm", skin);
        cancelButton = new TextButton("Cancel", skin);

        tableName = new Table(skin);
        tableSize = new Table(skin);
        tableTileset = new Table(skin);
        tableButtons = new Table(skin);

        tableName.add(labelWorldName).pad(5).width(150);
        tableName.add(fieldWorldName).width(150);

        tableSize.add(labelWidth).width(100);
        tableSize.add(fieldWidth).padRight(5).width(50);
        tableSize.add(labelHeight).width(100);
        tableSize.add(fieldHeight).width(50);

        tableTileset.add(labelTilesetPath).pad(5).width(150);
        tableTileset.add(fieldTilesetPath).width(120);
        tableTileset.add(buttonTilesetPath).width(30);

        tableButtons.add(confirmButton).width(152.5f);
        tableButtons.add(cancelButton).width(152.5f).row();

        root.add(tableName).row();
        root.add(tableSize).row();
        root.add(tableTileset).row();
        root.add(tableButtons);
    }

}
