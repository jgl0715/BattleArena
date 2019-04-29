package battlearena.game.entity;

import battlearena.common.RenderSettings;
import battlearena.common.entity.EBox;
import battlearena.common.entity.EntityConfig;
import battlearena.game.entity.behavior.BEnemyAI;

public class EEnemy extends EBox
{

    public EEnemy(EntityConfig Config)
    {
        super(Config);

        //addBehavior(BEnemyAI.class, "EnemyAI");

        renderSettings.mode = RenderSettings.RenderMode.FRAME;
    }

}
