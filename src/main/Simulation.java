package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import physics.Entity;
import physics.PhysicSpace;

/**
 *
 * @author da
 */
public class Simulation extends JPanel implements Runnable {

    public static final int WIDHT = 800;
    public static final int HEIGHT = 600;
    
    private static final int FPS = 60;
    private final int MAX_SUB_STEPS = 5;
    private final float FIXED_TIME_STEP = 1f / FPS;
    private boolean isRunning = false;

    private Thread updateThread;
    private float localTime = 0f;

    private PhysicSpace world;

    public Simulation() {
        setPreferredSize(new Dimension(WIDHT, HEIGHT));
        addMouseListener(new MouseClickListener());
        world = new PhysicSpace();
    }

    public void start() {
        setBackground(new Color(0x2030C0));
        setFocusable(true);
        requestFocus();
        isRunning = true;
        updateThread = new Thread(this);
        updateThread.start();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    @Override
    public void run() {
        long start = System.nanoTime();
        while (isRunning) {
            long now = System.nanoTime();
            float elapsed = (now - start) / 1000000000f;
            start = now;
            internalUpdateWithFixedTimeStep(elapsed);
            internalUpdateGraphicsInterpolated();
            if (1000000000 * FIXED_TIME_STEP - (System.nanoTime() - start) > 1000000) {
                try {
                    Thread.sleep(0, 999999);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    /**
     * Updates game state if possible and sets localTime for interpolation.
     *
     * @param elapsedSeconds
     */
    private void internalUpdateWithFixedTimeStep(float elapsedSeconds) {
        int numSubSteps = 0;
        if (MAX_SUB_STEPS != 0) {
            // fixed timestep with interpolation
            localTime += elapsedSeconds;
            if (localTime >= FIXED_TIME_STEP) {
                numSubSteps = (int) (localTime / FIXED_TIME_STEP);
                localTime -= numSubSteps * FIXED_TIME_STEP;
            }
        }
        if (numSubSteps != 0) {
            // clamp the number of substeps, to prevent simulation grinding spiralling down to a halt
            int clampedSubSteps = (numSubSteps > MAX_SUB_STEPS) ? MAX_SUB_STEPS : numSubSteps;
            for (int i = 0; i < clampedSubSteps; i++) {
                //world.update(FIXED_TIME_STEP);
                update(FIXED_TIME_STEP);
            }
        }
    }

    /**
     * Calls render with Graphics2D context and takes care of double buffering.
     */
    private void internalUpdateGraphicsInterpolated() {
        // TODO
        repaint();
    }
    
    private void render(Graphics g) {
        for (Entity entity : world.getEntities()) {
            entity.draw(g);
        }
        world.target.color = Color.green;
        world.target.draw(g);
        g.dispose();
    }
    
    private void update(float deltaTime) {
        world.update(deltaTime);
    }
    
    private class MouseClickListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                world.addEntity(e.getX(), e.getY());
                /*
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        world.addEntity(e.getX() + j * 50, e.getY() + i * 50);
                    }
                    
                }*/
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    public static void main(String[] args) {

        Simulation sim = new Simulation();
        new Thread() {

            {
                setDaemon(true);
                start();
            }

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(Integer.MAX_VALUE);
                    } catch (Throwable t) {
                    }
                }
            }
        };
        EventQueue.invokeLater(() -> createFrame(sim));
        sim.start();
    }

    private static void createFrame(Simulation sim) {
        JFrame frame = new JFrame("Fish Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(sim);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
