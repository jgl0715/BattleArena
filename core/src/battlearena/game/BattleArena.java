package battlearena.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import battlearena.common.file.TiledWorldImporter;
import battlearena.common.states.StateMachine;
import battlearena.common.world.TiledWorld;

public class BattleArena extends ApplicationAdapter
{

	public static final int VIRTUAL_WIDTH = 432;
	public static final int VIRTUAL_HEIGHT = 888;

	private StateMachine fsa;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private Viewport viewport;

	private TiledWorld world;

	@Override
	public void create ()
	{
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		camera.zoom = 0.5f;
		camera.update();

		world = new TiledWorldImporter("C:\\Development\\BattleArena\\android\\assets\\worlds\\test.world", new BAEntityFactory()).imp();
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

		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);

		world.render(batch, camera);
	}
}
