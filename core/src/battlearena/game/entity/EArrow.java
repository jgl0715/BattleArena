package battlearena.game.entity;

import com.badlogic.gdx.math.Vector2;

import battlearena.common.AnimationUtil;
import battlearena.common.entity.ELight;
import battlearena.common.entity.EntityConfig;
import battlearena.game.Assets;
import battlearena.game.BAEntityFactory;
import battlearena.game.BattleArena;
import battlearena.game.LayerType;
import battlearena.game.states.StatePlay;

public class EArrow extends EProjectile
{

    private ELight light;
    private Vector2 lightPos;

    public EArrow(EntityConfig Config)
    {
        super(Config);

        projectileAnim.Value = AnimationUtil.MakeAnim(BattleArena.I.getTexture(Assets.TEXTURE_PROJECTILES), 0, 0, new int[]{43}, new int[]{20}, 1, 0.0f);

        if(Config.GetConfigNumber("VelX") < 0)
            getRenderSettings().FlipX = true;

        light = BAEntityFactory.createLight(Config.getWorld(), Config.GetConfigNumber("X"), Config.GetConfigNumber("Y"), 0.0f, 0.2f, 0.0f, 10.0f, 1.0f, false);
        lightPos = light.getPos();
        Config.getWorld().addEntity(LayerType.LIGHTS.getName(), light);
    }

    @Override
    public EParticle createDestructionParticle(Vector2 contactPoint, float projectileSpeed)
    {
        return BAEntityFactory.CreateDestructionParticle_Arrow(getWorld(), contactPoint.x, contactPoint.y, projectileSpeed *0.1f, projectileSpeed);
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        // Update light position based on projectile position.
        lightPos.set(getPos());

        // Create streamers
        for(int i = 0; i < 3; i++)
        {
            Vector2 pos = getPos();
            float projectileSpeed = -getBody().getLinearVelocity().len();
            EParticle particle = BAEntityFactory.CreateDestructionParticle_Arrow(getWorld(), pos.x, pos.y, 3.0f, 3.0f);
            getWorld().addEntity(StatePlay.MOBS_LAYER, particle);
        }
    }

    @Override
    public void remove()
    {
        super.remove();

        light.remove();
    }
}
