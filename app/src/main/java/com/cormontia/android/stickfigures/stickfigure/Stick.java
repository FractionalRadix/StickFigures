package com.cormontia.android.stickfigures.stickfigure;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.util.Pair;

import com.cormontia.android.stickfigures.matrix3d.Mat44;
import com.cormontia.android.stickfigures.matrix3d.MatrixFactory;
import com.cormontia.android.stickfigures.matrix3d.Vec4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a single stick in a stick figure.
 * Sticks are defined by their position, length and orientation.
 * The length is usually fixed.
 * The orientation is expressed as yaw, pitch and roll values, in radians.
 * The position depends on how the stick is positioned with respect to other sticks in the stick figure.
 * Our stick figures are composed as trees: each stick has zero or more "child" sticks attached to its end.
 *
 */
public class Stick
{
    private double length;
    private double yaw, pitch, roll;
    private List<Stick> children = new ArrayList<Stick>();

    /** Derived value: starting position of the stick.
     * This value is derived from the parent joint and associated limb length.
     * This data member is just the cached value.
     */
    private Vec4 position;

    private Paint black  = new Paint( Color.BLACK );

    private void initPaint( )
    {
        black.setStyle( Paint.Style.STROKE );
        black.setStrokeWidth( 3.0f );
    }

    public Stick( Point3D P, Point3D Q )
    {
        //TODO!+ Move this elsewhere.
        initPaint( );

        SphericalCoordinates coordinates = calculateAngles( P, Q );
        this.yaw = coordinates.getTheta( );
        this.pitch = coordinates.getPhi( );
        this.length = coordinates.getLength( );

        this.position = P.getVector( );
    }

    public Stick(double x, double y, double z, double length)
    {
        //TODO!+ Move this elsewhere.
        initPaint( );

        position = new Vec4( x, y, z, 1 );

        this.length = length;
        this.yaw = 0;
        this.pitch = 0;
        this.roll = 0;
    }

    /**
     * Given a line segment PQ.
     * Determine the phi and theta angles (spherical coordinates) to go from P to Q.
     * @param P Starting point of the line segment.
     * @param Q Ending point of the line segment.
     * @return The spherical coordinates needed to move to Q, starting from P.
     */
    SphericalCoordinates calculateAngles( Point3D P, Point3D Q )
    {
        Vec4 vecP = P.getVector( );
        double Px = vecP.get( 0 );
        double Py = vecP.get( 1 );
        double Pz = vecP.get( 2 );

        Vec4 vecQ = Q.getVector( );
        double Qx = vecQ.get( 0 );
        double Qy = vecQ.get( 1 );
        double Qz = vecQ.get( 2 );

        double dx = Qx - Px;
        double dy = Qy - Py;
        double dz = Qz - Pz;

        double length = Math.sqrt( dx * dx + dy * dy + dz * dz );

        // The polar angle, phi. We arbitrarily default it to 0 when the radius (r, the length of our line segment) is 0.
        double phi = 0;
        if (length != 0 ) {
            phi = Math.acos(dz / length);
        }

        // The azimuthal angle, theta. We arbitrarily default it to 0 when dx == 0 AND dy == 0
        double theta = 0;
        if (! ( dx == 0.0 && dy == 0.0 ) ) {
            theta = Math.atan2(dy, dx); // Officially Math.atan( dy / dx ), but what if dx = 0 ?
        }

        return new SphericalCoordinates( phi, theta, length );
    }

    //TODO?~
    /**
     * Add a limb, that goes from this joint to the absolute point (x,y,z).
     * (As opposed to declaring it relative from this joint).
     * This method will then calculate the values of the joint angles and limb length.
     * @param x Absolute x-position where the new limb ends.
     * @param y Absolute y-position where the new limb ends.
     * @param z Absolute z-position where the new limb ends.
     * @return The Stick instance at the end of the new limb.
     */
    public Stick addLineTo(double x, double y, double z)
    {
        Stick child = new Stick(x, y, z, 0);
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

        child.length = Math.sqrt( dx * dx + dy * dy + dz * dz );

        // The polar angle, phi. We arbitrarily default it to 0 when the radius (r, the length of our line segment) is 0.
        double phi = 0;
        if (child.length != 0 ) {
            phi = Math.acos(dz / child.length);
        }

        //Log.i("PROP-ANGLES", "phi=="+phi+",dz=="+dz+",length=="+child.length);

        // The azimuthal angle, theta. We arbitrarily default it to 0 when dx == 0 AND dy == 0
        double theta = 0;
        if (! ( dx == 0.0 && dy == 0.0 ) ) {
            theta = Math.atan2(dy, dx); // Officially Math.atan( dy / dx ), but what if dx = 0 ?
        }

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
        for (Stick childNode : children)
        {
            //Log.i("PROP-ANGLES", "yaw=="+yaw+", pitch=="+pitch+", roll=="+roll+", length=="+length);

            // Rotation around the Z-axis corresponds to yaw, rotation around the Y axis corresponds to pitch.
            // Rotation around the X-axis corresponds to roll.
            // Next question is, what comes first?
            Mat44 yawMatrix = MatrixFactory.RotateAroundZ( childNode.yaw );
            Mat44 pitchMatrix = MatrixFactory.RotateAroundY( childNode.pitch );
            //Mat44 rollMatrix = MatrixFactory.RotateAroundX( childNode.roll );
            //Mat44 rotationMatrix = Mat44.multiply( rollMatrix, Mat44.multiply( pitchMatrix, yawMatrix ) );
            Mat44 rotationMatrix = Mat44.multiply( yawMatrix, pitchMatrix );

            Mat44 translationMatrix = MatrixFactory.Translate( 0, 0, childNode.length );
            Mat44 total = Mat44.multiply( translationMatrix, rotationMatrix );
            //Mat44 total = Mat44.multiply( rotationMatrix, translationMatrix );

            double x = position.get(0);
            double y = position.get(1);
            double z = position.get(2);
            Mat44 preTranslate = MatrixFactory.Translate(-x,-y,-z);
            Mat44 postTranslate = MatrixFactory.Translate(x,y,z);
            //Log.i("PROP-ANGLES", "position==("+position.get(0)+", "+position.get(1)+", "+position.get(2)+")");

            //childNode.position = total.multiply( position );
            Mat44 res0 = Mat44.multiply(translationMatrix, preTranslate);
            Mat44 res1 = Mat44.multiply(pitchMatrix, res0);
            Mat44 res2 = Mat44.multiply(yawMatrix, res1);
            Mat44 res3 = Mat44.multiply(postTranslate, res2);

            childNode.position = res3.multiply(position);


            //Log.i("PROP-ANGLES", "child position==("+childNode.position.get(0)+", "+childNode.position.get(1)+", "+childNode.position.get(2)+")");

            childNode.propagateAngles();
        }
    }

    //TODO?~
    private Mat44 matrixProduct( Mat44...m )
    {
        if ( m == null || m.length == 0 )
        {
            return MatrixFactory.Identity();
        }

        if ( m.length == 1 )
        {
            return m[0]; //TODO?~ Return a copy?
        }

        Mat44 res = m[ m.length - 1 ];
        for ( int i = m.length - 2; i >= 0; i-- )
        {
            res = Mat44.multiply(res, m[i]);
        }
        return res;
    }

    PointF draw2(Canvas c, Mat44 fullTransformation)
    {
        PointF p0 = calc(position, fullTransformation);
        Iterator<Stick> iter = children.iterator( );
        while (iter.hasNext( ) )
        {
            Stick child = iter.next( );
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

    public double getLength( ) { return length; } // Needed for the unit tests...
}
