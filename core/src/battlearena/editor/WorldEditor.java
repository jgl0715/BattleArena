package battlearena.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import battlearena.common.gui.HUD;
import battlearena.common.states.State;
import battlearena.editor.states.StateCreateTileset;
import battlearena.editor.states.StateCreateWorld;
import battlearena.editor.states.StateLoadTileset;
import battlearena.editor.states.StateLoadWorld;
import battlearena.common.states.StateMachine;
import battlearena.editor.states.StateMainMenu;
import battlearena.editor.states.StateQuit;
import battlearena.editor.states.StateTilesetEditor;
import battlearena.editor.states.StateWorldEditor;

public class WorldEditor extends ApplicationAdapter
{

	public static final WorldEditor I;

	public static final String TRANSITION_MAIN_MENU = "T_Main_Menu";
	public static final String TRANSITION_EDIT_WORLD = "T_Edit_World";
	public static final String TRANSITION_EDIT_TILESET = "T_Edit_Tileset";
	public static final String TRANSITION_CREATE_WORLD = "T_Create_World";
	public static final String TRANSITION_LOAD_WORLD = "T_Load_World";
	public static final String TRANSITION_CREATE_TILESET = "T_Create_Tileset";
	public static final String TRANSITION_LOAD_TILESET = "T_Load_Tileset";
	public static final String TRANSITION_QUIT = "T_Quit";

	public static final int VIRTUAL_WIDTH = 16 * 60;
	public static final int VIRTUAL_HEIGHT = 9 * 60;

	public static final State STATE_MAIN_MENU = new StateMainMenu();
	public static final State STATE_CREATE_WORLD = new StateCreateWorld();
	public static final State STATE_LOAD_WORLD = new StateLoadWorld();
	public static final State STATE_CREATE_TILESET = new StateCreateTileset();
	public static final State STATE_LOAD_TILESET = new StateLoadTileset();
	public static final State STATE_WORLD_EDITOR = new StateWorldEditor();
	public static final State STATE_TILESET_EDITOR = new StateTilesetEditor();
	public static final State STATE_QUIT = new StateQuit();

	private StateMachine fsa;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch uiBatch;
	private OrthographicCamera uiCamera;
	private Viewport uiViewport;
	private Skin uiSkin;
	private Stage uiScene;
	private Table rootComponent;

	static
	{
		I = new WorldEditor();
	}

	/**
	 * Instance of world editor is automatically managed internally. Use the global
	 * singleton instance.
	 */
	private WorldEditor()
	{

	}

	public void inputToFSA(String input)
	{
		fsa.transition(input, null);
	}

	public void inputToFSA(String input, Object transitionInput)
	{
		fsa.transition(input, transitionInput);
	}

	public OrthographicCamera getUiCamera()
	{
		return uiCamera;
	}

	public Skin getUiSkin()
	{
		return uiSkin;
	}

	public Viewport getUiViewport()
	{
		return uiViewport;
	}

	public SpriteBatch getUiBatch()
	{
		return uiBatch;
	}

	public Stage getUiScene()
	{
		return uiScene;
	}

	public void setHUD(HUD hud)
	{
		rootComponent.clear();
		rootComponent.add(hud.getRoot());
	}

	public Table getRootComponent()
	{
		return rootComponent;
	}

	public SpriteBatch getBatch()
	{
		return batch;
	}

	public ShapeRenderer getShapeRenderer()
	{
		return shapeRenderer;
	}

	public OrthographicCamera getCamera()
	{
		return camera;
	}

	public Viewport getViewport()
	{
		return viewport;
	}

	@Override
	public void create()
	{
		super.create();

		// Initialize state machine
		fsa = new StateMachine();

		// Initialize gui
		uiBatch = new SpriteBatch();
		uiCamera = new OrthographicCamera(WorldEditor.VIRTUAL_WIDTH, WorldEditor.VIRTUAL_HEIGHT);
		uiViewport = new StretchViewport(WorldEditor.VIRTUAL_WIDTH, WorldEditor.VIRTUAL_HEIGHT, uiCamera);
		uiScene = new Stage(uiViewport);
		uiSkin = new Skin(Gdx.files.internal("skins/ui_editor/skin.json"));
		rootComponent = new Table(uiSkin);
		rootComponent.setFillParent(true);
		uiScene.addActor(rootComponent);
		uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera(WorldEditor.VIRTUAL_WIDTH, WorldEditor.VIRTUAL_HEIGHT);
		viewport = new StretchViewport(WorldEditor.VIRTUAL_WIDTH, WorldEditor.VIRTUAL_HEIGHT, uiCamera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Register states
		fsa.registerState(STATE_MAIN_MENU, true);
		fsa.registerState(STATE_CREATE_WORLD);
		fsa.registerState(STATE_LOAD_WORLD);
		fsa.registerState(STATE_CREATE_TILESET);
		fsa.registerState(STATE_LOAD_TILESET);
		fsa.registerState(STATE_TILESET_EDITOR);
		fsa.registerState(STATE_WORLD_EDITOR);
		fsa.registerState(STATE_QUIT);

		// Register transitions
		fsa.registerTransition(STATE_MAIN_MENU, STATE_CREATE_WORLD, TRANSITION_CREATE_WORLD);
		fsa.registerTransition(STATE_MAIN_MENU, STATE_LOAD_TILESET, TRANSITION_LOAD_TILESET);
		fsa.registerTransition(STATE_MAIN_MENU, STATE_LOAD_WORLD, TRANSITION_LOAD_WORLD);
		fsa.registerTransition(STATE_MAIN_MENU, STATE_CREATE_WORLD, TRANSITION_CREATE_WORLD);
		fsa.registerTransition(STATE_MAIN_MENU, STATE_CREATE_TILESET, TRANSITION_CREATE_TILESET);
		fsa.registerTransition(STATE_MAIN_MENU, STATE_QUIT, TRANSITION_QUIT);
		fsa.registerTransition(STATE_MAIN_MENU, STATE_TILESET_EDITOR, TRANSITION_EDIT_TILESET);
		fsa.registerTransition(STATE_CREATE_WORLD, STATE_MAIN_MENU, TRANSITION_MAIN_MENU);
		fsa.registerTransition(STATE_CREATE_TILESET, STATE_MAIN_MENU, TRANSITION_MAIN_MENU);
		fsa.registerTransition(STATE_CREATE_TILESET, STATE_TILESET_EDITOR, TRANSITION_EDIT_TILESET);

		Gdx.input.setInputProcessor(uiScene);

		inputToFSA(TRANSITION_EDIT_TILESET, new battlearena.common.tile.Tileset("test", "C:\\Users\\fores\\Desktop\\Tile_Overworld.png", 8, 8));

	}

	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);

		uiViewport.update(width, height);
		viewport.update(width, height);
		fsa.resizeCurrent(width, height);
	}

	@Override
	public void dispose()
	{
		super.dispose();
	}

	public void update(float delta)
	{
		uiScene.act(delta);
		fsa.updateCurrent(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void render()
	{
		super.render();

		// Update system
		update(Gdx.graphics.getDeltaTime());

		// Clear screen and render frame.
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);

		shapeRenderer.setProjectionMatrix(camera.projection);
		shapeRenderer.setTransformMatrix(camera.view);

		fsa.preUiRenderCurrent();
		uiScene.draw();
		fsa.postUiRenderCurrent();

	}

}
