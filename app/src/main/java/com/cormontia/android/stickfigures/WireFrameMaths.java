package com.cormontia.android.stickfigures;

import com.cormontia.android.stickfigures.matrix3d.Vec4;

public class WireFrameMaths
{
    void placeholder(Vec4 P, Vec4 Q)
    {
        double dx = P.get( 0 ) - Q.get( 0 );
        double dy = P.get( 1 ) - Q.get( 1 );
        double dz = P.get( 2 ) - Q.get( 2 );

        double dxSquared = dx * dx;
        double dySquared = dy * dy;
        double dzSquared = dz * dz;

        //TODO!+ Careful what happens if any of the hypothenuses is 0 !!
        double hypotXY = Math.sqrt( dxSquared + dySquared );
        double hypotXZ = Math.sqrt( dxSquared + dzSquared );
        double hypotZY = Math.sqrt( dySquared + dzSquared );

        // Determine the angle around the X axis.
        // If the dY == dZ == 0 then the rotation around the X axis can be anything, so we set it to 0.
        // (This prevents a division by zero error).
        double angleX = 0;
        if (hypotZY != 0) //TODO!+ Slightly more robust test for zero values.
        {
            double sinX = dy / hypotZY;
            double cosX = dz / hypotZY;
            angleX = Math.atan2( sinX, cosX );
        }

        // Determine the angle around the Y axis.
        // If the dX == dZ == 0 then the rotation around the Y axis can be anything, so we set it to 0.
        // (This prevents a division by zero error).
        double angleY = 0;
        if (hypotXZ != 0) //TODO!+ Slightly more robust test for zero values.
        {
            double sinY = dz / hypotXZ;
            double cosY = dx / hypotXZ;
            // Angle around the Y axis.
            angleY = Math.atan2( sinY, cosY );
        }

        // Determine the angle around the Z axis.
        // If the dX == dY == 0 then the rotation around the Z axis can be anything, so we set it to 0.
        // (This prevents a division by zero error).
        double angleZ = 0;
        if (hypotXY != 0) //TODO!+ Slightly more robust test for zero values.
        {
            double sinZ = dx / hypotXY;
            double cosZ = dy / hypotXY;
            // Angle around the Z axis.
            angleZ = Math.atan2( sinZ, cosZ );
        }

    }

    double angle(double sinAngle, double cosAngle)
    {
        return Math.atan2( sinAngle, cosAngle );
    }
}
