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
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;

import battlearena.common.CollisionGroup;
import battlearena.common.entity.ELight;
import battlearena.common.entity.Entity;
import battlearena.common.entity.EntityFactory;
import battlearena.common.entity.data.DVector2;
import battlearena.common.file.TiledWorldExporter;
import battlearena.common.file.TilesetImporter;
import battlearena.common.tile.CollisionMask;
import battlearena.common.tile.Tile;
import battlearena.common.tile.Tileset;
import battlearena.common.world.EntityLayer;
import battlearena.common.world.Layer;
import battlearena.common.world.Location;
import battlearena.common.world.TileLayer;
import battlearena.common.world.TiledWorld;
import battlearena.editor.WorldEditor;
import battlearena.editor.view.HUDWorldEditor;
import box2dLight.PointLight;

public class StateWorldEditor extends battlearena.common.states.State
{

	public static final String LIGHTS_LAYER = "Lights";

	private TiledWorld editingWorld;
	private TiledWorldExporter exporter;
	private HUDWorldEditor hudWorldEditor;
	private Map<Layer, Table> layerTables;
	private Map<Tile, Table> tileTables;
	private Layer selectedLayer;
	private Tile selectedTile;

	private boolean disableMovement;
	private boolean renderGrid;
	private boolean deleteMode;
	private boolean floodFill;
	private Set<Location> floodFillResult;

	private InputMultiplexer muxer;

	private ELight lastLight;

