package battlearena.game.states;

import com.badlogic.gdx.Gdx;

import battlearena.common.states.State;
import battlearena.game.BattleArena;
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
