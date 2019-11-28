package com.cormontia.android.stickfigures.stickfigure;

public class StickFigureFactory
{
    public static StickFigure createHuman()
    {
        StickFigure res = new StickFigure( );

        Joint waistJoint = new Joint(0.0,0.0,0.8);
        int waist = res.setWaist( waistJoint );

        int neck = res.addJoint( waist,0.0, 0.0,  1.7);

        int leftShoulder  = res.addJoint( neck, -0.2, -0.2,  1.6);
        int rightShoulder = res.addJoint( neck, +0.2, +0.2, 1.6);

        int leftHip  = res.addJoint( waist,-0.2, 0.0, 0.8);
        int rightHip = res.addJoint(waist, +0.2, 0.0, 0.8);

        int leftKnee = res.addJoint( leftHip,-0.2, 0.0, 0.4);
        int rightKnee = res.addJoint( rightHip, +0.2, 0.0, 0.4);

        int leftAnkle = res.addJoint(leftKnee,-0.25, 0.0, 0.0);
        int rightAnkle = res.addJoint(rightKnee, +0.25, 0.0, 0.0);

        return res;
    }

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

    /*
    public static StickFigure OLD_createDog( )
    {
        StickFigure res = new StickFigure();
        float dogHeight = 0.45f; // Height measured at the shoulder

        Joint waist = new Joint( 0.0, 0.0, dogHeight );
        res.setWaist(waist);
        //joints.add(waist);
        Joint neck = waist.addLineTo( 0.5, 0.0, dogHeight );
        //joints.add(neck);

        Joint leftHip      = waist.addLineTo(         0.0,-0.1, dogHeight );
        //joints.add(leftHip);
        Joint leftHindPaw  = leftHip.addLineTo(       0.0,-0.1,0.0     );
        //joints.add(leftHindPaw);
        Joint rightHip     = waist.addLineTo(         0.0,+0.1, dogHeight );
        //joints.add(rightHip);
        Joint rightHindPaw = rightHip.addLineTo(      0.0,+0.1,0.0     );
        //joints.add(rightHindPaw);

        Joint leftShoulder  = neck.addLineTo(         0.5,-0.1, dogHeight );
        //joints.add(leftShoulder);
        Joint leftFrontPaw  = leftShoulder.addLineTo( 0.5,-0.1, 0.0    );
        //joints.add(leftFrontPaw);
        Joint rightShoulder = neck.addLineTo(         0.5,+0.1, dogHeight );
        //joints.add(rightShoulder);
        Joint rightFrontPaw = rightShoulder.addLineTo(0.5,+0.1, 0.0    );
        //joints.add(rightFrontPaw);

        Joint tailPart1 = waist    .addLineTo( -0.1, 0.0, dogHeight + 0.1 );
        //joints.add(tailPart1);
        Joint tailPart2 = tailPart1.addLineTo( -0.2, 0.0, dogHeight + 0.2 );
        //joints.add(tailPart2);
        Joint tailPart3 = tailPart2.addLineTo( -0.1, 0.0, dogHeight + 0.3 );
        //joints.add(tailPart3);

        Joint head = neck.addLineTo( 0.60, 0.0, dogHeight + 0.2 );
        //joints.add(head);
        Joint nose = head.addLineTo( 0.80, 0.0, dogHeight + 0.05 );
        //joints.add(nose);

        return res;
    }
*/
}
