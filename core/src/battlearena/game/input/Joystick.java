package battlearena.game.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Joystick extends InputAdapter
{

    public static final float JOYSTICK_RAD = 50.0f;
    public static final float JOYSTICK_KNOB_RAD = 30.0f;
    public static final float MIN_INPUT = 0.0f;
    public static final float MAX_INPUT = 1.0f;

    private float joystickX;
    private float joystickY;
    private float joystickKnobX;
    private float joystickKnobY;
    private boolean dragging;
    private int pointer;
    private OrthographicCamera cam;

    public Joystick(float joystickX, float joystickY, InputMultiplexer muxer, OrthographicCamera cam)
    {
        this.joystickX = joystickX;
        this.joystickY = joystickY;

        this.joystickKnobX = 0.0f;
        this.joystickKnobY = 0.0f;

        this.cam = cam;

        this.dragging = false;

        muxer.addProcessor(this);
    }

    public void joystickInput(Vector3 worldSpace)
    {
        Vector2 dir = new Vector2(worldSpace.x, worldSpace.y).sub(new Vector2(joystickX, joystickY));

        if(dir.len() >= JOYSTICK_RAD)
        {
            dir.nor().scl(JOYSTICK_RAD);
        }

        joystickKnobX = dir.x;
        joystickKnobY = dir.y;
    }

    private boolean inJoyStick(Vector3 worldSpace)
    {
        float dx = worldSpace.x - joystickX;
        float dy = worldSpace.y - joystickY;

        if(dx*dx+dy*dy<=JOYSTICK_RAD*JOYSTICK_RAD)
        {
            return true;
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        Vector3 worldSpace = cam.unproject(new Vector3(screenX, screenY, 0));

        if(inJoyStick(worldSpace) && !dragging)
        {
            this.pointer = pointer;
            dragging = true;
            joystickInput(worldSpace);
            return true;
        }


        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        if(pointer == this.pointer)
        {
            dragging = false;
            joystickKnobX = 0;
            joystickKnobY = 0;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        if(dragging && pointer == this.pointer)
        {
            Vector3 worldSpace = cam.unproject(new Vector3(screenX, screenY, 0));
            joystickInput(worldSpace);
            return true;
        }

        return false;
    }

    public Vector2 getJoystickInput()
    {
        Vector2 dir = new Vector2(joystickKnobX, joystickKnobY);
        Vector2 norm = new Vector2(dir).nor();
        float dist = dir.len();
        float val = (dist / JOYSTICK_RAD) * (MAX_INPUT - MIN_INPUT) + MIN_INPUT;
        return norm.scl(val);
    }

    public void render(ShapeRenderer sr)
    {
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        {
            sr.circle(joystickX, joystickY, JOYSTICK_RAD, 100);
        }
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.DARK_GRAY);
        {
            sr.circle(joystickX, joystickY, 15.0f, 100);
        }
        sr.setColor(Color.GRAY);
        {
            sr.rectLine(joystickX + joystickKnobX, joystickY + joystickKnobY, joystickX, joystickY, 10.0f);
        }

        sr.setColor(Color.RED);
        {
            sr.circle(joystickX + joystickKnobX, joystickY + joystickKnobY, JOYSTICK_KNOB_RAD, 100);
        }
        sr.end();

    }


}
