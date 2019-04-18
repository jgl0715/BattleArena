package battlearena.common.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import battlearena.common.entity.Entity;

public class EntityLayer extends Layer
{

    private List<Entity> entities;
    private List<Entity> forAdd;
    private Class<? extends Entity> entityType;

    public EntityLayer(String name)
    {
        super(name);

        // Accepts every type of entity
        this.entityType = null;

        entities = new ArrayList<Entity>();
        forAdd = new ArrayList<Entity>();
    }

    public EntityLayer(String name, Class<? extends Entity> et)
    {
        super(name);
        this.entityType = et;

        entities = new ArrayList<Entity>();
        forAdd = new ArrayList<Entity>();
    }

    public <T extends Entity> List<T> findEntitiesByClass(Class<T> Class)
    {
        List<T> objects = new ArrayList<T>();
        Iterator<Entity> itr = entities.iterator();

        while (itr.hasNext())
        {
            Entity next = itr.next();
            if (next.getClass() == Class)
                objects.add(Class.cast(next));
        }

        return objects;
    }

    public Class<? extends Entity> getEntityType()
    {
        return entityType;
    }

    @Override
    public void update(float delta)
    {
        int index = 0;

        // Add pending entities.
        entities.addAll(forAdd);
        forAdd.clear();

        // Update entities while removing entities marked for removal.
        while (index < entities.size())
        {
            Entity e = entities.get(index);

            if (e.isDead())
            {
                entities.remove(index);
            }
            else
            {
                e.Update(delta);
                index++;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch)
    {
        for (Entity e : entities)
            e.Render(batch);
    }
}
