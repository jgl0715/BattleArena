package battlearena.common.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import battlearena.editor.WorldEditor;

public abstract class HUD
{

    protected Stage ui;
    protected Table root;
    protected Skin skin;
    private Viewport viewport;
    private OrthographicCamera camera;

    public HUD(Skin skin, int virtualWidth, int virtualHeight)
    {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new StretchViewport(virtualWidth, virtualHeight, camera);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        root = new Table();
        root.setFillParent(true);

        this.skin = skin;

        ui = new Stage(viewport);

        ui.addActor(root);
    }

    public Table getRoot()
    {
        return root;
    }

    public Skin getSkin()
    {
        return skin;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Stage getUI()
    {
        return ui;
    }

    public void resize(int width, int height)
    {
        viewport.update(width, height);
    }

    public void setAsInput()
    {
        Gdx.input.setInputProcessor(ui);
    }

    public void render() {
        ui.act(Gdx.graphics.getDeltaTime());
        ui.draw();
    }

}
