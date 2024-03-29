package com.cormontia.android.stickfigures;

import com.cormontia.android.stickfigures.matrix3d.Vec4;
import com.cormontia.android.stickfigures.stickfigure.Stick;

import org.junit.Test;

public class JointParametersUnitTest
{
    @Test
    public void testUnitLengthXaxis( )
    {
        Stick j0 = new Stick(0,0,0,0 );
        Stick j1 = j0.addLineTo(1, 0, 0 );
        // Yaw is theta, pitch is phi.
        org.junit.Assert.assertEquals(1.0,           j1.getLength( ),0.00001 );
        org.junit.Assert.assertEquals(0.0 * Math.PI, j1.getYaw( ),   0.00001 );
        org.junit.Assert.assertEquals(0.5 * Math.PI, j1.getPitch( ), 0.00001 );
    }

    @Test
    public void testUnitLengthYaxis( )
    {
        Stick j0 = new Stick(0,0,0,0 );
        Stick j1 = j0.addLineTo(0,1,0 );
        // Yaw is theta, pitch is phi.
        org.junit.Assert.assertEquals(0.5 * Math.PI, j1.getYaw( ),  0.00001 );
        org.junit.Assert.assertEquals(0.5 * Math.PI, j1.getPitch( ),0.00001 );
    }

    @Test
    public void testUnitLengthZaxis( )
    {
        Stick j0 = new Stick(0,0,0, 0 );
        Stick j1 = j0.addLineTo(0,0,1 );
        // Yaw is theta, pitch is phi.
        org.junit.Assert.assertEquals(0.0 * Math.PI, j1.getYaw( ),  0.00001 ); // Actually yaw can be any value....
        org.junit.Assert.assertEquals(0.0 * Math.PI, j1.getPitch( ),0.00001 );
    }

    @Test
    public void testRecalc( )
    {
        Stick j0 = new Stick(3,4,5, 0);
        Stick j1 = j0.addLineTo( 5,6,8 );
        j0.propagateAngles( );
        Vec4 newLocation = j1.location( );
        org.junit.Assert.assertEquals( 5, newLocation.get( 0 ), 0.00001 );
        org.junit.Assert.assertEquals( 6, newLocation.get( 1 ), 0.00001 );
        org.junit.Assert.assertEquals( 8, newLocation.get( 2 ), 0.00001 );

    }
}
