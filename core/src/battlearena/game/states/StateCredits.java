package battlearena.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import battlearena.common.states.State;
import battlearena.game.BattleArena;
import battlearena.game.ui.HUDCredits;

/**
 * Created by joeyl on 5/2/2019.
 */

public class StateCredits extends State
{

    private HUDCredits hud;

    public StateCredits()
    {
        super("StateCredits");
    }

    @Override
    public void create()
    {
        hud = new HUDCredits(BattleArena.I.getSkin());

        hud.backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            BattleArena.I.inputToFSA(BattleArena.TRANSITION_MAIN_MENU);
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
        Gdx.input.setInputProcessor(hud.getUI());
    }

    @Override
    public void hide() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        hud.render();
    }
}
