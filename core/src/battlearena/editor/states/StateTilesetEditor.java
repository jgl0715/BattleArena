package battlearena.editor.states;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

import battlearena.editor.CollisionMask;
import battlearena.editor.TileDefinition;
import battlearena.editor.TileImage;
import battlearena.editor.Tileset;
import battlearena.editor.WorldEditor;

public class StateTilesetEditor extends State
{

	// GUI for tile pane
	private Table editTilePane;
	private TileImage paneTileImage;

	private Table definitionsTable;
	private Table bottomLeft;
	private Tileset tileset;
	private Label tileDefPaneLabel;
	private TextButton addTileDefButton;
	private TextButton deleteTileDefButton;
	private TextButton gridButton;
	private TextField tilesetNameLabel;
	private Map<TileDefinition, Table> defEntries;
	private Table entryHovered;
	private TileDefinition definitionSelected;

	private float originX;
	private float originY;

	private int editingVert;
	private boolean renderGrid;
	private boolean disableMovement;
	private boolean validName;
	private boolean deleteMode;

	public StateTilesetEditor()
	{
		super("Tileset Editor");
	}

	@Override
	public void create()
	{
		editingVert = -1;
	}

	@Override
	public void dispose()
	{

	}

	@Override
	public void resized(int width, int height)
	{

	}

	public void selectFirstTileDefinition()
	{
		selectTileDefinition(tileset.getDefinition(tileset.getDefinitionNameItr().next()));
	}

	public void selectTileDefinition(TileDefinition def)
	{
		definitionSelected = def;

		// Put tile information into edit tile pane.
		editTilePane.clear();

		// Put tile animation in
		paneTileImage = new TileImage(def, tileset);
		editTilePane.add(paneTileImage).width(100).height(100).center().pad(50).row();


	}

	public void setTileset(Tileset tileset)
	{
		this.tileset = tileset;

		originX = -tileset.getTilesheetWidth() / 2;
		originY = -tileset.getTilesheetHeight() / 2;
	}

	public TileDefinition addNewTileDefinition()
	{
		final TileDefinition def = new TileDefinition();
		Skin uiSkin = WorldEditor.I.getUiSkin();

		final Table entryTable = new Table();
		TileImage image = new TileImage(def, tileset);
		final TextField nameLabel = new TextField(def.getName(), uiSkin);

		int unnamedIndex = 1;
		String name = "Unnamed_" + unnamedIndex;
		while(tileset.nameTaken(name))
		{
			unnamedIndex++;
			name = "Unnamed_" + unnamedIndex;
		}
		nameLabel.setText(name);
		def.setName(name);
		def.getMask().makeBox(tileset.getTileWidth(), tileset.getTileHeight());

		// This stops input from being interpreted if the user is typing a name for the tile definition.
		nameLabel.addListener(new FocusListener()
		{
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused)
			{
				super.keyboardFocusChanged(event, actor, focused);

				disableMovement = focused;

				// Save name on focus change.
				if(!focused)
					tileset.updateTileDefinitionName(nameLabel.getText(), def);

			}
		});

		validName = true;

		nameLabel.setTextFieldListener(new TextField.TextFieldListener()
		{
			@Override
			public void keyTyped(TextField textField, char c)
			{
				String fullText = nameLabel.getText();
				System.out.println(tileset.getDefinition(fullText));
				if(tileset.nameTaken(fullText) && tileset.getDefinition(fullText) != def)
				{
					validName = false;
					nameLabel.setColor(Color.RED);
				}else
				{
					validName = true;
					nameLabel.setColor(Color.WHITE);
				}
			}
		});

