package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.example.calculator.databinding.ActivityCalculatorBinding;


public class CalculatorActivity extends AppCompatActivity {
     ActivityCalculatorBinding binding;
     StringBuilder builder;
     boolean canDecimal;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculatorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        builder = new StringBuilder();
        final int ZERO = 0;
        final int ONE = 1;
        final int TWO = 2;
        final int THREE = 3;
        final int FOUR = 4;
        final int FIVE = 5;
        final int SIX = 6;
        final int SEVEN = 7;
        final int EIGHT = 8;
        final int NINE = 9;
        final String decimal_point = ".";
        final String ADD = "+";
        final String SUBTRACT = "-";
        final String MULTIPLY = "×";
        final String DIVIDE = "÷";
        final String PERCENT = "%";
        final String EQUALS = "=";

        canDecimal = true;

        binding.userInputEditTextCalculatorActivity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int inType = binding.userInputEditTextCalculatorActivity.getInputType();
                binding.userInputEditTextCalculatorActivity.setInputType(InputType.TYPE_NULL); // disable soft input
                binding.userInputEditTextCalculatorActivity.onTouchEvent(motionEvent); // call native handler
                binding.userInputEditTextCalculatorActivity.setInputType(inType); // restore input type
                return true;            }
        });


        binding.zeroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!builder.toString().isEmpty()) {
                    if (builder.length() == 1 && TextUtils.equals(String.valueOf(builder.charAt(0)), "0")) {
                        return;
                    }
                }
                builder.append(ZERO);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });

        binding.doubleZeroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!builder.toString().isEmpty()){
                   if (builder.length() == 1 && TextUtils.equals(String.valueOf(builder.charAt(0)), "0")) {
                        return; }
                   builder.append(ZERO);
               }
                builder.append(ZERO);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });

        binding.oneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    initZero(ONE);
                    builder.append(ONE);
                    binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });


        binding.twoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(TWO);
                builder.append(TWO);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });

        binding.threeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(THREE);
                builder.append(THREE);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });
        binding.fourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(FOUR);
                builder.append(FOUR);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });
        binding.fiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(FIVE);
                builder.append(FIVE);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });
        binding.sixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(SIX);
                builder.append(SIX);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });
        binding.sevenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(SEVEN);
                builder.append(SEVEN);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });
        binding.eightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(EIGHT);
                builder.append(EIGHT);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });

        binding.nineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(NINE);
                builder.append(NINE);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });

        binding.decimalPointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!builder.toString().isEmpty()) {
                    if (!canDecimal){
                        return;
                    }
                } else {
                    builder.append(ZERO);
                }
                builder.append(decimal_point);
                canDecimal = false;
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });



        /* To avoid confusion
         * The operator button logic has been implemented below
        **/

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOperator(ADD);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });

        binding.subtractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOperator(SUBTRACT);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });

        binding.multiplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOperator(MULTIPLY);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());

            }
        });

        binding.divideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOperator(DIVIDE);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());

            }
        });







        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setLength(0);
                binding.userInputEditTextCalculatorActivity.setText(builder.toString());
            }
        });

    }

    protected void initZero(int number){
        if (builder.length() == 1 && TextUtils.equals(String.valueOf(builder.charAt(0)), "0")){
            builder.setCharAt(0, (char) number);
        }
    }

    protected void checkOperator(String operator){
        if (!builder.toString().isEmpty()){
            int lastIndex = builder.length() - 1;
            if ( String.valueOf(builder.charAt(lastIndex)).equals("+")
                    || String.valueOf(builder.charAt(lastIndex)).equals("-")
                        || String.valueOf(builder.charAt(lastIndex)).equals("×")
                            || String.valueOf(builder.charAt(lastIndex)).equals("÷")){

                builder.setCharAt(lastIndex, operator.charAt(0));
            }else {
                builder.append(operator);
            }
        }
        canDecimal = true;

    }

}