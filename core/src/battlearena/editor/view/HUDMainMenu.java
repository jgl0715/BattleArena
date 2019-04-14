package battlearena.editor.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import battlearena.common.gui.HUD;
import battlearena.editor.WorldEditor;

public class HUDMainMenu extends HUD
{


    public TextButton createWorld;
    public TextButton loadWorld;
    public TextButton createTileset;
    public TextButton loadTileset;
    public TextButton quit;

    public HUDMainMenu(Skin skin)
    {
        super(skin);

        root.defaults().pad(5);
        root.center();
        {
            createWorld = new TextButton("Create World", skin);
            loadWorld = new TextButton("Load World", skin);
            createTileset = new TextButton("Create Tileset", skin);
            loadTileset = new TextButton("Load Tileset", skin);
            quit = new TextButton("Quit", skin);

            root.add(createWorld).width(300).row();
            root.add(loadWorld).width(300).row();
            root.add(createTileset).width(300).row();
            root.add(loadTileset).width(300).row();
            root.add(quit).width(300).row();
        }
    }
}
