package my.tamagochka.ufos.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import my.tamagochka.ufos.Components.*;

import java.util.Random;

public class UpdatingSystem extends EntitySystem {

    private static final int CHANCE_TO_BLINK_STAR = 5;
    private static final int MAX_FREQUENCY_BLINKING = 3;

    private ComponentMapper<StarComponent> sm = ComponentMapper.getFor(StarComponent.class);
    private ComponentMapper<DirectionComponent> dm = ComponentMapper.getFor(DirectionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<LocationComponent> lm = ComponentMapper.getFor(LocationComponent.class);

    private ImmutableArray<Entity> stars;
    private Entity player;

    private Random random;
    private OrthographicCamera camera;
    private Vector2 worldSize;

    public UpdatingSystem(Random random, OrthographicCamera camera, Vector2 worldSie) {
        this.random = random;
        this.camera = camera;
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
        if(counter < MAX_FREQUENCY_BLINKING) {
            if(random.nextInt(100) < CHANCE_TO_BLINK_STAR) {
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
        VelocityComponent velocityComponent = vm.get(player);
        DirectionComponent directionComponent = dm.get(player);
        LocationComponent locationComponent = lm.get(player);

        float dx = velocityComponent.velocity * deltaTime * (float)Math.cos(directionComponent.angle);
        float dy = velocityComponent.velocity * deltaTime * (float)Math.sin(directionComponent.angle);
        locationComponent.position.x += dx;
        locationComponent.position.y += dy;


        // *** looping player move
/*
        if(locationComponent.position.x  - camera.viewportWidth / 2 < 0) locationComponent.position.x += worldSize.x;
        if(locationComponent.position.y  - camera.viewportWidth / 2 < 0) locationComponent.position.y += worldSize.y;
        if(locationComponent.position.x  - camera.viewportWidth / 2 > worldSize.x) locationComponent.position.x -= worldSize.x;
        if(locationComponent.position.y  - camera.viewportWidth / 2 > worldSize.y) locationComponent.position.y -= worldSize.y;
*/
        if(locationComponent.position.x < 0) locationComponent.position.x += worldSize.x;
        if(locationComponent.position.y < 0) locationComponent.position.y += worldSize.y;
        if(locationComponent.position.x > worldSize.x) locationComponent.position.x -= worldSize.x;
        if(locationComponent.position.y > worldSize.y) locationComponent.position.y -= worldSize.y;


        camera.position.x = locationComponent.position.x;
        camera.position.y = locationComponent.position.y;

        System.out.println(locationComponent.position);





        camera.update();





    }

}
