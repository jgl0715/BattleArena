package battlearena.game.modes;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import battlearena.common.entity.Entity;
import battlearena.common.world.EntityListener;
import battlearena.game.Assets;
import battlearena.game.BattleArena;
import battlearena.game.LayerType;
import battlearena.game.entity.EMob;
import battlearena.game.ui.HUDGame;

public class GMTeamDeathmatch extends GameMode
{

    public static final int MATCH_MINUTES = 0;
    public static final int MATCH_SECONDS = 30;

    private int redScore;
    private int blueScore;
    private float time;
    private BitmapFont font;

    public GMTeamDeathmatch()
    {
        hud = new HUDGame(BattleArena.I.getSkin());
        font = hud.getSkin().getFont("font");

        redScore = 0;
        blueScore = 0;
        time = 0.0f;

    }

    public int getRedScore() {
        return redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    @Override
    public void startMatch(InputMultiplexer muxer)
    {
        super.startMatch(muxer);

        // Three minutes
        time = MATCH_MINUTES * 60 + MATCH_SECONDS;

        world.getEntityLayer(LayerType.MOBS.getName()).addEntityListener(new EntityListener()
        {
            @Override
            public void onEntityAdd(Entity e)
            {

            }

            @Override
            public void onEntityRemove(Entity e)
            {
                if(e instanceof EMob)
                {
                    EMob mob = (EMob) e;

                    if(mob.getTeam() == BATeam.BLUE)
                        redScore++;
                    if(mob.getTeam() == BATeam.RED)
                        blueScore++;

                }
            }
        });

    }

    @Override
    public void endMatch()
    {
        super.endMatch();

        if(redScore > blueScore)
        {

            BattleArena.I.playSound(Assets.AUDIO_YOU_WIN);
        }
        else if(redScore < blueScore)
        {

            BattleArena.I.playSound(Assets.AUDIO_YOU_LOSE);
        }

        BattleArena.I.inputToFSA(BattleArena.TRANSITION_FINISH, this);
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);

        time -= delta;

        if(time <= 0)
        {
            endMatch();
        }
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer sr, OrthographicCamera camera)
    {
        super.render(batch, sr, camera);

        int x = BattleArena.VIRTUAL_WIDTH - 100;
        int y = 200;

        int minutes = (int) ((time) / 60);
        int seconds = ((int)time) % 60;

        batch.begin();
        font.draw(batch, String.format("%02d:%02d", minutes, seconds), BattleArena.VIRTUAL_WIDTH / 2 - 30, BattleArena.VIRTUAL_HEIGHT - 10);
        font.setColor(Color.RED);
        font.draw(batch, String.format("Red: %d", redScore), x+5, y+20);
        font.setColor(Color.BLUE);
        font.draw(batch, String.format("Blue: %d", blueScore), x, y+40);
        font.setColor(Color.WHITE);

        batch.end();

    }
}
