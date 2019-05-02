package battlearena.game.entity.behavior.ai.fsm;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import battlearena.common.world.path.Node;
import battlearena.game.entity.behavior.ai.BotComponent;

public class BotAgent implements Telegraph
{

    public StateMachine<BotAgent, BotState> stateMachine;      //fix the botstate

    public BotComponent botComponent;

    public float speed = 2.4f;

    public float timer;

    public Node nextNode; // for pursue or escape

    public BotAgent(BotComponent botComponent)
    {
        this.botComponent = botComponent;
        stateMachine = new DefaultStateMachine<>(this);

        timer = 0;
    }

    public Vector2 getPosition()
    {
        return botComponent.getBody().getPosition();
    }

    public void update(float deltaTime)
    {
        timer += deltaTime;

        stateMachine.update();
    }

    @Override
    public boolean handleMessage(Telegram msg)
    {
        return stateMachine.handleMessage(msg);

    }

}