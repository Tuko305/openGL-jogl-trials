package pt.globaltronic.jogltrials.entity;

import com.sun.javafx.geom.Vec3f;

public class Camera {

    private float distanceFromPlayer = 20;
    private float angleAroundPlayer = 0;

    private Vec3f position = new Vec3f(0,100,0);
    private float pitch = 30;
    private float yaw;
    private float roll;

    private Mouse mouse;

    public Camera(Mouse mouse){
        this.mouse = mouse;
    }

    public void move(){

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (mouse.getRotY() + angleAroundPlayer);
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

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance){
        float theta = mouse.getRotY() + angleAroundPlayer;
        float offsetX =(float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ =(float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = mouse.getPosition().x - offsetX;
        position.y = mouse.getPosition().y + verticalDistance;
        position.z = mouse.getPosition().z - offsetZ;

    }
    private float calculateHorizontalDistance(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    public float getAngleAroundPlayer() {
        return angleAroundPlayer;
    }

    public float getDistanceFromPlayer() {
        return distanceFromPlayer;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public void setAngleAroundPlayer(float angleAroundPlayer) {
        this.angleAroundPlayer = angleAroundPlayer;
    }

    public void setDistanceFromPlayer(float distanceFromPlayer) {
        this.distanceFromPlayer = distanceFromPlayer;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public void setPosition(Vec3f position) {
        this.position = position;
    }
}
