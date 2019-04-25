package battlearena.common.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import battlearena.common.RenderSettings;
import battlearena.common.entity.behavior.BPhysics;
import battlearena.common.entity.data.DBody;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DTextureRegion;
import battlearena.common.entity.data.DVector2;
import battlearena.game.BattleArena;

public class EBox extends Entity
{

	private Vector2 Pos;
	private Vector2 Size;
	private DTextureRegion Frame;
	private DFloat Rot;

	public EBox(EntityConfig Config)
	{
		super(Config);

		Pos = addData(DVector2.class, POSITION, true).Value;
		Size = addData(DVector2.class, SIZE, false).Value;
		Rot = addData(DFloat.class, ROTATION, true);
		Frame = addData(DTextureRegion.class, FRAME);
		addData(DBody.class, BODY);

		if (Config.HasItem("Width"))
			Size.x = Config.GetConfigNumber("Width");
		if (Config.HasItem("Height"))
			Size.y = Config.GetConfigNumber("Height");

		// Add a physics component
		addBehavior(BPhysics.class, "Physics");

		renderSettings.visible = true;
		renderSettings.mode = RenderSettings.RenderMode.TEXTURED;
	}

	public Vector2 GetPosition()
	{
		return Pos;
	}

	public Vector2 GetSize()
	{
		return Size;
	}

	@Override
	public void Update(float delta)
	{
		super.Update(delta);
	}

	@Override
	public void Render(SpriteBatch batch)
	{
		super.Render(batch);

		if (renderSettings.visible)
		{
			switch (renderSettings.mode)
			{
			case TEXTURED:

				float Wid = Size.x;
				float Hei = Size.y;
				float MidX = Wid / 2;
				float MidY = Hei / 2;

				batch.draw(Frame.Value, Pos.x - MidX, Pos.y - MidY, MidX, MidY, Wid, Hei, renderSettings.FlipX ? -1.0f : 1.0f, 1.0f, Rot.Value);

				break;
			case FRAME:

				// Use default entity texture scaled up here.
				 ShapeRenderer sr = BattleArena.I.getShapeRenderer();
				Vector2 position = find(DVector2.class, POSITION).Value;
				 Vector2 size = find(DVector2.class, SIZE).Value;
				 float rotation = find(DFloat.class, ROTATION).Value;
				 float width = size.x;
				 float height = size.y;

				 sr.setColor(Color.WHITE);
				 sr.begin(ShapeRenderer.ShapeType.Line);
				 sr.rect(position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, 1.0f, 1.0f, rotation);
				 sr.end();
					break;
			default:
				break;
			}
		}

	}

}
