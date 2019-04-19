package battlearena.common.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import battlearena.common.entity.Entity;
import battlearena.common.entity.data.DVector2;
import battlearena.editor.WorldEditor;

public class EntityLayer extends Layer
{

    private List<Entity> entities;
    private List<Entity> forAdd;

    public EntityLayer(String name)
    {
        super(name);

        entities = new ArrayList<Entity>();
        forAdd = new ArrayList<Entity>();
    }

    public Iterator<Entity> iterator()
    {
        return entities.iterator();
    }

    public void addEntity(Entity e)
    {
        forAdd.add(e);
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

        // Render boxes around entities.
        if (visible)
        {
            ShapeRenderer sr = WorldEditor.I.getShapeRenderer();

            sr.begin(ShapeRenderer.ShapeType.Line);
            for (Entity e : entities)
            {
                Vector2 pos = e.find(DVector2.class, Entity.POSITION).Value;
                Vector2 size = e.find(DVector2.class, Entity.SIZE).Value;

                sr.rect(pos.x - size.x / 2, pos.y - size.y / 2, size.x, size.y);

            }
            sr.end();
        }
    }
}
