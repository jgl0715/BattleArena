package battlearena.game.entity.behavior;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import battlearena.common.entity.Entity;
import battlearena.common.entity.behavior.Behavior;
import battlearena.common.entity.data.DBody;
import battlearena.common.entity.data.DVector2;
import battlearena.common.world.TiledWorld;
import battlearena.game.entity.EMob;
import battlearena.game.entity.EPlayerAI;
import battlearena.game.entity.behavior.ai.BotAI;
import battlearena.game.entity.behavior.ai.utils.Box2dLocation;
import battlearena.game.entity.behavior.ai.utils.Box2dSteeringUtils;

public class BPlayerAI extends Behavior
{


    private BotAI botAi;

    private Vector2 pos;
    private Vector2 size;
    private Body bod;
    private EPlayerAI parent;

    public BPlayerAI(String Name, Entity Parent)
    {
        super(Name, Parent);

        AddData(DVector2.class, Entity.POSITION);
        AddData(DVector2.class, Entity.SIZE);
        AddData(DBody.class, Entity.BODY);

        pos = Get(Vector2.class, Entity.POSITION);
        size = Get(Vector2.class, Entity.SIZE);
        bod = Get(Body.class, Entity.BODY);

        parent = (EPlayerAI) Parent;
        botAi = new BotAI(parent, 30.0f);
        botAi.setBehavior(BotAI.ARRIVE_BEHAVIOR);
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        TiledWorld world = (TiledWorld) GetParent().getWorld();

        EMob target = parent.getTarget();

        if(target != null)
        {
            Vector2 pos = target.getBody().getPosition();
        }
        else
        {

        }

        botAi.update(delta);



//        parent.getAttack().attack();

    }

}
