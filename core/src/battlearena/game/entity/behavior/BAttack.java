package battlearena.game.entity.behavior;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import battlearena.common.entity.ELight;
import battlearena.common.entity.Entity;
import battlearena.common.entity.behavior.Behavior;
import battlearena.common.entity.data.DAnimation;
import battlearena.common.entity.data.DBody;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DPointLight;
import battlearena.common.entity.data.DString;
import battlearena.common.entity.data.DVector2;
import battlearena.game.entity.BACharacter;
import battlearena.game.entity.EMob;
import battlearena.game.entity.EPlayer;
import box2dLight.PointLight;

public abstract class BAttack extends Behavior
{

    private Body bod;
    private Vector2 pos;
    private DString anim;
    private DFloat cooldown;
    private BACharacter type;
    private BController controller;
    private ELight light;

    private boolean charging;

    public BAttack(String Name, Entity Parent)
    {
        super(Name, Parent);

        bod = ((EMob) Parent).getHitbox();
        pos = GetParent().find(DVector2.class, Entity.POSITION).Value;
        anim = GetParent().find(DString.class, Entity.ANIM);
        cooldown = GetParent().find(DFloat.class, EPlayer.DATA_COOLDOWN);
        controller = GetParent().find(BController.class, "PlayerMovement");
    }

    public Body getBody()
    {
        return bod;
    }

    public Vector2 getPos() {
        return pos;
    }

    public DString getAnim()
    {
        return anim;
    }

    public BController getController()
    {
        return controller;
    }

    public DFloat getCooldown()
    {
        return cooldown;
    }

    public BACharacter getType()
    {
        return type;
    }

    public ELight getLight() {
        return light;
    }

    public void setLight(ELight light) {
        this.light = light;
    }

    public void setType(BACharacter type)
    {
        this.type = type;
    }

    public boolean canAttack()
    {
        if(cooldown.Value > 0.01f)
            return false;
        else
            return true;

    }

    public boolean isCharging()
    {
        return charging;
    }

    public void charge()
    {
        charging = true;
    }

    public void attack()
    {
        charging = false;
    }


    @Override
    public void Update(float delta)
    {
        super.Update(delta);

    }
}
