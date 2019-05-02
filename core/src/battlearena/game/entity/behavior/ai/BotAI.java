package battlearena.game.entity.behavior.ai;



import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.utils.Path;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import battlearena.common.world.TiledWorld;
import battlearena.common.world.World;
import battlearena.common.world.path.GamePathFinder;
import battlearena.common.world.path.Node;
import battlearena.game.entity.BACharacter;
import battlearena.game.entity.EPlayerAI;
import battlearena.common.entity.Entity;
import battlearena.game.entity.behavior.BAttack;
import battlearena.game.entity.behavior.BController;
import battlearena.game.entity.behavior.ai.utils.Box2dLocation;
import battlearena.game.entity.behavior.ai.utils.Box2dSteeringUtils;
import battlearena.game.modes.GameMode;


public class BotAI implements Steerable<Vector2>
{

    // behavior
    public static final int WANDER_BEHAVIOR = 0;
    public static final int ARRIVE_BEHAVIOR = 1;

    private BController controller;
    private BAttack attack;

    private final float boundingRadius;
    private boolean tagged;
    private float maxLinearAcceleration;
    private float maxAngularAcceleration;
    private float maxLinearSpeed;
    private float maxAngularSpeed;

    private float zeroLinearSpeedThreshold;

    private boolean ranged;
    private boolean charged;

    private SteeringBehavior<Vector2> steeringBehavior;
    private final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());

    private Wander<Vector2> wanderBehavior;
    private Arrive<Vector2> arriveBehavior;
