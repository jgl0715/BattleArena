package battlearena.common;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by fores on 4/28/2019.
 */

public class AnimationUtil
{

    public static Animation<TextureRegion> MakeAnim(Texture Tex, int StartX, int StartY, int[] FrameWidths, int[] FrameHeights, int AnimWidth, float Dur)
    {
        Array<TextureRegion> Result = new Array<TextureRegion>();

        int frameIndex = 0;

        int ax = StartX;
        int ay = StartY;

        for (int x = 0; x < AnimWidth; x++, frameIndex++)
        {
            int FrameWidth = FrameWidths[frameIndex];
            int FrameHeight = FrameHeights[frameIndex];

            Result.add(new TextureRegion(Tex, ax, ay, FrameWidth, FrameHeight));

            ax += FrameWidth;
        }

        Animation<TextureRegion> anim = new Animation<TextureRegion>(Dur, Result);
        anim.setPlayMode(Animation.PlayMode.LOOP);

        return anim;
    }
}
