package battlearena.game.modes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import java.util.Iterator;
import java.util.Set;

import battlearena.common.entity.Entity;
import battlearena.common.gui.HUD;
import battlearena.common.world.EntityListener;
import battlearena.common.world.Location;
import battlearena.common.world.TiledWorld;
import battlearena.common.world.World;
import battlearena.game.Assets;
import battlearena.game.BAEntityFactory;
import battlearena.game.BattleArena;
import battlearena.game.LayerType;
import battlearena.game.entity.BACharacter;
import battlearena.game.entity.EMob;
import battlearena.game.entity.EPlayer;
import battlearena.game.input.Button;
import battlearena.game.input.Joystick;
import battlearena.game.ui.HUDGame;

public abstract class GameMode
{

    public static GameMode running;

    public static final BATeam PLAYER_TEAM = BATeam.RED;
    public static final int PLAYER_SLOT = 0;
    public static final int RED_SPAWNS = 2;
    public static final int BLUE_SPAWNS = 1;

    protected HUDGame hud;
    protected EPlayer player;
    protected TiledWorld world;
    protected BACharacter[] blueTeamCharacters;
    protected BACharacter[] redTeamCharacters;
    protected EMob[] blueTeam;
    protected EMob[] redTeam;
    protected Set<Location> redSpawns;
    protected Set<Location> blueSpawns;
    protected int slots;
    private Box2DDebugRenderer dbgr;

    private Joystick stick;
    private Button buttonA;
    private Button buttonB;

    public GameMode()
    {

    }

    public int getSlots() {
        return slots;
    }

    public EMob[] getBlueTeam() {
        return blueTeam;
    }

    public EMob[] getRedTeam() {
        return redTeam;
    }

    public EMob[] getOtherTeam(EMob mob)
    {
        if(mob.getTeam() == BATeam.BLUE)
            return redTeam;
        else
            return blueTeam;
    }

    public int getPlayerSlot(EMob mob)
    {
        if(mob.getTeam() == BATeam.BLUE)
        {
            for(int i = 0; i < slots; i++)
                if(blueTeam[i] == mob)
                    return i;
        }
        else
        {
            for(int i = 0; i < slots; i++)
                if(redTeam[i] == mob)
                    return i;
        }

        return -1;
    }

    public void setHud(HUDGame hud)
    {
        this.hud = hud;
    }

    public void setPlayer(EPlayer player)
    {
        this.player = player;
    }

    public void setWorld(TiledWorld world)
    {
        this.world = world;

        dbgr = new Box2DDebugRenderer();

        redSpawns = world.findLocationsMatchingMeta(RED_SPAWNS);
        blueSpawns = world.findLocationsMatchingMeta(BLUE_SPAWNS);

        world.getEntityLayer(LayerType.MOBS.getName()).addEntityListener(new EntityListener()
        {
            @Override
            public void onEntityAdd(Entity e)
            {

            }

            @Override
            public void onEntityRemove(Entity e)
            {
                // Respawn entity

                for(int slot = 0; slot < slots; slot++)
                {
                    EMob blueChar = blueTeam[slot];
                    if(e == blueChar)
                    {
                        respawn(BATeam.BLUE, slot);
                    }
                }

                for(int slot = 0; slot < slots; slot++)
                {

                    EMob redChar = redTeam[slot];
                    if(e == redChar)
                    {
                        respawn(BATeam.RED, slot);
                    }
                }

            }
        });
    }

    public EMob getPlayer(BATeam team, int slot)
    {
        if(team == BATeam.BLUE)
            return blueTeam[slot];
        else if(team == BATeam.RED)
            return redTeam[slot];
        return null;
    }

    public void setTeams(BACharacter[] blueTeam, BACharacter[] redTeam, int slots)
    {
        this.blueTeamCharacters = blueTeam;
        this.redTeamCharacters = redTeam;
        this.slots = slots;

        this.blueTeam = new EMob[slots];
        this.redTeam = new EMob[slots];
    }

    public EPlayer getPlayer()
    {
        return player;
    }

    public HUDGame getHud()
    {
        return hud;
    }

    public TiledWorld getWorld()
    {
        return world;
    }

    public Location getRedSpawn(int slot)
    {
        int i = 0;
        Iterator<Location> itr = redSpawns.iterator();
        while(itr.hasNext())
        {
            Location next = itr.next();

            if(i == slot)
                return next;
            i++;
        }
        return null;
    }

    public Location getBlueSpawn(int slot)
    {
        int i = 0;
        Iterator<Location> itr = blueSpawns.iterator();
        while(itr.hasNext())
        {
            Location next = itr.next();

            if(i == slot)
                return next;
            i++;
        }
        return null;
    }


    public Vector2 locToEnt(Location loc)
    {
        return new Vector2(loc.getTileX() * world.getTileWidth(), world.getPixelHeight()-loc.getTileY()*world.getTileHeight()-1);
    }

    public void respawn(BATeam team, int slot)
    {
        EMob mob = null;
        Vector2 spawn = null;
        BACharacter character = null;
        EMob[] t = null;

        if(team == BATeam.BLUE)
        {
            spawn = locToEnt(getBlueSpawn(slot));
            character = blueTeamCharacters[slot];
            t = blueTeam;
        }
        else if(team == BATeam.RED)
        {
            spawn = locToEnt(getRedSpawn(slot));
            character = redTeamCharacters[slot];
            t = redTeam;
        }

        // Spawn mob
        if(team == PLAYER_TEAM && slot == PLAYER_SLOT)
        {
            player = BAEntityFactory.CreatePlayer(world, spawn.x, spawn.y, character);
            player.initInput(stick, buttonA, buttonB);
            mob = player;
        }
        else
        {
            mob = BAEntityFactory.CreateEnemy(world, spawn.x, spawn.y, character);
        }

        t[slot] = mob;
        mob.setTeam(team);
        world.getEntityLayer(LayerType.MOBS.getName()).addEntity(mob);
    }

