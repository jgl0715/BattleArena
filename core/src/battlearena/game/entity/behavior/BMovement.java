package battlearena.game.entity.behavior;

import com.badlogic.gdx.math.Vector2;

import battlearena.common.entity.Entity;
import battlearena.common.entity.behavior.Behavior;
import battlearena.common.entity.data.DBody;
import battlearena.game.BattleArena;
import battlearena.game.input.Joystick;
import battlearena.game.states.StatePlay;

import com.badlogic.gdx.physics.box2d.Body;

public class BMovement extends Behavior
{

    private Body bod;
    private Joystick stickSource;

    public BMovement(String Name, Entity Parent)
    {
        super(Name, Parent);

        AddData(DBody.class, Entity.BODY);
        bod = Get(Body.class, Entity.BODY);

        this.stickSource = StatePlay.I.getStick();
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        bod.applyForceToCenter(stickSource.getJoystickInput().scl(3), true);
    }
}
