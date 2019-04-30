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

public class EBullet extends EProjectile
{

    private ELight light;
    private Vector2 lightPos;

    public EBullet(EntityConfig Config)
    {
        super(Config);

        projectileAnim.Value = AnimationUtil.MakeAnim(BattleArena.I.getTexture(Assets.TEXTURE_PROJECTILES), 53, 1, new int[]{6}, new int[]{6}, 1, 0.0f);

        if(Config.GetConfigNumber("VelX") < 0)
            getRenderSettings().FlipX = true;

        light = BAEntityFactory.createLight(Config.getWorld(), Config.GetConfigNumber("X"), Config.GetConfigNumber("Y"), 0.0f, 0.2f, 0.0f, 10.0f, 1.0f);
        lightPos = light.getPos();
        Config.getWorld().addEntity(LayerType.LIGHTS.getName(), light);
    }

    @Override
    public EParticle createDestructionParticle(Vector2 contactPoint, float projectileSpeed)
    {
        return BAEntityFactory.CreateDestructionParticle_Bullet(getWorld(), contactPoint.x, contactPoint.y, projectileSpeed *0.1f, projectileSpeed);
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        // Update light position based on projectile position.
        lightPos.set(getPos());
    }

    @Override
    public void remove()
    {
        super.remove();

        light.remove();
    }
}
