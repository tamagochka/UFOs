package my.tamagochka.ufos.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsSystem extends EntitySystem {

    private World world;

    public PhysicsSystem(World world) {
        this.world = world;
    }

    @Override
    public void addedToEngine(Engine engine) {

    }

    @Override
    public void update(float deltaTime) {
        world.step(deltaTime, 6, 2);
    }

}
