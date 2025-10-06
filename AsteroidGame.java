import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class AsteroidGame extends JPanel implements ActionListener, KeyListener {
    Timer timer;
    Spaceship ship;
    ArrayList<Asteroid> asteroids = new ArrayList<>();
    boolean left, right, up;

    public AsteroidGame() {
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(800, 600));
        addKeyListener(this);
        ship = new Spaceship(400, 300);
        for (int i = 0; i < 5; i++) asteroids.add(new Asteroid());
        timer = new Timer(20, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ship.draw(g);
        for (Asteroid a : asteroids) a.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (left) ship.rotate(-5);
        if (right) ship.rotate(5);
        if (up) ship.thrust();
        ship.update();
        for (Asteroid a : asteroids) a.update();
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> left = true;
            case KeyEvent.VK_RIGHT -> right = true;
            case KeyEvent.VK_UP -> up = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> left = false;
            case KeyEvent.VK_RIGHT -> right = false;
            case KeyEvent.VK_UP -> up = false;
        }
    }

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Asteroid Game");
        AsteroidGame game = new AsteroidGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class Spaceship {
    double x, y, angle, dx, dy;

    public Spaceship(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void rotate(double degrees) {
        angle += Math.toRadians(degrees);
    }

    public void thrust() {
        dx += Math.cos(angle) * 0.5;
        dy += Math.sin(angle) * 0.5;
    }

    public void update() {
        x += dx;
        y += dy;
        wrap();
    }

    public void wrap() {
        if (x < 0) x += 800;
        if (x > 800) x -= 800;
        if (y < 0) y += 600;
        if (y > 600) y -= 600;
    }

    public void draw(Graphics g) {
        int[] xs = {(int)(x + 10 * Math.cos(angle)), (int)(x - 10 * Math.cos(angle + Math.PI / 2)), (int)(x - 10 * Math.cos(angle - Math.PI / 2))};
        int[] ys = {(int)(y + 10 * Math.sin(angle)), (int)(y - 10 * Math.sin(angle + Math.PI / 2)), (int)(y - 10 * Math.sin(angle - Math.PI / 2))};
        g.setColor(Color.WHITE);
        g.fillPolygon(xs, ys, 3);
    }
}

class Asteroid {
    double x, y, dx, dy;
    Random rand = new Random();

    public Asteroid() {
        x = rand.nextInt(800);
        y = rand.nextInt(600);
        dx = rand.nextDouble() * 2 - 1;
        dy = rand.nextDouble() * 2 - 1;
    }

    public void update() {
        x += dx;
        y += dy;
        if (x < 0) x += 800;
        if (x > 800) x -= 800;
        if (y < 0) y += 600;
        if (y > 600) y -= 600;
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval((int)x, (int)y, 30, 30);
    }
}