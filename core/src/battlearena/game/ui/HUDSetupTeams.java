package battlearena.game.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import battlearena.common.gui.HUD;
import battlearena.game.BattleArena;
import battlearena.game.entity.BACharacter;
import battlearena.game.modes.BATeam;

public class HUDSetupTeams extends HUD
{

    public  Table tableLeft;
    public  Table tableCenter;
    public  Table tableRight;

    public  Label redTeamLabel;
    public  Label charSelectLabel;
    public  Label blueTeamLabel;

    public  Table redCharacterOneBg;
    public  Table redCharacterTwoBg;
    public  Table redCharacterThreeBg;
    public  Table redCharacterFourBg;

    public  Table redCharacterOne;
    public  Table redCharacterTwo;
    public  Table redCharacterThree;
    public  Table redCharacterFour;

    public Table blueCharacterOneBg;
    public Table blueCharacterTwoBg;
    public Table blueCharacterThreeBg;
    public Table blueCharacterFourBg;

    public Table blueCharacterOne;
    public Table blueCharacterTwo;
    public Table blueCharacterThree;
    public Table blueCharacterFour;
    
    public Table characterStatsTable;
    public Label labelInfo;
    public TextButton buttonPrevCharacter;
    public TextButton buttonNextCharacter;
    public TextButton buttonNext;

    public TextButton addSlotButton;
    public TextButton removeSlotButton;

    public Label labelKnockback;
    public Label labelSpeed;
    public Label labelDexterity;

    public HUDSetupTeams(Skin skin)
    {
        super(skin, BattleArena.VIRTUAL_WIDTH, BattleArena.VIRTUAL_HEIGHT);

        root.setBackground(skin.getDrawable("round-gray"));

        redTeamLabel = new Label("RED", skin);
        charSelectLabel = new Label("CHARACTER SELECT", skin);
        charSelectLabel.setAlignment(Align.center);
        blueTeamLabel = new Label("BLUE", skin);

        labelKnockback = new Label("Knockback", skin);
        labelSpeed = new Label("Speed", skin);
        labelDexterity= new Label("Dexterity", skin);

        redCharacterOneBg = new Table();
        redCharacterTwoBg = new Table();
        redCharacterThreeBg = new Table();
        redCharacterFourBg = new Table();

        redCharacterOne = new Table();
        redCharacterTwo = new Table();
        redCharacterThree = new Table();
        redCharacterFour = new Table();

        redCharacterOneBg.setTouchable(Touchable.enabled);
        redCharacterTwoBg.setTouchable(Touchable.enabled);
        redCharacterThreeBg.setTouchable(Touchable.enabled);
        redCharacterFourBg.setTouchable(Touchable.enabled);

        redCharacterOneBg.setBackground(skin.getDrawable("round-white"));
        redCharacterTwoBg.setBackground(skin.getDrawable("round-white"));
        redCharacterThreeBg.setBackground(skin.getDrawable("round-white"));
        redCharacterFourBg.setBackground(skin.getDrawable("round-white"));

        blueCharacterOneBg = new Table();
        blueCharacterTwoBg = new Table();
        blueCharacterThreeBg = new Table();
        blueCharacterFourBg = new Table();

        blueCharacterOneBg.setTouchable(Touchable.enabled);
        blueCharacterTwoBg.setTouchable(Touchable.enabled);
        blueCharacterThreeBg.setTouchable(Touchable.enabled);
        blueCharacterFourBg.setTouchable(Touchable.enabled);

        blueCharacterOne = new Table();
        blueCharacterTwo = new Table();
        blueCharacterThree = new Table();
        blueCharacterFour = new Table();

        blueCharacterOneBg.setBackground(skin.getDrawable("round-white"));
        blueCharacterTwoBg.setBackground(skin.getDrawable("round-white"));
        blueCharacterThreeBg.setBackground(skin.getDrawable("round-white"));
        blueCharacterFourBg.setBackground(skin.getDrawable("round-white"));

        characterStatsTable = new Table();
        characterStatsTable.setBackground(skin.getDrawable("round-white"));

        labelInfo = new Label("lvl3", skin);
        labelInfo.setAlignment(Align.center);

        buttonPrevCharacter = new TextButton("<", skin);
        buttonNextCharacter = new TextButton(">", skin);

        buttonNext = new TextButton("GO", skin);

        addSlotButton = new TextButton("+", skin);
        removeSlotButton = new TextButton("-", skin);

        tableLeft =  new Table();
        tableLeft.setFillParent(true);
        tableLeft.pad(10);
        tableLeft.left().top();
        tableLeft.defaults().pad(5);
        {
            blueCharacterOneBg.add(blueCharacterOne);
            blueCharacterTwoBg.add(blueCharacterTwo);
            blueCharacterThreeBg.add(blueCharacterThree);
            blueCharacterFourBg.add(blueCharacterFour);

            tableLeft.add(blueTeamLabel).row();
            tableLeft.add(blueCharacterOneBg).width(48).height(64).row();
            tableLeft.add(blueCharacterTwoBg).width(48).height(64).row();
            tableLeft.add(blueCharacterThreeBg).width(48).height(64).row();
            tableLeft.add(blueCharacterFourBg).width(48).height(64).row();
        }

        tableCenter =  new Table();
        tableCenter.setFillParent(true);
        tableCenter.center().top();
        tableCenter.bottom();
        tableCenter.defaults().width(300).pad(1);
        {
            tableCenter.add(charSelectLabel).row();

            Table tableCharacter = new Table();
            {
                tableCharacter.add(buttonPrevCharacter).width(30).height(180);
                tableCharacter.add(characterStatsTable).height(180).width(300);
                tableCharacter.add(buttonNextCharacter).width(30).height(180);
            }

            tableCenter.add(tableCharacter).row();
            tableCenter.add(labelInfo).row();

            Table tableSlotButtons = new Table();
            {
                tableSlotButtons.add(addSlotButton).width(150).height(30);
                tableSlotButtons.add(removeSlotButton).width(150).height(30);
            }

            tableCenter.add(tableSlotButtons).row();
            tableCenter.add(buttonNext).row();
        }

        tableRight =  new Table();
        tableRight.setFillParent(true);
        tableRight.pad(10);
        tableRight.right().top();
        tableRight.defaults().pad(5);
        {
            redCharacterOneBg.add(redCharacterOne);
            redCharacterTwoBg.add(redCharacterTwo);
            redCharacterThreeBg.add(redCharacterThree);
            redCharacterFourBg.add(redCharacterFour);

            tableRight.add(redTeamLabel).row();
            tableRight.add(redCharacterOneBg).width(48).height(64).row();
            tableRight.add(redCharacterTwoBg).width(48).height(64).row();
            tableRight.add(redCharacterThreeBg).width(48).height(64).row();
            tableRight.add(redCharacterFourBg).width(48).height(64).row();
        }

        getUI().addActor(tableLeft);
        getUI().addActor(tableCenter);
        getUI().addActor(tableRight);

    }

