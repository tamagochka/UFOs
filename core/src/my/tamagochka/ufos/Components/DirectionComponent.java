package my.tamagochka.ufos.Components;

import com.badlogic.ashley.core.Component;

public class DirectionComponent implements Component {

    public float angle;

    public DirectionComponent(float angle) {
        this.angle = angle;
    }

}
