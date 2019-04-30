package battlearena.editor.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;

import battlearena.common.entity.ELight;
import battlearena.common.entity.Entity;
import battlearena.common.entity.data.DBoolean;
import battlearena.common.entity.data.DFloat;
import battlearena.common.world.World;
import battlearena.game.BAEntityFactory;
import battlearena.common.entity.data.DVector2;
import battlearena.common.file.TiledWorldExporter;
import battlearena.common.file.TilesetImporter;
import battlearena.common.tile.Tile;
import battlearena.common.tile.Tileset;
import battlearena.common.world.EntityLayer;
import battlearena.common.world.Layer;
import battlearena.common.world.Location;
import battlearena.common.world.TileLayer;
import battlearena.common.world.TiledWorld;
import battlearena.editor.WorldEditor;
import battlearena.editor.view.HUDWorldEditor;
import battlearena.game.LayerType;
import box2dLight.PointLight;

public class StateWorldEditor extends battlearena.common.states.State
{

	public static final int COLLISION_ENABLED_META = 0x01;

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
	private boolean showMeta;
	private Set<Location> floodFillResult;

	private InputMultiplexer muxer;

	private Vector2 dragOffset;
	private Entity hoveredEntity;
	private Entity selectedEntity;
	private Entity draggingEntity;

	private Box2DDebugRenderer dbgr;

	private Location selectedLocation;

	private float cameraSpeed = 5.0f;

