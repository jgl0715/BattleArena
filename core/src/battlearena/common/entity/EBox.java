package battlearena.common.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

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
	private Body bod;
	private DTextureRegion Frame;
	private DFloat Rot;

	public EBox(EntityConfig Config)
	{
		super(Config);

		Pos = addData(DVector2.class, POSITION, true).Value;
		Size = addData(DVector2.class, SIZE, false).Value;
		Rot = addData(DFloat.class, ROTATION, true);
		Frame = addData(DTextureRegion.class, FRAME);
		bod = addData(DBody.class, BODY).Value;

		if (Config.HasItem("NavboxWidth"))
			Size.x = Config.GetConfigNumber("NavboxWidth");
		if (Config.HasItem("NavboxHeight"))
			Size.y = Config.GetConfigNumber("NavboxHeight");

		// Add a physics component
		addBehavior(BPhysics.class, "Physics");

		renderSettings.visible = true;
		renderSettings.mode = RenderSettings.RenderMode.TEXTURED;
	}

	public Body getBody() {
		return bod;
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

		setZ((int) -GetPosition().y);
	}

	@Override
	public void Render(SpriteBatch batch, OrthographicCamera cam, RenderSettings.RenderMode filter)
	{
		super.Render(batch, cam, filter);

		if (renderSettings.visible && filter == renderSettings.mode)
		{
			switch (renderSettings.mode)
			{
			case TEXTURED:

				float Wid = Size.x;
				float Hei = Size.y;
				float MidX = Wid / 2;
				float MidY = Hei / 2;

				TextureRegion reg = Frame.Value;


				//batch.setColor(Color.RED);
				batch.draw(reg, Pos.x - MidX, Pos.y - MidY, MidX, MidY, reg.getRegionWidth(), reg.getRegionHeight(), renderSettings.FlipX ? -1.0f : 1.0f, 1.0f, Rot.Value);
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
