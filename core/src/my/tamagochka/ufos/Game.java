package my.tamagochka.ufos;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import my.tamagochka.ufos.Components.PhysicsComponent;
import my.tamagochka.ufos.Handlers.InputHandler;
import my.tamagochka.ufos.Handlers.InputProcessor;
import my.tamagochka.ufos.Screens.GameScreen;

public class Game extends com.badlogic.gdx.Game {

    public static final boolean DEBUG_MODE = true;

    public static final String TITLE = "UFOs";
    public static final int WIDTH = 1440; // 960;
    public static final int HEIGHT = 810; //540;
    public static final float SCALE = 1f;

    public static final Vector2 WORLD_SIZE = new Vector2(2880, 1620);

    public static final int COUNT_STARS = 512;
    public static final int CHANCE_TO_BLINK_STAR = 5;
    public static final int MAX_FREQUENCY_BLINKING = 3;

    public static final int AIM_RADIUS = 100;


    public static final float PPM = 100f; // pixels per meter scale
    public static final int VELOCITY_ITERATIONS = 10;
    public static final int POSITION_ITERATIONS = 10;


    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private OrthographicCamera b2ddrCamera; // debug camera for physics models

    private Engine engine;
    private World world;

    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public OrthographicCamera getHudCamera() {
        return hudCamera;
    }

    public OrthographicCamera getB2ddrCamera() {
        return b2ddrCamera;
    }

    public Engine getEngine() {
        return engine;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void create() {
        // *** input
        Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(new InputProcessor());
        // *** graphics
        batch = new SpriteBatch();
        // *** camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        // *** hud camera
        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, WIDTH, HEIGHT);
        // *** debug camera for physics models
        if(DEBUG_MODE) {
            b2ddrCamera = new OrthographicCamera();
            b2ddrCamera.setToOrtho(false, WIDTH / PPM, HEIGHT / PPM);
        }
        // *** entities
        engine = new Engine();
        // *** physics
        world = new World(new Vector2(0, 0), true);



        // *** set start screen
        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
        InputHandler.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
