import java.awt.Color;
import java.awt.Graphics;
public class Bullet {
    double x, y, dx, dy;
    int life = 90; // frames before disappearing

    public Bullet(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        dx = Math.cos(angle) * 5;
        dy = Math.sin(angle) * 5;
    }

    public void update() {
        x += dx;
        y += dy;
        life--;
    }

    public boolean isAlive() {
        return life > 0;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval((int)x, (int)y, 5, 5);
    }
}
