package com.example.unitconverter;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private TextView historyText;
    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        historyText = findViewById(R.id.historyText);

        // Получаем историю преобразований, переданную из MainActivity
        ArrayList<String> conversionHistory = getIntent().getStringArrayListExtra("conversionHistory");

        if (conversionHistory != null && !conversionHistory.isEmpty()) {
            StringBuilder historyBuilder = new StringBuilder();
            for (String entry : conversionHistory) {
                historyBuilder.append(entry).append("\n");
            }
            historyText.setText(historyBuilder.toString());
        } else {
            historyText.setText("История преобразований пуста.");
        }

        gestureDetector = new GestureDetectorCompat(this, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();

            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    onSwipeRight();
                }
                return true;
            }
            return false;
        }

        private void onSwipeRight() {
            Toast.makeText(SecondActivity.this, "Возврат в главное меню", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
