package my.tamagochka.ufos.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class AnimationComponent implements Component {

    public AnimatedSprite animatedSprite;
    public Vector2 size;

    public AnimationComponent(TextureAtlas atlas, String regionName, float frameDuration) {
        Animation<TextureRegion> animation = new Animation<TextureRegion>(frameDuration, atlas.findRegions(regionName));
        animatedSprite = new AnimatedSprite(animation);
        size = new Vector2(animatedSprite.getWidth(), animatedSprite.getHeight());
    }

    public Vector2 getSize() {
        return size;
    }

}
