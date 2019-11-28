package com.cormontia.android.stickfigures.matrix3d;

public class Mat44
{
    // Convention: a[row][column]
    private double[][] val;

    public Mat44( )
    {
        val = new double[ 4 ][ 4 ];
        zero( );
    }

    /** Fills the Matrix with zeroes.
     * It must be <CODE>final</CODE> because it is called from the Constructor.
     */
    final public void zero( )
    {
        for ( int r = 0; r < 4; r++ )
        {
            for ( int c = 0; c < 4; c++ )
            {
                val[ r ][ c ] = 0.0;
            }
        }
    }

    public void set( int row, int column, double value )
    {
        val[ row ][ column ] = value;
    }

    public double get( int row, int column )
    {
        return ( val[ row ][ column ] );
    }

    /**
     * Determine <CODE>this * v</CODE>.
     */
    public Vec4 multiply( Vec4 v )
    {
        Vec4 res = new Vec4( 0.0, 0.0, 0.0, 0.0 );
        for ( int row = 0; row < 4; row++ )
        {
            double intermediate_result = Vec4.multiply( getRow( row ), v );
            res.set( row, intermediate_result );
        }
        return ( res );
    }

    /**
     * Determine <CODE>this * [0,0,0,1]</CODE>.
     */
    public Vec4 multiplyOrigin(  )
    {
        Vec4 res = new Vec4( getRow( 0 ).get( 3 ), getRow( 1 ).get( 3 ), getRow( 2 ).get( 3 ), getRow( 3 ).get( 3 ) );
        return ( res );
    }

    public Vec4 getRow( int row )
    {
        //TODO!+ Bounds check
        Vec4 v = new Vec4(
                val[ row ][ 0 ],
                val[ row ][ 1 ],
                val[ row ][ 2 ],
                val[ row ][ 3 ]
        );
        return ( v );
    }

    public Vec4 getColumn( int col )
    {
        // TODO!+ Bounds check
        Vec4 v = new Vec4(
                val[ 0 ][ col ],
                val[ 1 ][ col ],
                val[ 2 ][ col ],
                val[ 3 ][ col ]
        );
        return ( v );
    }

    public static Mat44 multiply( Mat44 a, Mat44 b )
    {
        Mat44 res = new Mat44( );

        for ( int row = 0; row < 4; row++ )
        {
            for ( int col = 0; col < 4; col++ )
            {
                res.set( row, col, Vec4.multiply( a.getRow( row ), b.getColumn( col ) ) );
            }
        }

        return ( res );
    }

    /*
    // For debugging
    public void print( PrintStream ps )
    {
        for ( int row = 0; row < 4; row++ )
        {
            ps.println( );
            for ( int col = 0; col < 4; col++ )
            {
                ps.print( "\t" + val[ row ][ col ] );
            }
        }
    }
    */

}
