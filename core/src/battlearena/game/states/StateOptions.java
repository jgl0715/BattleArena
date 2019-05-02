package battlearena.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import battlearena.common.states.State;
import battlearena.game.Assets;
import battlearena.game.BattleArena;
import battlearena.game.ui.HUDOptions;

/**
 * Created by joeyl on 5/2/2019.
 */

public class StateOptions extends State
{

    private HUDOptions hudOptions;
    public static float master;
    public static float sfx;
    public static float music;

    public static float getMusic()
    {
        return music * master;
    }

    public static float getSfx()
    {
        return music * master;
    }

    public StateOptions()
    {
        super("StateOptions");


        sfx= 1.0f;
        music= 1.0f;
        master= 1.0f;
    }

    @Override
    public void create()
    {
        hudOptions = new HUDOptions(BattleArena.I.getSkin());
        hudOptions.getRoot().setBackground(hudOptions.getSkin().getDrawable("round-gray"));
        hudOptions.sfxVolume.setValue(sfx);
        hudOptions.masterVolume.setValue(master);
        hudOptions.musicVolume.setValue(music);

        hudOptions.backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);                BattleArena.I.playSound(Assets.AUDIO_CLICK);

                BattleArena.I.inputToFSA(BattleArena.TRANSITION_MAIN_MENU);

            }
        });

        hudOptions.masterVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                master = hudOptions.masterVolume.getValue();

                Array<Music> music = BattleArena.I.getAllMusic();

                for(Music m : music)
                    m.setVolume(getMusic());
            }
        });


        hudOptions.musicVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                music = hudOptions.musicVolume.getValue();

                Array<Music> music = BattleArena.I.getAllMusic();

                for(Music m : music)
                    m.setVolume(getMusic());
            }
        });

        hudOptions.sfxVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sfx = hudOptions.sfxVolume.getValue();
            }
        });
    }

    @Override
    public void dispose() {

    }

    @Override
    public void resized(int width, int height) {

    }

    @Override
    public void show(Object transitionInput) {
        Gdx.input.setInputProcessor(hudOptions.getUI());
    }

    @Override
    public void hide() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        hudOptions.render();
    }
}
