package battlearena.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import battlearena.common.gui.HUD;
import battlearena.game.BattleArena;

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


    public HUDSetupTeams(Skin skin)
    {
        super(skin, BattleArena.VIRTUAL_WIDTH, BattleArena.VIRTUAL_HEIGHT);

        root.setBackground(skin.getDrawable("round-gray"));

        redTeamLabel = new Label("RED", skin);
        charSelectLabel = new Label("CHARACTER SELECT", skin);
        charSelectLabel.setAlignment(Align.center);
        blueTeamLabel = new Label("BLUE", skin);

        redCharacterOne = new Table();
        redCharacterTwo = new Table();
        redCharacterThree = new Table();
        redCharacterFour = new Table();

        redCharacterOne.setBackground(skin.getDrawable("round-white"));
        redCharacterTwo.setBackground(skin.getDrawable("round-white"));
        redCharacterThree.setBackground(skin.getDrawable("round-white"));
        redCharacterFour.setBackground(skin.getDrawable("round-white"));

        blueCharacterOne = new Table();
        blueCharacterTwo = new Table();
        blueCharacterThree = new Table();
        blueCharacterFour = new Table();

        blueCharacterOne.setBackground(skin.getDrawable("round-white"));
        blueCharacterTwo.setBackground(skin.getDrawable("round-white"));
        blueCharacterThree.setBackground(skin.getDrawable("round-white"));
        blueCharacterFour.setBackground(skin.getDrawable("round-white"));

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
            tableLeft.add(blueTeamLabel).row();
            tableLeft.add(blueCharacterOne).width(48).height(64).row();
            tableLeft.add(blueCharacterTwo).width(48).height(64).row();
            tableLeft.add(blueCharacterThree).width(48).height(64).row();
            tableLeft.add(blueCharacterFour).width(48).height(64).row();
        }

        tableCenter =  new Table();
        tableCenter.setFillParent(true);
        tableCenter.center().top();
        tableCenter.defaults().width(300).pad(1);
        {
            tableCenter.add(charSelectLabel).row();
            tableCenter.add(characterStatsTable).height(180).row();
            tableCenter.add(labelInfo).row();

            Table tableSelectButtons = new Table();
            {
                tableSelectButtons.add(buttonPrevCharacter).width(90).height(30);
                tableSelectButtons.add(buttonNextCharacter).width(90).height(30);
            }

            Table tableSlotButtons = new Table();
            {
                tableSlotButtons.add(addSlotButton).width(90).height(30);
                tableSlotButtons.add(removeSlotButton).width(90).height(30);
            }

            tableCenter.add(tableSelectButtons).row();
            tableCenter.add(tableSlotButtons).row();
            tableCenter.add(buttonNext).row();
        }

        tableRight =  new Table();
        tableRight.setFillParent(true);
        tableRight.pad(10);
        tableRight.right().top();
        tableRight.defaults().pad(5);
        {
            tableRight.add(redTeamLabel).row();
            tableRight.add(redCharacterOne).width(48).height(64).row();
            tableRight.add(redCharacterTwo).width(48).height(64).row();
            tableRight.add(redCharacterThree).width(48).height(64).row();
            tableRight.add(redCharacterFour).width(48).height(64).row();
        }

        getUI().addActor(tableLeft);
        getUI().addActor(tableCenter);
        getUI().addActor(tableRight);

    }

    public void setAvailableSlots(int slots)
    {
        redCharacterFour.setVisible(false);
        redCharacterThree.setVisible(false);
        redCharacterTwo.setVisible(false);
        redCharacterOne.setVisible(false);

        blueCharacterFour.setVisible(false);
        blueCharacterThree.setVisible(false);
        blueCharacterTwo.setVisible(false);
        blueCharacterOne.setVisible(false);

        switch(slots)
        {
            case 4:
                redCharacterFour.setVisible(true);
                blueCharacterFour.setVisible(true);
            case 3:
                redCharacterThree.setVisible(true);
                blueCharacterThree.setVisible(true);
            case 2:
                redCharacterTwo.setVisible(true);
                blueCharacterTwo.setVisible(true);
            case 1:
                redCharacterOne.setVisible(true);
                blueCharacterOne.setVisible(true);
        }
    }

}
