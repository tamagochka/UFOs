package my.tamagochka.ufos.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import my.tamagochka.ufos.Components.*;
import my.tamagochka.ufos.Game;

public class RenderingSystem extends EntitySystem {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private OrthographicCamera b2ddrCamera;
    private Vector2 worldSize;
    private World world;

    private ImmutableArray<Entity> hudCameraEntities;
    private ImmutableArray<Entity> cameraEntities;

    private Box2DDebugRenderer b2ddr; // debug renderer for physics models

    private ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);

    public RenderingSystem(SpriteBatch batch, OrthographicCamera camera, OrthographicCamera hudCamera,
                           OrthographicCamera b2ddrCamera, Vector2 worldSize, World world) {
        this.batch = batch;
        this.camera = camera;
        this.hudCamera = hudCamera;
        this.worldSize = worldSize;
        this.world = world;
        this.b2ddrCamera = b2ddrCamera;
        if(Game.DEBUG_MODE) // debug renderer for physics models
            b2ddr = new Box2DDebugRenderer();

    }

    @Override
    public void addedToEngine(Engine engine) {
        hudCameraEntities = engine.getEntitiesFor(
                Family.all(PhysicsComponent.class, HUDCameraComponent.class, AnimationComponent.class).get());
        cameraEntities = engine.getEntitiesFor(
                Family.all(PhysicsComponent.class, CameraComponent.class, AnimationComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        // *** physics debug renderer
        if(Game.DEBUG_MODE)
            b2ddr.render(world, b2ddrCamera.combined);

        // *** game entities drawing
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        cameraEntities.forEach(entity -> {

            AnimationComponent animationComponent = am.get(entity);
            PhysicsComponent physicsComponent = pm.get(entity);

            float camPositionX = physicsComponent.getPositionX() - camera.position.x + camera.viewportWidth / 2;
            float camPositionY = physicsComponent.getPositionY() - camera.position.y + camera.viewportHeight / 2;

            // draw main field
            if((camPositionX > 0) && (camPositionX < camera.viewportWidth) && (camPositionY > 0) && (camPositionY < camera.viewportHeight)) {
                animationComponent.animatedSprite.setPosition(
                        physicsComponent.getPositionX() - animationComponent.getSize().x / 2,
                        physicsComponent.getPositionY() - animationComponent.getSize().y / 2
                );
                animationComponent.animatedSprite.draw(batch);
            }
            // if camera on the edge of the world
            // draw right edge
            if(camera.position.x + camera.viewportWidth / 2 > worldSize.x) {
                if((camPositionX + worldSize.x > 0) && (camPositionX + worldSize.x < camera.viewportWidth) && (camPositionY > 0) && (camPositionY < camera.viewportHeight)) {
                    animationComponent.animatedSprite.setPosition(
                            physicsComponent.getPositionX() + worldSize.x - animationComponent.getSize().x / 2,
                            physicsComponent.getPositionY()  - animationComponent.getSize().y / 2
                    );
                    animationComponent.animatedSprite.draw(batch);
                }
            }
            // draw left edge
            if(camera.position.x - camera.viewportWidth / 2 < 0) {
                if((camPositionX - worldSize.x > 0) && (camPositionX - worldSize.x < camera.viewportWidth) && (camPositionY > 0) && (camPositionY < camera.viewportHeight)) {
                    animationComponent.animatedSprite.setPosition(
                            physicsComponent.getPositionX() - worldSize.x - animationComponent.getSize().x / 2,
                            physicsComponent.getPositionY() - animationComponent.getSize().y / 2
                    );
                    animationComponent.animatedSprite.draw(batch);
                }
            }
            // draw top edge
            if(camera.position.y + camera.viewportHeight / 2 > worldSize.y) {
                if((camPositionX > 0) && (camPositionX < camera.viewportWidth) && (camPositionY + worldSize.y > 0) && (camPositionY + worldSize.y < camera.viewportHeight)) {
                    animationComponent.animatedSprite.setPosition(
                            physicsComponent.getPositionX() - animationComponent.getSize().x / 2,
                            physicsComponent.getPositionY() + worldSize.y - animationComponent.getSize().y / 2
                    );
                    animationComponent.animatedSprite.draw(batch);
                }
            }
            // draw bottom edge
            if(camera.position.y - camera.viewportHeight / 2 < 0) {
                if((camPositionX > 0) && (camPositionX < camera.viewportWidth) && (camPositionY - worldSize.y > 0) && (camPositionY - worldSize.y < camera.viewportHeight)) {
                    animationComponent.animatedSprite.setPosition(
                            physicsComponent.getPositionX()  - animationComponent.getSize().x / 2,
                            physicsComponent.getPositionY() - worldSize.y - animationComponent.getSize().y / 2
                    );
                    animationComponent.animatedSprite.draw(batch);
                }
            }
            // draw left bottom corner
            if((camera.position.x - camera.viewportWidth / 2 < 0) && (camera.position.y - camera.viewportHeight / 2 < 0)) {
                if((camPositionX - worldSize.x > 0) && (camPositionX - worldSize.x < camera.viewportWidth) && (camPositionY - worldSize.y > 0) && (camPositionY - worldSize.y < camera.viewportHeight)) {
                    animationComponent.animatedSprite.setPosition(
                            physicsComponent.getPositionX() - worldSize.x - animationComponent.getSize().x / 2,
                            physicsComponent.getPositionY() - worldSize.y - animationComponent.getSize().y / 2
                    );
                    animationComponent.animatedSprite.draw(batch);
                }
            }
            // draw right top corner
            if((camera.position.x + camera.viewportWidth / 2 > worldSize.x) && (camera.position.y + camera.viewportHeight / 2 > worldSize.y)) {
                if((camPositionX + worldSize.x > 0) && (camPositionX + worldSize.x < camera.viewportWidth) && (camPositionY + worldSize.y > 0) && (camPositionY + worldSize.y < camera.viewportHeight)) {
                    animationComponent.animatedSprite.setPosition(
                            physicsComponent.getPositionX() + worldSize.x - animationComponent.getSize().x / 2,
                            physicsComponent.getPositionY() + worldSize.y - animationComponent.getSize().y / 2
                    );
                    animationComponent.animatedSprite.draw(batch);
                }
            }
            // draw left top corner
            if((camera.position.x - camera.viewportWidth / 2 < 0) && (camera.position.y + camera.viewportHeight / 2 > worldSize.y)) {
                if((camPositionX - worldSize.x > 0) && (camPositionX - worldSize.x < camera.viewportWidth) && (camPositionY + worldSize.y > 0) && (camPositionY + worldSize.y < camera.viewportHeight)) {
                    animationComponent.animatedSprite.setPosition(
                            physicsComponent.getPositionX() - worldSize.x - animationComponent.getSize().x / 2,
                            physicsComponent.getPositionY() + worldSize.y - animationComponent.getSize().y / 2
                    );
                    animationComponent.animatedSprite.draw(batch);
                }
            }
            // draw right bottom corner
            if((camera.position.x + camera.viewportWidth / 2 > worldSize.x) && (camera.position.y - camera.viewportHeight / 2 < 0)) {
                if((camPositionX + worldSize.x > 0) && (camPositionX + worldSize.x < camera.viewportWidth) && (camPositionY - worldSize.y > 0) && (camPositionY - worldSize.y < camera.viewportHeight)) {
                    animationComponent.animatedSprite.setPosition(
                            physicsComponent.getPositionX() + worldSize.x - animationComponent.getSize().x / 2,
                            physicsComponent.getPositionY() - worldSize.y - animationComponent.getSize().y / 2
                    );
                    animationComponent.animatedSprite.draw(batch);
                }
            }
        });
        batch.end();
    }
}
