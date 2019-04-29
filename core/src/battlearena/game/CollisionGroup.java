package battlearena.game;

public class CollisionGroup
{

	public static final short TILES_GROUP = 0x0001;
	public static final short PLAYER_GROUP = 0x0004;
	public static final short ENTITIES_GROUP = 0x0008;
	public static final short LIGHTS_GROUP = 0x0020;
	public static final short WEAPON_GROUP = 0x0040;
	public static final short HITBOX_GROUP = 0x0080;

	public static final CollisionGroup PLAYER = new CollisionGroup(PLAYER_GROUP, (short) (TILES_GROUP | LIGHTS_GROUP | WEAPON_GROUP));
	public static final CollisionGroup LIGHTS = new CollisionGroup(LIGHTS_GROUP, (short) (TILES_GROUP));
	public static final CollisionGroup TILES = new CollisionGroup(TILES_GROUP, (short) (PLAYER_GROUP | ENTITIES_GROUP | LIGHTS_GROUP));
	public static final CollisionGroup ENTITIES = new CollisionGroup(ENTITIES_GROUP, (short) (TILES_GROUP | LIGHTS_GROUP | WEAPON_GROUP));
	public static final CollisionGroup WEAPON = new CollisionGroup(WEAPON_GROUP, (short) (TILES_GROUP | LIGHTS_GROUP | HITBOX_GROUP));
	public static final CollisionGroup HITBOX = new CollisionGroup(HITBOX_GROUP, (short) (WEAPON_GROUP));

	private short Channel;
	private short Group;
	private short Accepted;

	public CollisionGroup(short Channel, short Accepted)
	{
		this.Channel = Channel;
		this.Group = (short) 0;
		this.Accepted = Accepted;
	}

	public short getChannel()
	{
		return Channel;
	}

	public short getGroup()
	{
		return Group;
	}

	public short getAccepted()
	{
		return Accepted;
	}

}
