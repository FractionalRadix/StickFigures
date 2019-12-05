package com.cormontia.android.stickfigures.stickfigure;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import android.util.Log;

import com.cormontia.android.stickfigures.matrix3d.Mat44;
import com.cormontia.android.stickfigures.matrix3d.MatrixFactory;
import com.cormontia.android.stickfigures.matrix3d.Vec4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a joint and the associated limb.
 */
public class Joint
{
    /** Location of the joint.
     * This value is derived from the parent joint and associated limb length.
     * This data member is just the cached value.
     */
    private Vec4 position;

    private double length;
    private double yaw, pitch, roll;
    private List<Joint> children = new ArrayList<Joint>();

    private Paint black  = new Paint( Color.BLACK );

    public Joint(double x, double y, double z)
    {
//TODO!+ Move this elsewhere.
        black.setStyle( Paint.Style.STROKE );
        black.setStrokeWidth( 3.0f );

        position = new Vec4( x, y, z, 1 );
    }

    /**
     * Add a limb, that goes from this joint to the absolute point (x,y,z).
     * (As opposed to declaring it relative from this joint).
     * This method will then calculate the values of the joint angles and limb length.
     * @param x Absolute x-position where the new limb ends.
     * @param y Absolute y-position where the new limb ends.
     * @param z Absolute z-position where the new limb ends.
     * @return The Joint instance at the end of the new limb.
     */
    public Joint addLineTo(double x, double y, double z)
    {
        Joint child = new Joint(x, y, z);
        children.add(child);

        // We use spherical coordinates.
        // Yaw = theta, pitch = phi, roll = 0.
        // We may want to do a little extra work on pitch, e.g. pitch = ( 360 + (180 - phi) ) mod 360.

        double Px = this.position.get( 0 );
        double Py = this.position.get( 1 );
        double Pz = this.position.get( 2 );

        double Qx = x;
        double Qy = y;
        double Qz = z;

        double dx = Qx - Px;
        double dy = Qy - Py;
        double dz = Qz - Pz;

        length = Math.sqrt( dx * dx + dy * dy + dz * dz );

        double phi = Math.acos( dz / length );
        double theta = Math.atan2( dy, dx ); // Officially Math.atan( dy / dx ), but what if dx = 0 ?
            //TODO!+ Deal with the case that dx == 0 AND dy == 0.

        child.yaw = theta;
        child.pitch = phi;
        child.roll = 0;

        // TODO!+ Verify that these values are equal:
        // child.position = new Vec4( x, y, z, 1 );
        // child.position = Rotate ( yaw ) * Rotate( pitch ) * Translate( r )

        return child;
    }

    public Vec4 location( )
    {
        return position; //TODO?~  Copy value?
    }

    private PointF calc(Vec4 position, Mat44 transform)
    {
        //TODO?+ Also include re-calculation of position? I.e. propagate the limb rotations and translations?
        //  Or do that elsewhere?
        Vec4 projection = transform.multiply( position );
        PointF res = new PointF( (float) projection.get( 0 ), (float) projection.get( 1 ) ); //TODO?~ projection.get(2) instead of projection.get(1) ?
        return res;
    }

    /** Recursively re-calculate the positions of the joints, based on yaw-pitch-roll values.
     */
    public void propagateAngles( )
    {
        for (Joint childNode : children)
        {
            // Rotation around the Z-axis corresponds to yaw, rotation around the Y axis corresponds to pitch.
            // Rotation around the X-axis correspodns to roll.
            // Next question is, what comes first?
            Mat44 yawMatrix = MatrixFactory.RotateAroundZ( childNode.yaw );
            Mat44 pitchMatrix = MatrixFactory.RotateAroundY( childNode.pitch );
            Mat44 rollMatrix = MatrixFactory.RotateAroundX( childNode.roll );
            Mat44 rotationMatrix = Mat44.multiply( rollMatrix, Mat44.multiply( pitchMatrix, yawMatrix ) );

            Mat44 translationMatrix = MatrixFactory.Translate( 0, 0, childNode.length );
            //Mat44 intermediate1 = Mat44.multiply( yawMatrix, rollMatrix );
            //Mat44 intermediate2 = Mat44.multiply( pitchMatrix, translationMatrix );
            //Mat44 total = Mat44.multiply( intermediate1, intermediate2 );
            Mat44 total = Mat44.multiply( rotationMatrix, translationMatrix );

            //Log.i("PROP-ANGLE", "Child node pitch=="+cihl)
            childNode.position = total.multiply( position );

            childNode.propagateAngles();
        }
    }

    PointF draw2(Canvas c, Mat44 fullTransformation)
    {
        PointF p0 = calc(position, fullTransformation);
        Iterator<Joint> iter = children.iterator( );
        while (iter.hasNext( ) )
        {
            Joint child = iter.next( );
            //Log.i("JOINT-DRAW",""+child.getYaw());
            PointF p1 = child.draw2(c, fullTransformation);
            c.drawLine(p0.x, p0.y, p1.x, p1.y, black);
        }
        return p0;
    }

    public double getYaw( ) { return yaw; }
    void setYaw( double yaw ) { this.yaw = yaw; }

    public double getPitch( ) { return pitch; }
    void setPitch( double pitch ) { this.pitch = pitch; }

    public double getRoll( ) { return roll; }
    void setRoll( double roll ) { this.roll = roll; }
}
