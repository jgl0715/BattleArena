package battlearena.game;

public enum LayerType
{

    TILES(""),
    LIGHTS("Lights"),
    MOBS("Mobs");

    private String name;

    LayerType(String n)
    {
        this.name = n;
    }

    public String getName()
    {
        return name;
    }
}
