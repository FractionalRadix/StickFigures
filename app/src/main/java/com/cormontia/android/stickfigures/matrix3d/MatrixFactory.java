package com.cormontia.android.stickfigures.matrix3d;

public class MatrixFactory
{
    public static Mat44 Identity( )
    {
        Mat44 m = new Mat44( );
        for ( int i = 0; i < 4; i++ )
            m.set( i, i, 1.0 );
        return ( m );
    }

    public static Mat44 Scale( double Sx, double Sy, double Sz )
    {
        Mat44 m = new Mat44( );

        m.set( 0, 0, Sx );
        m.set( 1, 1, Sy );
        m.set( 2, 2, Sz );
        m.set( 3, 3, 1.0 );

        return ( m );
    }

    public static Mat44 Translate( double Tx, double Ty, double Tz )
    {
        Mat44 m = Identity( );

        m.set( 0, 3, Tx );
        m.set( 1, 3, Ty );
        m.set( 2, 3, Tz );
        m.set( 3, 3, 1.0 );

        return ( m );
    }

    public static Mat44 RotateAroundX( double angle )
    {
        Mat44 m = new Mat44( );

        double ca = Math.cos( angle );
        double sa = Math.sin( angle );

        m.set( 0, 0, 1.0 );
        m.set( 1, 1, ca );
        m.set( 1, 2, -sa );
        m.set( 2, 1, sa );
        m.set( 2, 2, ca );
        m.set( 3, 3, 1.0 );

        return( m );
    }

    public static Mat44 RotateAroundY( double angle )
    {
        Mat44 m = new Mat44( );

        double ca = Math.cos( angle );
        double sa = Math.sin( angle );

        m.set( 0, 0, ca );
        m.set( 0, 2,sa );
        m.set( 1, 1, 1.0 );
        m.set( 2, 0, -sa );
        m.set( 2, 2, ca );
        m.set( 3, 3, 1.0 );

        return( m );
    }

    public static Mat44 RotateAroundZ( double angle )
    {
        Mat44 m = new Mat44( );

        double ca = Math.cos( angle );
        double sa = Math.sin( angle );

        m.set( 0, 0, ca );
        m.set( 0, 1, -sa );
        m.set( 1, 0, sa );
        m.set( 1, 1, ca );
        m.set( 2, 2, 1.0 );
        m.set( 3, 3, 1.0 );

        return( m );
    }

    public static Mat44 RotateAroundAxis( Vec4 startOfAxis, Vec4 endOfAxis, double angle )
    {
        // From: "Robotics: Control, Sensing, Vision and Intelligence", K.S. Fu, R.C. Gonzalez, C.S.G. Lee,
        // McGraw-Hill publishers, 1987.

        //TODO!+ Exceptional case if startOfAxis==endOfAxis
        // That is, everything that makes Plength == 0.0....

        double Px = endOfAxis.get( 0 ) - startOfAxis.get( 0 );
        double Py = endOfAxis.get( 1 ) - startOfAxis.get( 1 );
        double Pz = endOfAxis.get( 2 ) - startOfAxis.get( 2 );
        double Plength = Math.sqrt( Px * Px + Py * Py + Pz * Pz );

        double rx = Px / Plength;
        double ry = Py / Plength;
        double rz = Pz / Plength;

        double Sa = Math.sin( angle );
        double Ca = Math.cos( angle );
        double Va = 1.0 - Ca;

        Mat44 rot = MatrixFactory.Identity( );
        rot.set( 0, 0, rx*rx*Va + Ca );
        rot.set( 0, 1, rx*ry*Va - rz*Sa );
        rot.set( 0, 2, rx*rz*Va + ry*Sa );

        rot.set( 1, 0, rx*ry*Va + rz*Sa );
        rot.set( 1, 1, ry*ry*Va + Ca );
        rot.set( 1, 2, ry*rz*Va - rx*Sa );

        rot.set( 2, 0, rx*rz*Va - ry*Sa );
        rot.set( 2, 1, ry*rz*Va + rx*Sa );
        rot.set( 2, 2, rz*rz*Va + Ca );

        Mat44 res = Mat44.multiply( MatrixFactory.Translate( -Px, -Py, -Pz ), rot );
        res = Mat44.multiply( res, MatrixFactory.Translate( +Px, +Py, +Pz ) );

        return ( res );
    }

}
