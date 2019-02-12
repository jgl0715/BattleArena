package battlearena.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;

public class BattleArena extends ApplicationAdapter
{
	
	private ShapeRenderer sr;

	@Override
	public void create ()
	{
		sr = new ShapeRenderer();

		Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureAdapter()
		{
			@Override
			public boolean tap(float x, float y, int count, int button)
			{
				if(count == 2)
				{
					color = (color + 1) % colors.length;
				}

				return true;
			}
		}
		));
	}

	@Override
	public void dispose ()
	{
		sr.dispose();
	}

	public Color[] colors = {Color.RED, Color.BLUE, Color.GREEN};
	public int color = 0;

	@Override
	public void render ()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sr.setColor(colors[color]);
		sr.begin(ShapeRenderer.ShapeType.Filled);
		{
			sr.circle(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 20.0f);
		}
		sr.end();

	}
}
