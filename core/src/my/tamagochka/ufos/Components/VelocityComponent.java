package my.tamagochka.ufos.Components;

import com.badlogic.ashley.core.Component;

public class VelocityComponent implements Component {

    public float curVelocity;
    public float maxVelocity;

    public VelocityComponent(float curVelocity, float maxVelocity) {
        this.curVelocity = curVelocity;
        this.maxVelocity = maxVelocity;
    }

}
