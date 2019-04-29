package battlearena.common.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

import battlearena.common.tile.Tile;
import battlearena.common.tile.Tileset;

public class TileLayer extends Layer
{

    private Tileset tileset;
    private int width;
    private int height;
    private Cell[] cells;
    private float delta;
    private boolean collisionEnabled;

    public TileLayer(String name, Tileset set, int width, int height, boolean collisionEnabled)
    {
        super(name);
        this.tileset = set;
        this.width = width;
        this.height = height;
        this.cells = new Cell[width * height];
        this.visible = true;
        this.collisionEnabled = collisionEnabled;

        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                cells[x + y * width] = new Cell();
            }
        }
    }

    public boolean isCollisionEnabled()
    {
        return collisionEnabled;
    }

    public void setCollisionEnabled(boolean collisionEnabled)
    {
        this.collisionEnabled = collisionEnabled;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public Tileset getTileset()
    {
        return tileset;
    }

    public void setTileset(Tileset tileset)
    {
        this.tileset = tileset;
    }

    public void changeWidth(int amount)
    {
        int oldWidth = width;
        width += amount;

        Cell[] newCells = new Cell[width * height];
        for(int i = 0; i < oldWidth && i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                newCells[i+j*width] = cells[i+j*oldWidth];
            }
        }

        for(int i = 0; i < newCells.length; i++)
        {
            if(newCells[i] == null)
                newCells[i] = new Cell();
        }

        cells = newCells;
    }

    public void changeHeight(int amount)
    {
        int oldHeight = height;
        height += amount;

        Cell[] newCells = new Cell[width * height];

        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < oldHeight && j < height; j++)
            {
                newCells[i+j*width] = cells[i+j*width];
            }
        }

        for(int i = 0; i < newCells.length; i++)
        {
            if(newCells[i] == null)
                newCells[i] = new Cell();
        }

        cells = newCells;
    }


    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCell(int x, int y)
    {
        return cells[x + y * width];
    }

    public Tile placeTile(Tile tile, int x, int y, int meta)
    {
        Cell c = getCell(x, y);
        Tile before = c.getTile();
        c.setTile(tile);
        c.setMeta(meta);
        return before;
    }


    public Tile placeTile(Tile tile, int x, int y)
    {
        return placeTile(tile, x, y, 0);
    }

    // TODO: optimize updating and rendering of tiles to only update and render the tiles in the viewport.

    public void update(float delta)
    {

    }

    public void render(SpriteBatch batch, OrthographicCamera cam)
    {
        if(visible)
        {
            delta += Gdx.graphics.getDeltaTime();

            int sx = (int) ((cam.position.x - cam.viewportWidth*cam.zoom / 2) / tileset.getTileWidth());
            int sy = height - (int) ((cam.position.y + cam.viewportHeight*cam.zoom / 2) / tileset.getTileHeight()) - 1;
            int ex = (int) ((cam.position.x + cam.viewportWidth*cam.zoom / 2) / tileset.getTileWidth()) + 1;
            int ey = (height - (int) ((cam.position.y - cam.viewportHeight*cam.zoom / 2) / tileset.getTileHeight()) - 1) + 1;

            for(int x = sx; x < ex; x++)
            {
                for(int y = sy; y < ey; y++)
                {
                    if(x >= 0 && x < width && y >= 0 && y < height)
                    {
                        Cell cell = getCell(x, y);
                        TextureRegion texture = cell.getFrame(delta, tileset);

                        if(texture != null)
                        {
                            batch.draw(texture, x * tileset.getTileWidth(), (height-y-1) * tileset.getTileHeight());
                        }
                    }
                }
            }
        }
    }

}
