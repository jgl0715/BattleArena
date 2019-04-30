package battlearena.editor.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import javax.swing.JFileChooser;

import battlearena.common.gui.HUD;
import battlearena.common.tile.Tileset;
import battlearena.editor.WorldEditor;

public class HUDCreateTileset extends HUD
{

    public Label nameLabel;
    public TextField nameField;
    public Label widthLabel;
    public TextField widthField;
    public Label heightLabel;
    public TextField heightField;
    public Label tilesheetLabel;
    public TextField tilesheetField;
    public TextButton chooseFileButton;
    public TextButton confirmButton;
    public TextButton cancelButton;

    public HUDCreateTileset(Skin skin)
    {
        super(skin, WorldEditor.VIRTUAL_WIDTH, WorldEditor.VIRTUAL_HEIGHT);

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


        }
    }
}
