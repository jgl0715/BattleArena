package battlearena.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import battlearena.common.AnimationUtil;
import battlearena.common.entity.Entity;
import battlearena.common.entity.data.DVector2;
import battlearena.common.file.TiledWorldImporter;
import battlearena.common.states.StateMachine;
import battlearena.common.world.EntityLayer;
import battlearena.common.world.TiledWorld;
import battlearena.game.entity.BACharacter;
import battlearena.game.entity.EPlayer;
import battlearena.game.input.Button;
import battlearena.game.input.Joystick;
import battlearena.game.states.StateMainMenu;
import battlearena.game.states.StatePlay;

public class BattleArena extends ApplicationAdapter
{

	public static final String TRANSITION_PLAY = "T_Play";

	public static final StateMainMenu STATE_MAIN_MENU = new StateMainMenu();
	public static final StatePlay STATE_PLAY = new StatePlay();

	public static BattleArena I = null;

	public static final int VIRTUAL_WIDTH = 16*40;
	public static final int VIRTUAL_HEIGHT = 9*40;

	private StateMachine fsa;
	private ShapeRenderer sr;
	private SpriteBatch batch;

	private OrthographicCamera camera;
	private Viewport viewport;

	private AssetManager assetManager;
	private Skin uiSkin;

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

	public SpriteBatch getBatch()
	{
		return batch;
	}

	public OrthographicCamera getCamera()
	{
		return camera;
	}

	public AssetManager getAssetManager()
	{
		return assetManager;
	}

	public Texture getTexture(String name)
	{
		return assetManager.get(name, Texture.class);
	}

	public Skin getSkin()
	{
		return assetManager.get(Assets.SKIN, Skin.class);
	}

	public void inputToFSA(String input)
	{
		fsa.transition(input, null);
	}

	public void inputToFSA(String input, Object transitionInput)
	{
		fsa.transition(input, transitionInput);
	}

	@Override
	public void create ()
	{
		batch = new SpriteBatch();
		sr = new ShapeRenderer();

		camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		camera.zoom = 0.1f;
		camera.update();

		fsa = new StateMachine();

		assetManager = new AssetManager();
		assetManager.load(Assets.TEXTURE_CHARACTERS, Texture.class);
		assetManager.load(Assets.SKIN, Skin.class);
		assetManager.finishLoading();

		// Setup character animations
		BACharacter.WARRIOR.setWalkAnim(AnimationUtil.MakeAnim(getTexture(Assets.TEXTURE_CHARACTERS), 0, 0, new int[]{40, 40}, new int[]{57, 57}, 2, 0.2f));
		BACharacter.WARRIOR.setAttackAnim(AnimationUtil.MakeAnim(getTexture(Assets.TEXTURE_CHARACTERS), 80, 0, new int[]{74}, new int[]{67}, 1, 0.0f));

		// Register states
		fsa.registerState(STATE_MAIN_MENU, true);
		fsa.registerState(STATE_PLAY);

		// Register transitions
		fsa.registerTransition(STATE_MAIN_MENU, STATE_PLAY, TRANSITION_PLAY);
	}

	@Override
	public void dispose ()
	{

	}

	public void update(float delta)
	{
		fsa.updateCurrent(delta);
	}

	@Override
	public void render ()
	{

		// Update logic
		update(Gdx.graphics.getDeltaTime());

		// Render logic
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);

		fsa.renderCurrent();
//
//		batch.begin();
//		batch.draw(new TextureRegion(getTexture(Assets.TEXTURE_CHARACTERS), 0, 0, 40, 57), 0, 0);
//		batch.end();
	}
}
