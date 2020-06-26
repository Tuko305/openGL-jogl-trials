package pt.globaltronic.jogltrials.entity;

import com.sun.javafx.geom.Vec3f;

public class Camera {

    private Vec3f position = new Vec3f(0,100,0);
    private float pitch = 90;
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
