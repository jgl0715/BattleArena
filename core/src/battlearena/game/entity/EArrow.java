package battlearena.game.entity;

import battlearena.common.AnimationUtil;
import battlearena.common.entity.EntityConfig;
import battlearena.game.Assets;
import battlearena.game.BattleArena;

public class EArrow extends EProjectile
{
    public EArrow(EntityConfig Config)
    {
        super(Config);

        projectileAnim.Value = AnimationUtil.MakeAnim(BattleArena.I.getTexture(Assets.TEXTURE_PROJECTILES), 0, 0, new int[]{43}, new int[]{20}, 1, 0.0f)
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);
    }
}
