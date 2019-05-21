package my.tamagochka.ufos.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.World;
import my.tamagochka.ufos.Components.DirectionComponent;
import my.tamagochka.ufos.Components.LocationComponent;
import my.tamagochka.ufos.Components.PhysicsComponent;
import my.tamagochka.ufos.Components.VelocityComponent;
import my.tamagochka.ufos.Game;

public class PhysicsSystem extends EntitySystem {

    private World world;

    private ImmutableArray<Entity> entities;

    private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<DirectionComponent> dm = ComponentMapper.getFor(DirectionComponent.class);
    private ComponentMapper<LocationComponent> lm = ComponentMapper.getFor(LocationComponent.class);

    public PhysicsSystem(World world) {
        this.world = world;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PhysicsComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        entities.forEach(entity -> {
            PhysicsComponent physicsComponent = pm.get(entity);
            VelocityComponent velocityComponent = vm.get(entity);
            DirectionComponent directionComponent = dm.get(entity);
            LocationComponent locationComponent = lm.get(entity);
            float dx = velocityComponent.curVelocity * (float)Math.cos(directionComponent.angle);
            float dy = velocityComponent.curVelocity * (float)Math.sin(directionComponent.angle);
            physicsComponent.applyForce(dx, dy);
            locationComponent.position.set(physicsComponent.getPosition());
        });
        world.step(deltaTime, Game.VELOCITY_ITERATIONS, Game.POSITION_ITERATIONS);
    }

}