    public void startMatch(InputMultiplexer muxer)
    {

        BattleArena.I.getMusic(Assets.MUSIC_BACKGROUND_MUSIC).play();
        BattleArena.I.getMusic(Assets.MUSIC_BACKGROUND_MUSIC).setLooping(true);

        OrthographicCamera uiCamera = hud.getCamera();
        stick = new Joystick(75, 75, muxer, uiCamera);
        buttonB = new Button(600, 100, new TextureRegion(BattleArena.I.getTexture(Assets.TEXTURE_BUTTONS), 50, 50, 50, 50), new TextureRegion(BattleArena.I.getTexture(Assets.TEXTURE_BUTTONS), 0, 50, 50, 50), muxer, uiCamera);
        buttonA = new Button(500, 50, new TextureRegion(BattleArena.I.getTexture(Assets.TEXTURE_BUTTONS), 50, 0, 50, 50), new TextureRegion(BattleArena.I.getTexture(Assets.TEXTURE_BUTTONS), 0, 0, 50, 50), muxer, uiCamera);

        hud.setStick(stick);
        hud.setAButton(buttonA);
        hud.setBButton(buttonB);

        running = this;

        for(int i = 0; i < slots; i++)
        {
            // Respawn (spawn) in all players.
            respawn(BATeam.BLUE, i);
            respawn(BATeam.RED, i);
        }

        player.initInput(stick, buttonA, buttonB);
    }

    public void endMatch()
    {
        running = null;

        BattleArena.I.getMusic(Assets.MUSIC_BACKGROUND_MUSIC).stop();
    }

    public void update(float delta)
    {
        world.update(Gdx.graphics.getDeltaTime());
    }

    public void renderWorldHealthBar(EMob mob, BATeam team, ShapeRenderer sr)
    {
        float percent = mob.getHealthPercentage();
        int hbWidth = 50;
        int hbHeight = 6;
        float mobX = mob.getPos().x;
        float mobY = mob.getPos().y;
        float hbX = mobX - (hbWidth ) / 2;
        float hbY = mobY + (mob.getHitboxSize().y);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        if(team == BATeam.BLUE)
            sr.setColor(Color.BLUE);
        else if(team == BATeam.RED)
            sr.setColor(Color.RED);

        sr.rect(hbX-4, hbY-4, hbWidth+8, hbHeight+8);

        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.RED);
        sr.rect(hbX, hbY, hbWidth, hbHeight);
        sr.setColor(Color.GREEN);
        sr.rect(hbX, hbY, (int)(hbWidth * percent), hbHeight);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.BLACK);
        sr.rect(hbX, hbY, hbWidth, hbHeight);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.BLACK);
        sr.rect(hbX-4, hbY-4, hbWidth+8, hbHeight+8);
        sr.end();



    }

    public void render(SpriteBatch batch, ShapeRenderer sr, OrthographicCamera camera)
    {

        OrthographicCamera uiCamera = hud.getCamera();

        sr.setProjectionMatrix(camera.projection);
        sr.setTransformMatrix(camera.view);

        world.render(batch, camera);

        for(EMob mob : blueTeam)
        {
            if(mob != player)
                renderWorldHealthBar(mob, BATeam.BLUE, sr);
        }

        for(EMob mob : redTeam)
        {
            if(mob != player)
                renderWorldHealthBar(mob, BATeam.RED, sr);
        }

//
//        Matrix4 mat = new Matrix4(camera.combined);
//        mat.scale(World.PIXELS_PER_METER, World.PIXELS_PER_METER, 1);
//        dbgr.render(world.getPhysicsWorld(), mat);

        sr.setProjectionMatrix(uiCamera.projection);
        sr.setTransformMatrix(uiCamera.view);
        batch.setProjectionMatrix(uiCamera.projection);
        batch.setTransformMatrix(uiCamera.view);

        int wx = BattleArena.VIRTUAL_WIDTH - 120;
        int wy = BattleArena.VIRTUAL_HEIGHT - 120;

        batch.begin();
        world.getLayer("Background").render(batch, uiCamera, 2.0f,wx,wy);
        world.getLayer("Foreground").render(batch, uiCamera, 2.0f,wx,wy);
        batch.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);

        Iterator<Entity> mobs = world.getEntityLayer(LayerType.MOBS.getName()).iterator();

        while(mobs.hasNext())
        {
            Entity e = mobs.next();
            if(e instanceof EMob)
            {
                EMob mob = (EMob) e;
                Vector2 pos = new Vector2(mob.getPos()).scl(2.0f / TiledWorld.TILE_SIZE);
                if(mob.getTeam() == BATeam.BLUE)
                {
                    sr.setColor(Color.BLUE);
                }
                else
                {
                    sr.setColor(Color.RED);
                }

                sr.circle(wx + pos.x, wy + pos.y, 2.0f);
            }
        }
        sr.end();

        hud.setHealthPercentage(player.getHealthPercentage());
        hud.render();
    }

}
