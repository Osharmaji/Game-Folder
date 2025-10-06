import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class AsteroidGame extends JPanel implements ActionListener, KeyListener {
    static final int WIDTH = 1920;
    static final int HEIGHT = 1080;

    Timer timer;
    Spaceship ship;
    ArrayList<Asteroid> asteroids = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    boolean left, right, up;
    int score = 0;
    int lives = 3;
    int asteroidSpawnCounter = 0;

    public AsteroidGame() {
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        ship = new Spaceship(WIDTH / 2.0, HEIGHT / 2.0);
        for (int i = 0; i < 5; i++) asteroids.add(new Asteroid());
        timer = new Timer(20, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ship.draw(g);
        for (Asteroid a : asteroids) a.draw(g);
        for (Bullet b : bullets) b.draw(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);
        g.drawString("Lives: " + lives, 20, 60);
    }

    public boolean checkCollision(Bullet b, Asteroid a) {
        double dist = Math.hypot(b.x - a.x, b.y - a.y);
        return dist < 15;
    }

    public boolean checkShipCollision(Spaceship s, Asteroid a) {
        double dist = Math.hypot(s.x - a.x, s.y - a.y);
        return dist < 20;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (left) ship.rotate(-2);
        if (right) ship.rotate(2);
        if (up) ship.thrust();
        ship.update(up);

        for (Asteroid a : asteroids) a.update();

        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.update();
            if (!b.isAlive()) {
                bullets.remove(i);
                continue;
            }

            for (int j = asteroids.size() - 1; j >= 0; j--) {
                Asteroid a = asteroids.get(j);
                if (checkCollision(b, a)) {
                    bullets.remove(i);
                    asteroids.remove(j);
                    score += 10;
                    break;
                }
            }
        }

        for (int i = asteroids.size() - 1; i >= 0; i--) {
            Asteroid a = asteroids.get(i);
            if (checkShipCollision(ship, a)) {
                asteroids.remove(i);
                lives--;
                if (lives <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + score);
                    System.exit(0);
                }
            }
        }

        asteroidSpawnCounter++;
        if (asteroidSpawnCounter % 100 == 0) {
            asteroids.add(new Asteroid());
        }

        repaint();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> left = true;
            case KeyEvent.VK_RIGHT -> right = true;
            case KeyEvent.VK_UP -> up = true;
            case KeyEvent.VK_SPACE -> bullets.add(new Bullet(ship.x, ship.y, ship.angle));
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
            dx += Math.cos(angle) * 0.1;
            dy += Math.sin(angle) * 0.1;
        }

        public void update(boolean thrusting) {
            if (!thrusting) {
                dx *= 0.98;
                dy *= 0.98;
            }
            x += dx;
            y += dy;
            wrap();
        }

        public void wrap() {
            if (x < 0) x += WIDTH;
            if (x > WIDTH) x -= WIDTH;
            if (y < 0) y += HEIGHT;
            if (y > HEIGHT) y -= HEIGHT;
        }

        public void draw(Graphics g) {
            int[] xs = {
                (int)(x + 10 * Math.cos(angle)),
                (int)(x - 10 * Math.cos(angle + Math.PI / 2)),
                (int)(x - 10 * Math.cos(angle - Math.PI / 2))
            };
            int[] ys = {
                (int)(y + 10 * Math.sin(angle)),
                (int)(y - 10 * Math.sin(angle + Math.PI / 2)),
                (int)(y - 10 * Math.sin(angle - Math.PI / 2))
            };
            g.setColor(Color.WHITE);
            g.fillPolygon(xs, ys, 3);
        }
    }

    class Asteroid {
        double x, y, dx, dy;
        Random rand = new Random();

        public Asteroid() {
            x = rand.nextInt(WIDTH);
            y = rand.nextInt(HEIGHT);
            dx = rand.nextDouble() * 2 - 1;
            dy = rand.nextDouble() * 2 - 1;
        }

        public void update() {
            x += dx;
            y += dy;
            if (x < 0) x += WIDTH;
            if (x > WIDTH) x -= WIDTH;
            if (y < 0) y += HEIGHT;
            if (y > HEIGHT) y -= HEIGHT;
        }

        public void draw(Graphics g) {
            g.setColor(Color.ORANGE);
            g.fillOval((int)x, (int)y, 30, 30);
        }
    }

    class Bullet {
        double x, y, dx, dy;
        int life = 40;

        public Bullet(double x, double y, double angle) {
            this.x = x;
            this.y = y;
            dx = Math.cos(angle) * 8;
            dy = Math.sin(angle) * 8;
        }

        public void update() {
            x += dx;
            y += dy;
            life--;
        }

        public boolean isAlive() {
            return life > 0 && x >= 0 && x <= WIDTH && y >= 0 && y <= HEIGHT;
        }

        public void draw(Graphics g) {
            g.setColor(Color.CYAN);
            g.fillOval((int)x - 2, (int)y - 2, 4, 4);
        }
    }
}