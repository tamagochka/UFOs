package my.tamagochka.ufos.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import my.tamagochka.ufos.Components.StarComponent;

import java.util.Random;

public class UpdatingSystem extends EntitySystem {

    private static final int CHANCE_TO_BLINK_STAR = 5;
    private static final int MAX_FREQUENCY_BLINKING = 3;

    private ComponentMapper<StarComponent> sm = ComponentMapper.getFor(StarComponent.class);
    private ImmutableArray<Entity> stars;
    private Random random;

    public UpdatingSystem(Random random) {
        this.random = random;
    }

    @Override
    public void addedToEngine(Engine engine) {
        stars = engine.getEntitiesFor(Family.all(StarComponent.class).get());
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









    }

}
