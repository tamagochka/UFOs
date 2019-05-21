package my.tamagochka.ufos.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import my.tamagochka.ufos.Components.*;
import my.tamagochka.ufos.Game;
import my.tamagochka.ufos.Handlers.InputHandler;

public class InputSystem extends EntitySystem {

    private OrthographicCamera hudCamera;

    private ComponentMapper<LocationComponent> lm = ComponentMapper.getFor(LocationComponent.class);
    private ComponentMapper<SizeComponent> sm = ComponentMapper.getFor(SizeComponent.class);
    private ComponentMapper<DirectionComponent> dm = ComponentMapper.getFor(DirectionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private ImmutableArray<Entity> entities;

    private Entity aim;
    private Entity player;

    private Vector2 mousePosition;
    private Vector2 viewportCenter;

    public InputSystem(OrthographicCamera hudCamera) {
        this.hudCamera = hudCamera;
        mousePosition = new Vector2();
        viewportCenter = new Vector2(hudCamera.viewportWidth / 2, hudCamera.viewportHeight / 2);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(AimComponent.class).get());
        aim = entities.first(); // get aim entity
        entities = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
        player = entities.first(); // get player entity


    }

    @Override
    public void update(float deltaTime) {
        // *** calculating direction moving, aim and mouse position
        mousePosition.x = InputHandler.MOUSE_POS.x;
        mousePosition.y = InputHandler.MOUSE_POS.y;
        InputHandler.unprojectToCamera(hudCamera, mousePosition);
        float x1 = viewportCenter.x, y1 = viewportCenter.y, x2 = mousePosition.x, y2 = mousePosition.y;
        float alpha = 0;
        if(x1 != x2) {
            if(y1 != y2) {
                float k = (y2 - y1) / (x2 - x1);
                alpha = k >= 0 ? (float)Math.atan(k) : (float)Math.PI - (float)Math.atan(Math.abs(k));
                alpha = y2 < y1 ? (float)Math.PI + alpha : alpha;
            } else {
                alpha = x2 < x1 ? (float)Math.PI : 0;
            }
        } else {
            alpha = y2 < y1 ? 3 * (float)Math.PI / 2 : (float)Math.PI / 2;
        }
        float dx = (float)Math.cos(alpha);
        float dy = (float)Math.sin(alpha);
        mousePosition.x = viewportCenter.x + dx * Game.AIM_RADIUS;
        mousePosition.y = viewportCenter.y + dy * Game.AIM_RADIUS;

            /*debug code*/
        if(Game.DEBUG_MODE) {
            Gdx.gl.glLineWidth(2);
            ShapeRenderer renderer = new ShapeRenderer();
            renderer.setProjectionMatrix(hudCamera.combined);
            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(new Color(1, 0, 0, 0));
            renderer.line(viewportCenter, mousePosition);
            renderer.end();
            Gdx.gl.glLineWidth(1);
        }
            /*debug code*/

        // *** set player direction moving
        DirectionComponent directionComponent = dm.get(player);
        directionComponent.angle = alpha;

        // *** aim and mouse handling
        LocationComponent locationComponent = lm.get(aim);
        SizeComponent sizeComponent = sm.get(aim);
        locationComponent.position.x = mousePosition.x - sizeComponent.size.x / 2;
        locationComponent.position.y = mousePosition.y - sizeComponent.size.y / 2;
        InputHandler.projectFromCamera(hudCamera, mousePosition);
        Gdx.input.setCursorPosition((int)mousePosition.x, (int)mousePosition.y);

        // *** mouse input handling
        VelocityComponent velocityComponent = vm.get(player);
        if(InputHandler.MOUSE_RIGHT) { // acceleration
            velocityComponent.increaseVelocity();
        } else {
            velocityComponent.decreaseVelocity();
        }

        if(InputHandler.MOUSE_LEFT) { // shooting

        }



        // *** keyboard handling
        if(InputHandler.isPressed(InputHandler.KEY_ESC)) {
            Gdx.app.exit();
        }


    }
}
