package battlearena.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.utils.Array;
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
import battlearena.game.states.StateChooseMap;
import battlearena.game.states.StateCredits;
import battlearena.game.states.StateGameFinished;
import battlearena.game.states.StateMainMenu;
import battlearena.game.states.StateOptions;
import battlearena.game.states.StatePlay;
import battlearena.game.states.StateSetupTeams;

public class BattleArena extends ApplicationAdapter
{

	// Add power up items: speed boost, knockback boost, cooldown lower
	// Add pause menu that allows you to view stats about the current game mode: tdm(deaths, kills, team scores) towers(kills, deaths, tower stats)
	// Add achievements: changing character, starting a game, kiling first enemy, kill ten enemies
	// Add map selection screen
	// Add game setup screen (tdm and towers)
	// Implement tdm game mode
	// Implement towers game mode
	// Implement AI for warrior
	// Implement AI for archer
	// Implement AI for gunner
	// Implement multiplayer room creation
	// Implement multiplayer lobby joining

	public static final String TRANSITION_PLAY = "T_Play";
	public static final String TRANSITION_CHOOSE_MAP = "T_Choose_Map";
	public static final String TRANSITION_SETUP_TEAMS = "T_Setup_Teams";
	public static final String TRANSITION_FINISH = "T_FINISH";
	public static final String TRANSITION_MAIN_MENU = "T_Main_Menu";
	public static final String TRANSITION_OPTIONS = "T_Options";
	public static final String TRANSITION_CREDITS = "T_Credits";

	public static final StateMainMenu STATE_MAIN_MENU = new StateMainMenu();
	public static final StatePlay STATE_PLAY = new StatePlay();
	public static final StateChooseMap STATE_CHOOSE_MAP = new StateChooseMap();
	public static final StateSetupTeams STATE_SETUP_TEAMS = new StateSetupTeams();
	public static final StateGameFinished STATE_GAME_FINISHED = new StateGameFinished();
	public static final StateOptions STATE_OPTIONS = new StateOptions();
	public static final StateCredits STATE_CREDITS = new StateCredits();

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

	public void playSound(String name)
	{
		Sound s = getAudio(name);;

		s.setVolume(s.play(), StateOptions.getSfx());
	}

	private Sound getAudio(String name)
	{
		return assetManager.get(name, Sound.class);
	}
	public Music getMusic(String name)
	{
		return assetManager.get(name, Music.class);
	}

	public Array<Sound> getAllSounds()
	{
		Array<Sound> res = new Array<Sound>();

		assetManager.getAll(Sound.class, res);

		return res;
	}


	public Array<Music> getAllMusic()
	{
		Array<Music> res = new Array<Music>();

		assetManager.getAll(Music.class, res);

		return res;
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
		assetManager.load(Assets.TEXTURE_PROJECTILES, Texture.class);
		assetManager.load(Assets.TEXTURE_MAIN_MENU, Texture.class);
		assetManager.load(Assets.TEXTURE_BUTTONS, Texture.class);
		assetManager.load(Assets.TEXTURE_CREDITS, Texture.class);
		assetManager.load(Assets.SKIN, Skin.class);
		assetManager.load("audio/background_music.ogg", Music.class);
		assetManager.load("audio/arrow.ogg", Sound.class);
		assetManager.load("audio/click.ogg", Sound.class);
		assetManager.load("audio/consume_item.ogg", Sound.class);
		assetManager.load("audio/hit_wall.ogg", Sound.class);
		assetManager.load("audio/maze_attack.ogg", Sound.class);
		assetManager.load("audio/retro_beep.ogg", Sound.class);
		assetManager.load("audio/sword.ogg", Sound.class);
		assetManager.load(Assets.AUDIO_YOU_WIN, Sound.class);
		assetManager.load(Assets.AUDIO_YOU_LOSE, Sound.class);
		assetManager.load("audio/laser_shoot.wav", Sound.class);
		assetManager.finishLoading();

		// Setup character animations
		BACharacter.WARRIOR.setWalkAnim(AnimationUtil.MakeAnim(getTexture(Assets.TEXTURE_CHARACTERS), 0, 0, new int[]{40, 40}, new int[]{57, 57}, 2, 0.2f));
		BACharacter.WARRIOR.setAttackAnim(AnimationUtil.MakeAnim(getTexture(Assets.TEXTURE_CHARACTERS), 80, 0, new int[]{74}, new int[]{67}, 1, 0.0f));

		BACharacter.ARCHER.setWalkAnim(AnimationUtil.MakeAnim(getTexture(Assets.TEXTURE_CHARACTERS), 0, 67, new int[]{37, 37}, new int[]{59, 59}, 2, 0.2f));
		BACharacter.ARCHER.setAttackAnim(AnimationUtil.MakeAnim(getTexture(Assets.TEXTURE_CHARACTERS), 73, 67, new int[]{86}, new int[]{67}, 1, 0.0f));

		BACharacter.GUNNER.setWalkAnim(AnimationUtil.MakeAnim(getTexture(Assets.TEXTURE_CHARACTERS), 0, 133, new int[]{35, 45}, new int[]{49, 51}, 2, 0.2f));
		BACharacter.GUNNER.setAttackAnim(AnimationUtil.MakeAnim(getTexture(Assets.TEXTURE_CHARACTERS), 80, 140, new int[]{63}, new int[]{45}, 1, 0.0f));

		// Register states
		fsa.registerState(STATE_MAIN_MENU, true);
		fsa.registerState(STATE_PLAY);
		fsa.registerState(STATE_CHOOSE_MAP);
		fsa.registerState(STATE_SETUP_TEAMS);
		fsa.registerState(STATE_GAME_FINISHED);
		fsa.registerState(STATE_OPTIONS);
		fsa.registerState(STATE_CREDITS);

		// Register transitions
		fsa.registerTransition(STATE_MAIN_MENU, STATE_PLAY, TRANSITION_PLAY);
		fsa.registerTransition(STATE_MAIN_MENU, STATE_CHOOSE_MAP, TRANSITION_CHOOSE_MAP);
		fsa.registerTransition(STATE_CHOOSE_MAP, STATE_SETUP_TEAMS, TRANSITION_SETUP_TEAMS);
		fsa.registerTransition(STATE_SETUP_TEAMS, STATE_PLAY, TRANSITION_PLAY);
		fsa.registerTransition(STATE_PLAY, STATE_GAME_FINISHED, TRANSITION_FINISH);
		fsa.registerTransition(STATE_GAME_FINISHED, STATE_MAIN_MENU, TRANSITION_MAIN_MENU);
		fsa.registerTransition(STATE_MAIN_MENU, STATE_OPTIONS, TRANSITION_OPTIONS);
		fsa.registerTransition(STATE_OPTIONS, STATE_MAIN_MENU, TRANSITION_MAIN_MENU);
		fsa.registerTransition(STATE_MAIN_MENU, STATE_CREDITS, TRANSITION_CREDITS);
		fsa.registerTransition(STATE_CREDITS, STATE_MAIN_MENU, TRANSITION_MAIN_MENU);

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
