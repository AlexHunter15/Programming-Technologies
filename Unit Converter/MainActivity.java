package com.example.unitconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText inputValue;
    private Button convertButton, historyButton;
    private RadioGroup unitGroup;
    private TextView outputText;
    private ImageView backgroundImage;

    private ArrayList<String> conversionHistory = new ArrayList<>();
    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        convertButton = findViewById(R.id.convertButton);
        historyButton = findViewById(R.id.historyButton);
        unitGroup = findViewById(R.id.unitGroup);
        outputText = findViewById(R.id.outputText);
        backgroundImage = findViewById(R.id.backgroundImage);

        gestureDetector = new GestureDetectorCompat(this, new GestureListener());

        // Загрузка истории преобразований из SharedPreferences
        loadHistory();

        // Применение анимации для кнопок
        setButtonAnimations();

        convertButton.setOnClickListener(v -> {
            convert();
            saveHistory(); // Сохраняем историю после преобразования
        });

        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putStringArrayListExtra("conversionHistory", conversionHistory);
            startActivity(intent);
        });

        // Устанавливаем изображение фона
        backgroundImage.setImageResource(R.drawable.im); // Изображение фона
    }

    private void setButtonAnimations() {
        // Анимация для кнопки "Преобразовать" - увеличение при нажатии
        ScaleAnimation scaleAnim = new ScaleAnimation(
                1.0f, 1.2f,  // scale X (от 1.0 до 1.2)
                1.0f, 1.2f,  // scale Y (от 1.0 до 1.2)
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot X (центр)
                Animation.RELATIVE_TO_SELF, 0.5f  // Pivot Y (центр)
        );
        scaleAnim.setDuration(200);
        scaleAnim.setFillAfter(true);
        convertButton.setAnimation(scaleAnim);

        // Анимация для кнопки "История" - изменение прозрачности
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.5f); // Начальная прозрачность 1, конечная 0.5
        alphaAnim.setDuration(200);
        alphaAnim.setRepeatCount(Animation.INFINITE);
        alphaAnim.setRepeatMode(Animation.REVERSE);
        historyButton.startAnimation(alphaAnim);
    }

    private void convert() {
        String input = inputValue.getText().toString();

        if (input.isEmpty()) {
            Toast.makeText(MainActivity.this, "Введите значение для преобразования", Toast.LENGTH_SHORT).show();
            return;
        }

        double value;
        try {
            value = Double.parseDouble(input);
            if (value <= 0) {
                Toast.makeText(MainActivity.this, "Значение должно быть больше нуля", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, "Введите корректное число", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedUnitId = unitGroup.getCheckedRadioButtonId();
        if (selectedUnitId == -1) {
            Toast.makeText(MainActivity.this, "Выберите единицу измерения", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedUnitId);
        String unit = selectedRadioButton.getText().toString();

        double result = 0;
        String output = "";

        switch (unit) {
            case "Метры в футы":
                result = value * 3.28084;
                output = String.format("Результат: %.2f футов", result);
                break;
            case "Футы в метры":
                result = value * 0.3048;
                output = String.format("Результат: %.2f метров", result);
                break;
            case "Метры в дюймы":
                result = value * 39.3701;
                output = String.format("Результат: %.2f дюймов", result);
                break;
            case "Дюймы в метры":
                result = value * 0.0254;
                output = String.format("Результат: %.2f метров", result);
                break;
            case "Метры в ярды":
                result = value * 1.09361;
                output = String.format("Результат: %.2f ярдов", result);
                break;
            case "Ярды в метры":
                result = value * 0.9144;
                output = String.format("Результат: %.2f метров", result);
                break;
            default:
                Toast.makeText(MainActivity.this, "Неизвестная операция", Toast.LENGTH_SHORT).show();
                return;
        }

        String historyEntry = String.format("Преобразовано %.2f %s в %.2f", value, unit, result);
        conversionHistory.add(historyEntry);
        outputText.setText(output);
    }

    private void saveHistory() {
        SharedPreferences prefs = getSharedPreferences("UnitConverterPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> historySet = new HashSet<>(conversionHistory);
        editor.putStringSet("conversionHistory", historySet);
        editor.apply();
    }

    private void loadHistory() {
        SharedPreferences prefs = getSharedPreferences("UnitConverterPrefs", MODE_PRIVATE);
        Set<String> historySet = prefs.getStringSet("conversionHistory", new HashSet<>());
        conversionHistory = new ArrayList<>(historySet);
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
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {

                }
                return true;
            }
            return false;
        }


    }
}
