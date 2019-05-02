package battlearena.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import battlearena.common.states.State;
import battlearena.game.Assets;
import battlearena.game.BattleArena;
import battlearena.game.entity.BACharacter;
import battlearena.game.modes.BATeam;
import battlearena.game.modes.GameMode;
import battlearena.game.ui.HUDSetupTeams;

public class StateSetupTeams extends State
{

    private HUDSetupTeams hudSetupTeams;

    private int gameSlots;
    private BATeam selectedTeam;
    private int selectedSlot;

    private BACharacter[] blueTeam;
    private BACharacter[] redTeam;

    private GameMode mode;


    public StateSetupTeams()
    {
        super("SetupTeams");
    }

    @Override
    public void create()
    {
        hudSetupTeams = new HUDSetupTeams(BattleArena.I.getSkin());

        hudSetupTeams.addSlotButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);

                gameSlots++;

                if(gameSlots > 4)
                    gameSlots = 4;

                hudSetupTeams.setAvailableSlots(gameSlots);


                BattleArena.I.playSound(Assets.AUDIO_CLICK);
            }
        });

        hudSetupTeams.removeSlotButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);


                if(selectedSlot == gameSlots && gameSlots > 1)
                {
                    selectedSlot--;

                    if(selectedTeam == BATeam.RED)
                        hudSetupTeams.selectCharacter(redTeam[selectedSlot], selectedTeam, selectedSlot);
                    else if(selectedTeam == BATeam.BLUE)
                        hudSetupTeams.selectCharacter(blueTeam[selectedSlot], selectedTeam, selectedSlot);

                }

                gameSlots--;

                if(gameSlots < 1)
                    gameSlots = 1;


                hudSetupTeams.setAvailableSlots(gameSlots);

                BattleArena.I.playSound(Assets.AUDIO_CLICK);
            }
        });

        hudSetupTeams.buttonNextCharacter.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);

                BACharacter current = null;

                if(selectedTeam == BATeam.RED)
                    current = redTeam[selectedSlot - 1];
                else if(selectedTeam == BATeam.BLUE)
                    current = blueTeam[selectedSlot - 1];

                int currentIndex = -1;
                BACharacter[] enumValues = BACharacter.values();
                for(int i = 0; i < enumValues.length; i++)
                {
                    if(enumValues[i] == current)
                        currentIndex = i;
                }

                currentIndex--;
                if(currentIndex < 0)
                    currentIndex = enumValues.length - 1;

                if(selectedTeam == BATeam.RED)
                    redTeam[selectedSlot - 1] = enumValues[currentIndex];
                else if(selectedTeam == BATeam.BLUE)
                    blueTeam[selectedSlot - 1] = enumValues[currentIndex];

                hudSetupTeams.updateCharacters(redTeam, blueTeam, gameSlots);
                hudSetupTeams.updateStatsPane(enumValues[currentIndex]);

                BattleArena.I.playSound(Assets.AUDIO_CLICK);
            }
        });

        hudSetupTeams.buttonPrevCharacter.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);

                BACharacter current = null;

                if(selectedTeam == BATeam.RED)
                    current = redTeam[selectedSlot - 1];
                else if(selectedTeam == BATeam.BLUE)
                    current = blueTeam[selectedSlot - 1];

                int currentIndex = -1;
                BACharacter[] enumValues = BACharacter.values();
                for(int i = 0; i < enumValues.length; i++)
                {
                    if(enumValues[i] == current)
                        currentIndex = i;
                }

                if(selectedTeam == BATeam.RED)
                    redTeam[selectedSlot - 1] = enumValues[(1+currentIndex)%enumValues.length];
                else if(selectedTeam == BATeam.BLUE)
                    blueTeam[selectedSlot - 1] = enumValues[(1+currentIndex)%enumValues.length];

                hudSetupTeams.updateCharacters(redTeam, blueTeam, gameSlots);
                hudSetupTeams.updateStatsPane(enumValues[currentIndex]);

                BattleArena.I.playSound(Assets.AUDIO_CLICK);
            }
        });

        hudSetupTeams.buttonNext.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);

                mode.setTeams(blueTeam, redTeam, gameSlots);

                BattleArena.I.inputToFSA(BattleArena.TRANSITION_PLAY, mode);

                BattleArena.I.playSound(Assets.AUDIO_CLICK);
            }
        });

        hudSetupTeams.redCharacterOneBg.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                selectedTeam = BATeam.RED;
                selectedSlot = 1;
                hudSetupTeams.selectCharacter(redTeam[selectedSlot-1], selectedTeam, selectedSlot);
                hudSetupTeams.labelInfo.setText("Player");

                BattleArena.I.playSound(Assets.AUDIO_CLICK);
                return true;
            }
        });

        hudSetupTeams.redCharacterTwoBg.addListener(new ClickListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                selectedTeam = BATeam.RED;
                selectedSlot = 2;
                hudSetupTeams.selectCharacter(redTeam[selectedSlot-1], selectedTeam, selectedSlot);
                hudSetupTeams.labelInfo.setText("Bot");

                BattleArena.I.playSound(Assets.AUDIO_CLICK);
                return true;
            }
        });

        hudSetupTeams.redCharacterThreeBg.addListener(new ClickListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                selectedTeam = BATeam.RED;
                selectedSlot = 3;
                hudSetupTeams.selectCharacter(redTeam[selectedSlot-1], selectedTeam, selectedSlot);
                hudSetupTeams.labelInfo.setText("Bot");

                BattleArena.I.playSound(Assets.AUDIO_CLICK);
                return true;
            }
        });


        hudSetupTeams.redCharacterFourBg.addListener(new ClickListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                selectedTeam = BATeam.RED;
                selectedSlot = 4;
                hudSetupTeams.selectCharacter(redTeam[selectedSlot-1], selectedTeam, selectedSlot);
                hudSetupTeams.labelInfo.setText("Bot");

                BattleArena.I.playSound(Assets.AUDIO_CLICK);
                return true;
            }
        });


        hudSetupTeams.blueCharacterOneBg.addListener(new ClickListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                selectedTeam = BATeam.BLUE;
                selectedSlot = 1;
                hudSetupTeams.selectCharacter(blueTeam[selectedSlot-1], selectedTeam, selectedSlot);
                hudSetupTeams.labelInfo.setText("Bot");

                BattleArena.I.playSound(Assets.AUDIO_CLICK);
                return true;
            }
        });


        hudSetupTeams.blueCharacterTwoBg.addListener(new ClickListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                selectedTeam = BATeam.BLUE;
                selectedSlot = 2;
                hudSetupTeams.selectCharacter(blueTeam[selectedSlot-1], selectedTeam, selectedSlot);
                hudSetupTeams.labelInfo.setText("Bot");

                BattleArena.I.playSound(Assets.AUDIO_CLICK);
                return true;
            }
        });


        hudSetupTeams.blueCharacterThreeBg.addListener(new ClickListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                selectedTeam = BATeam.BLUE;
                selectedSlot = 3;
                hudSetupTeams.selectCharacter(blueTeam[selectedSlot-1], selectedTeam, selectedSlot);
                hudSetupTeams.labelInfo.setText("Bot");

                BattleArena.I.playSound(Assets.AUDIO_CLICK);
                return true;
            }
        });


        hudSetupTeams.blueCharacterFourBg.addListener(new ClickListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                selectedTeam = BATeam.BLUE;
                selectedSlot = 4;
                hudSetupTeams.selectCharacter(blueTeam[selectedSlot-1], selectedTeam, selectedSlot);
                hudSetupTeams.labelInfo.setText("Bot");

                BattleArena.I.playSound(Assets.AUDIO_CLICK);
                return true;
            }
        });

        blueTeam = new BACharacter[4];
        redTeam = new BACharacter[4];

        for(int i = 0; i < 4; i++)
        {
            blueTeam[i] = BACharacter.WARRIOR;
            redTeam[i] = BACharacter.WARRIOR;
        }

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
        Gdx.input.setInputProcessor(hudSetupTeams.getUI());

        gameSlots = 4;
        selectedSlot = 1;
        selectedTeam = BATeam.RED;

        hudSetupTeams.updateCharacters(redTeam, blueTeam, gameSlots);
        hudSetupTeams.selectCharacter(redTeam[selectedSlot-1], BATeam.RED, selectedSlot);
        hudSetupTeams.updateStatsPane(redTeam[selectedSlot-1]);
        hudSetupTeams.labelInfo.setText("Player");

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
        hudSetupTeams.render();

        Vector2 statsCoords = hudSetupTeams.characterStatsTable.localToScreenCoordinates(new Vector2(0, 0));
        ShapeRenderer sr = BattleArena.I.getShapeRenderer();

        sr.begin(ShapeRenderer.ShapeType.Filled);

        sr.end();
    }

}
