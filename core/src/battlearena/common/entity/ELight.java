package battlearena.common.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DPointLight;
import battlearena.common.entity.data.DVector2;
import battlearena.common.world.World;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class ELight extends Entity
{

    public static final String DATA_LIGHT = "Light";
    public static final String DATA_RED = "Color.Red";
    public static final String DATA_GREEN= "Color.Green";
    public static final String DATA_BLUE= "Color.Blue";
    public static final String DATA_DISTANCE= "Distance";
    public static final String DATA_SHADOW_SOFTNESS= "ShadowSoftness";

    private Vector2 pos;
    private Vector2 size;

    // Todo: make physics system independent.
    private PointLight box2dLight;

    public ELight(EntityConfig config)
    {
        super(config);

        World wor = config.getWorld();
        RayHandler handler = wor.getRayHandler();

        pos = addData(DVector2.class, POSITION, true).Value;
        size = addData(DVector2.class, SIZE, false).Value;
        box2dLight = (addData(DPointLight.class, DATA_LIGHT, false).Value);

        addData(DFloat.class, DATA_RED);
        addData(DFloat.class, DATA_GREEN);
        addData(DFloat.class, DATA_BLUE);
        addData(DFloat.class, DATA_DISTANCE);
        addData(DFloat.class, DATA_SHADOW_SOFTNESS);

        float red = config.GetConfigFloat("Color.Red");
        float green = config.GetConfigFloat("Color.Green");
        float blue = config.GetConfigFloat("Color.Blue");
        float distance = config.GetConfigFloat("Distance");
        float shadowSoftness = config.GetConfigFloat("ShadowSoftness");
        float x = config.GetConfigFloat("Pos.X");
        float y = config.GetConfigFloat("Pos.Y");

        pos.set(x, y);

        find(DFloat.class, DATA_RED).Value = red;
        find(DFloat.class, DATA_GREEN).Value = green;
        find(DFloat.class, DATA_BLUE).Value = blue;
        find(DFloat.class, DATA_DISTANCE).Value = distance;
        find(DFloat.class, DATA_SHADOW_SOFTNESS).Value = shadowSoftness;

        find(DPointLight.class, DATA_LIGHT).Value = new PointLight(handler, World.RAYS_NUM, new Color(red, green, blue, 1), distance, x, y);
    }

    public Vector2 getPos()
    {
        return pos;
    }

    public Vector2 getSize()
    {
        return size;
    }

    public PointLight getBox2dLight()
    {
        return box2dLight;
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        float red = find(DFloat.class, DATA_RED).Value;
        float green = find(DFloat.class, DATA_GREEN).Value;
        float blue = find(DFloat.class, DATA_BLUE).Value;
        float distance = find(DFloat.class, DATA_DISTANCE).Value;
        float shadowSoftness = find(DFloat.class, DATA_SHADOW_SOFTNESS).Value;
        float x = pos.x;
        float y = pos.y;

        // Set light attributes based on data components.
        box2dLight.setSoftnessLength(shadowSoftness);
        box2dLight.setDistance(distance);
        box2dLight.setColor(red, green, blue, 1);
        box2dLight.setPosition(pos.x, pos.y);
    }

    @Override
    public void Render(SpriteBatch batch)
    {
        super.Render(batch);
    }
}