	public StateWorldEditor()
	{
		super("World Editor");

		layerTables = new HashMap<Layer, Table>();
		tileTables = new HashMap<Tile, Table>();
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

	public TileLayer addNewTileLayer()
	{
		int unnamedIndex = 1;
		String name = "Unnamed_" + unnamedIndex;
		while(editingWorld.layerExists(name))
		{
			unnamedIndex++;
			name = "Unnamed_" + unnamedIndex;
		}

		return addNewTileLayer(name);
	}

	public void addNewLayer(final Layer newLayer, boolean viewOnly, boolean nameEditable)
	{
		if(!viewOnly)
		{
			if(newLayer instanceof TileLayer)
			{
				editingWorld.addTileLayer((TileLayer) newLayer);
			}
			else if(newLayer instanceof EntityLayer)
			{
				editingWorld.addEntityLayer((EntityLayer) newLayer);
			}
		}


		// Add the new layer to the view.
		final Table tableLayer = hudWorldEditor.addLayer(newLayer, nameEditable);

		layerTables.put(newLayer, tableLayer);

		tableLayer.addListener(new FocusListener()
		{
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused)
			{
				super.keyboardFocusChanged(event, actor, focused);

				disableMovement = focused;
			}
		});

		tableLayer.addListener(new InputListener(){
			@Override
			public boolean keyDown(InputEvent event, int keycode)
			{

				// Unfocus on pressing enter
				if(keycode == Input.Keys.ENTER)
				{
					hudWorldEditor.getUI().setKeyboardFocus(null);
				}


				return true;
			}
		});

		tableLayer.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				// For now, the user can only remove tile layers.
				if(deleteMode && newLayer instanceof TileLayer)
				{
					removeLayer(newLayer);
					deleteMode = false;
				}else
				{

					// Makes the user click the layer again in order to edit the text.
					if(selectedLayer != newLayer)
					{
						hudWorldEditor.getUI().setKeyboardFocus(null);
					}

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
	}

	/**
	 * Convenience method for adding a new layer.
	 * @param layerName
	 * @return
	 */
	public TileLayer addNewTileLayer(String layerName)
	{
		final TileLayer newLayer = new TileLayer(layerName, editingWorld.getTileset(), editingWorld.getWidth(), editingWorld.getHeight());

		addNewLayer(newLayer, false, true);

		return newLayer;
	}

	public EntityLayer addNewEntityLayer(String layerName)
	{
		final EntityLayer newLayer = new EntityLayer(layerName);

		addNewLayer(newLayer, false, false);

		return newLayer;
	}


	public void removeLayer(Layer layer)
	{
		// Get the corresponding view component.
		Table tableLayer = layerTables.get(layer);

		// Remove layer from view and world (model).
		hudWorldEditor.removeLayer(tableLayer);
		layerTables.remove(layer);

		if(layer instanceof TileLayer)
		{
			editingWorld.removeTileLayer(layer.getName());
		}
		else if(layer instanceof EntityLayer)
		{
			editingWorld.removeEntityLayer(layer.getName());
		}


		if(layerTables.size() > 0)
		{
			selectFirstLayer();
		}
		else
		{
			selectLayer(null);
		}
	}

	public void selectLayer(Layer layer)
	{
		selectedLayer = layer;
	}

	public void selectFirstLayer()
	{
		selectedLayer = layerTables.keySet().iterator().next();
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

	public void updateTileset()
	{
		JFileChooser chooser = new JFileChooser();
		int res = chooser.showOpenDialog(null);
		Tileset result = null;
		if (res == JFileChooser.APPROVE_OPTION)
		{
			TilesetImporter importer = new TilesetImporter(chooser.getSelectedFile().getAbsolutePath());
			result = importer.imp();
		}

		if(result != null)
		{
			editingWorld.changeTileset(result);
			System.out.println("Tileset sucessfully changed!");
		}
		else
		{
			System.err.println("Error loading tileset, tileset could not be changed.");
		}
	}

	@Override
	public void create()
	{
		renderGrid = true;

		hudWorldEditor = new HUDWorldEditor(WorldEditor.I.getUiSkin());

		hudWorldEditor.addLayerButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				addNewTileLayer();
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

			int touchingButton;

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button)
			{
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer)
			{
				return dragLoc(touchingButton);
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button)
			{
				hudWorldEditor.getUI().setKeyboardFocus(null);

				touchingButton = button;

				return clickLoc(button);
			}

			public boolean clickLoc(int button)
			{
				if(hudWorldEditor.tileHovered == null && !hudWorldEditor.addLayerButton.isOver() && !hudWorldEditor.deleteLayerButton.isOver() && hudWorldEditor.layerHovered == null)
				{
					if (selectedLayer instanceof EntityLayer)
					{
						int mwx = WorldEditor.I.getMouseWorldX();
						int mwy = WorldEditor.I.getMouseWorldY();

						lastLight = EntityFactory.createLight(editingWorld, mwx, mwy, 1, 0, 0, 10, 1.0f);
						editingWorld.addEntity(LIGHTS_LAYER, lastLight);
					}
					else
					{
						editTile(button);
					}
				}

				return false;
			}

			public boolean dragLoc(int button)
			{
				if(hudWorldEditor.tileHovered == null && !hudWorldEditor.addLayerButton.isOver() && !hudWorldEditor.deleteLayerButton.isOver() && hudWorldEditor.layerHovered == null)
				{
					if (selectedLayer instanceof EntityLayer)
					{

					}
					else
					{
						return editTile(button);
					}
				}
				return false;
			}

			public boolean editTile(int button)
			{
				if(button == Input.Buttons.LEFT)
				{
					return tileManip(getMouseTileX(), getMouseTileY(), false);
				}else if(button == Input.Buttons.RIGHT)
				{
					return tileManip(getMouseTileX(), getMouseTileY(), true);
				}
				return false;
			}

			public boolean tileManip(int mtx, int mty, boolean remove)
			{

				if(mtx >= 0 && mty >= 0 && mtx < editingWorld.getWidth() && mty < editingWorld.getHeight())
				{
					if(hudWorldEditor.tileHovered == null && !hudWorldEditor.addLayerButton.isOver() && !hudWorldEditor.deleteLayerButton.isOver() && hudWorldEditor.layerHovered == null && selectedLayer != null && selectedTile != null)
					{
						String layer = selectedLayer.getName();

						Set<Location> manipLocations = new HashSet<Location>();

						if(floodFill)
							manipLocations.addAll(floodFillResult);
						else
							manipLocations.add(new Location(mtx, mty));

						Iterator<Location> locItr = manipLocations.iterator();
						while(locItr.hasNext())
						{
							Location loc = locItr.next();

							if(remove)
								editingWorld.removeTile(layer, loc);
							else
								editingWorld.placeTile(layer, selectedTile, loc);
						}

						return true;

					}
				}

				return false;
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


				if(keycode == Input.Keys.F)
				{
					// Enable flood filling
					floodFill = false;
				}

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

				// Global command handling

				if(hudWorldEditor.getUI().getKeyboardFocus() == null)
				{

					if(keycode == Input.Keys.F1)
					{
						// Decrease world width by one
						editingWorld.changeWidth(-1);
					}

					if(keycode == Input.Keys.F2)
					{
						// Increase world width by one
						editingWorld.changeWidth(1);
					}

					if(keycode == Input.Keys.F3)
					{
						// Decrease world height by one
						editingWorld.changeHeight(-1);
					}

					if(keycode == Input.Keys.F4)
					{
						// Increase world height by one
						editingWorld.changeHeight(1);
					}

					if(keycode == Input.Keys.T)
					{
						if((Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)))
						{
							// Modify tileset in editor
							exporter.exp();

							WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_EDIT_TILESET, editingWorld.getTileset());
						}
						else
						{
							// Change/update tileset
							updateTileset();
						}
					}

					if(keycode == Input.Keys.F)
					{
						// Enable flood filling
						floodFill = true;
					}

					if(keycode == Input.Keys.V)
					{
						// Toggle layer visibility
						selectedLayer.setVisible(!selectedLayer.isVisible());

						if(selectedLayer.isVisible())
						{
							layerTables.get(selectedLayer).setColor(Color.BLACK);
						}
						else
						{
							layerTables.get(selectedLayer).setColor(Color.RED);
						}
					}

					if(keycode == Input.Keys.G)
					{
						// Enable/Disable grid
						renderGrid = !renderGrid;
					}

					if(keycode == Input.Keys.S && (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)))
					{
						// Save world
						exporter.exp();
					}

					if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
					{
						// Transition back to main menu AND at least attempt to export the world.
						exporter.exp();
						WorldEditor.I.inputToFSA(WorldEditor.TRANSITION_MAIN_MENU);
					}
				}

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
		hudWorldEditor.resize(width, height);
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
			exporter = new TiledWorldExporter(editingWorld, "/worlds/" + editingWorld.getName() + ".world");

