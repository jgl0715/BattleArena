package battlearena.common.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Layer
{

    protected String name;
    protected boolean visible;

    public Layer(String n)
    {
        this.name = n;
        this.visible = true;
    }

    public String getName()
    {
        return name;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    /**
     * Updates the contents of this layer.
     *
     * @param delta The time between this frame and the last frame, in seconds.
     */
    public abstract void update(float delta);

    /**
     * Renders this layer.
     *
     * @param batch The sprite batch to render the layer with. The matrix transformations of the batch
     *              are assumed to have already been set.
     */
    public abstract void render(SpriteBatch batch, OrthographicCamera cam);
}
