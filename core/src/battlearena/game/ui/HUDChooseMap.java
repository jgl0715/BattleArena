package battlearena.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import battlearena.common.gui.HUD;
import battlearena.game.BattleArena;

public class HUDChooseMap extends HUD
{

    public Table mapImage;
    public Label mapName;
    public TextButton prevMapButton;
    public TextButton nextMapButton;
    public TextButton nextButton;

    public HUDChooseMap(Skin skin)
    {
        super(skin, BattleArena.VIRTUAL_WIDTH, BattleArena.VIRTUAL_HEIGHT);

        root.defaults().pad(0);

        nextButton = new TextButton("NEXT", skin);
        mapName = new Label("test name", skin);
        mapName.setAlignment(Align.top);

        Table midTable = new Table();
        midTable.defaults().pad(5);
        {
            prevMapButton = new TextButton("<", skin);
            nextMapButton = new TextButton(">", skin);
            mapImage = new Table();

            midTable.add(prevMapButton).width(50).height(180);
            midTable.add(mapImage).width(320).height(180);
            midTable.add(nextMapButton).width(50).height(180);
        }

        root.bottom();
        root.defaults().pad(10);
        root.setBackground(skin.getDrawable("round-gray"));
        root.add(mapName).width(320).row();
        root.add(midTable).row();
        root.add(nextButton).width(320);

    }

}
