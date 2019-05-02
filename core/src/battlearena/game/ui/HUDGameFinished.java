package battlearena.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import battlearena.common.gui.HUD;
import battlearena.game.Assets;
import battlearena.game.BattleArena;
import battlearena.game.modes.GameMode;

public class HUDGameFinished extends HUD
{

    public Label winLose;
    public Label redScore;
    public Label blueScore;
    public TextButton menuButton;
    public TextButton playAgainButton;

    public HUDGameFinished(Skin skin)
    {
        super(skin, BattleArena.VIRTUAL_WIDTH, BattleArena.VIRTUAL_HEIGHT);

        winLose = new Label("", skin);
        redScore = new Label("Red: ", skin);
        blueScore = new Label("Blue: ", skin);
        menuButton = new TextButton("Return to Menu", skin);
        playAgainButton = new TextButton("Play Again", skin);

        root.center();
        root.setBackground(skin.getDrawable("round-gray"));
        root.defaults().pad(10).width(200);
        root.add(winLose).row();
        root.add(redScore).row();
        root.add(blueScore).row();
        root.add(menuButton).row();
        root.add(playAgainButton);
    }
}