		entryTable.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);

				if(deleteMode)
				{
					removeTileDefinition(def);
					deleteMode = false;
				}else
				{
					selectTileDefinition(def);
				}

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

		entryTable.add(image).width(20).height(20).expand().fill();
		entryTable.add(nameLabel).width(180);
		definitionsTable.add(entryTable).row();

		defEntries.put(def, entryTable);

		tileset.addDefinition(def);

		selectTileDefinition(def);

		return def;
	}

	public void removeTileDefinition(TileDefinition def)
	{
		Table entry = defEntries.get(def);

		entry.remove();

		tileset.removeDefinition(def);

		if(definitionSelected == def)
		{
			if(tileset.getDefinitionCount() > 0)
				selectFirstTileDefinition();
			else
				selectTileDefinition(null);
		}
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
			final Stage uiScene = WorldEditor.I.getUiScene();
			Skin uiSkin = WorldEditor.I.getUiSkin();
			defEntries = new HashMap<TileDefinition, Table>();

			WorldEditor.I.getRootComponent().clear();

			editTilePane = new Table();
			editTilePane.setFillParent(true);
			editTilePane.left().top();
			{

			}

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

			Table tileDefPane = new Table();
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

						addNewTileDefinition();
					}
				});

				deleteTileDefButton.addListener(new ClickListener()
				{
					@Override
					public void clicked(InputEvent event, float x, float y)
					{
						super.clicked(event, x, y);

						deleteMode = !deleteMode;
					}
				});

			}

			uiScene.addActor(bottomLeft);
			uiScene.addActor(editTilePane);
			uiScene.addActor(tileDefPane);

			setTileset((Tileset) transitionInput);

			// Add blank tile definition if there aren't any
			if (tileset.getDefinitionCount() < 1)
			{
				addNewTileDefinition();
			}

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
					int mtx = getMouseTileX();
					int mty = getMouseTileY();
					int tileIndex = mtx + mty * tileset.getWidth();

					if(definitionSelected != null)
					{
						TileDefinition def = tileset.getDefinition(definitionSelected.getName());

						if(mtx >= 0 && mty >= 0 && mtx < tileset.getWidth() && mty < tileset.getHeight())
						{
							if(button == Input.Buttons.LEFT)
							{
								// Add a tile frame
								def.addFrame(tileIndex);
							}
							else if(button == Input.Buttons.RIGHT)
							{
								// Remove a tile frame.
								def.removeFrame(tileIndex);
							}
						}

					}

					if(validName)
					{
						uiScene.setKeyboardFocus(null);
					}

					return false;
				}

				@Override
				public boolean scrolled(int amount)
				{
					OrthographicCamera camera = WorldEditor.I.getCamera();
					WorldEditor.I.getCamera().zoom += amount / 5.0f;

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
			}, WorldEditor.I.getUiScene());

			Gdx.input.setInputProcessor(muxer);

			disableMovement = false;

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

		if(!disableMovement)
		{
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
		}
		else
		{
		}

		// Check if attempting to move tile definition vertices.
		CollisionMask mask = definitionSelected.getMask();
		int verts = mask.getVertexCount();

		if (verts > 0)
		{
			int mouseX = Gdx.input.getX();
			int mouseY = Gdx.graphics.getHeight()-Gdx.input.getY()-1;

			if(editingVert >= 0)
			{			float scale = tileset.getTileWidth() / 100.0f;


				if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
				{
					Vector2 toLocal = paneTileImage.stageToLocalCoordinates(new Vector2(mouseX, mouseY)).scl(scale);

					if(toLocal.x < 0)
						toLocal.x = 0;
					if(toLocal.x > tileset.getTileWidth())
						toLocal.x = tileset.getTileWidth();

					if(toLocal.y < 0)
						toLocal.y = 0;
					if(toLocal.y > tileset.getTileHeight())
						toLocal.y = tileset.getTileHeight();
					
					mask.getVertex(editingVert).set(toLocal);
				}
				else
				{
					editingVert = -1;
				}
			}
			else
			{
				int rad = 5;
				float scale = 100.0f / tileset.getTileWidth();

				for (int i = 0; i < verts; i++)
				{
					Vector2 vert = mask.getVertex(i);
					Vector2 toStage = paneTileImage.localToStageCoordinates(new Vector2(vert.x * scale, vert.y * scale));
					float dx = toStage.x - mouseX;
					float dy = toStage.y - mouseY;

					if(dx*dx+dy*dy <= rad)
					{							System.out.println("hello");

						if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
						{
							editingVert = i;
						}
					}
				}
			}



		}

		camera.update();
	}

	public Vector2[] getMaskVerticesStage()
	{
		return null;
	}

	public int getMouseTileX()
	{
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		OrthographicCamera camera = WorldEditor.I.getCamera();
		return (int) Math.floor((camera.unproject(new Vector3(x, y, 0)).x - originX) / tileset.getTileWidth());
	}

	public int getMouseTileY()
	{
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		OrthographicCamera camera = WorldEditor.I.getCamera();
		return tileset.getHeight() - (int) Math.floor((camera.unproject(new Vector3(x, y, 0)).y - originY) / tileset.getTileHeight()) - 1;
	}

	@Override
	public void preUiRender()
	{
		SpriteBatch batch = WorldEditor.I.getBatch();
		ShapeRenderer sr = WorldEditor.I.getShapeRenderer();

		int mtx = getMouseTileX(), mty = getMouseTileY();

		batch.begin();
		batch.draw(tileset.getTileSheet(), originX, originY);
		batch.end();

		sr.begin(ShapeType.Line);
		sr.setColor(Color.WHITE);
		if (renderGrid)
		{
			for (int row = 0; row <= tileset.getHeight(); row++)
				sr.line(originX, originY + row * tileset.getTileHeight(), originX + tileset.getTilesheetWidth(), originY + row * tileset.getTileHeight());
			for (int col = 0; col <= tileset.getWidth(); col++)
				sr.line(originX + col * tileset.getTileWidth(), originY, originX + col * tileset.getTileWidth(), originY + tileset.getTilesheetHeight());

		}

		// Outline the current tiles that are selected for the animation.
		if(definitionSelected != null)
		{
			List<Integer> animFrames = definitionSelected.getAnimFrames();
			sr.setColor(Color.BLUE);
			for (int animFrame : animFrames) {
				int tx = (int) (animFrame % tileset.getWidth());
				int ty = (int) (animFrame / tileset.getHeight());
				sr.rect(originX + tx * tileset.getTileWidth(), originY + tileset.getTilesheetHeight() - (1 + ty) * tileset.getTileHeight(), tileset.getTileWidth(), tileset.getTileHeight());

			}
		}

		if (mtx >= 0 && mtx < tileset.getWidth() && mty >= 0 && mty < tileset.getHeight())
		{
			sr.setColor(Color.GREEN);
			sr.rect(originX + mtx * tileset.getTileWidth(), originY + tileset.getTilesheetHeight() - (1+mty) * tileset.getTileHeight(), tileset.getTileWidth(), tileset.getTileHeight());
		}

		sr.end();

	}

	@Override
	public void postUiRender()
	{
		ShapeRenderer sr = WorldEditor.I.getShapeRenderer();

		// Render the selected definition.
		sr.setProjectionMatrix(WorldEditor.I.getUiCamera().projection);
		sr.setTransformMatrix(WorldEditor.I.getUiCamera().view);
		sr.setColor(Color.WHITE);
		sr.begin(ShapeType.Line);
		if (definitionSelected != null)
		{
			Table selectedEntry = defEntries.get(definitionSelected);
			Vector2 vec = selectedEntry.localToStageCoordinates(new Vector2(0, 0));
			sr.rect(vec.x, vec.y, selectedEntry.getWidth(), selectedEntry.getHeight());
		}
		sr.end();

		// Render the box around the selected tile definition
		if(deleteMode)
			sr.setColor(Color.RED);
		else
			sr.setColor(Color.BLUE);
		sr.begin(ShapeType.Line);
		if (entryHovered != null)
		{
			Vector2 vec = entryHovered.localToStageCoordinates(new Vector2(0, 0));

			sr.rect(vec.x, vec.y, entryHovered.getWidth(), entryHovered.getHeight());
		}
		sr.end();

		// Render physics body on current tile image.

		// Render the physics body over the tile image.
		CollisionMask mask = definitionSelected.getMask();
		if(mask != null)
		{
			int verts = mask.getVertexCount();
			float scale = 100.0f / tileset.getTileWidth();

			if (verts > 0)
			{
				sr.begin(ShapeRenderer.ShapeType.Line);
				sr.setColor(Color.GREEN);
				float[] vertsAsArray = new float[verts * 2];

				for (int i = 0; i < verts; i++) {
					Vector2 vert = mask.getVertex(i);
					Vector2 toStage = paneTileImage.localToStageCoordinates(new Vector2(vert.x * scale, vert.y * scale));

					sr.circle(toStage.x, toStage.y, 5);

					vertsAsArray[2 * i + 0] = toStage.x;
					vertsAsArray[2 * i + 1] = toStage.y;

				}
				sr.polygon(vertsAsArray);

				sr.end();
			}
		}
	}

}
