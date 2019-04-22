package battlearena.common.entity;

import battlearena.common.world.World;

public abstract class EntityFactory
{

    public abstract Entity makeEntity(World wor, int type);
}
