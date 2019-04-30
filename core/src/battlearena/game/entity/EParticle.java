package battlearena.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Stack;

import battlearena.common.entity.EBox;
import battlearena.common.entity.Entity;
import battlearena.common.entity.EntityConfig;
import battlearena.common.entity.data.DAnimation;
import battlearena.common.entity.data.DFloat;
import battlearena.common.entity.data.DString;

public class EParticle extends EBox
{

    public static final String DATA_PARTICLE_FRAME = "ParticleFrame";

    private float life;


    public EParticle(EntityConfig Config)
    {
        super(Config);

        float randAngle = (float) (Math.random() * Math.PI * 2);
        float minSpeed = Config.GetConfigFloat("MinSpeed");
        float maxSpeed = Config.GetConfigFloat("MaxSpeed");
        float minLife = Config.GetConfigFloat("MinLife");
        float maxLife = Config.GetConfigFloat("MaxLife");

        Vector2 dir = new Vector2((float) Math.cos(randAngle), (float)Math.sin(randAngle));
        Vector2 vel = new Vector2(dir).scl((float)(Math.random() * (maxSpeed - minSpeed) + minSpeed));

        life = (float) (Math.random() * (maxLife - minLife) + minLife);
        getBody().setLinearVelocity(vel);

        getFrame().Value = Config.GetConfigItem(TextureRegion.class, Entity.FRAME);
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        life -= delta;

        if(life <= 0)
        {
            remove();
        }
    }
}
