package com.example.ihavetofly;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements Runnable{

    private Thread thread;
    boolean isPlaying;
    private int screenX,screenY;
    public static float screenRatioX,screenRatioY;
    private Paint paint;
    private Flight flight;
    private Bacground bacground1,bacground2;

    public GameView(Context context ,int screenX,int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX =1920f/screenX;
        screenRatioY = 1080f/screenY;
        flight = new Flight(screenY,getResources());
        bacground1 = new Bacground(screenX,screenY,getResources());
        bacground2 = new Bacground(screenX,screenY,getResources());
        bacground2.x = screenX;

        paint = new Paint();
    }


    @Override
    public void run() {
        while (isPlaying)
        {
            update();
            draw();
            sleep();
        }
    }
    private void update()
    {
        bacground1.x -= 5*screenRatioX;
        bacground2.x -= 5*screenRatioX;
        if (bacground1.x + bacground1.background.getWidth()< 0)
        {
            bacground1.x = screenX;
        }
        if (bacground2.x + bacground2.background.getWidth()< 0)
        {
            bacground2.x = screenX;
        }
        if (flight.isGoingUp)
        {
            flight.y-=10*screenRatioY;
        }
        else
        {
            flight.y+=10*screenRatioY;
        }

        if (flight.y<0)
        {
            flight.y=0;
        }
        if (flight.y>=screenY - flight.height)
        {
            flight.y = screenY-flight.height;
        }
    }
    private void draw()
    {
        if(getHolder().getSurface().isValid())
        {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(bacground1.background,bacground1.x,bacground1.y,paint);
            canvas.drawBitmap(bacground2.background,bacground2.x,bacground2.y,paint);

            canvas.drawBitmap(flight.getFlight(),flight.x,flight.y,paint);
            getHolder().unlockCanvasAndPost(canvas);


        }
    }
    private void sleep()
    {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume()
    {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }
    public  void pause()
    {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX/2)
                {
                    flight.isGoingUp=true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp=false;
                break;
        }
        return true;

    }
}
