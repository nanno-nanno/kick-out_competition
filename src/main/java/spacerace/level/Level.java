package spacerace.level;

import java.io.Serializable;
import java.util.List;

import spacerace.domain.Rectangle2D;
import spacerace.domain.Vector2D;

public class Level implements Serializable {

    public static int WIDTH  = 900;
    public static int HEIGHT = 600;

    private int               number;
    private List<Rectangle2D> rectangles;
    private Vector2D          startPosition;
    private Rectangle2D       goal;

    public Level() {
        // For JSON conversion
    }

    public Level(final int number, final List<Rectangle2D> rectangles, final Vector2D startPosition, final Rectangle2D goal) {
        this.number = number;
        this.rectangles = rectangles;
        this.startPosition = startPosition;
        this.goal = goal;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(final int number) {
        this.number = number;
    }

    public List<Rectangle2D> getRectangles() {
        return rectangles;
    }

    public void setRectangles(final List<Rectangle2D> rectangles) {
        this.rectangles = rectangles;
    }

    public Vector2D getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(final Vector2D startPosition) {
        this.startPosition = startPosition;
    }

    public Rectangle2D getGoal() {
        return goal;
    }

    public void setGoal(final Rectangle2D goal) {
        this.goal = goal;
    }
}
