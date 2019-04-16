package battlearena.common.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import battlearena.common.CollisionGroup;
import battlearena.common.entity.Entity;
import battlearena.editor.WorldEditor;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class World
{

    public static final int RAYS_NUM = 500;
    public static final float PIXELS_PER_METER = 10.0f;

    private String name;
    private List<Entity> entities;
    private List<Entity> forAdd;
    private com.badlogic.gdx.physics.box2d.World PhysicsWorld;
    private RayHandler handler;
    private Box2DDebugRenderer dbgr;

    public World(String n)
    {
        this.name = n;
        entities = new ArrayList<Entity>();
        forAdd = new ArrayList<Entity>();

        PhysicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0 / PIXELS_PER_METER), false);

        dbgr = new Box2DDebugRenderer();

        handler = new RayHandler(PhysicsWorld);
       // handler.setShadows(true);
        handler.setAmbientLight(0.6f);
    }

    public com.badlogic.gdx.physics.box2d.World getPhysicsWorld()
    {
        return PhysicsWorld;
    }

    public String getName()
    {
        return name;
    }

    public <T extends Entity> List<T> findObjectsByClass(Class<T> Class)
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

    public PointLight createPointLight(Color lightColor, float lightDistance, float x, float y, CollisionGroup group)
    {
        PointLight light = new PointLight(handler, RAYS_NUM, lightColor, lightDistance, x/ PIXELS_PER_METER, y / PIXELS_PER_METER);

        light.setSoft(true);
       // light.setStaticLight(true);
        light.setSoftnessLength(1.0f);

    //    light.setContactFilter(group.getChannel(), group.getGroup(), group.getAccepted());

        return light;
    }

    public Body createQuad(BodyDef.BodyType Type, float x, float y, Vector2[] verts, CollisionGroup Group)
    {
        BodyDef BDef = new BodyDef();
        BDef.fixedRotation = true;
        BDef.active = true;
        BDef.allowSleep = false;
        BDef.angle = 0.0f;
        BDef.angularDamping = 0.0f;
        BDef.angularVelocity = 0.0f;
        BDef.awake = true;
        BDef.bullet = false;
        BDef.linearDamping = 10.0f;
        BDef.type = Type;

        // The position of the body is the middle of the tile.
        BDef.position.set(x / PIXELS_PER_METER, y / PIXELS_PER_METER);

        Body Body = getPhysicsWorld().createBody(BDef);
        FixtureDef FDef = new FixtureDef();
        PolygonShape Box = new PolygonShape();
        Box.set(verts);

        FDef.density = 1.0f;
        FDef.friction = 0.0f;
        FDef.isSensor = false;
        FDef.restitution = 0.2f;
        FDef.shape = Box;

      //ffffff  FDef.filter.categoryBits = Group.getChannel();
      //  FDef.filter.groupIndex = Group.getGroup();
     //   FDef.filter.maskBits = Group.getAccepted();

        Body.createFixture(FDef);

        return Body;
    }

    public Body createBox(BodyDef.BodyType Type, float x, float y, float w, float h, CollisionGroup Group)
    {

        BodyDef BDef = new BodyDef();
        BDef.fixedRotation = true;
        BDef.active = true;
        BDef.allowSleep = false;
        BDef.angle = 0.0f;
        BDef.angularDamping = 0.0f;
        BDef.angularVelocity = 0.0f;
        BDef.awake = true;
        BDef.bullet = false;
        BDef.linearDamping = 10.0f;
        BDef.type = Type;

        // The position of the body is the middle of the tile.
        BDef.position.set(x / PIXELS_PER_METER, y / PIXELS_PER_METER);

        Body Body = getPhysicsWorld().createBody(BDef);
        FixtureDef FDef = new FixtureDef();
        PolygonShape Box = new PolygonShape();
        Box.setAsBox((w / 2.0f) / PIXELS_PER_METER, (h / 2.0f) / PIXELS_PER_METER);
        FDef.density = 1.0f;
        FDef.friction = 0.0f;
        FDef.isSensor = false;
        FDef.restitution = 0.2f;
        FDef.shape = Box;

        FDef.filter.categoryBits = Group.getChannel();
        FDef.filter.groupIndex = Group.getGroup();
        FDef.filter.maskBits = Group.getAccepted();

        Body.createFixture(FDef);

        return Body;
    }

    public void update(float delta)
    {

        int Index = 0;

        // Step the physics world.
        PhysicsWorld.step(delta, 6, 8);

        // Add pending entities.
        entities.addAll(forAdd);
        forAdd.clear();

        // Update entities while removing entities marked for removal.
        while (Index < entities.size())
        {
            Entity e = entities.get(Index);

            if (e.isDead())
            {
                entities.remove(Index);
            }
            else
            {
                e.Update(delta);
                Index++;
            }
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera cam)
    {
        for (Entity E : entities)
            E.Render(batch);

        Matrix4 mat = new Matrix4(cam.combined);
        mat.scale(World.PIXELS_PER_METER, World.PIXELS_PER_METER, 1);

        handler.setCombinedMatrix(mat, cam.position.x / PIXELS_PER_METER, cam.position.y / PIXELS_PER_METER, cam.viewportWidth * cam.zoom, cam.viewportHeight * cam.zoom);
        handler.updateAndRender();

        dbgr.render(getPhysicsWorld(), mat);
    }
}
