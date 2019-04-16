package battlearena.common.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

import battlearena.common.entity.Entity;
import battlearena.editor.WorldEditor;

public class World
{

    public static final float PIXELS_PER_METER = 10.0f;

    private String name;
    private List<Entity> entities;
    private List<Entity> forAdd;
    private com.badlogic.gdx.physics.box2d.World PhysicsWorld;

    public World(String n)
    {
        this.name = n;
    }

    public com.badlogic.gdx.physics.box2d.World getPhysicsWorld()
    {
        return PhysicsWorld;
    }

    public String getName()
    {
        return name;
    }

    public void update(float delta)
    {

    }

    public void render()
    {

    }
}
