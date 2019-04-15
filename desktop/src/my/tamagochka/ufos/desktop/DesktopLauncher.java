package my.tamagochka.ufos.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import my.tamagochka.ufos.Game;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = Game.TITLE;
        config.width = (int)(Game.WIDTH * Game.SCALE);
        config.height = (int)(Game.HEIGHT * Game.SCALE);


        new LwjglApplication(new Game(), config);
    }
}
