package battlearena.game.entity.behavior.ai;

//import com.badlogic.ashley.core.Component;                  //fix ashley: need to import our character into component
import com.badlogic.gdx.physics.box2d.Body;					// in body body below variable: it should initialze our enemy
import battlearena.game.entity.behavior.ai.fsm.BotAgent;
import battlearena.game.entity.behavior.ai.fsm.BotState;


import battlearena.common.entity.Entity;



import battlearena.game.entity.EMob;
import battlearena.common.entity.Entity;
////
import battlearena.game.entity.BACharacter;
import battlearena.game.entity.behavior.BAttack;
/////

import battlearena.game.entity.behavior.BAttackArcher;
import battlearena.game.entity.behavior.BAttackGunner;
import battlearena.game.entity.behavior.BAttackWarrior;

public class BotComponent
{

    // state
    public static final int MOVE_UP = 0;
    public static final int MOVE_DOWN = 1;
    public static final int MOVE_LEFT = 2;
    public static final int MOVE_RIGHT = 3;
    public static final int ESCAPE = 4;
    public static final int DIE = 5;

    public static final float WEAK_TIME = 10f;
    public float weak_time;

    public BotAgent botAgent;
	public BAttack botAttack;
	private final Body body;

    public int currentState;

    public boolean weaken;

    private EMob mob;
    private BACharacter character;

    public BotComponent(EMob mob)
    {
        this.body = mob.getBody();
        this.character = mob.getCharacter();

        botAgent = new BotAgent(this);
//        botAgent.stateMachine.setInitialState(BotState.MOVE_DOWN);
        currentState = MOVE_DOWN;
		weaken = false;
    }

    public Body getBody()
    {
        return body;
    }

    public BACharacter getCharacter()
    {
        return character;
    }

	public boolean isWeaken()
    {
        return weaken;

//		if(hp<=0)
//			weaken = true;

	}

    public void respawn()
    {
//        hp = 100;
//        coolDown = 100;

		weaken = false;
    }
}