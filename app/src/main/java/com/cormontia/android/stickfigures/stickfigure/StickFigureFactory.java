package com.cormontia.android.stickfigures.stickfigure;

public class StickFigureFactory
{
    //TODO!- Make a Dog or Animal class, like we did for Human.
    public static StickFigure createDog( )
    {
        float dogHeight = 0.45f; // Height measured at the shoulder
        StickFigure res = new StickFigure( 0.0, 0.0, dogHeight );

        int neck = res.addStickToRoot(0.5, 0.0, dogHeight );

        int leftHip      = res.addStickToRoot(          0.0,-0.1, dogHeight );
        int leftHindPaw  = res.addStickToStick( leftHip,       0.0,-0.1,0.0     );
        int rightHip     = res.addStickToRoot(          0.0,+0.1, dogHeight );
        int rightHindPaw = res.addStickToStick( rightHip,      0.0,+0.1,0.0     );

        int leftShoulder  = res.addStickToStick( neck,         0.5,-0.1, dogHeight );
        int leftFrontPaw  = res.addStickToStick( leftShoulder, 0.5,-0.1, 0.0    );
        int rightShoulder = res.addStickToStick( neck,         0.5,+0.1, dogHeight );
        int rightFrontPaw = res.addStickToStick( rightShoulder,0.5,+0.1, 0.0    );

        int tailPart1 = res.addStickToRoot(      -0.1, 0.0, dogHeight + 0.1 );
        int tailPart2 = res.addStickToStick( tailPart1, -0.2, 0.0, dogHeight + 0.2 );
        int tailPart3 = res.addStickToStick( tailPart2, -0.1, 0.0, dogHeight + 0.3 );

        int head = res.addStickToStick( neck,0.60, 0.0, dogHeight + 0.2 );
        int nose = res.addStickToStick( head,0.80, 0.0, dogHeight + 0.05 );

        return res;
    }

}
