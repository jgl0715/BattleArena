package battlearena.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import battlearena.common.file.TiledWorldImporter;
import battlearena.common.states.StateMachine;
import battlearena.common.world.TiledWorld;

public class BattleArena extends ApplicationAdapter
{

	public static BattleArena I = null;

	public static final int VIRTUAL_WIDTH = 16*60;
	public static final int VIRTUAL_HEIGHT = 9*60;

	private boolean onDevice;
	private StateMachine fsa;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private Viewport viewport;

	private TiledWorld world;

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
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		camera.zoom = 0.1f;
		camera.update();

		Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureAdapter(){
			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY)
			{
				camera.translate(-deltaX/5.0f, deltaY/5.0f);
				camera.update();

				return super.pan(x, y, deltaX, deltaY);
			}

			@Override
			public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
				return super.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
			}
		}));


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

		world.update(Gdx.graphics.getDeltaTime());

		if (Gdx.input.isKeyPressed(Input.Keys.W))
		{
			camera.translate(0, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S))
		{
			camera.translate(0, -1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A))
		{
			camera.translate(-1, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D))
		{
			camera.translate(1, 0);
		}

		camera.update();

		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);

		world.render(batch, camera);
	}
}
