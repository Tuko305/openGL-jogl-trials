package pt.globaltronic.jogltrials.entity;

import com.sun.javafx.geom.Vec3f;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera {

    private Vec3f position = new Vec3f(0,0,0);
    private float pitch;
    private float yaw;
    private float roll;

    public Camera(){

    }


    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    public float getYaw() {
        return yaw;
    }

    public Vec3f getPosition() {
        return position;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
