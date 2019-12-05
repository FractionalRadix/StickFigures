package com.cormontia.android.stickfigures;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.cormontia.android.stickfigures.stickfigure.HumanStickFigure;
import com.cormontia.android.stickfigures.stickfigure.StickFigure;
import com.cormontia.android.stickfigures.stickfigure.StickFigureFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class MyView extends View
{
    // Parameters for the drawing window.
    private int paddingLeft, paddingTop, paddingRight, paddingBottom;
    private int contentWidth, contentHeight;

    // Parameters for the animation.
    private int dx = 1, dy;
    private float x = 0.3f;

    private ValueAnimator valueAnim;

    private List<StickFigure> stickFigures = new ArrayList<>();

    public MyView(Context context)
    {
        super( context );
        init( null, 0 );
    }

    public MyView(Context context, AttributeSet attrs)
    {
        super( context, attrs );
        init( attrs, 0 );
    }

    public MyView(Context context, AttributeSet attrs, int defStyle)
    {
        super( context, attrs, defStyle );
        init( attrs, defStyle );
    }

    private void init(AttributeSet attrs, int defStyle)
    {
        // Set the parameters for the drawing window.
        paddingLeft = getPaddingLeft( );
        paddingTop = getPaddingTop( );
        paddingRight = getPaddingRight( );
        paddingBottom = getPaddingBottom( );

        contentWidth = getWidth( ) - paddingLeft - paddingRight;
        contentHeight = getHeight( ) - paddingTop - paddingBottom;

        // Initialize the animation.
        valueAnim = ValueAnimator.ofInt( 0, 359 );
        //valueAnim  = ValueAnimator.ofFloat( 0.0f, 2.68f );
        valueAnim.setDuration( 6000 ); // duration in microseconds.
        valueAnim.setRepeatCount( ValueAnimator.INFINITE );
        valueAnim.setRepeatMode( ValueAnimator.REVERSE );

        final HumanStickFigure human = new HumanStickFigure();
        StickFigure fig1 = human.createHuman();
        StickFigure fig2 = StickFigureFactory.createDog( );
        stickFigures.add( fig1 );
        stickFigures.add( fig2 );

        valueAnim.addUpdateListener(
            new ValueAnimator.AnimatorUpdateListener( )
            {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator)
                {
                    Integer animatedValue = (Integer) valueAnimator.getAnimatedValue( );
                    x = animatedValue.floatValue();
                    human.rotateLeftShoulder();
                    invalidate();
                }
            }
        );

        valueAnim.start( );

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw( canvas );

        double degToRadFactor = Math.PI / 180.0;
        double t = x * degToRadFactor;
        double factor = 1;
        for (StickFigure figure : stickFigures )
        {
            factor *= -1;
            if ( figure != null )
            {
                figure.draw( canvas, 0, (float) (factor * t)  );
            }
        }
    }

}
