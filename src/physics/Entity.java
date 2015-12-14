package physics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author da
 */
public class Entity {

    float x, y;
    float lastX, lastY;
    float vx, vy;
    float accX, accY;
    float radius = 10;
    
    // temp
    public Color color = Color.red;
    float targetX, targetY;
    
    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
        
        lastX = x;
        lastY = y;
    }
    
    public Entity(Point p) {
        this(p.x, p.y);
    }
 
    public void move(float deltaTime) {
        
        float deltaTimeSq = deltaTime * deltaTime;
        
        
        vx = x - lastX;
        vy = y - lastY;
        
        calculateAcceleration();
        
        float nextX = x + vx + accX * deltaTimeSq;
        float nextY = y + vy + accY * deltaTimeSq;
        
        lastX = x;
        lastY = y;
        
        x = nextX;
        y = nextY;
    }
    
    public void setTarget(float x, float y) {
        targetX = x;
        targetY = y;
    }
    
    private void calculateAcceleration() {
        
        float deltaX = x - targetX;
        float deltaY = y - targetY;
        
        float length = (float)Math.sqrt(deltaX * deltaX + deltaY * deltaY );
        
        if (length > 0) {
            accX = (length / 10) * 10 * (-(deltaX / (length)) );
            accY = (length / 10) * 10 * (-(deltaY / (length)) );
        }
        
    }
    
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.fillOval((int)x - 10, (int)y -10, 20, 20);
        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(Color.black);
        g2d.drawLine((int)x, (int)y, (int)(x + accX * 10), (int)(y + accY * 10));
        g2d.setColor(Color.white);
        g2d.drawLine((int)x + 1, (int)y + 1, (int)(x + vx * 100) + 1, (int)(y + vy * 100) + 1);
    }
}
