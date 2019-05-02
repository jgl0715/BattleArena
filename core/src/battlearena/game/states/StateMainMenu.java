package battlearena.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import battlearena.common.states.State;
import battlearena.game.Assets;
import battlearena.game.BattleArena;
import battlearena.game.modes.GMTeamDeathmatch;
import battlearena.game.ui.HUDMainMenu;

public class StateMainMenu extends State
{

    private HUDMainMenu hudMainMenu;

    public StateMainMenu()
    {
        super("MainMenu");
    }

    @Override
    public void create()
    {

        hudMainMenu = new HUDMainMenu(BattleArena.I.getSkin());

        hudMainMenu.tdmButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                BattleArena.I.playSound(Assets.AUDIO_CLICK);
                // Start StatePlay
                BattleArena.I.inputToFSA(BattleArena.TRANSITION_CHOOSE_MAP, new GMTeamDeathmatch());
            }
        });

        hudMainMenu.creditsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                BattleArena.I.playSound(Assets.AUDIO_CLICK);
                BattleArena.I.inputToFSA(BattleArena.TRANSITION_CREDITS);

            }
        });

        hudMainMenu.optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                BattleArena.I.playSound(Assets.AUDIO_CLICK);

                // Start StatePlay
                BattleArena.I.inputToFSA(BattleArena.TRANSITION_OPTIONS);
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
    public void show(Object transitionInput)
    {
        Gdx.input.setInputProcessor(hudMainMenu.getUI());
    }

    @Override
    public void hide() {

    }

    @Override
    public void update(float delta)
    {
    }

    @Override
    public void render()
    {
        hudMainMenu.render();
    }
}
