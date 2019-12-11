package com.cormontia.android.stickfigures.stickfigure;

/**
 * Represents a point in 3D space, by its spherical coordinates.
 */
public class SphericalCoordinates
{
    double phi, theta, length;

    public SphericalCoordinates( double phi, double theta, double length )
    {
        this.phi = phi;
        this.theta = theta;
        this.length = length;
    }

    double getPhi( ) { return phi; }
    double getTheta( ) { return theta; }
    double getLength( ) { return length; }
}