//    private FollowPath<Vector2, BAPath> pathFindBehavior;

    private TiledWorld world;
    private EPlayerAI host;
    private GamePathFinder pathFinder;
    private Node nextNode;
    private Node currentNode;
    private Node wanderDst;
    private boolean hasLineOfSight;
    private boolean wandering;

    private class PathArrival implements Location<Vector2>
    {
        @Override
        public Vector2 getPosition()
        {
            if(hasLineOfSight && host.getTarget() != null)
            {
                return host.getTarget().getBody().getPosition();
            }
            else if(nextNode != null)
            {
                float x = (nextNode.x * TiledWorld.TILE_SIZE + TiledWorld.TILE_SIZE / 2) / World.PIXELS_PER_METER;
                float y = ((world.getHeight() - nextNode.y - 1) * TiledWorld.TILE_SIZE + TiledWorld.TILE_SIZE / 2) / World.PIXELS_PER_METER;

                return new Vector2(x, y);
            }

            return Vector2.Zero;
        }

        @Override
        public float getOrientation()
        {
            return 0.0f;
        }

        @Override
        public void setOrientation(float orientation)
        {

        }

        @Override
        public float vectorToAngle(Vector2 vector)
        {
            return Box2dSteeringUtils.vectorToAngle(vector);
        }

        @Override
        public Vector2 angleToVector(Vector2 outVector, float angle)
        {
            return Box2dSteeringUtils.angleToVector(outVector, angle);
        }

        @Override
        public Location<Vector2> newLocation()
        {
            return new Box2dLocation();
        }
    }

    public BotAI(EPlayerAI host, float boundingRadius)
    {
        this.host = host;
        this.world = (TiledWorld) host.getWorld();
        this.pathFinder = new GamePathFinder((TiledWorld) host.getWorld());

        if(host.getCharacter() == BACharacter.GUNNER)
        {
            ranged = true;

            if(host.getCharacter() == BACharacter.ARCHER )
            {
                charged = true;
            }
        }

        this.controller = host.getMovement();
        this.attack = host.getAttack();

        this.boundingRadius = boundingRadius;

        tagged = false;
        maxAngularAcceleration = 1.0f;
        maxLinearAcceleration = 1.0f;
        maxLinearSpeed = 2f;
        maxAngularSpeed = 1.0f;

        createWanderBehavior();
    }

    private void createWanderBehavior() {
        wanderBehavior = new Wander<>(this)
                .setEnabled(true)
                .setWanderRadius(2f)
                .setWanderRate(MathUtils.PI2 * 4);
    }

    public void setBehavior(int behavior)
    {
        switch (behavior) {
            case ARRIVE_BEHAVIOR:
                if (arriveBehavior == null)
                {
                    if (host != null)
                    {
                        //////////////
                        arriveBehavior = new Arrive<>(this, new PathArrival())
                                .setEnabled(true)
                                .setTimeToTarget(0.1f)
                                .setArrivalTolerance(0.1f)
                                .setLimiter(new Limiter() {
                                    @Override
                                    public float getZeroLinearSpeedThreshold() {
                                        return 0;
                                    }

                                    @Override
                                    public void setZeroLinearSpeedThreshold(float value) {

                                    }

                                    @Override
                                    public float getMaxLinearSpeed() {
                                        return 100.0f;
                                    }

                                    @Override
                                    public void setMaxLinearSpeed(float maxLinearSpeed) {

                                    }

                                    @Override
                                    public float getMaxLinearAcceleration() {
                                        return 100.0f;
                                    }

                                    @Override
                                    public void setMaxLinearAcceleration(float maxLinearAcceleration)
                                    {

                                    }

                                    @Override
                                    public float getMaxAngularSpeed()
                                    {
                                        return 0;
                                    }

                                    @Override
                                    public void setMaxAngularSpeed(float maxAngularSpeed) {

                                    }

                                    @Override
                                    public float getMaxAngularAcceleration() {
                                        return 0;
                                    }

                                    @Override
                                    public void setMaxAngularAcceleration(float maxAngularAcceleration) {

                                    }
                                });
                        steeringBehavior = arriveBehavior;
                    }
                } else {
                    steeringBehavior = arriveBehavior;
                }
                break;
            case WANDER_BEHAVIOR:
                steeringBehavior = wanderBehavior;
            default:
                break;
        }
    }

    public void update(float deltaTime)
    {
        currentNode = host.getNearestNode();

        boolean hold = false;

        if(host.getTarget() != null)
        {
            wandering = false;
            wanderDst = null;

            float dist = host.getPos().dst(host.getTarget().getPos());

            RayCastCallback callback = new RayCastCallback()
            {
                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction)
                {
                    Entity e1 = (Entity) fixture.getBody().getUserData();

                    if(e1 == null)
                    {
                        hasLineOfSight = false;
                        return 0;
                    }

                    return 1;
                }
            };

            hasLineOfSight = true;
            world.getPhysicsWorld().rayCast(callback, host.getBody().getPosition(), host.getTarget().getBody().getPosition());

            if(hasLineOfSight && ranged)
            {
                float dy = Math.abs(host.getTarget().getPos().y - host.getPos().y);
                float dx = Math.abs(host.getTarget().getPos().x - host.getPos().x);

                if(dy <= 10)
                {
                    controller.setSteady(true);
                    if(dx >= 400.0f && charged)
                    {
                        attack.charge();
                    }
                    else
                    {
                        attack.attack();
                    }
                }else
                {
                    controller.setSteady(false);
                }
            }
            if(dist <= 50)
            {
                attack.attack();
            }

            // Find host current tile position.
            Node targetTile = host.getTarget().getNearestNode();
            nextNode = pathFinder.findNextNode(currentNode, targetTile);

        }
        else
        {
            wandering = true;


            if(wanderDst == null)
            {
                Node spawn = null;
                do
                {
                    int randX = (int)(Math.random() * world.getWidth());
                    int randY = (int)(Math.random() * world.getHeight());
                    spawn = world.getNodeAt(randX, randY);

                    if(pathFinder.findNextNode(currentNode, spawn) != null)
                        wanderDst = spawn;

                }while(wanderDst == null);
            }
            else
            {
                if(currentNode.x == wanderDst.x && currentNode.y == wanderDst.y)
                    wanderDst = null;
                else
                    nextNode= pathFinder.findNextNode(currentNode, wanderDst);
            }
        }


        if (steeringBehavior != null)
        {
            steeringBehavior.calculateSteering(steeringOutput);

            if(!hold)
                applyingSteering(deltaTime);
            else
                controller.setDirection(new Vector2(0, 0));

        }
    }

    public void applyingSteering(float deltaTime)
    {
        if (!steeringOutput.linear.isZero())
        {
            controller.setDirection(new Vector2(steeringOutput.linear).nor());
        }
    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return host.getBody().getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return host.getBody().getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Vector2 getPosition() {
        return host.getBody().getPosition();
    }

    @Override
    public float getOrientation() {
        return host.getBody().getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
        host.getBody().setTransform(getPosition(), orientation);
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return Box2dSteeringUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Box2dSteeringUtils.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Box2dLocation();
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return zeroLinearSpeedThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        this.zeroLinearSpeedThreshold = value;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

}