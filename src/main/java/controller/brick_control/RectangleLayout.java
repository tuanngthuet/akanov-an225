package controller.brick_control;

import controller.InitVari;

import java.util.List;

public class RectangleLayout implements BrickSpawner, BrickVari {
    @Override
    public void spawn(List<Brick> brickList) {
        if (!brickList.isEmpty()) return;

        int bricksPerRow = (InitVari.SCREEN_WIDTH - BRICK_GAP) / (BRICK_WIDTH + BRICK_GAP);
        double totalWidth = bricksPerRow * BRICK_WIDTH + (bricksPerRow - 1) * BRICK_GAP;
        double startX = (InitVari.SCREEN_WIDTH - totalWidth) / 2;
        double startY = BRICK_GAP * 2;

        BrickManager manager = BrickManager.getInstance();

        for (int row = 0; row < BRICK_ROWS; row++) {
            for (int col = 0; col < bricksPerRow; col++) {
                double x = startX + col * (BRICK_WIDTH + BRICK_GAP);
                double y = startY + row * (BRICK_HEIGHT + BRICK_GAP * 1.5);
                if (BrickManager.isSpawn())
                    manager.createBrick((int) x, (int) y, BrickManager.getRandomBrickType());
            }
        }
    }
}
