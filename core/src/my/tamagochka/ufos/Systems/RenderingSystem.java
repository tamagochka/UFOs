package my.tamagochka.ufos.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import my.tamagochka.ufos.Components.*;

public class RenderingSystem extends EntitySystem {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private Vector2 worldSize;
    private World world;

    private ImmutableArray<Entity> hudCameraEntities;
    private ImmutableArray<Entity> cameraEntities;
    private Box2DDebugRenderer b2ddr;

    private ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    private ComponentMapper<LocationComponent> lm = ComponentMapper.getFor(LocationComponent.class);


    public RenderingSystem(SpriteBatch batch, OrthographicCamera camera, OrthographicCamera hudCamera,
                           Vector2 worldSize, World world) {
        this.batch = batch;
        this.camera = camera;
        this.hudCamera = hudCamera;
        this.worldSize = worldSize;
        this.world = world;
        b2ddr = new Box2DDebugRenderer();

    }

    @Override
    public void addedToEngine(Engine engine) {
        hudCameraEntities = engine.getEntitiesFor(Family.all(LocationComponent.class, HUDCameraComponent.class)
                .one(TextureComponent.class, AnimationComponent.class).get());
        cameraEntities = engine.getEntitiesFor(Family.all(LocationComponent.class, CameraComponent.class)
                .one(TextureComponent.class, AnimationComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        // *** physics debug renderer
        b2ddr.render(world, camera.combined);

        // *** game entities drawing
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        cameraEntities.forEach(entity -> {
            LocationComponent locationComponent = lm.get(entity);
            AnimationComponent animationComponent = am.get(entity);
            TextureComponent textureComponent = tm.get(entity);
            float camPositionX = locationComponent.position.x - camera.position.x + camera.viewportWidth / 2;
            float camPositionY = locationComponent.position.y - camera.position.y + camera.viewportHeight / 2;
            // draw main field
            if((camPositionX > 0) && (camPositionX < camera.viewportWidth) && (camPositionY > 0) && (camPositionY < camera.viewportHeight)) {
                if(textureComponent != null) {
                    batch.draw(textureComponent.texture, locationComponent.position.x, locationComponent.position.y);
                }
                if(animationComponent != null) {
                    animationComponent.animatedSprite.setPosition(locationComponent.position.x, locationComponent.position.y);
                    animationComponent.animatedSprite.draw(batch);
                }
            }
            // if camera on the edge of the world
            // draw right edge
            if(camera.position.x + camera.viewportWidth / 2 > worldSize.x) {
                if((camPositionX + worldSize.x > 0) && (camPositionX + worldSize.x < camera.viewportWidth) && (camPositionY > 0) && (camPositionY < camera.viewportHeight)) {
                    if(textureComponent != null) {
                        batch.draw(textureComponent.texture, locationComponent.position.x + worldSize.x, locationComponent.position.y);
                    }
                    if(animationComponent != null) {
                        animationComponent.animatedSprite.setPosition(
                                locationComponent.position.x + worldSize.x,
                                locationComponent.position.y);
                        animationComponent.animatedSprite.draw(batch);
                    }
                }
            }
            // draw left edge
            if(camera.position.x - camera.viewportWidth / 2 < 0) {
                if((camPositionX - worldSize.x > 0) && (camPositionX - worldSize.x < camera.viewportWidth) && (camPositionY > 0) && (camPositionY < camera.viewportHeight)) {
                    if(textureComponent != null) {
                        batch.draw(textureComponent.texture, locationComponent.position.x - worldSize.x, locationComponent.position.y);
                    }
                    if(animationComponent != null) {
                        animationComponent.animatedSprite.setPosition(
                                locationComponent.position.x - worldSize.x,
                                locationComponent.position.y);
                        animationComponent.animatedSprite.draw(batch);
                    }
                }
            }
            // draw top edge
            if(camera.position.y + camera.viewportHeight / 2 > worldSize.y) {
                if((camPositionX > 0) && (camPositionX < camera.viewportWidth) && (camPositionY + worldSize.y > 0) && (camPositionY + worldSize.y < camera.viewportHeight)) {
                    if(textureComponent != null) {
                        batch.draw(textureComponent.texture, locationComponent.position.x, locationComponent.position.y + worldSize.y);
                    }
                    if(animationComponent != null) {
                        animationComponent.animatedSprite.setPosition(
                                locationComponent.position.x,
                                locationComponent.position.y + worldSize.y);
                        animationComponent.animatedSprite.draw(batch);
                    }
                }
            }
            // draw bottom edge
            if(camera.position.y - camera.viewportHeight / 2 < 0) {
                if((camPositionX > 0) && (camPositionX < camera.viewportWidth) && (camPositionY - worldSize.y > 0) && (camPositionY - worldSize.y < camera.viewportHeight)) {
                    if(textureComponent != null) {
                        batch.draw(textureComponent.texture, locationComponent.position.x, locationComponent.position.y - worldSize.y);
                    }
                    if(animationComponent != null) {
                        animationComponent.animatedSprite.setPosition(
                                locationComponent.position.x,
                                locationComponent.position.y - worldSize.y);
                        animationComponent.animatedSprite.draw(batch);
                    }
                }
            }
            // draw left bottom corner
            if((camera.position.x - camera.viewportWidth / 2 < 0) && (camera.position.y - camera.viewportHeight / 2 < 0)) {
                if((camPositionX - worldSize.x > 0) && (camPositionX - worldSize.x < camera.viewportWidth) && (camPositionY - worldSize.y > 0) && (camPositionY - worldSize.y < camera.viewportHeight)) {
                    if(textureComponent != null) {
                        batch.draw(textureComponent.texture,
                                locationComponent.position.x - worldSize.x,
                                locationComponent.position.y - worldSize.y);
                    }
                    if(animationComponent != null) {
                        animationComponent.animatedSprite.setPosition(
                                locationComponent.position.x - worldSize.x,
                                locationComponent.position.y - worldSize.y);
                        animationComponent.animatedSprite.draw(batch);
                    }
                }
            }
            // draw right top corner
            if((camera.position.x + camera.viewportWidth / 2 > worldSize.x) && (camera.position.y + camera.viewportHeight / 2 > worldSize.y)) {
                if((camPositionX + worldSize.x > 0) && (camPositionX + worldSize.x < camera.viewportWidth) && (camPositionY + worldSize.y > 0) && (camPositionY + worldSize.y < camera.viewportHeight)) {
                    if(textureComponent != null) {
                        batch.draw(textureComponent.texture,
                                locationComponent.position.x + worldSize.x,
                                locationComponent.position.y + worldSize.y);
                    }
                    if(animationComponent != null) {
                        animationComponent.animatedSprite.setPosition(
                                locationComponent.position.x + worldSize.x,
                                locationComponent.position.y + worldSize.y);
                        animationComponent.animatedSprite.draw(batch);
                    }
                }
            }
            // draw left top corner
            if((camera.position.x - camera.viewportWidth / 2 < 0) && (camera.position.y + camera.viewportHeight / 2 > worldSize.y)) {
                if((camPositionX - worldSize.x > 0) && (camPositionX - worldSize.x < camera.viewportWidth) && (camPositionY + worldSize.y > 0) && (camPositionY + worldSize.y < camera.viewportHeight)) {
                    if(textureComponent != null) {
                        batch.draw(textureComponent.texture,
                                locationComponent.position.x - worldSize.x,
                                locationComponent.position.y + worldSize.y);
                    }
                    if(animationComponent != null) {
                        animationComponent.animatedSprite.setPosition(
                                locationComponent.position.x - worldSize.x,
                                locationComponent.position.y + worldSize.y);
                        animationComponent.animatedSprite.draw(batch);
                    }
                }
            }
            // draw right bottom corner
            if((camera.position.x + camera.viewportWidth / 2 > worldSize.x) && (camera.position.y - camera.viewportHeight / 2 < 0)) {
                if((camPositionX + worldSize.x > 0) && (camPositionX + worldSize.x < camera.viewportWidth) && (camPositionY - worldSize.y > 0) && (camPositionY - worldSize.y < camera.viewportHeight)) {
                    if(textureComponent != null) {
                        batch.draw(textureComponent.texture,
                                locationComponent.position.x + worldSize.x,
                                locationComponent.position.y - worldSize.y);
                    }
                    if(animationComponent != null) {
                        animationComponent.animatedSprite.setPosition(
                                locationComponent.position.x + worldSize.x,
                                locationComponent.position.y - worldSize.y);
                        animationComponent.animatedSprite.draw(batch);
                    }
                }
            }
        });
        batch.end();


        // *** HUD entities drawing
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        hudCameraEntities.forEach(entity -> {
            LocationComponent locationComponent = lm.get(entity);
            AnimationComponent animationComponent = am.get(entity);
            TextureComponent textureComponent = tm.get(entity);
            if(textureComponent != null)
                batch.draw(textureComponent.texture, locationComponent.position.x, locationComponent.position.y);
            if(animationComponent != null) {
                animationComponent.animatedSprite.setPosition(locationComponent.position.x, locationComponent.position.y);
                animationComponent.animatedSprite.draw(batch);
            }
        });
        batch.end();


    }
}
