package com.cormontia.android.stickfigures.stickfigure;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;

import com.cormontia.android.stickfigures.matrix3d.Mat44;
import com.cormontia.android.stickfigures.matrix3d.MatrixFactory;
import com.cormontia.android.stickfigures.matrix3d.Vec4;

public class StickFigure
{
    private Mat44 fullTransformation; // From world coordinates to screen coordinates.

    private Joint waist, neck;

    public StickFigure( StickFigureType figureType )
    {
        switch (figureType) {
            case Human:
                stickFigHuman();
                break;
            case Dog:
                stickFigDog();
        }
    }

    //TODO!~ Change "float x" to a heartbeat/clock-tick counter.
    //  Then leave it to the stick figure instance what it should do.
    //TODO!+ In this method, we can put all the drawing of our Stick Figure! :-)
    // It's already called from MyView, it's already in use.
    // All that's left is:
    // - Design the data structure properly
    // - Get a better translation from projection coordinates to screen coordinates.
    //   We want the origin of the world in the bottom of the screen.
    //   Note that this is a transformation of  the PROJECTION plane to the SCREEN, so it's 2D to 2D.

    public void draw(Canvas canvas, int clockTick, float x)
    {
        fullTransformation = this.determineTransformationMatrix();
        Mat44 trans = MatrixFactory.Translate( 0.1 * x, 0.1 * x,  0.1 * x );
        Mat44 rot = MatrixFactory.RotateAroundZ( x );

        fullTransformation = Mat44.multiply( fullTransformation, Mat44.multiply( trans, rot ) );
        calculate( fullTransformation );

        waist.draw2( canvas, fullTransformation );
    }

    /**
     * Calculate the projection of the Stick Figure.
     */
    private void calculate( Mat44 transformation )
    {
        Vec4 waistLocation  = waist.location();
        Vec4 neckLocation = neck.location(); //TODO!~  Determine from how it "descends" from waist, which is the root of our stick figure defnition tree.

        Vec4 waistOnScreen = fullTransformation.multiply( waistLocation );
        Vec4 neckOnScreen = fullTransformation.multiply( neckLocation );

        PointF waist2D = new PointF( (float) waistOnScreen.get( 0 ), (float) waistOnScreen.get( 1 ) );
        PointF neck2D = new PointF( (float) neckOnScreen.get( 0 ), (float) neckOnScreen.get( 1 ) );

        Log.i("STICKFIGURE_LOGGIN",  "Waist position on screen: (" + waist2D.x + ", " + waist2D.y + ")" );
        Log.i("STICKFIGURE_LOGGIN",  "Neck position on screen: (" + neck2D.x + ", " + neck2D.y + ")" );
    }

    public void stickFigHuman()
    {
        fullTransformation = determineTransformationMatrix();

        waist = new Joint(0.0,0.0,0.8);
        //neck = new Joint(0.0, 0.0, 0.6);
        neck = waist.addLineTo(0.0, 0.0,  1.7);
        Joint leftShoulder  = neck.addLineTo( -0.2, -0.2,  1.6);
        Joint rightShoulder = neck.addLineTo( +0.2, +0.2, 1.6);

        Joint leftHip  = waist.addLineTo(-0.2, 0.0, 0.8);
        Joint rightHip = waist.addLineTo(+0.2, 0.0, 0.8);


        Joint leftKnee = leftHip.addLineTo(-0.2, 0.0, 0.4);
        Joint rightKnee = rightHip.addLineTo(+0.2, 0.0, 0.4);

        Joint leftAnkle = leftKnee.addLineTo(-0.25, 0.0, 0.0);
        Joint rightAnkle = rightKnee.addLineTo(+0.25, 0.0, 0.0);

        //TODO!+

        // But ... is "addLineTo"  adding absolute coordinates, or coordinates relative to the joint that we're adding?
        // The first would be easier when importing Kinect Skeletons...
    }

