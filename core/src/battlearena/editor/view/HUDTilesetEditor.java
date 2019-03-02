package battlearena.editor.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import battlearena.common.gui.HUD;
import battlearena.common.tile.TileDefinition;
import battlearena.common.tile.Tileset;
import battlearena.editor.TileImage;

public class HUDTilesetEditor extends HUD
{

    // GUI for tile pane
    public Table editTilePane;
    public TileImage paneTileImage;
    public Label labelFrameTime;
    public TextField fieldFrameTime;

    // Bottom left menu GUI
    public Table tableBottomLeft;
    public TextButton buttonGrid;
    public TextButton buttonExportTileset;
    public TextButton buttonImportTileset;

    // Right menu for tile definitions
    public TextField fieldTilesetName;
    public Table tableDefinitions;
    public TextField tilesetNameLabel;
    public Label tileDefPaneLabel;
    public TextButton addTileDefButton;
    public TextButton deleteTileDefButton;
    public Table entryHovered;

    public HUDTilesetEditor(Skin skin)
    {
        super(skin);
    }

    @Override
    public void create() {

        editTilePane = new Table();
        editTilePane.setFillParent(true);
        editTilePane.left().top();
        {
        }

        tableBottomLeft = new Table();
        tableBottomLeft.setFillParent(true);
        tableBottomLeft.bottom().left();
        tableBottomLeft.pad(5);
        {
            buttonGrid = new TextButton("Grid", skin);
            buttonExportTileset = new TextButton("Export", skin);
            buttonImportTileset = new TextButton("Import", skin);

            tableBottomLeft.add(buttonGrid).width(50);
            tableBottomLeft.add(buttonExportTileset).width(50);
            tableBottomLeft.add(buttonImportTileset).width(50);

        }


        Table tileDefPane = new Table();
        tileDefPane.setFillParent(true);
        tileDefPane.right().top();
        tileDefPane.pad(5);
        {
            Table tableNameRow = new Table();
            Table tableHeaderRow = new Table();
            Table buttonsRow = new Table();
            tableDefinitions = new Table();

            fieldTilesetName = new TextField("", skin);
            tileDefPaneLabel = new Label("Tile Definitions", skin);
            addTileDefButton = new TextButton("+", skin);
            deleteTileDefButton = new TextButton("-", skin);

            tableNameRow.add(fieldTilesetName).width(200);

            tableHeaderRow.add(tileDefPaneLabel).width(100);
            tableHeaderRow.add(addTileDefButton).width(50);
            tableHeaderRow.add(deleteTileDefButton).width(50).row();

            tileDefPane.add(tableNameRow).row();
            tileDefPane.add(tableHeaderRow).row();
            tileDefPane.add(tableDefinitions).row();
        }

        ui.addActor(tableBottomLeft);
        ui.addActor(editTilePane);
        ui.addActor(tileDefPane);
    }

    public void showTile(Tileset tileset, TileDefinition def)
    {
        editTilePane.clear();

        if(def != null)
        {
            paneTileImage = new TileImage(def, tileset);
            editTilePane.add(paneTileImage).width(100).height(100).center().pad(50).row();

            Table tableFrameTime = new Table();
            tableFrameTime.defaults().pad(5);
            labelFrameTime = new Label("Frame Time (ms)", skin);
            fieldFrameTime = new TextField(Integer.toString((int)(def.getFrameTime()*1000)), skin);
            tableFrameTime.add(labelFrameTime).left();
            tableFrameTime.add(fieldFrameTime).left().width(50);

            editTilePane.add(tableFrameTime).pad(10).row();
        }
    }

    public void render()
    {
        ui.act();
        ui.draw();
    }
}
