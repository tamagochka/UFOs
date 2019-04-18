package my.tamagochka.ufos.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PhysicsComponent implements Component {

    public static final float PPM = 100f;

    public PhysicsComponent(World world, Vector2 position, BodyDef.BodyType bodyType) {

        BodyDef bdef = new BodyDef();
        bdef.position.set(position.x/* / PPM*/, position.y/* / PPM*/);
        bdef.type = bodyType;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10/* / PPM*/, 10/* / PPM*/);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;

        Body body = world.createBody(bdef);
        body.createFixture(fdef);


    }
}
