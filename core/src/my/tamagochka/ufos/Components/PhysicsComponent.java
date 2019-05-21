package my.tamagochka.ufos.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static my.tamagochka.ufos.Game.PPM;

public class PhysicsComponent implements Component {

    private Body body;

    public PhysicsComponent(World world, Vector2 position, BodyDef.BodyType bodyType) {

        BodyDef bdef = new BodyDef();
        bdef.position.set(position.x / PPM, position.y / PPM);
        bdef.type = bodyType;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10 / PPM, 10 / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;

        body = world.createBody(bdef);
        body.createFixture(fdef);

        body.setLinearDamping(1.0f);
        MassData massData = body.getMassData();
        massData.mass = 10;
        body.setMassData(massData);


    }

    // variable for scaling from physics world to graphics and back
    private Vector2 positionCalculator = new Vector2();

    public Vector2 getPosition() {
        positionCalculator.set(body.getPosition());
        positionCalculator.x *= PPM;
        positionCalculator.y *= PPM;
        return positionCalculator;
    }

    public void setPosition(Vector2 position) {
        positionCalculator.set(position);
        positionCalculator.x /= PPM;
        positionCalculator.y /= PPM;
        body.setTransform(positionCalculator, body.getAngle());
    }

    public void applyForce(float dx, float dy) {
        body.applyForceToCenter(dx, dy, true);
    }

}
