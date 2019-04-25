package battlearena.game.entity.behavior;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import battlearena.common.entity.Entity;
import battlearena.common.entity.behavior.Behavior;
import battlearena.common.entity.data.DBody;
import battlearena.common.entity.data.DVector2;
import battlearena.common.world.Location;
import battlearena.common.world.TiledWorld;

public class BEnemyAI extends Behavior
{

    private Vector2 pos;
    private Vector2 size;
    private Body bod;

    public BEnemyAI(String Name, Entity Parent)
    {
        super(Name, Parent);

        AddData(DVector2.class, Entity.POSITION);
        AddData(DVector2.class, Entity.SIZE);
        AddData(DBody.class, Entity.BODY);

        pos = Get(Vector2.class, Entity.POSITION);
        size = Get(Vector2.class, Entity.SIZE);
        bod = Get(Body.class, Entity.BODY);
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        TiledWorld world = (TiledWorld) GetParent().getWorld();
        Location beneath = world.entityLocationToTileLocation(pos);
        beneath.setTileY(beneath.getTileY() + 1);

        if(world.isWall(beneath))
        {
            System.out.println("There is a wall beneath you!");
            bod.applyForceToCenter(new Vector2(0, -100), true);
        }

    }
}