			WorldEditor.I.setLastLoadedWorldPath(exporter.getAbsoluteLocation());

			hudWorldEditor.tableTiles.clear();
			hudWorldEditor.tableLayers.clear();
			layerTables.clear();
			tileTables.clear();

			hudWorldEditor.fieldWorldName.setText(editingWorld.getName());

			// Note: At the moment, this will get triggered on existing worlds that do not have any layers.
			// e.x. user deletes all layers, saves world, and then loads again.
			if(editingWorld.getLayerCount() < 1)
			{
				// Add two pre-made layers.
				addNewEntityLayer("Lights");
				addNewTileLayer("Background");
				addNewTileLayer("Foreground");
			}
			else
			{
				// Load existing tile layers
				Iterator<TileLayer> layerItr = editingWorld.layerIterator();
				while(layerItr.hasNext())
				{
					TileLayer layer = layerItr.next();
					addNewLayer(layer, true, true);
				}
			}

			selectFirstLayer();

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

		editingWorld.update(delta);

		// This can be made more efficient (only calculate when mouse changes tiles)
		if(floodFill)
		{
			floodFillResult = editingWorld.floodSearch(selectedLayer.getName(), getMouseTileX(), getMouseTileY());
		}

		if(lastLight != null)
		{

			PointLight light = lastLight.getBox2dLight();

			if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_1))
			{
				// Create point light in world.
				Color c = light.getColor();
				c.r -= 0.01;
				if (c.r < 0)
					c.r = 0;
				if (c.r > 1)
					c.r = 1;
				light.setColor(c);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2))
			{
				// Create point light in world.
				Color c = light.getColor();
				c.r += 0.01;
				if (c.r < 0)
					c.r = 0;
				if (c.r > 1)
					c.r = 1;
				light.setColor(c);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4))
			{
				// Create point light in world.
				Color c = light.getColor();
				c.g -= 0.01;
				if (c.g < 0)
					c.g = 0;
				if (c.g > 1)
					c.g = 1;
				light.setColor(c);
			}


			if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_5))
			{
				// Create point light in world.
				Color c = light.getColor();
				c.g += 0.01;
				if (c.g < 0)
					c.g = 0;
				if (c.g > 1)
					c.g = 1;
				light.setColor(c);
			}


			if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_7))
			{
				// Create point light in world.
				Color c = light.getColor();
				c.b -= 0.01;

				if (c.b < 0)
					c.b = 0;
				if (c.b > 1)
					c.b = 1;
				light.setColor(c);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8))
			{
				// Create point light in world.
				Color c = light.getColor();
				c.b += 0.01;

				if (c.b < 0)
					c.b = 0;
				if (c.b > 1)
					c.b = 1;
				light.setColor(c);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.NUM_1))
			{
				// Create point light in world.

				float dist = light.getDistance();

				dist -= 1f;

				if (dist < 0)
					dist = 0;

				light.setDistance(dist);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.NUM_2))
			{
				float dist = light.getDistance();

				dist += 1f;
				light.setDistance(dist);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.NUM_1))
			{
				// Create point light in world.

				float dist = light.getDistance();

				dist -= 1f;

				if (dist < 0)
					dist = 0;

				light.setDistance(dist);
			}


			if (Gdx.input.isKeyPressed(Input.Keys.NUM_2))
			{
				float dist = light.getDistance();

				dist += 1f;


				light.setDistance(dist);
			}
		}

		camera.update();
	}

	@Override
	public void render()
	{
		ShapeRenderer sr = WorldEditor.I.getShapeRenderer();

		editingWorld.render(WorldEditor.I.getBatch(), WorldEditor.I.getCamera());

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

				if (renderGrid)
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
				sr.setColor(Color.GREEN);
				if(floodFill)
				{
					// Render result of flood filling.
					Iterator<Location> locItr = floodFillResult.iterator();

					while(locItr.hasNext())
					{
						Location loc = locItr.next();

						if (mtx >= 0 && mtx < worldWidth && mty >= 0 && mty < worldHeight)
						{
							sr.rect(originX + loc.getTileX() * tileWidth, originY + editingWorld.getPixelHeight() - (1 + loc.getTileY()) * tileHeight, tileWidth, tileHeight);
						}
					}
				}
				else
				{
					// Render single tile.
					if (mtx >= 0 && mtx < worldWidth && mty >= 0 && mty < worldHeight)
					{
						sr.rect(originX + mtx * tileWidth, originY + editingWorld.getPixelHeight() - (1 + mty) * tileHeight, tileWidth, tileHeight);
					}
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
