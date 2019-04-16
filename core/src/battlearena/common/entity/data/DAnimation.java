package battlearena.common.entity.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import battlearena.common.entity.Entity;

public class DAnimation extends Data
{

	public Animation<TextureRegion> Value;

	public DAnimation(String Name, Entity Parent)
	{
		super(Name, Parent, false);
	}

	public void Make(Texture Tex, int StartX, int StartY, int FrameWidth, int FrameHeight, int AnimWidth, int AnimHeight, float Dur)
	{
		Array<TextureRegion> Result = new Array<TextureRegion>();

		for (int x = 0; x < AnimWidth; x++)
		{
			for (int y = 0; y < AnimHeight; y++)
			{
				int AX = StartX + FrameWidth * x;
				int AY = StartY + FrameHeight * y;

				Result.add(new TextureRegion(Tex, AX, AY, FrameWidth, FrameHeight));
			}
		}

		Value = new Animation<TextureRegion>(Dur, Result);
		Value.setPlayMode(PlayMode.LOOP);
	}

}
