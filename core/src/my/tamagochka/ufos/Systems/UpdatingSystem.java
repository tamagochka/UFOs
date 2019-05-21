package my.tamagochka.ufos.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import my.tamagochka.ufos.Components.*;
import my.tamagochka.ufos.Game;

import java.util.Random;

public class UpdatingSystem extends EntitySystem {

    private ComponentMapper<StarComponent> sm = ComponentMapper.getFor(StarComponent.class);
    private ComponentMapper<DirectionComponent> dm = ComponentMapper.getFor(DirectionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<LocationComponent> lm = ComponentMapper.getFor(LocationComponent.class);
    private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);

    private ImmutableArray<Entity> stars;
    private Entity player;

    private Random random;
    private OrthographicCamera camera;
    private OrthographicCamera b2ddrCamera;
    private Vector2 worldSize;

    public UpdatingSystem(Random random, OrthographicCamera camera, OrthographicCamera b2ddrCamera, Vector2 worldSie) {
        this.random = random;
        this.camera = camera;
        this.b2ddrCamera = b2ddrCamera;
        this.worldSize = worldSie;
    }

    @Override
    public void addedToEngine(Engine engine) {
        stars = engine.getEntitiesFor(Family.all(StarComponent.class).get());
        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
    }


    private float buffer = 0;
    private float counter = 0;

    @Override
    public void update(float deltaTime) {

        // *** stars blinking
        buffer += deltaTime;
        if(buffer > 1) {
            buffer = 0;
            counter = 0;
        }
        if(counter < Game.MAX_FREQUENCY_BLINKING) {
            if(random.nextInt(100) < Game.CHANCE_TO_BLINK_STAR) {
                counter++;
                StarComponent starComponent = sm.get(stars.random());
                starComponent.blink();
            }
        }
        stars.forEach(star -> {
            StarComponent starComponent = sm.get(star);
            if(starComponent.isBlink()) starComponent.update();
        });

        // *** updating game camera position depending on player moving
        LocationComponent locationComponent = lm.get(player);
        PhysicsComponent physicsComponent = pm.get(player);

        // ** looping player move
        if(locationComponent.position.x < 0) {
            locationComponent.position.x += worldSize.x;
            physicsComponent.setPosition(locationComponent.position);
        }
        if(locationComponent.position.y < 0) {
            locationComponent.position.y += worldSize.y;
            physicsComponent.setPosition(locationComponent.position);
        }
        if(locationComponent.position.x > worldSize.x) {
            locationComponent.position.x -= worldSize.x;
            physicsComponent.setPosition(locationComponent.position);
        }
        if(locationComponent.position.y > worldSize.y) {
            locationComponent.position.y -= worldSize.y;
            physicsComponent.setPosition(locationComponent.position);
        }

        // ** camera follow for player
        camera.position.x = locationComponent.position.x;
        camera.position.y = locationComponent.position.y;
        camera.update();

        // ** camera for physics renderer follow for player
        if(Game.DEBUG_MODE) {
            b2ddrCamera.position.x = camera.position.x / Game.PPM;
            b2ddrCamera.position.y = camera.position.y / Game.PPM;
            b2ddrCamera.update();
        }





    }

}
