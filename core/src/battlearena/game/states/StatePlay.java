package battlearena.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import battlearena.common.entity.Entity;
import battlearena.common.entity.data.DVector2;
import battlearena.common.states.State;
import battlearena.game.BattleArena;
import battlearena.game.modes.GameMode;

public class StatePlay extends State
{

    public static final String MOBS_LAYER = "Mobs";

    public static StatePlay I;

    private InputMultiplexer muxer;
    private Vector2 pos;

    private OrthographicCamera uiCamera;
    private Viewport uiViewport;


    private GameMode mode;

    public StatePlay()
    {
        super("Play");

        I = this;
    }

    @Override
    public void create()
    {
        uiCamera = new OrthographicCamera(BattleArena.VIRTUAL_WIDTH, BattleArena.VIRTUAL_HEIGHT);
        uiViewport = new FitViewport(BattleArena.VIRTUAL_WIDTH, BattleArena.VIRTUAL_HEIGHT, uiCamera);
        uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        muxer = new InputMultiplexer();

    }


    @Override
    public void dispose() {

    }

    @Override
    public void resized(int width, int height) {

    }

    @Override
    public void show(Object transitionInput)
    {
        Gdx.input.setInputProcessor(muxer);

        mode = (GameMode) transitionInput;
        mode.startMatch(muxer);
    }

    @Override
    public void hide()
    {

    }

    @Override
    public void update(float delta)
    {
        OrthographicCamera camera = BattleArena.I.getCamera();

        // Update logic
        mode.getWorld().update(Gdx.graphics.getDeltaTime());

        Vector2 pos = mode.getPlayer().find(DVector2.class, Entity.POSITION).Value;
        Vector2 d = new Vector2(pos).sub(camera.position.x, camera.position.y);
        Vector2 movement = d.scl(0.09f);

        camera.zoom = 1.3f;
        // Center camera on player
        camera.translate(movement);
//        camera.position.set(pos.x, pos.y, 0);
        camera.update();

    }

    @Override
    public void render()
    {

        mode.render(BattleArena.I.getBatch(), BattleArena.I.getShapeRenderer(), BattleArena.I.getCamera());

    }
}
