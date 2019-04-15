package my.tamagochka.ufos.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import my.tamagochka.ufos.Components.*;

public class RenderingSystem extends EntitySystem {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    private ImmutableArray<Entity> hudCameraEntities;
    private ImmutableArray<Entity> cameraEntities;

    private ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    private ComponentMapper<LocationComponent> lm = ComponentMapper.getFor(LocationComponent.class);

    public RenderingSystem(SpriteBatch batch, OrthographicCamera camera, OrthographicCamera hudCamera) {
        this.batch = batch;
        this.camera = camera;
        this.hudCamera = hudCamera;
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
        // *** game entities drawing
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        cameraEntities.forEach(entity -> {
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
