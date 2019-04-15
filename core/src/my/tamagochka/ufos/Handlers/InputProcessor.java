package my.tamagochka.ufos.Handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputProcessor extends InputAdapter {

    public boolean mouseMoved(int screenX, int screenY) {
        InputHandler.MOUSE_POS.x = screenX;
        InputHandler.MOUSE_POS.y = screenY;
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        InputHandler.MOUSE_POS.x = screenX;
        InputHandler.MOUSE_POS.y = screenY;
        if(button == Input.Buttons.LEFT)
            InputHandler.MOUSE_LEFT = true;
        if(button == Input.Buttons.RIGHT)
            InputHandler.MOUSE_RIGHT = true;
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        InputHandler.MOUSE_POS.x = screenX;
        InputHandler.MOUSE_POS.y = screenY;
        if(button == Input.Buttons.LEFT)
            InputHandler.MOUSE_LEFT = false;
        if(button == Input.Buttons.RIGHT)
            InputHandler.MOUSE_RIGHT = false;
        return false;
    }

    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ESCAPE)
            InputHandler.setKey(0, true);
        return false;
    }

    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.ESCAPE)
            InputHandler.setKey(0, false);
        return false;
    }

}
