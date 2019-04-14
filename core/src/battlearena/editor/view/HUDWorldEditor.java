package battlearena.editor.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import battlearena.common.gui.HUD;
import battlearena.common.tile.Tile;
import battlearena.common.tile.Tileset;
import battlearena.common.world.TileLayer;
import battlearena.editor.TileImage;

public class HUDWorldEditor extends HUD
{

    // Right pane
    public TextField fieldWorldName;
    public ScrollPane scrollPaneLayers;
    public Table tableLayers;
    public Label labelLayers;
    public TextButton addLayerButton;
    public TextButton deleteLayerButton;
    public Table layerHovered;

    // Left pane
    private ScrollPane scrollPaneTiles;
    public Table tableDefinitions;
    public Label tileDefPaneLabel;
    public Table tileHovered;

    public HUDWorldEditor(Skin skin)
    {
        super(skin);

        Table tableLeftPane = new Table();
        tableLeftPane.setFillParent(true);
        tableLeftPane.left().top();
        tableLeftPane.pad(5);
        {
            Table tableHeaderRow = new Table();
            tableDefinitions = new Table();
            scrollPaneTiles = new ScrollPane(tableDefinitions, skin);

            tileDefPaneLabel = new Label("Tile Definitions", skin);

            tableHeaderRow.add(tileDefPaneLabel).width(200).row();

            tableLeftPane.add(tableHeaderRow).row();
            tableLeftPane.add(scrollPaneTiles).row();

            scrollPaneTiles.setFadeScrollBars(false);
        }

        Table tableRightPane = new Table();
        tableRightPane.setFillParent(true);
        tableRightPane.right().top();
        tableRightPane.pad(5);
        {
            Table tableNameRow = new Table();
            Table tableHeaderRow = new Table();

            fieldWorldName = new TextField("", skin);

            tableLayers = new Table();
            scrollPaneLayers = new ScrollPane(tableLayers, skin);

            labelLayers = new Label("Layers", skin);

            addLayerButton = new TextButton("+", skin);
            deleteLayerButton = new TextButton("-", skin);

            tableNameRow.add(fieldWorldName).width(200);

            tableHeaderRow.add(labelLayers).width(150);
            tableHeaderRow.add(addLayerButton).width(25);
            tableHeaderRow.add(deleteLayerButton).width(25);

            tableRightPane.add(tableNameRow).row();
            tableRightPane.add(tableHeaderRow).row();
            tableRightPane.add(scrollPaneLayers).row();

        }

        ui.addActor(tableRightPane);
        ui.addActor(tableLeftPane);
    }

    public Table addTile(Tileset set, Tile tile)
    {
        final Table entryTable = new Table();
        TileImage image = new TileImage(tile, set);
        final Label nameLabel = new Label(tile.getName(), skin);

        nameLabel.setText(tile.getName());

        entryTable.add(image).width(20).height(20).expand().fill();
        entryTable.add(nameLabel).width(180);
        tableDefinitions.add(entryTable).row();

        return entryTable;
    }


    public Table addLayer(TileLayer layer)
    {
        Table tableLayer = new Table(skin);

        TextField fieldLayerName = new TextField(layer.getName(), skin);

        tableLayer.add(fieldLayerName).width(200);

        tableLayers.add(tableLayer).row();
        return tableLayer;
    }

    public void removeLayer(Table tableLayer)
    {
        tableLayer.remove();
    }


}
