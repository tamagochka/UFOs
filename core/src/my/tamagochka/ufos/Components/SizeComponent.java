package my.tamagochka.ufos.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class SizeComponent implements Component {

    public Vector2 size;

    public SizeComponent(Vector2 size) {
        this.size = size;
    }

}
