package battlearena.game.entity.behavior;

import com.badlogic.gdx.math.Vector2;

import battlearena.common.entity.Entity;
import battlearena.common.entity.behavior.Behavior;
import battlearena.common.entity.data.DBody;
import battlearena.common.entity.data.DFloat;
import battlearena.game.BattleArena;
import battlearena.game.input.Joystick;
import battlearena.game.states.StatePlay;

import com.badlogic.gdx.physics.box2d.Body;

public class BMovement extends Behavior
{

    private Body bod;
    private Joystick stickSource;

    private boolean dash;
    private float dashLength;

    public BMovement(String Name, Entity Parent)
    {
        super(Name, Parent);

        AddData(DBody.class, Entity.BODY);
        AddData(DFloat.class, Entity.ANIM_TIME);
        bod = Get(Body.class, Entity.BODY);

        dash = false;

        this.stickSource = StatePlay.I.getStick();
    }

    public void dash()
    {
        dash = true;
        dashLength = 0.1f;
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        Vector2 input = stickSource.getJoystickInput();

        if(input.len() > 0.01f)
        {
            if(dash)
            {
                bod.setLinearVelocity(stickSource.getJoystickInput().scl(50));
                dashLength -= delta;

                if(dashLength < 0)
                    dash = false;
            }
            else
            {
                bod.setLinearVelocity(stickSource.getJoystickInput().scl(15));
            }

            if(input.x < 0.0f)
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
            Set(Entity.ANIM_TIME, 0.0f);
            bod.setLinearVelocity(new Vector2(0,0));
        }

//        bod.applyLinearImpulse(stickSource.getJoystickInput().scl(5), bod.getLocalCenter(), true);
//        bod.applyForceToCenter(stickSource.getJoystickInput().scl(250), true);
    }
}
