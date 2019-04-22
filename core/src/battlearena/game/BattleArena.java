package battlearena.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import battlearena.common.file.TiledWorldImporter;
import battlearena.common.states.StateMachine;
import battlearena.common.world.TiledWorld;

public class BattleArena extends ApplicationAdapter
{

	public static BattleArena I = null;

	public static final int VIRTUAL_WIDTH = 16*40;
	public static final int VIRTUAL_HEIGHT = 9*40;

	private boolean onDevice;
	private StateMachine fsa;
	private ShapeRenderer sr;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	private OrthographicCamera camera;
	private Viewport viewport;

	private OrthographicCamera uiCamera;
	private Viewport uiViewport;


	private TiledWorld world;

	private float joystickX;
	private float joystickY;
	private float joystickRad;
	private float joystickKnobRad;
	private float joystickKnobX;
	private float joystickKnobY;
	private boolean dragging;

	private InputMultiplexer muxer;

	public BattleArena(boolean onDevice)
	{
		this.onDevice = onDevice;

		if(I == null)
		{
			I = this;
		}
		else
		{
			throw new IllegalStateException("Can only create one instance of BattleArena during runtime.");
		}

	}

	public boolean isOnDevice()
	{
		return onDevice;
	}

	@Override
	public void create ()
	{
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		sr = new ShapeRenderer();

		camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		uiCamera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		uiViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
		uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		joystickX = -VIRTUAL_WIDTH / 2 * 0.7f;
		joystickY = -VIRTUAL_HEIGHT / 2 * 0.7f;
		joystickKnobRad = 20.0f;
		joystickRad = 25.0f;

		camera.zoom = 0.3f;
		camera.update();

		muxer = new InputMultiplexer(new InputAdapter()
		{

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer)
			{
				if(dragging)
				{
					Vector3 worldSpace = uiCamera.unproject(new Vector3(screenX, screenY, 0));

					Vector2 dir = new Vector2(worldSpace.x, worldSpace.y).sub(new Vector2(joystickX, joystickY));

					if(dir.len() >= joystickRad)
					{
						dir.nor().scl(joystickRad);
					}

					joystickKnobX = dir.x;
					joystickKnobY = dir.y;

					return true;

				}

				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button)
			{
				Vector3 worldSpace = uiCamera.unproject(new Vector3(screenX, screenY, 0));
				float dx = worldSpace.x - joystickX;
				float dy = worldSpace.y - joystickY;

				System.out.println(worldSpace.x + " " + worldSpace.y);
				System.out.println((joystickX + joystickKnobX) + " " + (joystickY + joystickKnobY));
				System.out.println(dx + " " + dy);

				System.out.println();

				if(dx*dx+dy*dy<=joystickKnobRad*joystickKnobRad)
				{
					dragging = true;
					System.out.println("dragging");
					return true;
				}

				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button)
			{
				dragging = false;
				joystickKnobX = 0;
				joystickKnobY = 0;
				return false;
			}
		}
		, new GestureDetector(new GestureDetector.GestureAdapter(){
			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY)
			{
				camera.translate(-deltaX/15.0f, deltaY/15.0f);
				camera.update();

				return super.pan(x, y, deltaX, deltaY);
			}

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

		Gdx.input.setInputProcessor(muxer);

		world = new TiledWorldImporter("worlds/test.world", true, new BAEntityFactory()).imp();
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
		if (Gdx.input.isKeyPressed(Input.Keys.W))
			camera.translate(0, 1);
		if (Gdx.input.isKeyPressed(Input.Keys.S))
			camera.translate(0, -1);
		if (Gdx.input.isKeyPressed(Input.Keys.A))
			camera.translate(-1, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.D))
			camera.translate(1, 0);
		camera.update();

		// Render logic

		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);

		sr.setProjectionMatrix(uiCamera.projection);
		sr.setTransformMatrix(uiCamera.view);

		viewport.apply();
		world.render(batch, camera);

		// Render joystick.

		uiViewport.apply();
		{
			sr.begin(ShapeRenderer.ShapeType.Line);
			sr.setColor(Color.WHITE);
			{
				sr.circle(joystickX, joystickY, joystickRad, 100);
			}
			sr.end();

			sr.begin(ShapeRenderer.ShapeType.Filled);
			sr.setColor(Color.GRAY);
			{
				sr.rectLine(joystickX + joystickKnobX, joystickY + joystickKnobY, joystickX, joystickY, 5.0f);
			}
			sr.setColor(Color.RED);
			{
				sr.circle(joystickX + joystickKnobX, joystickY + joystickKnobY, joystickKnobRad, 100);
			}
			sr.end();
		}
	}
}
