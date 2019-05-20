package my.tamagochka.ufos.Components;

import com.badlogic.ashley.core.Component;

public class VelocityComponent implements Component {

    public float curVelocity;
    public float maxVelocity;
    public float stepVelocity;

    public VelocityComponent(float curVelocity, float maxVelocity, float stepVelocity) {
        this.curVelocity = curVelocity;
        this.maxVelocity = maxVelocity;
        this.stepVelocity = stepVelocity;
    }

    public void increaseVelocity() {
        if(curVelocity < maxVelocity) {
            curVelocity += stepVelocity;
        }
    }

    public void decreaseVelocity() {
        if(curVelocity > 0) {
            curVelocity -= stepVelocity;
        }
        if(curVelocity < 0) {
            curVelocity = 0;
        }
    }

}
