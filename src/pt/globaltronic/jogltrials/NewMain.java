package pt.globaltronic.jogltrials;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import com.sun.javafx.geom.Vec3f;
import pt.globaltronic.jogltrials.entity.Camera;
import pt.globaltronic.jogltrials.entity.Entity;
import pt.globaltronic.jogltrials.entity.Light;
import pt.globaltronic.jogltrials.entity.Mouse;
import pt.globaltronic.jogltrials.models.RawModel;
import pt.globaltronic.jogltrials.models.TexturedModel;
import pt.globaltronic.jogltrials.terrain.Terrain;
import pt.globaltronic.jogltrials.textures.ModelTexture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NewMain implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {


    MasterRenderer renderer;
    Loader loader;
    RawModel model;
    Entity entity;
    ModelTexture texture;
    TexturedModel texturedModel;
    Camera camera;
    Light sun;
    Terrain terrain;
    Terrain terrain2;
    Mouse mouse;
    boolean[] Keys = new boolean[4];
    boolean rightClickDown;
    Robot r;

    private static long lastFrameTime = 0;
    private static float delta;


    @Override
    public void display(GLAutoDrawable glad) {
        entity.increaseRotation(0, 1, 0);
        mouse.move();
        camera.move();
        renderer.processTerrain(terrain);
        renderer.processTerrain(terrain2);
        renderer.processEntity(entity);
        renderer.processEntity(mouse);
        renderer.render(sun, camera);
        long currentFrameTime = System.currentTimeMillis();
        delta = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }


    @Override
    public void dispose(GLAutoDrawable arg0) {
        renderer.cleanUp();
        loader.cleanUpMemory();
    }

    @Override
    public void init(GLAutoDrawable glad) {

        renderer = new MasterRenderer(glad.getGL().getGL3());
        loader = new Loader(glad.getGL().getGL3());


        model = OBJLoader.loadObjectModel("dragon", loader);
        //use the loader to get the id of the texture and pass it to the new texture
        texture = new ModelTexture(loader.loadTexture("white"));
        texturedModel = new TexturedModel(model, texture);
        texturedModel.getModelTexture().setReflectivity(1);
        texturedModel.getModelTexture().setShineDamper(10);
        entity = new Entity(texturedModel, new Vec3f(0, 0, -25), 0, 0, 0, 0.1f);
        mouse = new Mouse(texturedModel, new Vec3f(25, 0, -25), 0, 180, 0, 1.0f);
        camera = new Camera(mouse);
        sun = new Light(new Vec3f(3000, 2000, 2000), new Vec3f(1, 1, 1));
        terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
        terrain2 = new Terrain(0, 1, loader, new ModelTexture(loader.loadTexture("grass")));

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W)
            mouse.setCurrentSpeed(1.0f);
        if (e.getKeyCode() == KeyEvent.VK_A)
            mouse.setCurrentTurnSpeed(1.0f);
        if (e.getKeyCode() == KeyEvent.VK_S)
            mouse.setCurrentSpeed(-1.0f);
        if (e.getKeyCode() == KeyEvent.VK_D)
            mouse.setCurrentTurnSpeed(-1.0f);

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W)
            mouse.setCurrentSpeed(0f);
        if (e.getKeyCode() == KeyEvent.VK_A)
            mouse.setCurrentTurnSpeed(0f);
        if (e.getKeyCode() == KeyEvent.VK_S)
            mouse.setCurrentSpeed(0f);
        if (e.getKeyCode() == KeyEvent.VK_D)
            mouse.setCurrentTurnSpeed(0f);
    }


    void CenterMouse(MouseEvent e) {
        try {
            r = new Robot();
            r.mouseMove((int)(e.getComponent().getLocationOnScreen().getX() + (1024)/2), (int)(e.getComponent().getLocationOnScreen().getY() + (768)/2));
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    public void mouseDragged(MouseEvent arg0) {

        if (rightClickDown) {
            float pitch = camera.getPitch();
            float angleAroundPlayer = camera.getAngleAroundPlayer();

            float pitchChange = (arg0.getY()) * 0.0001f;
            camera.setPitch(pitch + pitchChange);





            float angleChange = (arg0.getX() - 1024 / 2) * 0.1f;
            camera.setAngleAroundPlayer(angleAroundPlayer + angleChange);



           CenterMouse(arg0);
        }
    }



    public void mouseMoved(MouseEvent arg0) {

    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
        if (arg0.getButton() == MouseEvent.BUTTON3) {
            rightClickDown = true;
        }

    }

    public void mouseReleased(MouseEvent arg0) {
        if (arg0.getButton() == MouseEvent.BUTTON3) {
            rightClickDown = false;
        }
    }

    public void mouseWheelMoved(MouseWheelEvent arg0) {
        float distanceFromPlayer = camera.getDistanceFromPlayer();
        if (arg0.getUnitsToScroll() > 0) {
            if (distanceFromPlayer > 0)
                camera.setDistanceFromPlayer(distanceFromPlayer +1 * arg0.getUnitsToScroll());
        } else {
            camera.setDistanceFromPlayer(distanceFromPlayer +1 * arg0.getUnitsToScroll());
        }
    }

    public static void main(String[] args) {
        NewMain main = new NewMain();

        JFrame frame = new JFrame("JOGL Events");
        Toolkit t = Toolkit.getDefaultToolkit();
        GLJPanel canvas = new GLJPanel();
        //since the canvas is set as focusabled and requests focus, it needs to event listeners
        canvas.addGLEventListener(main);
        canvas.addKeyListener(main);
        canvas.addMouseListener(main);
        canvas.addMouseMotionListener(main);
        canvas.addMouseWheelListener(main);
        canvas.setFocusable(true);
        canvas.requestFocus();
        frame.add(canvas);
        frame.setSize(1024, 768);
        //frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();


    }


}