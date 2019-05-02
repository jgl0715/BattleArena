package battlearena.common.world;

import battlearena.common.entity.Entity;

public interface EntityListener
{
    public void onEntityAdd(Entity e);

    public void onEntityRemove(Entity e);
}
