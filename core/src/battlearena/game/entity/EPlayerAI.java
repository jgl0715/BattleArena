package battlearena.game.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

import battlearena.common.entity.EntityConfig;
import battlearena.game.entity.behavior.BPlayerAI;
import battlearena.game.entity.behavior.ai.utils.Box2dLocation;
import battlearena.game.entity.behavior.ai.utils.Box2dSteeringUtils;
import battlearena.game.modes.BATeam;
import battlearena.game.modes.GameMode;

public class EPlayerAI extends EMob implements Location<Vector2>
{

    private int targetSlot;
    public static final int WANDER_THRESHOLD = 500;

    public EPlayerAI(EntityConfig Config)
    {
        super(Config);

        addBehavior(BPlayerAI.class, "AI");

        // TODO: Instead of picking a random target, pick a target based on criteria.
        targetSlot = (int)(Math.random() * GameMode.running.getSlots());
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        EMob closeMob = null;
        float closeDist = Float.MAX_VALUE;

        EMob[] other = GameMode.running.getOtherTeam(this);
        for(EMob m : other)
        {
            float dist = m.getPos().dst(getPos());

            if(dist < closeDist)
            {
                closeDist = dist;
                closeMob = m;
                closeDist = dist;
            }
        }

        if(closeDist >= WANDER_THRESHOLD)
        {
            targetSlot = -1;
        }
        else
        {
            targetSlot = GameMode.running.getPlayerSlot(closeMob);
        }



    }


    public EMob getTarget()
    {
        if(targetSlot < 0)
            return null;

        EMob target = GameMode.running.getPlayer(getTeam() == BATeam.BLUE ? BATeam.RED : BATeam.BLUE, targetSlot);

        if(target.isDead())
            return null;

        return target;
    }

    @Override
    public Vector2 getPosition()
    {
        return getTarget().getBody().getPosition();
    }

    @Override
    public float getOrientation()
    {
        return getTarget().getBody().getAngle();
    }

    @Override
    public void setOrientation(float orientation)
    {
        getTarget().getBody().setTransform(getPosition(), orientation);
    }

    @Override
    public float vectorToAngle(Vector2 vector)
    {
        return Box2dSteeringUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle)
    {
        return Box2dSteeringUtils.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation()
    {
        return new Box2dLocation();
    }
}
