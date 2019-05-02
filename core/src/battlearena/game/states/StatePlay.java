package battlearena.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Set;

import battlearena.common.entity.Entity;
import battlearena.common.entity.data.DVector2;
import battlearena.common.file.TiledWorldImporter;
import battlearena.common.states.State;
import battlearena.common.world.EntityLayer;
import battlearena.common.world.Location;
import battlearena.common.world.TiledWorld;
import battlearena.common.world.World;
import battlearena.game.Assets;
import battlearena.game.BAEntityFactory;
import battlearena.game.BattleArena;
import battlearena.game.entity.BACharacter;
import battlearena.game.entity.EEnemy;
import battlearena.game.entity.EPlayer;
import battlearena.game.input.Button;
import battlearena.game.input.Joystick;
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

        camera.zoom = 1.3f;
        // Center camera on player
        camera.position.set(pos.x, pos.y, 0);
        camera.update();

    }

    @Override
    public void render()
    {

        mode.render(BattleArena.I.getBatch(), BattleArena.I.getShapeRenderer(), BattleArena.I.getCamera());

    }
}
