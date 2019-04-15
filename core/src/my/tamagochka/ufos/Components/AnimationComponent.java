package my.tamagochka.ufos.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class AnimationComponent implements Component {

    public AnimatedSprite animatedSprite;

    public AnimationComponent(TextureAtlas atlas, String regionName, float frameDuration) {
        Animation<TextureRegion> animation = new Animation<TextureRegion>(frameDuration, atlas.findRegions(regionName));
        animatedSprite = new AnimatedSprite(animation);
    }



}
