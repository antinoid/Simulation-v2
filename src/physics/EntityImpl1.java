package physics;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import main.Drawable;

/**
 *
 * @author da
 */
public class EntityImpl1 extends Entity{

    private static HashMap<Long, EntityImpl1> entities = new HashMap<>();
    
    private Drawable drawable = new Drawable() {
        @Override
        public void draw(Graphics g) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    
    public EntityImpl1(int x, int y) {
        super(x, y);
    }
}
