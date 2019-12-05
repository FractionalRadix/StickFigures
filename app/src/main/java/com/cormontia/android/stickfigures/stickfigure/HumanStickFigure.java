package com.cormontia.android.stickfigures.stickfigure;

import android.util.Log;

public class HumanStickFigure
{
    private StickFigure figure;
    private Joint waistJoint;

    private int waist, neck;
    private int leftShoulder, rightShoulder;
    private int leftHip, rightHip;
    private int leftKnee, rightKnee;
    private int leftAnkle, rightAnkle;

    public StickFigure createHuman()
    {
        figure = new StickFigure( );

        waistJoint = new Joint(0.0,0.0,0.8);
        waist = figure.setWaist( waistJoint );

        neck = figure.addJoint( waist,0.0, 0.0,  1.7);

        leftShoulder  = figure.addJoint( neck, -0.2, -0.2,  1.6);
        rightShoulder = figure.addJoint( neck, +0.2, +0.2, 1.6);

        leftHip  = figure.addJoint( waist, -0.2, 0.0, 0.8);
        rightHip = figure.addJoint( waist, +0.2, 0.0, 0.8);

        leftKnee = figure.addJoint(  leftHip,  -0.2, 0.0, 0.4);
        rightKnee = figure.addJoint( rightHip, +0.2, 0.0, 0.4);

        leftAnkle = figure.addJoint( leftKnee, -0.25, 0.0, 0.0);
        rightAnkle = figure.addJoint(rightKnee,+0.25, 0.0, 0.0);

        return figure;
    }

    //TODO!~ This is just to demo, to show that it's possible...
    public void rotateLeftShoulder( )
    {
        double leftShoulderYaw = figure.getYaw( leftShoulder );
        leftShoulderYaw += 0.1;
        figure.setYaw( leftShoulder, leftShoulderYaw );

        double leftShoulderPitch = figure.getPitch( leftShoulder );
        leftShoulderPitch += 0.1;
        figure.setPitch( leftShoulder, leftShoulderPitch );

        waistJoint.propagateAngles();

        Log.i("STICKFIGURES", "yaw=="+leftShoulderYaw+", pitch=="+leftShoulderPitch);
    }

}
