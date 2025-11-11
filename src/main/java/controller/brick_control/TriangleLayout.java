package controller.brick_control;

import controller.InitVari;

import java.util.List;

public class TriangleLayout implements BrickSpawner, BrickVari{
    @Override
    public void spawn(List<Brick> brickList) {
        if (!brickList.isEmpty()) return;

        BrickManager manager = BrickManager.getInstance();

        for (int row = 0; row < BRICK_ROWS + 2; row++) {
            int bricksInRow = row + 1;
            double totalWidth = bricksInRow * BRICK_WIDTH + (bricksInRow - 1) * BRICK_GAP;
            double startX = (InitVari.SCREEN_WIDTH - totalWidth) / 2;
            double startY = BRICK_GAP * 2;

            for (int col = 0; col < bricksInRow; col++) {
                double x = startX + col * (BRICK_WIDTH + BRICK_GAP);
                double y = startY + row * (BRICK_HEIGHT + BRICK_GAP);
                if (BrickManager.isSpawn())
                    manager.createBrick((int) x, (int) y, BrickManager.getRandomBrickType());
            }
        }
    }
}
