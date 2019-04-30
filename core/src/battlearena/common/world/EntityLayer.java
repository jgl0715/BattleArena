package battlearena.common.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import battlearena.common.RenderSettings;
import battlearena.common.entity.Entity;
import battlearena.common.entity.data.DBody;
import battlearena.common.entity.data.DBoolean;
import battlearena.common.entity.data.DFloat;
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

    public int getEntityCount()
    {
        return entities.size();
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
                e.onKill();
                entities.remove(index);

                DBody bod = e.find(DBody.class, Entity.BODY);
                if(bod != null)
                {
                    e.getWorld().getPhysicsWorld().destroyBody(bod.Value);
                }

            }
            else
            {
                e.Update(delta);
                index++;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera cam)
    {
        // Sort entities before rendering them.
        Collections.sort(entities);

        batch.begin();
        for (Entity e : entities)
            e.Render(batch, cam, RenderSettings.RenderMode.TEXTURED);
        batch.end();

        for (Entity e : entities)
            e.Render(batch, cam, RenderSettings.RenderMode.FRAME);
    }

}
