package com.cormontia.android.stickfigures.matrix3d;

public class Vec4
{
    private double[] data;

    public Vec4( double a, double b, double c, double d )
    {
        data = new double[ ] { a, b, c, d };
    }

    public double get( int index )
    {
        //TODO!+ Bounds check
        return ( data[ index ] );
    }

    public void set( int index, double value )
    {
        //TODO!+ Bounds check
        data[ index ] = value;
    }

    public static double multiply( Vec4 a, Vec4 b )
    {
        double res = 0.0;
        for ( int i = 0; i < 4; i++ )
            res += ( a.get( i ) * b.get( i ) );
        return ( res );
    }
}
