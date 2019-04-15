package my.tamagochka.ufos.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import my.tamagochka.ufos.Components.AnimationComponent;
import my.tamagochka.ufos.Components.LocationComponent;
import my.tamagochka.ufos.Components.TextureComponent;

public class RenderingSystem extends EntitySystem {

    private SpriteBatch batch;
    private OrthographicCamera hudCamera;

    private ImmutableArray<Entity> entities;

    private ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    private ComponentMapper<LocationComponent> lm = ComponentMapper.getFor(LocationComponent.class);

    public RenderingSystem(SpriteBatch batch, OrthographicCamera hudCamera) {
        this.batch = batch;
        this.hudCamera = hudCamera;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(LocationComponent.class)
                .one(TextureComponent.class, AnimationComponent.class).get());


    }

    @Override
    public void update(float deltaTime) {

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        entities.forEach(entity -> {
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
