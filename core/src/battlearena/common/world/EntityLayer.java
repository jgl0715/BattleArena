package battlearena.common.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import battlearena.common.entity.Entity;
import battlearena.common.entity.behavior.BEditorHoverable;
import battlearena.common.entity.data.DBoolean;
import battlearena.common.entity.data.DVector2;
import battlearena.editor.WorldEditor;

public class EntityLayer extends Layer
{

    private List<Entity> entities;
    private List<Entity> forAdd;

    private Entity hoveredEntity;
    private Entity selectedEntity;

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

            DBoolean hovered = e.find(DBoolean.class, Entity.DATA_HOVERED);
            DBoolean pressed = e.find(DBoolean.class, Entity.DATA_PRESSED);
            if(hovered != null && pressed != null)
            {
                if(hovered.Value)
                {
                    hoveredEntity = e;
                }
                else if(pressed.Value)
                {
                    selectedEntity = e;
                }
            }

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

        ShapeRenderer sr = WorldEditor.I.getShapeRenderer();

        sr.begin(ShapeRenderer.ShapeType.Line);
        for(Entity e : entities)
        {
            DBoolean hovered = e.find(DBoolean.class, Entity.DATA_HOVERED);
            DBoolean pressed = e.find(DBoolean.class, Entity.DATA_PRESSED);

            if(hovered.Value)
            {
                sr.setColor(Color.GREEN);
            }
            else if(pressed.Value)
            {
                sr.setColor(Color.WHITE);
            }

            if(hovered != null)
            {

                Vector2 editorSize = e.find(DVector2.class, Entity.EDITOR_SIZE).Value;
                Vector2 pos = e.find(DVector2.class, Entity.POSITION).Value;

                sr.rect(pos.x - editorSize.x / 2, pos.y - editorSize.y / 2, editorSize.x, editorSize.y);
            }
        }
        sr.end();

    }
}
