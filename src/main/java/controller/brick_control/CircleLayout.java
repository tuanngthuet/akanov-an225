package controller.brick_control;

import controller.InitVari;

import java.util.List;

public class CircleLayout implements BrickSpawner, BrickVari{
    @Override
    public void spawn(List<Brick> bricks) {
        if (!bricks.isEmpty()) return;

        BrickManager manager = BrickManager.getInstance();
        int centerX = InitVari.SCREEN_WIDTH / 2;
        int centerY = 200;
        double maxRadius = 150;
        double brickW = BrickVari.BRICK_WIDTH;
        double brickH = BrickVari.BRICK_HEIGHT;
        double gap = BrickVari.BRICK_GAP;

        for (double r = 0; r <= maxRadius; r += brickH + gap) {

            double circumference = 2 * Math.PI * r;
            double bricksInCircle = circumference / (brickW + gap);
            double angleStep = 360.0 / Math.max(bricksInCircle, 1);

            for (double angle = 0; angle < 360; angle += angleStep) {
                double rad = Math.toRadians(angle);
                int x = (int) (centerX + r * Math.cos(rad));
                int y = (int) (centerY + r * Math.sin(rad));
                manager.createBrick(x, y, BrickManager.getRandomBrickType());
            }
        }
    }
}
