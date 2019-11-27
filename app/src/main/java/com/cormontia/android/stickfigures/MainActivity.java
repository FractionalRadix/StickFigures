package com.cormontia.android.stickfigures;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        init();
    }

    private void init( )
    {
        //Paint paint = new Paint( );
        //paint.setColor( Color.GREEN );

        //StickFigure fig1 = new StickFigure();
        //fig1.stickFigHuman();
    }
}
