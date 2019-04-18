package my.tamagochka.ufos.Screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import my.tamagochka.ufos.Components.*;
import my.tamagochka.ufos.Game;
import my.tamagochka.ufos.Handlers.InputHandler;
import my.tamagochka.ufos.Systems.InputSystem;
import my.tamagochka.ufos.Systems.PhysicsSystem;
import my.tamagochka.ufos.Systems.RenderingSystem;
import my.tamagochka.ufos.Systems.UpdatingSystem;

import java.util.Random;

public class GameScreen implements Screen {

    private static final Vector2 WORLD_SIZE = new Vector2(2880, 1620);
    private static final int COUNT_STARS = 512;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private Engine engine;
    private Random random = new Random();
    private TextureAtlas atlas;

    public GameScreen(Game game) {

        batch = game.getBatch();
        camera = game.getCamera();
        hudCamera = game.getHudCamera();
        engine = game.getEngine();

        atlas = new TextureAtlas("star0.atlas");

        // *** entities
        // ** aim
        Entity aim = new Entity();
        aim.add(new TextureComponent("aim.png"));
        aim.add(new LocationComponent(new Vector2(hudCamera.viewportWidth / 2, hudCamera.viewportHeight / 2)));
        aim.add(new SizeComponent(aim.getComponent(TextureComponent.class).getSize()));
        aim.add(new AimComponent());
        aim.add(new HUDCameraComponent());
        engine.addEntity(aim);

        // ** stars
        int[][] rectangles = new int[COUNT_STARS][4];
        rectangles[0][0] = 0;
        rectangles[0][1] = 0;
        rectangles[0][2] = (int)WORLD_SIZE.x;
        rectangles[0][3] = (int)WORLD_SIZE.y;
        rectangles = splitRectangle(rectangles, 1, true, COUNT_STARS);

        Entity star;
        for(int i = 0; i < COUNT_STARS; i++) {
            star = new Entity();
            // TODO add more types of stars
            AnimationComponent animationComponent = new AnimationComponent(atlas, "star0", 0.1f);
            animationComponent.animatedSprite.stop();
            star.add(animationComponent);
            Vector2 size = new Vector2(star.getComponent(AnimationComponent.class).animatedSprite.getWidth(),
                    star.getComponent(AnimationComponent.class).animatedSprite.getHeight());
            star.add(new LocationComponent(
                    new Vector2(
                            random.nextInt(rectangles[i][2] - (int)size.x) + rectangles[i][0],
                            random.nextInt(rectangles[i][3] - (int)size.y) + rectangles[i][1]
                    )
            ));
            star.add(new SizeComponent(size));
            star.add(new StarComponent(star));
            star.add(new CameraComponent());
            engine.addEntity(star);
        }

            /* debug code */
/*
        star = new Entity();
        AnimationComponent animationComponent = new AnimationComponent(atlas, "star0", 0.1f);
        animationComponent.animatedSprite.stop();
        star.add(animationComponent);
        Vector2 size = new Vector2(star.getComponent(AnimationComponent.class).animatedSprite.getWidth(),
                star.getComponent(AnimationComponent.class).animatedSprite.getHeight());
        star.add(new LocationComponent(new Vector2(0, 0)));
        star.add(new SizeComponent(size));
        star.add(new StarComponent(star));
        star.add(new CameraComponent());
        engine.addEntity(star);
*/
            /* debug code */

        // ** player
        Entity player = new Entity();
        player.add(new LocationComponent(InputHandler.projectFromCamera(camera, new Vector2(camera.position.x, camera.position.y - 1))));
        player.add(new VelocityComponent(0, 1000));
        player.add(new DirectionComponent(0));
        player.add(new PlayerComponent());
        player.add(new PhysicsComponent(game.getWorld(), player.getComponent(LocationComponent.class).position, BodyDef.BodyType.DynamicBody));
        engine.addEntity(player);


        // *** systems
        EntitySystem renderingSystem = new RenderingSystem(batch, camera, hudCamera, WORLD_SIZE, game.getWorld());
        engine.addSystem(renderingSystem);

        EntitySystem aimingSystem = new InputSystem(hudCamera);
        engine.addSystem(aimingSystem);

        EntitySystem updatingSystem = new UpdatingSystem(random, camera, WORLD_SIZE);
        engine.addSystem(updatingSystem);

        EntitySystem physicsSystem = new PhysicsSystem(game.getWorld());
        engine.addSystem(physicsSystem);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


//        batch.begin();


        engine.update(delta);


//        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private int[][] splitRectangle(int[][] in_rectangles, int ind,
                                   boolean vertical, int count) {
        int[][] out_rectangles = new int[ind * 2 > count ? count : ind * 2][4];
        int n = 0;
        for(int i = 0; i < ind; i++) {
            out_rectangles[n][0] = in_rectangles[i][0];
            out_rectangles[n][1] = in_rectangles[i][1];
            out_rectangles[n][2] = in_rectangles[i][2] / (vertical ? 2 : 1);
            out_rectangles[n][3] = in_rectangles[i][3] / (vertical ? 1 : 2);
            n++;
            out_rectangles[n][0] = in_rectangles[i][0] + (vertical ? out_rectangles[n - 1][2] : 0);
            out_rectangles[n][1] = in_rectangles[i][1] + (vertical ? 0 : out_rectangles[n - 1][3]);
            out_rectangles[n][2] = out_rectangles[n - 1][2];
            out_rectangles[n][3] = out_rectangles[n - 1][3];
            n++;
            if(ind - i + n > count) {
                for(int j = i + 1; j < ind; j++) {
                    out_rectangles[n] = in_rectangles[j];
                    n++;
                }
                return out_rectangles;
            }
        }
        return splitRectangle(out_rectangles, n, !vertical, count);
    }

}
