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

    public TileLayer(String name, int width, int height)
    {
        this.name = name;
        this.width = width;
        this.height = height;
        this.cells = new Cell[width * height];
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
        getCell(x, y).t
    }

    public void render(SpriteBatch batch, float delta, Tileset set)
    {
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                Cell cell = getCell(x, y);
                TextureRegion texture = cell.getFrame(delta, set);

                batch.draw(texture, x * set.getTilesheetWidth(), y * set.getTilesheetHeight());
            }
        }
    }

}
