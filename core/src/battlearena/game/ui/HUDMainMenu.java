package battlearena.game.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import battlearena.common.gui.HUD;
import battlearena.editor.WorldEditor;
import battlearena.game.Assets;
import battlearena.game.BattleArena;

public class HUDMainMenu extends HUD
{

    public TextButton tdmButton;
    public TextButton towersButton;
    public TextButton optionsButton;
    public TextButton creditsButton;

    public HUDMainMenu(Skin skin)
    {
        super(skin, BattleArena.VIRTUAL_WIDTH, BattleArena.VIRTUAL_HEIGHT);

        root.setBackground(new TextureRegionDrawable(new TextureRegion(BattleArena.I.getTexture(Assets.TEXTURE_MAIN_MENU))));
        root.bottom();

        Table buttonGroup = new Table();
        buttonGroup.bottom();
        {
            tdmButton = new TextButton("Deathmatch", skin);
            towersButton = new TextButton("Towers", skin);
            creditsButton = new TextButton("Credits", skin);
            optionsButton = new TextButton("Options", skin);

            buttonGroup.defaults().width(214).height(42).padTop(6).expand();
            buttonGroup.add(tdmButton).row();
            buttonGroup.add(towersButton).row();
            buttonGroup.add(creditsButton).row();
            buttonGroup.add(optionsButton).padBottom(23).row();
        }

        root.add(buttonGroup).pad(0);

    }


}
