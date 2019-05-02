package battlearena.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import battlearena.common.gui.HUD;
import battlearena.game.Assets;
import battlearena.game.BattleArena;

/**
 * Created by joeyl on 5/2/2019.
 */

public class HUDCredits extends HUD
{

    public TextButton backButton;

    public HUDCredits(Skin skin)
    {
        super(skin, BattleArena.VIRTUAL_WIDTH, BattleArena.VIRTUAL_HEIGHT);

        root.setBackground(new TextureRegionDrawable(BattleArena.I.getTexture(Assets.TEXTURE_CREDITS)));

        backButton = new TextButton("Back to Main Menu", skin);

        root.bottom().left();

        root.add(backButton).pad(5);
    }
}
