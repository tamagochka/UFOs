package my.tamagochka.ufos.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;

public class StarComponent implements Component {

    private Entity entity;
    private ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    private AnimationComponent animationComponent;

    private boolean blink = false;
    private boolean phase1 = false;
    private boolean phase2 = false;

    public StarComponent(Entity entity) {
        this.entity = entity;
        animationComponent = am.get(entity);
    }

    public void blink() {
        animationComponent.animatedSprite.stop();
        animationComponent.animatedSprite.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
        animationComponent.animatedSprite.play();
        blink = true;
        phase1 = true;
    }

    public boolean isBlink() {
        return blink;
    }

    public void update() {
        if(blink) {
            if(phase1) {
                if(!animationComponent.animatedSprite.isAnimationFinished()) {
                    phase1 = false;
                    phase2 = true;
                    animationComponent.animatedSprite.getAnimation().setPlayMode(Animation.PlayMode.REVERSED);
                    animationComponent.animatedSprite.play();
                }
            }
            if(phase2) {
                if(!animationComponent.animatedSprite.isPlaying()) {
                    phase2 = false;
                    blink = false;
                }
            }
        }
    }


}
