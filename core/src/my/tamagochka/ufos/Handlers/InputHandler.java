package my.tamagochka.ufos.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class InputHandler {

    public static final int NUM_KEYS = 2;

    public static final int KEY_ESC = 0;


    public static boolean[] keys;
    public static boolean[] pkeys;

    public static boolean MOUSE_RIGHT = false;
    public static boolean MOUSE_LEFT = false;

    public static Vector2 MOUSE_POS = new Vector2(0, 0);

    static {
        keys = new boolean[NUM_KEYS];
        pkeys = new boolean[NUM_KEYS];
    }

    public static void update() {
        for(int i = 0; i < NUM_KEYS; i++) {
            pkeys[i] = keys[i];
        }
    }

    public static void setKey(int i, boolean b) {
        keys[i] = b;
    }

    public static boolean isDown(int i) {
        return keys[i];
    }

    public static boolean isPressed(int i) {
        return keys[i] && !pkeys[i];
    }

    public static void setMousePos(Vector2 position) {
        Gdx.input.setCursorPosition((int)position.x, (int)position.y);
    }

    private static Vector3 tmp = new Vector3();

    public static void unprojectToCamera(OrthographicCamera camera, Vector2 vector) {
        tmp.x = vector.x;
        tmp.y = vector.y;
        tmp = camera.unproject(tmp);
        vector.x = tmp.x;
        vector.y = tmp.y;
    }

    public static void projectFromCamera(OrthographicCamera camera, Vector2 vector) {
        tmp.x = vector.x;
        tmp.y = camera.viewportHeight - vector.y;
        tmp = camera.project(tmp);
        vector.x = tmp.x;
        vector.y = tmp.y - 1;
    }

}
