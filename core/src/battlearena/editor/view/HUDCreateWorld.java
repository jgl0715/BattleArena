package battlearena.editor.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import battlearena.common.gui.HUD;

public class HUDCreateWorld extends HUD
{

    private Table tableName;
    private Table tableSize;
    private Table tableTileset;
    private Label labelWorldName;
    private TextField fieldWorldName;
    private Label labelWidth;
    private TextField fieldWidth;
    private Label labelHeight;
    private TextField fieldHeight;
    private Label labelTilesetPath;
    private TextField fieldTilesetPath;
    private TextButton buttonTilesetPath;

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

        tableName = new Table(skin);
        tableSize = new Table(skin);
        tableTileset = new Table(skin);

        tableName.add(labelWorldName).pad(5);
        tableName.add(fieldWorldName).pad(5);

        tableSize.add(labelWidth).pad(5);
        tableSize.add(fieldWidth).pad(5);
        tableSize.add(labelHeight).pad(5);
        tableSize.add(fieldHeight).pad(5);

        tableTileset.add(labelTilesetPath);
        tableTileset.add(fieldWorldName);
        tableTileset.add(buttonTilesetPath);


        root.add(tableName);
        root.add(tableSize);
        root.add(tableTileset);
    }

}
