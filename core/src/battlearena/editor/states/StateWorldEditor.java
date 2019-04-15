package battlearena.editor.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import battlearena.common.tile.CollisionMask;
import battlearena.common.tile.Tile;
import battlearena.common.tile.Tileset;
import battlearena.common.world.TileLayer;
import battlearena.common.world.TiledWorld;
import battlearena.editor.WorldEditor;
import battlearena.editor.view.HUDWorldEditor;

public class StateWorldEditor extends battlearena.common.states.State
{

	private TiledWorld editingWorld;
	private HUDWorldEditor hudWorldEditor;
	private Map<TileLayer, Table> layerTables;
	private Map<Tile, Table> tileTables;
	private TileLayer selectedLayer;
	private Tile selectedTile;

	private boolean disableMovement;
	private boolean renderGrid;
	private boolean deleteMode;

	private InputMultiplexer muxer;

	public StateWorldEditor()
	{
		super("World Editor");

		layerTables = new HashMap<TileLayer, Table>();
		tileTables = new HashMap<Tile, Table>();
	}

	public TileLayer addNewLayer()
	{
		int unnamedIndex = 1;
		String name = "Unnamed_" + unnamedIndex;
		while(editingWorld.layerExists(name))
		{
			unnamedIndex++;
			name = "Unnamed_" + unnamedIndex;
		}

		return addNewLayer(name);
	}

