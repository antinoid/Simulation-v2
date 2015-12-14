package physics;

import java.util.List;
import java.util.Vector;

/**
 *
 * @author da
 */
public class PhysicSpace {

    float centerX;
    float centerY;
    private Vector<Entity> entities;
    public Entity target = new Entity(0, 0);
    
    public PhysicSpace() {
        entities = new Vector<>();
    }
    
    public List<Entity> getEntities() {
        return entities;
    }
    
    public synchronized void addEntity(int x, int y) {
        entities.add(new Entity(x, y));
    }
    
    public synchronized void update(float deltaTime) {
        
        centerX = 0;
        centerY = 0;
        for (Entity entity : entities) {
            centerX += entity.x;
            centerY += entity.y;
        }
        centerX /= entities.size();
        centerY /= entities.size();
        
        
        for (Entity entity : entities) {
            //ArrayList<Entity> neighbors = new ArrayList<>(entities);
            //neighbors.remove(entity);
            entity.setTarget(centerX, centerY);
            entity.move(deltaTime);
        }
        
        target.x = centerX;
        target.y = centerY;
    }
}
