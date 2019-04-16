package battlearena.common;

public class CollisionGroup
{

	public static final short TILES_GROUP = 0x0001;
	public static final short PLAYER_GROUP = 0x0004;
	public static final short ENTITIES_GROUP = 0x0008;
	public static final short PIT_GROUP = 0x0010;
	public static final short LIGHTS_GEN_GROUP = 0x0020;
	public static final short LIGHTS_PIT_GROUP = 0x0040;
	public static final short ITEM_GROUP = 0x0080;

	public static final CollisionGroup PLAYER = new CollisionGroup(PLAYER_GROUP, (short) (TILES_GROUP | ENTITIES_GROUP | LIGHTS_GEN_GROUP | PIT_GROUP | LIGHTS_PIT_GROUP | ITEM_GROUP));
	public static final CollisionGroup LIGHTS_GEN = new CollisionGroup(LIGHTS_GEN_GROUP, (short) (TILES_GROUP));
	public static final CollisionGroup LIGHTS_PIT = new CollisionGroup(LIGHTS_PIT_GROUP, (short) (PIT_GROUP | PLAYER_GROUP));
	public static final CollisionGroup TILES = new CollisionGroup(TILES_GROUP, (short) (PLAYER_GROUP | ENTITIES_GROUP | LIGHTS_GEN_GROUP));
	public static final CollisionGroup ENTITIES = new CollisionGroup(ENTITIES_GROUP, (short) (PLAYER_GROUP));
	public static final CollisionGroup PIT = new CollisionGroup(PIT_GROUP, (short) (ENTITIES_GROUP | PLAYER_GROUP | LIGHTS_PIT_GROUP));
	public static final CollisionGroup ITEMS = new CollisionGroup(ITEM_GROUP, PLAYER_GROUP);

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
