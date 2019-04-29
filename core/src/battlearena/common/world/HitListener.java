package battlearena.common.world;

import battlearena.common.entity.Entity;
import com.badlogic.gdx.physics.box2d.Fixture;

public interface HitListener
{

    public void beginHit(Entity e1, Entity e2, Fixture f1, Fixture f2);
    public void endHit(Entity e1, Entity e2, Fixture f1, Fixture f2);

}
