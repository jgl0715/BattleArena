package battlearena.game;

public enum LayerType
{

    TILES(""),
    LIGHTS("Lights");

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
