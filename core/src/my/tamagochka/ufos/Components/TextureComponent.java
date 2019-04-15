package my.tamagochka.ufos.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class TextureComponent implements Component {

    public Texture texture;

    public TextureComponent(String pathToTexture) {
        texture = new Texture(pathToTexture);
    }

    public Vector2 getSize() {
        return new Vector2(texture.getWidth(), texture.getHeight());
    }

}
