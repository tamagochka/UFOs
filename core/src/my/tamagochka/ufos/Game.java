package my.tamagochka.ufos;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import my.tamagochka.ufos.Handlers.InputHandler;
import my.tamagochka.ufos.Handlers.InputProcessor;
import my.tamagochka.ufos.Screens.GameScreen;

public class Game extends com.badlogic.gdx.Game {

    public static final String TITLE = "UFOs";
    public static final int WIDTH = 1440; // 960;
    public static final int HEIGHT = 810; //540;
    public static final float SCALE = 1f;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private Engine engine;

    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public OrthographicCamera getHudCamera() {
        return hudCamera;
    }

    public Engine getEngine() {
        return engine;
    }

    @Override
    public void create() {
        Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(new InputProcessor());
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
//        camera.position.x = 0; camera.position.y = 0;
//        camera.update();
        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, WIDTH, HEIGHT);
//        hudCamera.position.x = 0; hudCamera.position.y = 0;
//        hudCamera.update();
        engine = new Engine();


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
