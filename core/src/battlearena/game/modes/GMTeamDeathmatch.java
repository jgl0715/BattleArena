package battlearena.game.modes;

import com.badlogic.gdx.InputMultiplexer;

import battlearena.game.BattleArena;
import battlearena.game.ui.HUDGame;

public class GMTeamDeathmatch extends GameMode
{

    public GMTeamDeathmatch()
    {
        hud = new HUDGame(BattleArena.I.getSkin());
    }

    @Override
    public void startMatch(InputMultiplexer muxer)
    {
        super.startMatch(muxer);
    }

    @Override
    public void endMatch()
    {
    }
}
