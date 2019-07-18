package my.tamagochka.ufos.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static my.tamagochka.ufos.Game.PPM;

public class PhysicsComponent implements Component {

    private Body body;

    public PhysicsComponent(World world, Vector2 position, BodyDef.BodyType bodyType, boolean active) {

        BodyDef bdef = new BodyDef();
        bdef.position.set(position.x / PPM, position.y / PPM);
        bdef.type = bodyType;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(30 / PPM, 15 / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;

        body = world.createBody(bdef);
        body.createFixture(fdef);

        body.setActive(active);

        body.setLinearDamping(1.0f);
        MassData massData = body.getMassData();
        massData.mass = 10;
        body.setMassData(massData);


    }

    public float getPositionX() {
        return body.getPosition().x * PPM;
    }

    public float getPositionY() {
        return body.getPosition().y * PPM;
    }

    public void setPositionX(float x) {
        body.setTransform(x / PPM, body.getPosition().y, body.getAngle());
    }

    public void setPositionY(float y) {
        body.setTransform(body.getPosition().x, y / PPM, body.getAngle());
    }

    public void applyForce(float dx, float dy) {
        body.applyForceToCenter(dx, dy, true);
    }

}
