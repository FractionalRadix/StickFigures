package com.cormontia.android.stickfigures.stickfigure;

public class DogStickFigure
{
    private StickFigure res;
    private Stick waistJoint;

    private int neck, head, nose;
    private int leftHip, leftHindPaw;
    private int rightHip, rightHindPaw;
    private int leftShoulder, leftFrontPaw;
    private int rightShoulder, rightFrontPaw;
    private int tailPart1, tailPart2, tailPart3;

    public StickFigure createDog( )
    {
        float dogHeight = 0.45f; // Height measured at the shoulder
        res = new StickFigure( 0.0, 0.0, dogHeight );

        neck = res.addStickToRoot(0.5, 0.0, dogHeight );

        leftHip      = res.addStickToRoot(          0.0,-0.1, dogHeight );
        leftHindPaw  = res.addStickToStick( leftHip,       0.0,-0.1,0.0     );
        rightHip     = res.addStickToRoot(          0.0,+0.1, dogHeight );
        rightHindPaw = res.addStickToStick( rightHip,      0.0,+0.1,0.0     );

        leftShoulder  = res.addStickToStick( neck,         0.5,-0.1, dogHeight );
        leftFrontPaw  = res.addStickToStick( leftShoulder, 0.5,-0.1, 0.0    );
        rightShoulder = res.addStickToStick( neck,         0.5,+0.1, dogHeight );
        rightFrontPaw = res.addStickToStick( rightShoulder,0.5,+0.1, 0.0    );

        tailPart1 = res.addStickToRoot(             -0.1, 0.0, dogHeight + 0.1 );
        tailPart2 = res.addStickToStick( tailPart1, -0.2, 0.0, dogHeight + 0.2 );
        tailPart3 = res.addStickToStick( tailPart2, -0.1, 0.0, dogHeight + 0.3 );

        head = res.addStickToStick( neck,0.60, 0.0, dogHeight + 0.2 );
        nose = res.addStickToStick( head,0.80, 0.0, dogHeight + 0.05 );

        return res;
    }

    private double tailWaggingIncrement = 0.04;
    private double tailWaggingDelta = tailWaggingIncrement;
    public void wagTail( )
    {
        double tailAngle = res.getYaw( tailPart1 );
        tailAngle += tailWaggingDelta;
        if ( tailAngle > .25 * Math.PI )
        {
            tailWaggingDelta = -tailWaggingIncrement;
        }
        else if ( tailAngle < -.25 * Math.PI )
        {
            tailWaggingDelta = +tailWaggingIncrement;
        }
        res.setYaw( tailPart1, tailAngle );

        res.propagateAngles( );
    }
}
