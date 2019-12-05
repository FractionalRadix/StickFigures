package com.cormontia.android.stickfigures.stickfigure;

public class StickFigureFactory
{
    //TODO!- Make a Dog or Animal class, like we did for Human.
    public static StickFigure createDog( )
    {
        StickFigure res = new StickFigure();
        float dogHeight = 0.45f; // Height measured at the shoulder

        Joint waistJoint = new Joint( 0.0, 0.0, dogHeight );
        int waist = res.setWaist(waistJoint);
        int neck = res.addJoint( waist, 0.5, 0.0, dogHeight );

        int leftHip      = res.addJoint( waist,         0.0,-0.1, dogHeight );
        int leftHindPaw  = res.addJoint( leftHip,       0.0,-0.1,0.0     );
        int rightHip     = res.addJoint( waist,         0.0,+0.1, dogHeight );
        int rightHindPaw = res.addJoint( rightHip,      0.0,+0.1,0.0     );

        int leftShoulder  = res.addJoint( neck,         0.5,-0.1, dogHeight );
        int leftFrontPaw  = res.addJoint( leftShoulder, 0.5,-0.1, 0.0    );
        int rightShoulder = res.addJoint( neck,         0.5,+0.1, dogHeight );
        int rightFrontPaw = res.addJoint( rightShoulder,0.5,+0.1, 0.0    );

        int tailPart1 = res.addJoint( waist,     -0.1, 0.0, dogHeight + 0.1 );
        int tailPart2 = res.addJoint( tailPart1, -0.2, 0.0, dogHeight + 0.2 );
        int tailPart3 = res.addJoint( tailPart2, -0.1, 0.0, dogHeight + 0.3 );

        int head = res.addJoint( neck,0.60, 0.0, dogHeight + 0.2 );
        int nose = res.addJoint( head,0.80, 0.0, dogHeight + 0.05 );

        return res;
    }

}
