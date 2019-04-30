package battlearena.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import battlearena.common.states.State;
import battlearena.game.BattleArena;
import battlearena.game.ui.HUDSetupTeams;

public class StateSetupTeams extends State
{

    private HUDSetupTeams hudSetupTeams;

    private int gameSlots;

    public StateSetupTeams()
    {
        super("SetupTeams");
    }

    @Override
    public void create()
    {
        hudSetupTeams = new HUDSetupTeams(BattleArena.I.getSkin());

        hudSetupTeams.addSlotButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);

                gameSlots++;

                if(gameSlots > 4)
                    gameSlots = 4;

                hudSetupTeams.setAvailableSlots(gameSlots);
            }
        });

        hudSetupTeams.removeSlotButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);

                gameSlots--;

                if(gameSlots < 1)
                    gameSlots = 1;

                hudSetupTeams.setAvailableSlots(gameSlots);
            }
        });
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void resized(int width, int height)
    {

    }

    @Override
    public void show(Object transitionInput)
    {
        Gdx.input.setInputProcessor(hudSetupTeams.getUI());

        gameSlots = 4;
    }

    @Override
    public void hide()
    {

    }

    @Override
    public void update(float delta)
    {

    }

    @Override
    public void render()
    {
        hudSetupTeams.render();
    }

}
