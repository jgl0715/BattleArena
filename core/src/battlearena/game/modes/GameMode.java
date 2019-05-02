package battlearena.game.modes;

import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;
import java.util.Set;

import battlearena.common.entity.Entity;
import battlearena.common.gui.HUD;
import battlearena.common.world.EntityListener;
import battlearena.common.world.Location;
import battlearena.common.world.TiledWorld;
import battlearena.game.BAEntityFactory;
import battlearena.game.LayerType;
import battlearena.game.entity.BACharacter;
import battlearena.game.entity.EMob;
import battlearena.game.entity.EPlayer;

public abstract class GameMode
{

    public static final BATeam PLAYER_TEAM = BATeam.RED;
    public static final int PLAYER_SLOT = 0;
    public static final int RED_SPAWNS = 1;
    public static final int BLUE_SPAWNS = 2;

    protected HUD hud;
    protected EPlayer player;
    protected TiledWorld world;
    protected BACharacter[] blueTeamCharacters;
    protected BACharacter[] redTeamCharacters;
    protected EMob[] blueTeam;
    protected EMob[] redTeam;
    protected Set<Location> redSpawns;
    protected Set<Location> blueSpawns;
    protected int slots;

    public GameMode()
    {

    }

    public void setHud(HUD hud)
    {g
        this.hud = hud;
    }

    public void setPlayer(EPlayer player)
    {
        this.player = player;
    }

    public void setWorld(TiledWorld world)
    {
        this.world = world;

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
                        respawn(BATeam.BLUE, slot);
                }

                for(int slot = 0; slot < slots; slot++)
                {
                    EMob redChar = redTeam[slot];
                    if(e == redChar)
                        respawn(BATeam.RED, slot);
                }

            }
        });
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

    public HUD getHud()
    {
        return hud;
    }

    public TiledWorld getWorld()
    {
        return world;
    }

    public Location getRedSpawn()
    {
        int index = (int) (Math.random() * redSpawns.size());
        int i = 0;
        Iterator<Location> itr = redSpawns.iterator();
        while(itr.hasNext())
        {
            Location next = itr.next();

            if(i == index)
                return next;
            i++;
        }
        return null;
    }

    public Location getBlueSpawn()
    {
        int index = (int) (Math.random() * blueSpawns.size());
        int i = 0;
        System.out.println(index);
        Iterator<Location> itr = blueSpawns.iterator();
        while(itr.hasNext())
        {
            Location next = itr.next();

            if(i == index)
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
            mob = blueTeam[slot];

            spawn = locToEnt(getBlueSpawn());
            character = blueTeamCharacters[slot];
            t = blueTeam;
        }
        else if(team == BATeam.RED)
        {
            mob = redTeam[slot];
            spawn = locToEnt(getRedSpawn());
            character = redTeamCharacters[slot];
            t = redTeam;
        }
        if(mob == null)
        {

            // Spawn mob
            if(team == PLAYER_TEAM && slot == PLAYER_SLOT)
            {
                player = BAEntityFactory.CreatePlayer(world, spawn.x, spawn.y, character);
                mob = player;
            }
            else
            {
                mob = BAEntityFactory.CreateEnemy(world, spawn.x, spawn.y, character);
            }
        }

        t[slot] = mob;
        world.getEntityLayer(LayerType.MOBS.getName()).addEntity(mob);
    }

    public void startMatch()
    {
        for(int i = 0; i < slots; i++)
        {
            // Respawn (spawn) in all players.
            respawn(BATeam.BLUE, i);
            respawn(BATeam.RED, i);

        }
    }

    public abstract void endMatch();

}