	public StateWorldEditor()
	{
		super("World Editor");

		layerTables = new HashMap<Layer, Table>();
		tileTables = new HashMap<Tile, Table>();

		selectedEntity = null;
		draggingEntity = null;
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
		final TileLayer newLayer = new TileLayer(layerName, editingWorld.getTileset(), editingWorld.getWidth(), editingWorld.getHeight(), true);

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

	public LayerType getSelectedLayerType()
	{
		if(selectedLayer instanceof TileLayer)
		{
			return LayerType.TILES;
		}
		else if(selectedLayer instanceof EntityLayer)
		{
			String name = selectedLayer.getName();

			if(name.equalsIgnoreCase(LayerType.LIGHTS.getName()))
				return LayerType.LIGHTS;

		}

		return null;
	}


	public int getMouseTileX()
	{
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		float originX = 0;
		OrthographicCamera camera = WorldEditor.I.getCamera();
		return (int) Math.floor((camera.unproject(new Vector3(x, y, 0)).x - originX) / TiledWorld.TILE_SIZE);
	}

	public int getMouseTileY()
	{
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		float originY = 0;
		OrthographicCamera camera = WorldEditor.I.getCamera();
		return editingWorld.getHeight() - (int) Math.floor((camera.unproject(new Vector3(x, y, 0)).y - originY) / TiledWorld.TILE_SIZE) - 1;
	}

	public void updateTileset()
	{
		JFileChooser chooser = new JFileChooser();
		int res = chooser.showOpenDialog(null);
		Tileset result = null;
		if (res == JFileChooser.APPROVE_OPTION)
		{
			TilesetImporter importer = new TilesetImporter(chooser.getSelectedFile().getAbsolutePath(), false);
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
		showMeta = true;

		hudWorldEditor = new HUDWorldEditor(WorldEditor.I.getUiSkin());

		hudWorldEditor.fieldMeta.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{

				if(editingWorld.isLocInBounds(selectedLocation) &&  hudWorldEditor.fieldMeta.getText().length() > 0)
				{
					editingWorld.getLayer(selectedLayer.getName()).getCell(selectedLocation.getTileX(), selectedLocation.getTileY()).setMeta(Integer.parseInt(hudWorldEditor.fieldMeta.getText()));
				}
			}
		});

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

		muxer = new InputMultiplexer(hudWorldEditor.getUI(), new InputProcessor()
		{
			int touchingButton;

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button)
			{
				draggingEntity = null;
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

			public boolean dragLoc(int button)
			{
				LayerType type = getSelectedLayerType();
				if(canModifyWorld())
				{
					switch(type){
						case TILES:
							return editTile(button);
						default:
							// The dragging of entities is handled in update.
							return false;
					}
				}

				return false;
			}

			public boolean clickLoc(int button)
			{
				LayerType type = getSelectedLayerType();
				if(canModifyWorld())
				{
					switch(type){
						case LIGHTS:
							return clickEnt(button, type, hoveredEntity);
						case TILES:
							return editTile(button);
						default:
							return false;
					}
				}

				return false;
			}

			public boolean clickEnt(int button, LayerType type, Entity hoveredEnt)
			{
				if(button == Input.Buttons.LEFT)
				{
					Vector2 mouseWorld = new Vector2(WorldEditor.I.getMouseWorldX(), WorldEditor.I.getMouseWorldY());
					if(hoveredEnt == null)
					{
						// Create new entity based on layer type.
						if(type == LayerType.LIGHTS)
							hoveredEnt = BAEntityFactory.createLight(editingWorld, mouseWorld.x, mouseWorld.y, 1, 0, 0, 10, 1.0f);

						editingWorld.addEntity(type.getName(), hoveredEnt);
					}

					selectedEntity = hoveredEnt;
					draggingEntity = selectedEntity;

					DVector2 data = hoveredEnt.find(DVector2.class, Entity.POSITION);

					if(data != null)
					{
						dragOffset = new Vector2(mouseWorld).sub(data.Value);
					}
					else
					{
						dragOffset = null;
					}

					return true;
				}
				else if(button == Input.Buttons.RIGHT)
				{
					// Delete light

					if(hoveredEnt != null)
					{
						hoveredEnt.remove();
					}

					return true;
				}

				return false;
			}

			public boolean editTile(int button)
			{
				int mtx = getMouseTileX();
				int mty = getMouseTileY();

				selectedLocation = new Location(mtx, mty);

				if(editingWorld.isLocInBounds(selectedLocation))
				{
					hudWorldEditor.fieldMeta.setText("" + editingWorld.getLayer(selectedLayer.getName()).getCell(mtx, mty).getMeta());
				}

				if(button == Input.Buttons.LEFT)
				{
					return tileManip(mtx, mty, false);
				}else if(button == Input.Buttons.RIGHT)
				{
					return tileManip(mtx, mty, true);
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
							{
								editingWorld.placeTile(layer, selectedTile, loc);
							}
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

					LayerType type = getSelectedLayerType();

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

					if(keycode == Input.Keys.M)
					{
						// Decrease world width by one
						showMeta = !showMeta;
					}

					if(keycode == Input.Keys.C && type == LayerType.TILES)
					{
						// Enable/Disable layer collision
						TileLayer tl = (TileLayer) selectedLayer;
						tl.setCollisionEnabled(!tl.isCollisionEnabled());
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

					if(keycode == Input.Keys.F && getSelectedLayerType() == LayerType.TILES)
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

		});


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
			dbgr = new Box2DDebugRenderer();
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
				// Load existing entity layers
				Iterator<EntityLayer> entityLayerItr = editingWorld.entityLayerIterator();
				while(entityLayerItr.hasNext())
				{
					EntityLayer layer = entityLayerItr.next();
					addNewLayer(layer, true, false);
				}

				// Load existing tile layers
				Iterator<TileLayer> layerItr = editingWorld.tileLayerIterator();
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
				camera.translate(0, cameraSpeed);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S))
			{
				camera.translate(0, -cameraSpeed);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.A))
			{
				camera.translate(-cameraSpeed, 0);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.D))
			{
				camera.translate(cameraSpeed, 0);
			}
		}

		hoveredEntity = null;
		Iterator<EntityLayer> entLayerItr = editingWorld.entityLayerIterator();
		while(entLayerItr.hasNext())
		{
			EntityLayer layer = entLayerItr.next();
			Iterator<Entity> entItr = layer.iterator();

			while(entItr.hasNext())
			{
				Entity e = entItr.next();

				DBoolean hovered = e.find(DBoolean.class, Entity.HOVERED);
				if(hovered != null)
				{
					if(hovered.Value)
					{
						hoveredEntity = e;
					}
				}
			}
		}

		if(draggingEntity != null)
		{
			Vector2 pos = draggingEntity.find(DVector2.class, Entity.POSITION).Value;
			Vector2 mouseWorld = new Vector2(WorldEditor.I.getMouseWorldX(), WorldEditor.I.getMouseWorldY());
			Vector2 entWorld = mouseWorld.sub(dragOffset);

			pos.set(entWorld);
		}

		editingWorld.update(delta);

		// This can be made more efficient (only calculate when mouse changes tiles)
		if(floodFill && getSelectedLayerType() == LayerType.TILES)
		{
			floodFillResult = editingWorld.floodSearch(selectedLayer.getName(), getMouseTileX(), getMouseTileY());
		}

		if(getSelectedLayerType() == LayerType.LIGHTS)
		{
			ELight lightEnt = (ELight)selectedEntity;
			if(lightEnt != null)
			{
				DFloat red = lightEnt.find(DFloat.class, ELight.DATA_RED);
				DFloat green = lightEnt.find(DFloat.class, ELight.DATA_GREEN);
				DFloat blue = lightEnt.find(DFloat.class, ELight.DATA_BLUE);
				DFloat dist = lightEnt.find(DFloat.class, ELight.DATA_DISTANCE);
				DFloat shadow = lightEnt.find(DFloat.class, ELight.DATA_SHADOW_SOFTNESS);

				PointLight light = lightEnt.getBox2dLight();
				Color c = light.getColor();

				if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
					// Create point light in world.
					cameraSpeed += 0.01;
				}

				if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
					// Create point light in world.
					cameraSpeed -= 0.01;
					if(cameraSpeed <= 1.0f)
						cameraSpeed = 1.0f;
				}

				if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_1)) {
					// Create point light in world.
					red.Value -= 0.01f;
					if (red.Value < 0)
						red.Value = 0.0f;
				}

				if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2)) {
					// Create point light in world.
					red.Value += 0.01f;
					if (red.Value > 1.0f)
						red.Value = 1.0f;
				}

				if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4))
				{
					// Create point light in world.
					green.Value -= 0.01f;
					if (green.Value < 0)
						green.Value = 0.0f;
				}


				if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_5)) {
					// Create point light in world.
					green.Value += 0.01f;

					if (green.Value > 1)
						green.Value = 1.0f;

				}


				if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_7)) {
					// Create point light in world.
					blue.Value -= 0.01f;
					if (blue.Value < 0)
						blue.Value = 0.0f;
				}

				if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8)) {
					// Create point light in world.
					blue.Value += 0.01f;
					if (blue.Value > 1)
						blue.Value = 1.0f;
				}

				if (Gdx.input.isKeyPressed(Input.Keys.NUM_1))
				{
					// Create point light in world.
					dist.Value -= 1f;

					if (dist.Value < 0)
						dist.Value = 0.0f;
				}

				if (Gdx.input.isKeyPressed(Input.Keys.NUM_2))
				{
					dist.Value += 1f;
				}

				if (Gdx.input.isKeyPressed(Input.Keys.NUM_3))
				{
					// Create point light in world.
					shadow.Value -= 0.01f;

					if (shadow.Value < 0)
						shadow.Value = 0.0f;
				}

				if (Gdx.input.isKeyPressed(Input.Keys.NUM_4))
				{
					shadow.Value += 0.01f;
				}

			}
		}

		camera.update();
	}

	@Override
	public void render()
	{
		ShapeRenderer sr = WorldEditor.I.getShapeRenderer();
		SpriteBatch batch = WorldEditor.I.getBatch();

		editingWorld.render(WorldEditor.I.getBatch(), WorldEditor.I.getCamera());


		OrthographicCamera cam = WorldEditor.I.getCamera();
		Matrix4 mat = new Matrix4(cam.combined);
		mat.scale(World.PIXELS_PER_METER, World.PIXELS_PER_METER, 1);
		dbgr.render(editingWorld.getPhysicsWorld(), mat);

		sr.begin(ShapeRenderer.ShapeType.Line);
		{
			int worldWidth = editingWorld.getWidth();
			int worldHeight = editingWorld.getHeight();
			int tileWidth = TiledWorld.TILE_SIZE;
			int tileHeight = TiledWorld.TILE_SIZE;
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

			LayerType layerType = getSelectedLayerType();

			// Render tile outline for tile under mouse.
			if(layerType == LayerType.TILES)
			{
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
			else if(layerType != null)
			{
				Iterator<EntityLayer> entLayerItr = editingWorld.entityLayerIterator();
				while(entLayerItr.hasNext())
				{
					EntityLayer layer = entLayerItr.next();
					Iterator<Entity> entItr = layer.iterator();

					while(entItr.hasNext())
					{
						Entity e = entItr.next();

						if(selectedEntity == e)
							sr.setColor(Color.WHITE);
						else
							sr.setColor(Color.GREEN);

						DFloat radDat = e.find(DFloat.class, Entity.EDITOR_RADIUS);
						DVector2 posDat = e.find(DVector2.class, Entity.POSITION);

						if(radDat != null && posDat != null)
						{
							float editorRad = radDat.Value;
							Vector2 pos = posDat.Value;

							sr.circle(pos.x, pos.y, editorRad, 100);
						}
					}
				}
			}

		}

		sr.end();

		// Render meta
		batch.begin();
		batch.setColor(Color.WHITE);
		if(showMeta)
		{
			{
				Skin sk = WorldEditor.I.getUiSkin();
				BitmapFont font = sk.getFont("default");

				for(int x = 0; x < editingWorld.getWidth(); x++)
				{
					for(int y = 0; y < editingWorld.getHeight(); y++)
					{
						if(getSelectedLayerType() == LayerType.TILES)
						{
							TileLayer tileLayer = (TileLayer) selectedLayer;
							int meta = tileLayer.getCell(x, y).getMeta();
							if(meta != 0)
							{
								font.draw(batch, meta + "", x*TiledWorld.TILE_SIZE, (editingWorld.getHeight()-y-1+1)*TiledWorld.TILE_SIZE);
							}
						}
					}
				}

			}
		}
		batch.end();

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


	private boolean canModifyWorld()
	{

		if(hudWorldEditor.tileHovered != null)
			return false;

		if(hudWorldEditor.layerHovered != null)
			return false;

		if(hudWorldEditor.addLayerButton.isOver())
			return false;

		if(hudWorldEditor.deleteLayerButton.isOver())
			return false;

		return true;
	}


}
