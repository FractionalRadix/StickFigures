package com.cormontia.android.stickfigures.stickfigure;

import android.util.Log;

public class HumanStickFigure
{
    private StickFigure figure;
    private Stick waistJoint;

    private int waist, neck;
    private int leftShoulder, rightShoulder;
    private int leftHip, rightHip;
    private int leftKnee, rightKnee;
    private int leftAnkle, rightAnkle;

    public StickFigure createHuman()
    {
        figure = new StickFigure( 0.0, 0.0, 0.8 );
        waist = 0;

        neck = figure.addStickToRoot(0.0, 0.0, 1.7);

        leftShoulder  = figure.addStickToStick( neck, -0.2, -0.2, 1.6);
        rightShoulder = figure.addStickToStick( neck, +0.2, +0.2, 1.6);

        leftHip  = figure.addStickToRoot(      -0.2, 0.0, 0.8);
        rightHip = figure.addStickToRoot(      +0.2, 0.0, 0.8);

        leftKnee = figure.addStickToStick(      leftHip,  -0.2, 0.0, 0.4);
        rightKnee = figure.addStickToStick(     rightHip, +0.2, 0.0, 0.4);

        leftAnkle = figure.addStickToStick(     leftKnee, -0.25, 0.0, 0.0);
        rightAnkle = figure.addStickToStick(    rightKnee,+0.25, 0.0, 0.0);

        //TODO?+
        figure.propagateAngles();

        return figure;
    }

    //TODO!~ This is just to demo, to show that it's possible...
    public void rotateLeftShoulder( )
    {
        //double leftShoulderYaw = figure.getYaw( leftShoulder );
        //leftShoulderYaw += 0.02;
        //figure.setYaw( leftShoulder, leftShoulderYaw );

        double leftShoulderPitch = figure.getPitch( leftShoulder );
        leftShoulderPitch += 0.015;
        figure.setPitch( leftShoulder, leftShoulderPitch );

        //TODO!+
        figure.propagateAngles();

        //Log.i("STICKFIGURES", "yaw=="+leftShoulderYaw+", pitch=="+leftShoulderPitch);
    }

}
