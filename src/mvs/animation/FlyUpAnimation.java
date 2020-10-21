package mvs.animation;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsObject;

public class FlyUpAnimation extends GraphicsGroup {
    private final GraphicsObject content;
    private final double flyRate, minSpeed;

    public FlyUpAnimation(GraphicsObject content, double flyRate, double minSpeed) {
        add(content);
        this.content = content;
        this.flyRate = flyRate;
        this.minSpeed = minSpeed;
    }

    public void animate(double dt) {
        content.setY(
            Math.max(0,
                content.getY() * Math.pow(flyRate, dt) - minSpeed * dt));
        content.setRotation(content.getRotation() * Math.pow(flyRate, dt));
    }
}
