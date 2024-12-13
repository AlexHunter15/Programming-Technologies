package com.example.unitconverter;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GestureActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast.makeText(GestureActivity.this, "Двойной тап!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
