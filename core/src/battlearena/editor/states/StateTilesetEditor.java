package battlearena.editor.states;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import battlearena.editor.TileDefinition;
import battlearena.editor.TileImage;
import battlearena.editor.Tileset;
import battlearena.editor.WorldEditor;

public class StateTilesetEditor extends State
{

	private Table definitionsTable;
	private Table bottomLeft;
	private Tileset tileset;
	private Label tileDefPaneLabel;
	private TextButton addTileDefButton;
	private TextButton deleteTileDefButton;
	private TextButton gridButton;
	private Map<TileDefinition, Table> defEntries;
	private Table entryHovered;
	private TileDefinition definitionSelected;

	private boolean renderGrid;

	public StateTilesetEditor()
	{
		super("Tileset Editor");
	}

	@Override
	public void create()
	{

	}

	@Override
	public void dispose()
	{

	}

	@Override
	public void resized(int width, int height)
	{

	}

	public void selectTileDefinition(TileDefinition def)
	{

	}

	public void addTileDefinition(final TileDefinition def)
	{
		Skin uiSkin = WorldEditor.I.getUiSkin();
		tileset.addDefinition(def);

		final Table entryTable = new Table();
		TileImage image = new TileImage(def, tileset);
		TextField nameLabel = new TextField(def.getName(), uiSkin);

		entryTable.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				definitionSelected = def;
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
			{
				super.enter(event, x, y, pointer, fromActor);
				entryHovered = entryTable;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
			{
				super.exit(event, x, y, pointer, toActor);

				if (entryHovered == entryTable)
					entryHovered = null;
			}
		});

		entryTable.add(image).width(20);
		entryTable.add(nameLabel).width(180);
		definitionsTable.add(entryTable).row();