    public void updateCharacters(BACharacter[] redTeam, BACharacter[] blueTeam, int slots)
    {
        redCharacterOne.setBackground(new TextureRegionDrawable((TextureRegion) redTeam[0].getWalkAnim().getKeyFrames()[0]));
        blueCharacterOne.setBackground(new TextureRegionDrawable((TextureRegion) blueTeam[0].getWalkAnim().getKeyFrames()[0]));

        if (slots >= 2)
        {
            redCharacterTwo.setBackground(new TextureRegionDrawable((TextureRegion) redTeam[1].getWalkAnim().getKeyFrames()[0]));
            blueCharacterTwo.setBackground(new TextureRegionDrawable((TextureRegion) blueTeam[1].getWalkAnim().getKeyFrames()[0]));
        }

        if (slots >= 3)
        {
            redCharacterThree.setBackground(new TextureRegionDrawable((TextureRegion) redTeam[2].getWalkAnim().getKeyFrames()[0]));
            blueCharacterThree.setBackground(new TextureRegionDrawable((TextureRegion) blueTeam[2].getWalkAnim().getKeyFrames()[0]));
        }

        if (slots >= 4)
        {
            redCharacterFour.setBackground(new TextureRegionDrawable((TextureRegion) redTeam[3].getWalkAnim().getKeyFrames()[0]));
            blueCharacterFour.setBackground(new TextureRegionDrawable((TextureRegion) blueTeam[3].getWalkAnim().getKeyFrames()[0]));
        }

    }

