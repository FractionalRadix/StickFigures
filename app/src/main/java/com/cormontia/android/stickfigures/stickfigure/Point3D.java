package com.cormontia.android.stickfigures.stickfigure;

import com.cormontia.android.stickfigures.matrix3d.Vec4;

public class Point3D
{
    private double x, y, z;

    public Point3D( double x, double y, double z )
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec4 getVector( )
    {
        return new Vec4( x, y, z, 1 );
    }
}