		defEntries.put(def, entryTable);
	}

	public void removeTileDefinition(TileDefinition def)
	{
		Table entry = defEntries.get(def);

		entry.remove();

		tileset.removeDefinition(def);
	}

	@Override
	public void show(Object transitionInput)
	{
		if (!(transitionInput instanceof Tileset))
		{
			throw new IllegalStateException("Tileset editor state only takes a tileset in as input");
		}
		else
		{
			Table tileDefPane = new Table();
			Stage uiScene = WorldEditor.I.getUiScene();
			Skin uiSkin = WorldEditor.I.getUiSkin();
			defEntries = new HashMap<TileDefinition, Table>();

			WorldEditor.I.getRootComponent().clear();

			bottomLeft = new Table();
			bottomLeft.setFillParent(true);
			bottomLeft.bottom().left();
			bottomLeft.pad(5);
			{
				gridButton = new TextButton("Grid", uiSkin);

				bottomLeft.add(gridButton).width(50);

				gridButton.addListener(new ClickListener()
				{
					@Override
					public void clicked(InputEvent event, float x, float y)
					{
						super.clicked(event, x, y);

						renderGrid = !renderGrid;
					}
				});
			}

			tileDefPane = new Table();
			tileDefPane.setFillParent(true);
			tileDefPane.right().top();
			tileDefPane.pad(5);
			{
				Table nameRow = new Table();
				Table buttonsRow = new Table();
				definitionsTable = new Table();

				tileDefPaneLabel = new Label("Tile Definitions", uiSkin);
				addTileDefButton = new TextButton("+", uiSkin);
				deleteTileDefButton = new TextButton("-", uiSkin);

				nameRow.add(tileDefPaneLabel).width(100);
				nameRow.add(addTileDefButton).width(50);
				nameRow.add(deleteTileDefButton).width(50).row();

				tileDefPane.add(nameRow).row();
				tileDefPane.add(definitionsTable).row();

				addTileDefButton.addListener(new ClickListener()
				{
					@Override
					public void clicked(InputEvent event, float x, float y)
					{
						super.clicked(event, x, y);
					}
				});

				deleteTileDefButton.addListener(new ClickListener()
				{
					@Override
					public void clicked(InputEvent event, float x, float y)
					{
						super.clicked(event, x, y);
					}
				});

			}

			uiScene.addActor(bottomLeft);
			uiScene.addActor(tileDefPane);

			tileset = (Tileset) transitionInput;

			// Add blank tile definition if there aren't any
			if (tileset.getDefinitionCount() < 1)
				addTileDefinition(new TileDefinition());

			InputMultiplexer muxer = new InputMultiplexer(new InputProcessor()
			{

				@Override
				public boolean touchUp(int screenX, int screenY, int pointer, int button)
				{
					return false;
				}

				@Override
				public boolean touchDragged(int screenX, int screenY, int pointer)
				{
					return false;
				}

				@Override
				public boolean touchDown(int screenX, int screenY, int pointer, int button)
				{
					return false;
				}

				@Override
				public boolean scrolled(int amount)
				{
					OrthographicCamera camera = WorldEditor.I.getCamera();
					WorldEditor.I.getCamera().zoom += amount / 5.0f;

					if (camera.zoom < 0.3f)
						camera.zoom = 0.3f;
					if (camera.zoom > 2.0f)
						camera.zoom = 2.0f;

					return true;
				}

				@Override
				public boolean mouseMoved(int screenX, int screenY)
				{
					return false;
				}

				@Override
				public boolean keyUp(int keycode)
				{
					return false;
				}

				@Override
				public boolean keyTyped(char character)
				{
					return false;
				}

				@Override
				public boolean keyDown(int keycode)
				{
					return false;
				}
			}, WorldEditor.I.getUiScene());

			Gdx.input.setInputProcessor(muxer);

		}
	}

	@Override
	public void hide()
	{
		bottomLeft.remove();
	}

	@Override
	public void update(float delta)
	{
		OrthographicCamera camera = WorldEditor.I.getCamera();

		if (camera.position.x > Gdx.graphics.getWidth() / 2 * camera.zoom)
			camera.position.x = Gdx.graphics.getWidth() / 2 * camera.zoom;
		if (camera.position.x < -Gdx.graphics.getWidth() / 2 * camera.zoom)
			camera.position.x = -Gdx.graphics.getWidth() / 2 * camera.zoom;

		if (camera.position.y > Gdx.graphics.getHeight() / 2 * camera.zoom)
			camera.position.y = Gdx.graphics.getHeight() / 2 * camera.zoom;
		if (camera.position.y < -Gdx.graphics.getHeight() / 2 * camera.zoom)
			camera.position.y = -Gdx.graphics.getHeight() / 2 * camera.zoom;

		if (Gdx.input.isKeyPressed(Keys.W))
		{
			camera.translate(0, 1);
		}
		if (Gdx.input.isKeyPressed(Keys.S))
		{
			camera.translate(0, -1);
		}
		if (Gdx.input.isKeyPressed(Keys.A))
		{
			camera.translate(-1, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.D))
		{
			camera.translate(1, 0);
		}

		camera.update();
	}

	public int getMouseTileX(float ox)
	{
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		OrthographicCamera camera = WorldEditor.I.getCamera();
		return (int) Math.floor((camera.unproject(new Vector3(x, y, 0)).x - ox) / tileset.getTileWidth());
	}

	public int getMouseTileY(float oy)
	{
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		OrthographicCamera camera = WorldEditor.I.getCamera();
		return (int) Math.floor((camera.unproject(new Vector3(x, y, 0)).y - oy) / tileset.getTileHeight());
	}

	@Override
	public void preUiRender()
	{
		SpriteBatch batch = WorldEditor.I.getBatch();
		ShapeRenderer sr = WorldEditor.I.getShapeRenderer();

		float ox = -tileset.getTilesheetWidth() / 2;
		float oy = -tileset.getTilesheetHeight() / 2;
		int mtx = getMouseTileX(ox), mty = getMouseTileY(oy);

		batch.begin();
		batch.draw(tileset.getTileSheet(), ox, oy);
		batch.end();

		sr.begin(ShapeType.Line);
		sr.setColor(Color.WHITE);
		if (renderGrid)
		{
			for (int row = 0; row <= tileset.getHeight(); row++)
				sr.line(ox, oy + row * tileset.getTileHeight(), ox + tileset.getTilesheetWidth(), oy + row * tileset.getTileHeight());
			for (int col = 0; col <= tileset.getWidth(); col++)
				sr.line(ox + col * tileset.getTileWidth(), oy, ox + col * tileset.getTileWidth(), oy + tileset.getTilesheetHeight());

		}

		if (mtx >= 0 && mtx < tileset.getWidth() && mty >= 0 && mty < tileset.getHeight())
		{
			sr.setColor(Color.GREEN);
			sr.rect(ox + mtx * tileset.getTileWidth(), oy + mty * tileset.getTileHeight(), tileset.getTileWidth(), tileset.getTileHeight());
		}

		sr.end();

	}

	@Override
	public void postUiRender()
	{
		ShapeRenderer sr = WorldEditor.I.getShapeRenderer();

		sr.setProjectionMatrix(WorldEditor.I.getUiCamera().projection);
		sr.setTransformMatrix(WorldEditor.I.getUiCamera().view);
		sr.setColor(Color.BLUE);
		sr.begin(ShapeType.Line);
		if (entryHovered != null)
		{
			Vector2 vec = entryHovered.localToStageCoordinates(new Vector2(0, 0));

			sr.rect(vec.x, vec.y, entryHovered.getWidth(), entryHovered.getHeight());
		}
		sr.end();

		sr.setColor(Color.WHITE);
		sr.begin(ShapeType.Line);
		if (definitionSelected != null)
		{
			Table selectedEntry = defEntries.get(definitionSelected);
			Vector2 vec = selectedEntry.localToStageCoordinates(new Vector2(0, 0));
			sr.rect(vec.x, vec.y, selectedEntry.getWidth(), selectedEntry.getHeight());
		}
		sr.end();
	}

}
