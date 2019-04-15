package my.tamagochka.ufos.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class LocationComponent implements Component {

    public Vector2 position;

    public LocationComponent(Vector2 position) {
        this.position = position;
    }

}
