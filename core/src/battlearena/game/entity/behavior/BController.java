package battlearena.game.entity.behavior;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import battlearena.common.entity.Entity;
import battlearena.common.entity.behavior.Behavior;
import battlearena.common.entity.data.DBody;
import battlearena.common.entity.data.DFloat;
import battlearena.game.entity.EMob;

/**
 * Created by fores on 4/28/2019.
 */

public class BController extends Behavior
{

    public static final float DASH_BOOST = 300.0f;

    private DFloat animTime;
    private DFloat speed;
    private Body bod;
    private boolean dash;
    private boolean steady;
    private float dashLength;
    private Vector2 direction;

    public BController(String Name, Entity Parent)
    {
        super(Name, Parent);

        animTime = Parent.find(DFloat.class, Entity.ANIM_TIME);
        speed = Parent.find(DFloat.class, EMob.DATA_SPEED);
        bod = Parent.find(DBody.class, Entity.BODY).Value;
        direction = new Vector2();
    }

    public void dash()
    {
        dash = true;
        dashLength = 0.2f;

        if(direction.len() <= .01f)
        {
            if(GetParent().getRenderSettings().FlipX)
            {
                direction = new Vector2(-1.0f, 0.0f);
            }
            else
            {
                direction = new Vector2(1.0f, 0.0f);
            }
        }
    }

    public boolean isDashing()
    {
        return dash;
    }

    public boolean isSteady() {
        return steady;
    }

    public void setDirection(Vector2 vel)
    {
        this.direction = vel;
    }

    public Vector2 getDirection()
    {
        return direction;
    }

    public void setSteady(boolean steady) {
        this.steady = steady;
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);


        if(dash)
        {
            bod.applyForceToCenter(new Vector2(direction).scl(DASH_BOOST), true);

            dashLength -= delta;

            if(dashLength < 0)
                dash = false;
        }

        if(steady)
        {
            bod.applyForceToCenter(new Vector2(direction).scl(speed.Value*3), true);
        }
        else
        {
            bod.applyForceToCenter(new Vector2(direction).scl(speed.Value*15), true);
        }

        if(bod.getLinearVelocity().x < 0.0f)
        {
            GetParent().getRenderSettings().FlipX = true;
        }
        else
        {
            GetParent().getRenderSettings().FlipX = false;
        }

        if(direction.len() <= 0.01f)
        {
            animTime.Value = 0.0f;
        }
    }
}
