package battlearena.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import battlearena.common.gui.HUD;
import battlearena.game.BattleArena;
import battlearena.game.entity.EPlayer;
import battlearena.game.input.Button;
import battlearena.game.input.Joystick;

public class HUDGame extends HUD
{

    private Joystick stick;
    private Button aButton;
    private Button bButton;
    private float healthPercentage;
    private float cooldownPercentage;

    public HUDGame(Skin skin)
    {
        super(skin, BattleArena.VIRTUAL_WIDTH, BattleArena.VIRTUAL_HEIGHT);
    }

    public void setStick(Joystick stick)
    {
        this.stick = stick;
    }

    public void setAButton(Button aButton)
    {
        this.aButton = aButton;
    }

    public void setBButton(Button bButton)
    {
        this.bButton = bButton;
    }

    public void setHealthPercentage(float healthPercentage)
    {
        this.healthPercentage = healthPercentage;
    }

    public void setCooldownPercentage(float cooldownPercentage)
    {
        this.cooldownPercentage = cooldownPercentage;
    }

    @Override
    public void render()
    {
        super.render();

        ShapeRenderer sr = BattleArena.I.getShapeRenderer();
        SpriteBatch batch = (SpriteBatch) ui.getBatch();

        sr.setTransformMatrix(batch.getTransformMatrix());
        sr.setProjectionMatrix(batch.getProjectionMatrix());

        // Render input UI
        stick.render(sr);
        aButton.render(batch);
        bButton.render(batch);

        // Render HUD

        // Health bar
        int hbWidth = 200;
        int hbHeight = 20;
        int hbX = (int) 10;
        int hbY = (int) (BattleArena.VIRTUAL_HEIGHT - hbHeight-  10);



        sr.begin(ShapeRenderer.ShapeType.Filled);
        {

            sr.setColor(Color.RED);
            sr.rect(hbX, hbY, hbWidth, hbHeight);

            sr.setColor(Color.GREEN);
            sr.rect(hbX, hbY, (int)(hbWidth * healthPercentage), hbHeight);
        }
        sr.end();

        Gdx.gl20.glLineWidth(2.0f);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.DARK_GRAY);
        sr.rect(hbX, hbY, hbWidth, hbHeight);
        sr.end();
    }
}
