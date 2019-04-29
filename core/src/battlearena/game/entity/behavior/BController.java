package battlearena.game.entity.behavior;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import battlearena.common.entity.Entity;
import battlearena.common.entity.behavior.Behavior;
import battlearena.common.entity.data.DBody;
import battlearena.common.entity.data.DFloat;

/**
 * Created by fores on 4/28/2019.
 */

public class BController extends Behavior
{

    private DFloat animTime;
    private Body bod;
    private boolean dash;
    private float dashLength;
    private Vector2 direction;

    public BController(String Name, Entity Parent)
    {
        super(Name, Parent);

        animTime = Parent.find(DFloat.class, Entity.ANIM_TIME);
        bod = Parent.find(DBody.class, Entity.BODY).Value;
        direction = new Vector2();
    }

    public void dash()
    {
        dash = true;
        dashLength = 0.1f;
    }

    public boolean isDashing()
    {
        return dash;
    }

    public void setDirection(Vector2 vel)
    {
        this.direction = vel;
    }

    public Vector2 getDirection()
    {
        return direction;
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        if(direction.len() > 0.01f)
        {
            if(dash)
            {
                bod.setLinearVelocity(new Vector2(direction).scl(50));
                dashLength -= delta;

                if(dashLength < 0)
                    dash = false;
            }
            else
            {
                bod.setLinearVelocity(direction.scl(15));
            }

            if(direction.x < 0.0f)
            {
                GetParent().getRenderSettings().FlipX = true;
            }
            else
            {
                GetParent().getRenderSettings().FlipX = false;
            }
        }
        else
        {
            animTime.Value = 0.0f;
            bod.setLinearVelocity(new Vector2(0, 0));
        }
    }
}
