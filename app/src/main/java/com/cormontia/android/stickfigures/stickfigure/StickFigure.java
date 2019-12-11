package com.cormontia.android.stickfigures.stickfigure;

import android.graphics.Canvas;
import android.util.Log;

import com.cormontia.android.stickfigures.matrix3d.Mat44;
import com.cormontia.android.stickfigures.matrix3d.MatrixFactory;

import java.util.ArrayList;
import java.util.List;

public class StickFigure
{
    private Mat44 fullTransformation; // From world coordinates to screen coordinates.

    private Stick root;
    private List<Stick> sticks = new ArrayList<>( );

    /**
     * Create a new Stick Figure, with a root point at (x,y,z).
     * @param x x-coordinate of the starting point of the stick figure.
     * @param y y-coordinate of the starting point of the stick figure.
     * @param z z-coordinate of the starting point of the stick figure.
     */
    public StickFigure( double x, double y, double z )
    {
        // The first "stick" is a 0-length stick.
        root = new Stick( x, y, z,0 );
        sticks.add( root );
    }

    public void propagateAngles( )
    {
        root.propagateAngles();
    }

    /**
     * Add a new Stick to the root point.
     * @param endPointX X coordinate of the end of the next stick.
     * @param endPointY Y coordinate of the end of the next stick.
     * @param endPointZ Z coordinate of the end of the next stick.
     * @return ID of the new Stick.
     */
    public int addStickToRoot( double endPointX, double endPointY, double endPointZ )
    {
        Stick source = sticks.get( 0 );
        Stick nextJoint = source.addLineTo( endPointX, endPointY, endPointZ );
        sticks.add( nextJoint );
        return sticks.size( )-1;
    }

    /**
     * Given the ID of a Stick, add a new Stick to its end.
     * @param sourceID ID of the stick, to which the new stack will be attached.
     * @param endPointX X coordinate of the end of the next stick.
     * @param endPointY Y coordinate of the end of the next stick.
     * @param endPointZ Z coordinate of the end of the next stick.
     * @return ID of the new Stick.
     */
    public int addStickToStick(int sourceID, double endPointX, double endPointY, double endPointZ )
    {
        Stick source = sticks.get( sourceID );
        Stick nextStick = source.addLineTo( endPointX, endPointY, endPointZ );
        sticks.add( nextStick );
        return sticks.size( )-1;
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

    public void draw( Canvas canvas, int clockTick, float x )
    {
        fullTransformation = this.determineTransformationMatrix(); //TODO!~ No need to re-calculate this for every re-draw....
        Mat44 trans = MatrixFactory.Translate( 0.1 * x, 0.1 * x,  0.1 * x );
        Mat44 rot = MatrixFactory.RotateAroundZ( x );

        fullTransformation = Mat44.multiply( fullTransformation, Mat44.multiply( trans, rot ) );

        //if (root == null)
        //{
        //    Stick start = sticks.get(0);
        //    start.draw2( canvas, fullTransformation );
        //}
        //else
        //{
            root.draw2(canvas, fullTransformation);
        //}
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

        //logMatrix( projection );

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

    double getYaw( int jointID ) { return sticks.get( jointID ).getYaw( ); }
    void setYaw( int jointID, double yaw ) { sticks.get(jointID).setYaw( yaw ); }

    double getPitch( int jointID ) { return sticks.get( jointID ).getPitch( ); }
    void setPitch( int jointID , double pitch ) { sticks.get( jointID ).setPitch( pitch ); }

    double getRoll( int jointID ) { return sticks.get( jointID ).getRoll( ); }
    void setRoll( int jointID , double roll ) { sticks.get( jointID ).setRoll( roll ); }

}