    public void selectCharacter(BACharacter character, BATeam team, int slot)
    {

        redCharacterFourBg.setBackground(skin.getDrawable("round-white"));
        redCharacterThreeBg.setBackground(skin.getDrawable("round-white"));
        redCharacterTwoBg.setBackground(skin.getDrawable("round-white"));
        redCharacterOneBg.setBackground(skin.getDrawable("round-white"));
        blueCharacterFourBg.setBackground(skin.getDrawable("round-white"));
        blueCharacterThreeBg.setBackground(skin.getDrawable("round-white"));
        blueCharacterTwoBg.setBackground(skin.getDrawable("round-white"));
        blueCharacterOneBg.setBackground(skin.getDrawable("round-white"));

        if(team == BATeam.BLUE)
        {

            switch(slot)
            {
                case 1:
                    blueCharacterOneBg.setBackground(skin.getDrawable("round-gray"));
                    break;
                case 2:
                    blueCharacterTwoBg.setBackground(skin.getDrawable("round-gray"));
                    break;
                case 3:
                    blueCharacterThreeBg.setBackground(skin.getDrawable("round-gray"));
                    break;
                case 4:
                    blueCharacterFourBg.setBackground(skin.getDrawable("round-gray"));
                    break;
            }

        }
        else if(team == BATeam.RED)
        {
            switch(slot)
            {
                case 1:
                    redCharacterOneBg.setBackground(skin.getDrawable("round-gray"));
                    break;
                case 2:
                    redCharacterTwoBg.setBackground(skin.getDrawable("round-gray"));
                    break;
                case 3:
                    redCharacterThreeBg.setBackground(skin.getDrawable("round-gray"));
                    break;
                case 4:
                    redCharacterFourBg.setBackground(skin.getDrawable("round-gray"));
                    break;
            }
        }

        updateStatsPane(character);
    }

    public void updateStatsPane(BACharacter character)
    {

        // Update stats pane.
        characterStatsTable.clear();

        labelSpeed.setAlignment(Align.left);
        labelDexterity.setAlignment(Align.left);
        labelKnockback.setAlignment(Align.left);

        characterStatsTable.defaults().pad(2);
        {
            int speedBlocks = (int) Math.round(character.getSpeed() / BACharacter.MAX_SPEED * 5.0f);
            int kbBlocks = (int) Math.round(character.getKnockback() / BACharacter.MAX_KNOCKBACK * 5.0f);
            int dexBlocks = (int) Math.round(character.getDexterity() / BACharacter.MAX_DEXTERITY * 5.0f);

            characterStatsTable.add(labelSpeed).left();

            for(int i = 0; i < 5; i++)
            {
                Table block = new Table();
                if(i < speedBlocks)
                {
                    block.setBackground(skin.getDrawable("round-gray"));
                }
                else
                {
                    block.setBackground(skin.getDrawable("round-white"));
                }
                characterStatsTable.add(block).width(30).height(30);
            }

            characterStatsTable.row();
            characterStatsTable.add(labelKnockback).left();

            for(int i = 0; i < 5; i++)
            {
                Table block = new Table();
                if(i < kbBlocks)
                {
                    block.setBackground(skin.getDrawable("round-gray"));
                }
                else
                {
                    block.setBackground(skin.getDrawable("round-white"));
                }
                characterStatsTable.add(block).width(30).height(30);
            }

            characterStatsTable.row();
            characterStatsTable.add(labelDexterity).left();

            for(int i = 0; i < 5; i++)
            {
                Table block = new Table();
                if(i < dexBlocks)
                {
                    block.setBackground(skin.getDrawable("round-gray"));
                }
                else
                {
                    block.setBackground(skin.getDrawable("round-white"));
                }
                characterStatsTable.add(block).width(30).height(30);
            }

        }
    }


    public void setAvailableSlots(int slots)
    {
        redCharacterFourBg.setVisible(false);
        redCharacterThreeBg.setVisible(false);
        redCharacterTwoBg.setVisible(false);
        redCharacterOneBg.setVisible(false);

        blueCharacterFourBg.setVisible(false);
        blueCharacterThreeBg.setVisible(false);
        blueCharacterTwoBg.setVisible(false);
        blueCharacterOneBg.setVisible(false);

        switch(slots)
        {
            case 4:
                redCharacterFourBg.setVisible(true);
                blueCharacterFourBg.setVisible(true);
            case 3:
                redCharacterThreeBg.setVisible(true);
                blueCharacterThreeBg.setVisible(true);
            case 2:
                redCharacterTwoBg.setVisible(true);
                blueCharacterTwoBg.setVisible(true);
            case 1:
                redCharacterOneBg.setVisible(true);
                blueCharacterOneBg.setVisible(true);
        }
    }

}
