package battlearena.common.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import battlearena.common.tile.Tile;
import battlearena.common.tile.Tileset;

public class TileLayer
{

    private String name;
    private int width;
    private int height;
    private Cell[] cells;
    private boolean visible;

    public TileLayer(String name, int width, int height)
    {
        this.name = name;
        this.width = width;
        this.height = height;
        this.cells = new Cell[width * height];
        this.visible = true;

        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                cells[x + y * width] = new Cell();
            }
        }
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
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

    public void placeTile(Tile tile, int x, int y)
    {
        getCell(x, y).setTile(tile);
    }

    public void render(SpriteBatch batch, float delta, Tileset set)
    {
        if(visible)
        {
            for(int x = 0; x < width; x++)
            {
                for(int y = 0; y < height; y++)
                {
                    Cell cell = getCell(x, y);
                    TextureRegion texture = cell.getFrame(delta, set);

                    if(texture != null)
                    {
                        batch.draw(texture, x * set.getTileWidth(), (height-y-1) * set.getTileHeight());
                    }
                }
            }
        }
    }

}
