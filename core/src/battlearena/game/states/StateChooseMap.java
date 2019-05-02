package battlearena.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.List;

import battlearena.common.file.TiledWorldImporter;
import battlearena.common.states.State;
import battlearena.common.world.EntityLayer;
import battlearena.common.world.TiledWorld;
import battlearena.game.BAEntityFactory;
import battlearena.game.BattleArena;
import battlearena.game.LayerType;
import battlearena.game.modes.GameMode;
import battlearena.game.ui.HUDChooseMap;

public class StateChooseMap extends State
{

    private int selectedMap;
    private List<Map> gameMaps;
    private HUDChooseMap hudChooseMap;
    private GameMode mode;

    private class Map
    {
        private String path;
        private String name;
        private Texture icon;

        Map(String n, String path, Texture icon)
        {
            this.name = n;
            this.path = path;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public Texture getIcon() {
            return icon;
        }
    }

    public StateChooseMap()
    {
        super("ChooseMap");
    }

    private void updateMapInfo()
    {
        Map map = gameMaps.get(selectedMap);
        String mapName = map.getName();
        Texture mapPreview = map.getIcon();

        hudChooseMap.mapImage.setBackground(new TextureRegionDrawable(new TextureRegion(mapPreview)));
        hudChooseMap.mapName.setText(mapName);
    }

    @Override
    public void create()
    {
        hudChooseMap = new HUDChooseMap(BattleArena.I.getSkin());

        hudChooseMap.nextMapButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);

                selectedMap++;
                if(selectedMap >= gameMaps.size())
                    selectedMap = 0;

                updateMapInfo();
            }
        });

        hudChooseMap.prevMapButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);

                selectedMap--;
                if(selectedMap < 0)
                    selectedMap = gameMaps.size() - 1;

                updateMapInfo();
            }
        });

        hudChooseMap.nextButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);

                String path = gameMaps.get(selectedMap).getPath();
                TiledWorld world = new TiledWorldImporter(path, true, new BAEntityFactory()).imp();
                world.addEntityLayer(new EntityLayer(LayerType.MOBS.getName()));

                mode.setWorld(world);

                BattleArena.I.inputToFSA(BattleArena.TRANSITION_SETUP_TEAMS, mode);
            }
        });


        selectedMap = 0;

        String[] mapNames = {"WorldOne", "WorldTwo"};

        gameMaps = new ArrayList<Map>();

        for(String x : mapNames)
        {
            String worldPath = "worlds/" + x + ".world";
            String previewPath = "previews/" + x + ".png";

            Texture preview = null;
            FileHandle handle = Gdx.files.internal(previewPath);

            if(handle.exists())
            {
                preview = new Texture(handle);
            }
            else
            {
                preview = new Texture(Gdx.files.internal("previews/no_preview.png"));
            }

            gameMaps.add(new Map(x, worldPath, preview));

            System.out.println(x);
        }

        updateMapInfo();
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void resized(int width, int height)
    {

    }

    @Override
    public void show(Object transitionInput)
    {
        Gdx.input.setInputProcessor(hudChooseMap.getUI());

        mode = (GameMode) transitionInput;
    }

    @Override
    public void hide()
    {

    }

    @Override
    public void update(float delta)
    {

    }

    @Override
    public void render()
    {
        hudChooseMap.render();
    }
}
