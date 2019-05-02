package battlearena.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import battlearena.common.states.State;
import battlearena.game.BattleArena;
import battlearena.game.modes.GMTeamDeathmatch;
import battlearena.game.modes.GameMode;
import battlearena.game.ui.HUDGameFinished;

public class StateGameFinished extends State
{

    private HUDGameFinished hudGameFinished;
    private GMTeamDeathmatch tdm;

    public StateGameFinished()
    {
        super("StateFinished");
    }

    @Override
    public void create()
    {
        hudGameFinished = new HUDGameFinished(BattleArena.I.getSkin());

        hudGameFinished.menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
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
    public void show(Object transitionInput) {

        Gdx.input.setInputProcessor(hudGameFinished.getUI());

        tdm = (GMTeamDeathmatch) transitionInput;
        hudGameFinished.redScore.setText("Red: " + tdm.getRedScore());
        hudGameFinished.blueScore.setText("Blue: " + tdm.getBlueScore());

        if(tdm.getRedScore() > tdm.getBlueScore())
        {
            hudGameFinished.winLose.setText("YOU WIN!");
        }else if(tdm.getRedScore() < tdm.getBlueScore())
        {
            hudGameFinished.winLose.setText("YOU LOSE!");
        }
        else
        {
            hudGameFinished.winLose.setText("TIE");
        }


    }

    @Override
    public void hide() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render()
    {
        hudGameFinished.render();
    }
}
