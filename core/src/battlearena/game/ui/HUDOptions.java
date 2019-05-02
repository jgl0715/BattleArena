package battlearena.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import battlearena.common.gui.HUD;
import battlearena.game.BattleArena;

public class HUDOptions extends HUD
{

    public Label sfxLabel;
    public Label musicLabel;
    public Label masterLabel;

    public Slider sfxVolume;
    public Slider musicVolume;
    public Slider masterVolume;

    public TextButton backButton;

    public HUDOptions(Skin skin)
    {
        super(skin, BattleArena.VIRTUAL_WIDTH, BattleArena.VIRTUAL_HEIGHT);

        sfxLabel = new Label("SFX", skin);
        musicLabel = new Label("Music", skin);
        masterLabel = new Label("Master", skin);

        root.center().left();
        root.pad(10);

        root.setBackground(skin.getDrawable("round-gray"));


        sfxVolume = new Slider(0.0f, 1.0f, 0.01f, false, skin);
        musicVolume = new Slider(0.0f, 1.0f, 0.01f, false, skin);
        masterVolume = new Slider(0.0f, 1.0f, 0.01f, false, skin);

        backButton = new TextButton("Back to Main Menu", skin);

        root.defaults().width(300);
        root.add(sfxLabel);
        root.add(sfxVolume).row();
        root.add(musicLabel);
        root.add(musicVolume).row();
        root.add(masterLabel);
        root.add(masterVolume).row();
        root.add(backButton).row();
    }
}
