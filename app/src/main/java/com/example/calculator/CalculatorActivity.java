package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;

import com.example.calculator.databinding.ActivityCalculatorBinding;


public class CalculatorActivity extends AppCompatActivity {
    private ActivityCalculatorBinding binding;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculatorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.userInputEditTextCalculatorActivity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int inType = binding.userInputEditTextCalculatorActivity.getInputType();
                binding.userInputEditTextCalculatorActivity.setInputType(InputType.TYPE_NULL); // disable soft input
                binding.userInputEditTextCalculatorActivity.onTouchEvent(motionEvent); // call native handler
                binding.userInputEditTextCalculatorActivity.setInputType(inType); // restore input type
                return true;            }
        });
    }


}