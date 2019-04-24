package battlearena.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import battlearena.common.entity.Entity;
import battlearena.common.entity.data.DVector2;
import battlearena.common.file.TiledWorldImporter;
import battlearena.common.states.StateMachine;
import battlearena.common.world.EntityLayer;
import battlearena.common.world.TiledWorld;
import battlearena.game.entity.EPlayer;
import battlearena.game.input.Button;
import battlearena.game.input.Joystick;

public class BattleArena extends ApplicationAdapter
{

	public static BattleArena I = null;

	public static final int VIRTUAL_WIDTH = 16*40;
	public static final int VIRTUAL_HEIGHT = 9*40;

	private StateMachine fsa;
	private ShapeRenderer sr;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	private OrthographicCamera camera;
	private Viewport viewport;

	private OrthographicCamera uiCamera;
	private Viewport uiViewport;

	private TiledWorld world;
	private InputMultiplexer muxer;
	private Joystick stick;
	private Button buttonA;
	private Button buttonB;

	private EPlayer player;
	private Vector2 pos;


	public BattleArena()
	{
		if(I == null)
		{
			I = this;
		}
		else
		{
			throw new IllegalStateException("Can only create one instance of BattleArena during runtime.");
		}
	}

	public ShapeRenderer getShapeRenderer()
	{
		return sr;
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
	public void create ()
	{
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		sr = new ShapeRenderer();

		camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		uiCamera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		uiViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
		uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		camera.zoom = 0.1f;
		camera.update();

		muxer = new InputMultiplexer(
		new GestureDetector(new GestureDetector.GestureAdapter(){
			@Override
			public boolean zoom(float initialDistance, float distance)
			{
				camera.zoom += (initialDistance-distance)/90000.0f;
				camera.update();

				if(camera.zoom < 0.1)
					camera.zoom = 0.1f;

				return super.zoom(initialDistance, distance);
			}

		}));

		stick = new Joystick(-VIRTUAL_WIDTH / 2 * 0.7f, -VIRTUAL_HEIGHT / 2 * 0.6f, muxer, uiCamera);
		buttonA = new Button(250, -50, Color.DARK_GRAY, Color.RED, muxer, uiCamera);
		buttonB = new Button(175, -125, Color.DARK_GRAY, Color.YELLOW, muxer, uiCamera);

		Gdx.input.setInputProcessor(muxer);


		world = new TiledWorldImporter("worlds/test.world", true, new BAEntityFactory()).imp();

		player = BAEntityFactory.CreatePlayer(world, 100, 100);
		pos = player.find(DVector2.class, Entity.POSITION).Value;
		EntityLayer mobs = new EntityLayer("Mobs");
		mobs.addEntity(player);
		world.addEntityLayer(mobs);
	}

	@Override
	public void dispose ()
	{

	}

	@Override
	public void render ()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Update logic
		world.update(Gdx.graphics.getDeltaTime());

		camera.translate(stick.getJoystickInput());
		// Center camera on player
		camera.position.set(pos.x, pos.y, 0);
		camera.update();

		// Render logic

		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);

		sr.setProjectionMatrix(camera.projection);
		sr.setTransformMatrix(camera.view);
		world.render(batch, camera);


		sr.setProjectionMatrix(uiCamera.projection);
		sr.setTransformMatrix(uiCamera.view);

		// Render input UI
		stick.render(sr);
		buttonA.render(sr);
		buttonB.render(sr);
	}
}
