package battlearena.common.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import battlearena.game.CollisionGroup;
import battlearena.common.RenderSettings;
import battlearena.common.entity.behavior.BEditorHoverable;
import battlearena.common.entity.data.DBoolean;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DPointLight;
import battlearena.common.entity.data.DVector2;
import battlearena.common.world.World;
import battlearena.editor.WorldEditor;
import box2dLight.PointLight;

public class ELight extends Entity
{

    public static final int EDITOR_WIDTH = 4;
    public static final int EDITOR_HEIGHT = 4;

    public static final String DATA_LIGHT = "Light";
    public static final String DATA_RED = "Color.Red";
    public static final String DATA_GREEN= "Color.Green";
    public static final String DATA_BLUE= "Color.Blue";
    public static final String DATA_DISTANCE= "Distance";
    public static final String DATA_SHADOW_SOFTNESS= "ShadowSoftness";

    private Vector2 pos;
    private DFloat dataRed;
    private DFloat dataGreen;
    private DFloat dataBlue;

    // Todo: make physics system independent.
    private PointLight box2dLight;

    private boolean hovered;

    public ELight(EntityConfig config)
    {
        super(config);

        World wor = config.getWorld();

        pos = addData(DVector2.class, POSITION, true).Value;

        addData(DFloat.class, Entity.EDITOR_RADIUS, false);
        dataRed = addData(DFloat.class, DATA_RED, true);
        dataGreen = addData(DFloat.class, DATA_GREEN, true);
        dataBlue = addData(DFloat.class, DATA_BLUE, true);
        addData(DFloat.class, DATA_DISTANCE, true);
        addData(DFloat.class, DATA_SHADOW_SOFTNESS, true);

        // Editor data
        addData(DBoolean.class, Entity.HOVERED, false);
        addData(DBoolean.class, Entity.PRESSED, false);

        float red = 1.0f;
        float green = 1.0f;
        float blue = 1.0f;
        float distance = 10.0f;
        float shadowSoftness = 1.0f;
        float x = 0;
        float y = 0;

        if(config.HasItem(DATA_RED))
            red = config.GetConfigFloat(DATA_RED);
        if(config.HasItem(DATA_GREEN))
            green = config.GetConfigFloat(DATA_GREEN);
        if(config.HasItem(DATA_BLUE))
            blue = config.GetConfigFloat(DATA_BLUE);
        if(config.HasItem(DATA_DISTANCE))
            distance = config.GetConfigFloat(DATA_DISTANCE);
        if(config.HasItem(DATA_SHADOW_SOFTNESS))
            shadowSoftness = config.GetConfigFloat(DATA_SHADOW_SOFTNESS);
        if(config.HasItem(DATA_X))
            x = config.GetConfigFloat(DATA_X);
        if(config.HasItem(DATA_Y))
            y = config.GetConfigFloat(DATA_Y);

        pos.set(x, y);

        find(DFloat.class, EDITOR_RADIUS).Value = 5.0f;
        find(DFloat.class, DATA_RED).Value = red;
        find(DFloat.class, DATA_GREEN).Value = green;
        find(DFloat.class, DATA_BLUE).Value = blue;
        find(DFloat.class, DATA_DISTANCE).Value = distance;
        find(DFloat.class, DATA_SHADOW_SOFTNESS).Value = shadowSoftness;

        addData(DPointLight.class, DATA_LIGHT).Value = (box2dLight = wor.createPointLight(new Color(red,green,blue,1),distance, x, y, CollisionGroup.LIGHTS));

        // Add behaviors

        if(WorldEditor.I.isRunning())
            addBehavior(BEditorHoverable.class, "EditorHover");

        this.hovered = false;
    }

    public boolean isHovered()
    {
        return hovered;
    }

    public Vector2 getPos()
    {
        return pos;
    }

    public void setColor(float r, float g, float b)
    {
        dataRed.Value = r;
        dataGreen.Value = g;
        dataBlue.Value = b;
    }

    public PointLight getBox2dLight()
    {
        return box2dLight;
    }

    @Override
    public void remove()
    {
        super.remove();

        getBox2dLight().remove(true);
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
        float x = pos.x / World.PIXELS_PER_METER;
        float y = pos.y / World.PIXELS_PER_METER;

        // Set light attributes based on data components.
        box2dLight.setSoftnessLength(shadowSoftness);
        box2dLight.setDistance(distance);
        box2dLight.setColor(red, green, blue, 1);
        box2dLight.setPosition(x, y);

    }

    @Override
    public void Render(SpriteBatch batch, OrthographicCamera cam, RenderSettings.RenderMode filter)
    {
        super.Render(batch, cam, filter);

        float screenRadius = (float)Math.sqrt(cam.viewportWidth/2*cam.zoom*cam.viewportWidth/2*cam.zoom+cam.viewportHeight/2*cam.zoom*+cam.viewportHeight/2*cam.zoom);
        float d = box2dLight.getDistance()*World.PIXELS_PER_METER;
        float dx = (cam.position.x - box2dLight.getPosition().x * World.PIXELS_PER_METER);
        float dy = (cam.position.y - box2dLight.getPosition().y * World.PIXELS_PER_METER);

        if(renderSettings.visible && (dx*dx-screenRadius*screenRadius)+(dy*dy-screenRadius*screenRadius) <= d*d)
        {
            box2dLight.setActive(true);
        }
        else
        {
            box2dLight.setActive(false);
        }
    }
}
