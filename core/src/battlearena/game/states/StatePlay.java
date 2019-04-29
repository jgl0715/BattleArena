package battlearena.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
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
import battlearena.game.BAEntityFactory;
import battlearena.game.BattleArena;
import battlearena.game.entity.BACharacter;
import battlearena.game.entity.EEnemy;
import battlearena.game.entity.EPlayer;
import battlearena.game.input.Button;
import battlearena.game.input.Joystick;

public class StatePlay extends State
{

    public static StatePlay I;

    private InputMultiplexer muxer;
    private TiledWorld world;
    private EPlayer player;
    private EEnemy enemy;
    private Vector2 pos;
    private Joystick stick;
    private Button buttonA;
    private Button buttonB;

    private OrthographicCamera uiCamera;
    private Viewport uiViewport;

    public StatePlay()
    {
        super("Play");

        I = this;
    }

    public Joystick getStick()
    {
        return stick;
    }

    public Button getButtonA()
    {
        return buttonA;
    }

    public Button getButtonB()
    {
        return buttonB;
    }

    @Override
    public void create()
    {
        uiCamera = new OrthographicCamera(BattleArena.VIRTUAL_WIDTH, BattleArena.VIRTUAL_HEIGHT);
        uiViewport = new FitViewport(BattleArena.VIRTUAL_WIDTH, BattleArena.VIRTUAL_HEIGHT, uiCamera);
        uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        muxer = new InputMultiplexer(
                new GestureDetector(new GestureDetector.GestureAdapter(){
                    @Override
                    public boolean zoom(float initialDistance, float distance)
                    {
                        uiCamera.zoom += (initialDistance-distance)/90000.0f;
                        uiCamera.update();

                        if(uiCamera.zoom < 0.1)
                            uiCamera.zoom = 0.1f;

                        return super.zoom(initialDistance, distance);
                    }
                }));

        stick = new Joystick(-BattleArena.VIRTUAL_WIDTH / 2 * 0.7f, -BattleArena.VIRTUAL_HEIGHT / 2 * 0.6f, muxer, uiCamera);
        buttonA = new Button(250, -50, Color.DARK_GRAY, Color.RED, muxer, uiCamera);
        buttonB = new Button(175, -125, Color.DARK_GRAY, Color.YELLOW, muxer, uiCamera);
    }

    public Vector2 getSpawnWithMeta(int meta)
    {
        Location spawn = world.findLocationsMatchingMeta(meta).iterator().next();

        return new Vector2(spawn.getTileX() * world.getTileWidth(), world.getPixelHeight()-spawn.getTileY()*world.getTileHeight()-1);
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

        world = new TiledWorldImporter("worlds/test.world", true, new BAEntityFactory()).imp();

        Vector2 playerSpawn = getSpawnWithMeta(1);
        Vector2 enemySpawn = getSpawnWithMeta(2);

        player = BAEntityFactory.CreatePlayer(world, playerSpawn.x, playerSpawn.y, BACharacter.WARRIOR);
        enemy = BAEntityFactory.CreateEnemy(world, enemySpawn.x, enemySpawn.y);

        // Add entities here.
        EntityLayer mobs = new EntityLayer("Mobs");
        mobs.addEntity(player);
        mobs.addEntity(enemy);

        world.addEntityLayer(mobs);

        pos = player.find(DVector2.class, Entity.POSITION).Value;
    }

    @Override
    public void hide() {

    }

    @Override
    public void update(float delta)
    {
        OrthographicCamera camera = BattleArena.I.getCamera();

        // Update logic
        world.update(Gdx.graphics.getDeltaTime());

        camera.zoom = 0.8f;
        // Center camera on player
        camera.position.set(pos.x, pos.y, 0);
        camera.update();

    }

    @Override
    public void render()
    {

        ShapeRenderer sr = BattleArena.I.getShapeRenderer();
        SpriteBatch batch = BattleArena.I.getBatch();
        OrthographicCamera camera = BattleArena.I.getCamera();

        sr.setProjectionMatrix(camera.projection);
        sr.setTransformMatrix(camera.view);

        world.render(batch, camera);

        sr.setProjectionMatrix(uiCamera.projection);
        sr.setTransformMatrix(uiCamera.view);

        // Render input UI
        stick.render(sr);
        buttonA.render(sr);
        buttonB.render(sr);

        // Render HUD


    }
}
