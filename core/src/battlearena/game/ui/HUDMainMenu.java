package battlearena.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import battlearena.common.gui.HUD;

public class HUDMainMenu extends HUD
{

    public TextButton singlePlayerButton;
    public TextButton optionsButton;
    public TextButton multiplayerButton;

    public HUDMainMenu(Skin skin)
    {
        super(skin);

        root.setFillParent(true);
        root.defaults().pad(50);
        {
            singlePlayerButton = new TextButton("Single Player", skin);
            multiplayerButton = new TextButton("MultiPlayer", skin);
            optionsButton = new TextButton("Options", skin);

            root.add(singlePlayerButton).row();
            root.add(multiplayerButton).row();
            root.add(optionsButton).row();
        }
    }


}
