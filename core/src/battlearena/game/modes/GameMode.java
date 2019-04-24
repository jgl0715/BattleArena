package battlearena.game.modes;

import battlearena.common.gui.HUD;
import battlearena.common.world.Location;
import battlearena.game.entity.EPlayer;

public abstract class GameMode
{

    protected HUD hud;
    protected EPlayer player;

    public GameMode()
    {

    }

    public abstract void startMatch();
    public abstract void endMatch();

}
