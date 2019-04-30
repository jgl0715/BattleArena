package battlearena.game.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class Button extends InputAdapter
{

    public static final float BUTTON_RAD = 30.0f;

    private float buttonX;
    private float buttonY;
    private boolean pressed;
    private TextureRegion pressedIm;
    private TextureRegion releasedIm;

    private OrthographicCamera cam;

    private List<ButtonListener> listeners;

    public Button(float x, float y, TextureRegion pressed, TextureRegion released, InputMultiplexer muxer, OrthographicCamera cam)
    {
        this.buttonX = x;
        this.buttonY = y;
        this.cam = cam;
        this.pressedIm = pressed;
        this.releasedIm = released;

        listeners = new ArrayList<ButtonListener>();

        muxer.addProcessor(this);
    }

    public void addListener(ButtonListener listener)
    {
        listeners.add(listener);
    }

    public boolean isPressed()
    {
        return pressed;
    }

    private boolean inButton(Vector3 worldSpace)
    {
        float dx = worldSpace.x - buttonX;
        float dy = worldSpace.y - buttonY;

        if(dx*dx+dy*dy<=BUTTON_RAD*BUTTON_RAD)
        {
            return true;
        }

        return false;
    }

    private void press()
    {
        pressed = true;

        for(ButtonListener listener : listeners)
            listener.buttonPressed();

    }

    public void render(SpriteBatch batch)
    {
        batch.begin();
        if(pressed)
            batch.draw(pressedIm, (int)(buttonX - BUTTON_RAD), (int)(buttonY - BUTTON_RAD), BUTTON_RAD*2, BUTTON_RAD*2);
        else
            batch.draw(releasedIm, (int)(buttonX - BUTTON_RAD), (int)(buttonY - BUTTON_RAD), BUTTON_RAD*2, BUTTON_RAD*2);
        batch.end();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        Vector3 worldSpace = cam.unproject(new Vector3(screenX, screenY, 0));

        if(inButton(worldSpace))
        {
            press();
            return true;
        }

        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        if(pressed)
        {
            for(ButtonListener listener : listeners)
                listener.buttonReleased();
        }

        pressed = false;

        return false;
    }
}