	public void setTileset(Tileset set)
	{
		Iterator<String> tileNameItr = set.getTileNameIterator();
		Tile firstTile = null;

		while(tileNameItr.hasNext())
		{
			String tileName = tileNameItr.next();

			final Tile tile = set.getTile(tileName);
			final Table tableTile = hudWorldEditor.addTile(editingWorld.getTileset(), tile);

			tileTables.put(tile, tableTile);

			tableTile.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					super.clicked(event, x, y);

					selectTile(tile);
				}

				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
				{
					super.enter(event, x, y, pointer, fromActor);
					hudWorldEditor.tileHovered = tableTile;
				}

				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
				{
					super.exit(event, x, y, pointer, toActor);

					if (hudWorldEditor.tileHovered  == tableTile)
						hudWorldEditor.tileHovered = null;
				}
			});

			if(firstTile == null)
				firstTile = tile;

		}

		// Select the first tile loaded.
		selectTile(firstTile);
	}

	public void selectTile(Tile tile)
	{
		selectedTile = tile;
	}


	public TileLayer addNewLayer(String layerName)
	{
		final TileLayer newLayer = new TileLayer(layerName, editingWorld.getWidth(), editingWorld.getHeight());

		// Background gets rendered before foreground so add background first.
		editingWorld.addLayer(newLayer);

		// Add the new layer to the view.
		final Table tableLayer = hudWorldEditor.addLayer(newLayer);

		layerTables.put(newLayer, tableLayer);

		tableLayer.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				System.out.println("i been clicked");

				if(deleteMode)
				{
					removeLayer(newLayer);
					deleteMode = false;
				}else
				{
					selectLayer(newLayer);
				}
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
			{
				super.enter(event, x, y, pointer, fromActor);
				hudWorldEditor.layerHovered = tableLayer;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
			{
				super.exit(event, x, y, pointer, toActor);

				if (hudWorldEditor.layerHovered == tableLayer)
					hudWorldEditor.layerHovered = null;
			}
		});

		return newLayer;
	}

	public void removeLayer(TileLayer layer)
	{
		// Get the corresponding view component.
		Table tableLayer = layerTables.get(layer);

		// Remove layer from view and world (model).
		hudWorldEditor.removeLayer(tableLayer);
		editingWorld.removeLayer(layer.getName());
	}

	public void selectLayer(TileLayer layer)
	{
		selectedLayer = layer;
	}

	public int getMouseTileX()
	{
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		float originX = 0;
		OrthographicCamera camera = WorldEditor.I.getCamera();
		return (int) Math.floor((camera.unproject(new Vector3(x, y, 0)).x - originX) / editingWorld.getTileset().getTileWidth());
	}

	public int getMouseTileY()
	{
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		float originY = 0;
		OrthographicCamera camera = WorldEditor.I.getCamera();
		return editingWorld.getHeight() - (int) Math.floor((camera.unproject(new Vector3(x, y, 0)).y - originY) / editingWorld.getTileset().getTileHeight()) - 1;
	}

	@Override
	public void create()
	{
		hudWorldEditor = new HUDWorldEditor(WorldEditor.I.getUiSkin());

		hudWorldEditor.addLayerButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				addNewLayer();
			}
		});

		hudWorldEditor.deleteLayerButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				deleteMode = !deleteMode;

			}
		});

		muxer = new InputMultiplexer(new InputProcessor()
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

				int mtx = getMouseTileX();
				int mty = getMouseTileY();
				Tile t = selectedTile;

				System.out.println("trying to place tile");

				if(hudWorldEditor.tileHovered == null && selectedLayer != null && selectedTile != null)
				{
					String layer = selectedLayer.getName();
					System.out.println("placing tile");
					editingWorld.placeTile(layer, t, mtx, mty);
				}else
				{
					return false;
				}

				return true;
			}

			@Override
			public boolean scrolled(int amount)
			{
				OrthographicCamera camera = WorldEditor.I.getCamera();
				camera.zoom += amount / 5.0f;

				if (camera.zoom < 0.1f)
					camera.zoom = 0.1f;
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
		}, hudWorldEditor.getUI());


	}

	@Override
	public void dispose()
	{
		
	}

	@Override
	public void resized(int width, int height)
	{
		
	}

	@Override
	public void show(Object transitionInput)
	{
		hudWorldEditor.setAsInput();

		if(!(transitionInput instanceof TiledWorld))
		{
			throw new IllegalArgumentException("Transition input must be a TiledWorld");
		}
		else
		{
			editingWorld = (TiledWorld) transitionInput;

			hudWorldEditor.fieldWorldName.setText(editingWorld.getName());

			// Note: At the moment, this will get triggered on existing worlds that do not have any layers.
			// e.x. user deletes all layers, saves world, and then loads again.
			if(editingWorld.getLayerCount() < 1)
			{
				System.out.println("test1");
				// Add two pre-made layers.
				addNewLayer("Foreground");
				addNewLayer("Background");
			}else
			{
				// Load existing layers
			}

			setTileset(editingWorld.getTileset());

			Gdx.input.setInputProcessor(muxer);
		}
	}

	@Override
	public void hide()
	{
		
	}

	@Override
	public void update(float delta)
	{
		OrthographicCamera camera = WorldEditor.I.getCamera();

		if(!disableMovement)
		{
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
		}
		else
		{
		}

//		if (camera.position.x >= WorldEditor.I.VIRTUAL_WIDTH / 2 * camera.zoom)
//			camera.position.x = WorldEditor.I.VIRTUAL_WIDTH / 2 * camera.zoom;
//		if (camera.position.x <= -WorldEditor.I.VIRTUAL_WIDTH / 2 * camera.zoom)
//			camera.position.x = -WorldEditor.I.VIRTUAL_WIDTH / 2 * camera.zoom;
//
//		if (camera.position.y >= WorldEditor.I.VIRTUAL_HEIGHT / 2 * camera.zoom)
//			camera.position.y = WorldEditor.I.VIRTUAL_HEIGHT / 2 * camera.zoom;
//		if (camera.position.y <= -WorldEditor.I.VIRTUAL_HEIGHT / 2 * camera.zoom)
//			camera.position.y = -WorldEditor.I.VIRTUAL_HEIGHT / 2 * camera.zoom;

		camera.update();
	}

	@Override
	public void render()
	{
		ShapeRenderer worldSR = WorldEditor.I.getShapeRenderer();
		ShapeRenderer sr = WorldEditor.I.getShapeRenderer();

		editingWorld.render();

		sr.begin(ShapeRenderer.ShapeType.Line);
		{
			int worldWidth = editingWorld.getWidth();
			int worldHeight = editingWorld.getHeight();
			int tileWidth = editingWorld.getTileset().getTileWidth();
			int tileHeight = editingWorld.getTileset().getTileHeight();
			float originX = 0;
			float originY = 0;

			// Grid rendering
			{
				sr.setColor(Color.WHITE);

				// change to renderGrid==true
				if (true)
				{
					for (int row = 0; row <= worldHeight; row++)
						sr.line(originX, originY + row * tileHeight, originX + editingWorld.getPixelWidth(), originY + row * tileHeight);
					for (int col = 0; col <= worldWidth; col++)
						sr.line(originX + col * tileWidth, originY, originX + col * tileWidth, originY + editingWorld.getPixelHeight());
				}

			}

			// Render tile outline for tile under mouse.
			int mtx = getMouseTileX();
			int mty = getMouseTileY();
			{
				if (mtx >= 0 && mtx < worldWidth && mty >= 0 && mty < worldHeight)
				{
					sr.setColor(Color.GREEN);
					sr.rect(originX + mtx * tileWidth, originY + editingWorld.getPixelHeight() - (1 + mty) * tileHeight, tileWidth, tileHeight);
				}
			}
		}
		sr.end();

		hudWorldEditor.render();

		// Render the selected definition.
		sr.setProjectionMatrix(hudWorldEditor.getCamera().projection);
		sr.setTransformMatrix(hudWorldEditor.getCamera().view);

		sr.begin(ShapeRenderer.ShapeType.Line);
		{
			// Post UI render for layers.
			{
				// Render box around selected layer to edit.
				{
					sr.setColor(Color.WHITE);
					if (selectedLayer != null)
					{
						Table selectedEntry = layerTables.get(selectedLayer);
						Vector2 vec = selectedEntry.localToStageCoordinates(new Vector2(0, 0));
						sr.rect(vec.x, vec.y, selectedEntry.getWidth(), selectedEntry.getHeight());
					}
				}

				// Render box around the hovered layer. Red if deleting and blue if not.
				{
					if(deleteMode)
						sr.setColor(Color.RED);
					else
						sr.setColor(Color.BLUE);

					if (hudWorldEditor.layerHovered != null)
					{
						Vector2 vec = hudWorldEditor.layerHovered.localToStageCoordinates(new Vector2(0, 0));

						sr.rect(vec.x, vec.y, hudWorldEditor.layerHovered.getWidth(), hudWorldEditor.layerHovered.getHeight());
					}
				}
			}

			// Post UI render for tileset.
			{
				// Render box around selected tile to edit.
				{
					sr.setColor(Color.WHITE);
					if (selectedTile != null)
					{
						Table selectedEntry = tileTables.get(selectedTile);
						Vector2 vec = selectedEntry.localToStageCoordinates(new Vector2(0, 0));
						sr.rect(vec.x, vec.y, selectedEntry.getWidth(), selectedEntry.getHeight());
					}
				}

				// Render box around the hovered tile. Red if deleting and blue if not.
				{
					if(deleteMode)
						sr.setColor(Color.RED);
					else
						sr.setColor(Color.BLUE);

					if (hudWorldEditor.tileHovered != null)
					{
						Vector2 vec = hudWorldEditor.tileHovered.localToStageCoordinates(new Vector2(0, 0));

						sr.rect(vec.x, vec.y, hudWorldEditor.tileHovered.getWidth(), hudWorldEditor.tileHovered.getHeight());
					}
				}
			}
		}
		sr.end();
	}


}
