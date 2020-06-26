package pt.globaltronic.jogltrials;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import com.sun.javafx.geom.Vec3f;
import pt.globaltronic.jogltrials.entity.Camera;
import pt.globaltronic.jogltrials.entity.Entity;
import pt.globaltronic.jogltrials.entity.Light;
import pt.globaltronic.jogltrials.models.RawModel;
import pt.globaltronic.jogltrials.models.TexturedModel;
import pt.globaltronic.jogltrials.terrain.Terrain;
import pt.globaltronic.jogltrials.textures.ModelTexture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NewMain implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

    String VERTEX_SHADER_PATH = "shaders/vertexShader.vp";
    String FRAGMENT_SHADER_PATH = "shaders/fragmentShader.fp";
    boolean running = true;

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
    boolean[] Keys = new boolean[4];
    boolean rightClickDown;
    Robot r;


    @Override
    public void display(GLAutoDrawable glad) {
        entity.increaseRotation(0, 1, 0);
        moveCamera();
        renderer.processTerrain(terrain);
        renderer.processTerrain(terrain2);
        renderer.processEntity(entity);
        renderer.render(sun, camera);
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
        StaticShader shader = renderer.getShader();
        texture = new ModelTexture(loader.loadTexture("white"));
        texturedModel = new TexturedModel(model, texture);
        entity = new Entity(texturedModel, new Vec3f(0, 0, -25), 0, 0, 0, 1.0f);
        camera = new Camera();
        sun = new Light(new Vec3f(3000,2000,2000), new Vec3f(1,1,1));
        terrain = new Terrain(0,0, loader, new ModelTexture(loader.loadTexture("grass")));
        terrain2 = new Terrain(0,1, loader, new ModelTexture(loader.loadTexture("grass")));

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }

    public void moveCamera(){
        if(Keys[0]){ camera.getPosition().z -= 0.02f; }
        if(Keys[1]){ camera.getPosition().x -= 0.02f;}
        if(Keys[2]){ camera.getPosition().z += 0.02f; }
        if(Keys[3]){ camera.getPosition().x += 0.02f; }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W)
            Keys[0] = true;
        if(e.getKeyCode() == KeyEvent.VK_A)
            Keys[1] = true;
        if(e.getKeyCode() == KeyEvent.VK_S)
            Keys[2] = true;
        if(e.getKeyCode() == KeyEvent.VK_D)
            Keys[3] = true;

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W)
            Keys[0] = false;
        if(e.getKeyCode() == KeyEvent.VK_A)
            Keys[1] = false;
        if(e.getKeyCode() == KeyEvent.VK_S)
            Keys[2] = false;
        if(e.getKeyCode() == KeyEvent.VK_D)
            Keys[3] = false;
    }

    void MouseMovement(float NewMouseX, float NewMouseY)
    {
        float difX = (NewMouseX - 1024/2);
        float difY = (NewMouseY - 768/2);
        difY *= 6 - Math.abs(camera.getYaw()) * 5;

        float tempy = camera.getPitch();
        float tempx = camera.getYaw();
        camera.setPitch(tempy - difY);
        camera.setYaw(tempx + difX);

        if(camera.getPitch()>0.999)
            camera.setPitch(0.999f);

        if(camera.getPitch()<-0.999)
            camera.setPitch(-0.999f);

    }
    void CenterMouse()
    {
        try {
            r = new Robot();
            r.mouseMove((int)1024/2, (int)768/2);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void mouseDragged(MouseEvent arg0) {

        if(rightClickDown) {
            MouseMovement(arg0.getX(), arg0.getY());
            CenterMouse();
        }


    }

    public void mouseMoved(MouseEvent arg0) {

        if(rightClickDown) {
            MouseMovement(arg0.getX(), arg0.getY());
            CenterMouse();
        }


    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
        if(arg0.getButton() == MouseEvent.BUTTON3){
            rightClickDown = true;
        }

    }

    public void mouseReleased(MouseEvent arg0) {
        if(arg0.getButton() == MouseEvent.BUTTON3){
            rightClickDown = false;
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
        canvas.setFocusable(true);
        canvas.requestFocus();
        frame.add(canvas);
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();


    }


}