    public void stickFigDog( )
    {
        float dogHeight = 0.45f; // Height measured at the shoulder

        waist = new Joint( 0.0, 0.0, dogHeight );
        neck = waist.addLineTo( 0.5, 0.0, dogHeight );

        Joint leftHip      = waist.addLineTo(         0.0,-0.1, dogHeight );
        Joint leftHindPaw  = leftHip.addLineTo(       0.0,-0.1,0.0     );
        Joint rightHip     = waist.addLineTo(         0.0,+0.1, dogHeight );
        Joint rightHindPaw = rightHip.addLineTo(      0.0,+0.1,0.0     );

        Joint leftShoulder  = neck.addLineTo(         0.5,-0.1, dogHeight );
        Joint leftFrontPaw  = leftShoulder.addLineTo( 0.5,-0.1, 0.0    );
        Joint rightShoulder = neck.addLineTo(         0.5,+0.1, dogHeight );
        Joint rightFrontPaw = rightShoulder.addLineTo(0.5,+0.1, 0.0    );

        Joint tailPart1 = waist.addLineTo( -0.1, 0.0, dogHeight + 0.1 );
        Joint tailPart2 = tailPart1.addLineTo( -0.2, 0.0, dogHeight + 0.2 );
        Joint tailPart3 = tailPart2.addLineTo( -0.1, 0.0, dogHeight + 0.3 );

        Joint head = neck.addLineTo( 0.60, 0.0, dogHeight + 0.2 );
        Joint nose = head.addLineTo( 0.80, 0.0, dogHeight + 0.05 );
    }

    /**
     * Determine the complete transformation matrix, the combination of the projection matrix and the projection-plane-coordinates-to-screen-coordinates transformation.
     */
    private Mat44 determineTransformationMatrix()
    {
        // We're going for a simple orthogonal projection.
        // Since the ground floor is the XY plane, and the Z axis pointing upward.
        // Let's use the YZ plane as the projection plane - meaning the X value is zero in our projection matrix.
        Mat44 projection = MatrixFactory.Identity();
        // First row: translate world Y coordinate to projection X coordinate.
        projection.set( 0, 0,0 );
        projection.set(0, 1, 1 );
        // Second row: project world Z coordinate to projection Y coordinate.
        projection.set(1, 1, 0);
        projection.set(1,2,1);
        // Third row: all zeroes.
        projection.set(2, 2, 0);
        // Fourth row: no changes.

        logMatrix( projection );

        // After projection, go from (projected) world coordinates to screen coordinates.

        //TODO!~ Do the transformation from "projection coordinates" to "screen coordinates"  properly.
        // To translate and scale properly, from projection coordinates to screen coordinates:
        // First, translate to the origin (if necessary).
        // Second, scale to the desired size, including inversion (scale factor for Y axis is negative).
        // Third and final, translate from the origin of the screen to the center of the screen.
        Mat44 worldToScreen_scale = MatrixFactory.Scale( 300, -200, 100);
        Mat44 worldToScreen_translate = MatrixFactory.Translate(400,810,10 );
        Mat44 worldToScreen = Mat44.multiply( worldToScreen_translate, worldToScreen_scale );

        Mat44 fullTransformation = Mat44.multiply( worldToScreen, projection );
        return fullTransformation;
    }

    public static void logMatrix(Mat44 matrix)
    {
        Log.i("MAT44", "[" + matrix.get(0,0) +"," + matrix.get(0,1 )+", " + matrix.get(0,2) +", " +matrix.get(0,3) + "]");
        Log.i("MAT44", "[" + matrix.get(1,0) +"," + matrix.get(1,1 )+", " + matrix.get(1,2) +", " +matrix.get(1,3) + "]");
        Log.i("MAT44", "[" + matrix.get(2,0) +"," + matrix.get(2,1 )+", " + matrix.get(2,2) +", " +matrix.get(2,3) + "]");
        Log.i("MAT44", "[" + matrix.get(3,0) +"," + matrix.get(3,1 )+", " + matrix.get(3,2) +", " +matrix.get(3,3) + "]");
    }


}
