package my.tamagochka.ufos.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import my.tamagochka.ufos.Components.*;

public class RenderingSystem extends EntitySystem {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private Vector2 wordSize;

    private ImmutableArray<Entity> hudCameraEntities;
    private ImmutableArray<Entity> cameraEntities;

    private ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    private ComponentMapper<LocationComponent> lm = ComponentMapper.getFor(LocationComponent.class);

    public RenderingSystem(SpriteBatch batch, OrthographicCamera camera, OrthographicCamera hudCamera, Vector2 wordSize) {
        this.batch = batch;
        this.camera = camera;
        this.hudCamera = hudCamera;
        this.wordSize = wordSize;
    }

    @Override
    public void addedToEngine(Engine engine) {
        hudCameraEntities = engine.getEntitiesFor(Family.all(LocationComponent.class, HUDCameraComponent.class)
                .one(TextureComponent.class, AnimationComponent.class).get());
        cameraEntities  = engine.getEntitiesFor(Family.all(LocationComponent.class, CameraComponent.class)
                .one(TextureComponent.class, AnimationComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

            /* debug code */
        camera.translate(-2, -2);
        camera.update();
        System.out.println(camera.position);
            /* debug code */

        // *** looping camera move
        if(camera.position.x - camera.viewportWidth / 2 < 0) {
            camera.position.x = camera.position.x + wordSize.x;
            camera.update();
        }
        if(camera.position.y - camera.viewportHeight / 2 < 0) {
            camera.position.y = camera.position.y + wordSize.y;
            camera.update();
        }
        if(camera.position.x - camera.viewportWidth / 2 > wordSize.x) {
            camera.position.x = camera.position.x - wordSize.x;
            camera.update();
        }
        if(camera.position.y - camera.viewportHeight / 2 > wordSize.y) {
            camera.position.y = camera.position.y - wordSize.y;
            camera.update();
        }

        // *** game entities drawing
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        cameraEntities.forEach(entity -> {
            LocationComponent locationComponent = lm.get(entity);
            AnimationComponent animationComponent = am.get(entity);
            TextureComponent textureComponent = tm.get(entity);
            if(textureComponent != null) {
                batch.draw(textureComponent.texture, locationComponent.position.x, locationComponent.position.y);
            }
            if(animationComponent != null) {
                animationComponent.animatedSprite.setPosition(locationComponent.position.x, locationComponent.position.y);
                animationComponent.animatedSprite.draw(batch);
            }
            // if camera on the edge of the world
            if(camera.position.x + camera.viewportWidth / 2 > wordSize.x) {
                if(textureComponent != null) {
                    batch.draw(textureComponent.texture, locationComponent.position.x + wordSize.x, locationComponent.position.y);
                }
                if(animationComponent != null) {
                    animationComponent.animatedSprite.setPosition(
                            locationComponent.position.x + wordSize.x,
                            locationComponent.position.y);
                    animationComponent.animatedSprite.draw(batch);
                }
            }
            if(camera.position.y + camera.viewportHeight > wordSize.y) {
                if(textureComponent != null) {
                    batch.draw(textureComponent.texture, locationComponent.position.x, locationComponent.position.y  + wordSize.y);
                }
                if(animationComponent != null) {
                    animationComponent.animatedSprite.setPosition(
                            locationComponent.position.x,
                            locationComponent.position.y + wordSize.y);
                    animationComponent.animatedSprite.draw(batch);
                }
            }
            if(camera.position.x + camera.viewportWidth > wordSize.x && camera.position.y + camera.viewportHeight > wordSize.y) {
                if(textureComponent != null) {
                    batch.draw(textureComponent.texture, locationComponent.position.x + wordSize.x, locationComponent.position.y  + wordSize.y);
                }
                if(animationComponent != null) {
                    animationComponent.animatedSprite.setPosition(
                            locationComponent.position.x + wordSize.x,
                            locationComponent.position.y + wordSize.y);
                    animationComponent.animatedSprite.draw(batch);
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
