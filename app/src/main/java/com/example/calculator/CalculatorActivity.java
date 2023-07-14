package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.calculator.databinding.ActivityCalculatorBinding;

import java.util.Objects;


public class CalculatorActivity extends AppCompatActivity {
     ActivityCalculatorBinding binding;
     StringBuilder builder;
     boolean canDecimal;
     boolean canAppendZeroZero;
     int cursorPosition;


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
        canAppendZeroZero = true;

        binding.userInputEditText.setShowSoftInputOnFocus(false);

        binding.userInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(binding.userInputEditText.getText())
                        && binding.userInputEditText.length() == 1){
                            binding.userInputEditText.requestFocus();
                }
            }
        });




        binding.zeroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(binding.userInputEditText.getText())) {
                    if (binding.userInputEditText.length() == 1
                            && binding.userInputEditText.getText().toString().charAt(0) == '0') {
                                return;
                    }
                    if (checkOperator()){
                        builder.append(ZERO);
                        binding.userInputEditText.append(String.valueOf(ZERO));
                        canAppendZeroZero = false;

                        return;

                    }

                    if (!canAppendZeroZero){
                        return;
                    }
                }
                builder.append(ZERO);
                binding.userInputEditText.append(String.valueOf(ZERO));
                Log.d("cursorAt", String.valueOf(getCursorPosition()));
                Log.d("length", String.valueOf(binding.userInputEditText.length()));


            }
        });

//        binding.doubleZeroBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!builder.toString().isEmpty()){
//                    if (builder.length() == 1 && TextUtils.equals(String.valueOf(builder.charAt(0)), "0")) {
//                        return; } // end code if true
//
//                    if (checkOperator()){
//                        builder.append(ZERO);
//                        canAppendZeroZero = false;
//                        binding.userInputEditText.setText(builder.toString());
//
//                        return;
//
//                    }
//
//                    if (!canAppendZeroZero){
//                        return;
//                    }
//
//                    builder.append(ZERO);
//                }
//                // continue code
//                builder.append(ZERO);
//                binding.userInputEditText.setText(builder.toString());
//            }
//        });




        binding.oneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(ONE);
                builder.append(ONE);
                binding.userInputEditText.append("1");
                canAppendZeroZero = true;

                Log.d("cursorAt", String.valueOf(getCursorPosition()));
                Log.d("length", String.valueOf(binding.userInputEditText.length()));


            }
        });


//        binding.twoBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                initZero(TWO);
//                builder.append(TWO);
//                canAppendZeroZero = true;
//                binding.userInputEditText.setText(builder.toString());
//            }
//        });
//
//        binding.threeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                initZero(THREE);
//                builder.append(THREE);
//                canAppendZeroZero = true;
//                binding.userInputEditText.setText(builder.toString());
//            }
//        });
//        binding.fourBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                initZero(FOUR);
//                builder.append(FOUR);
//                canAppendZeroZero = true;
//                binding.userInputEditText.setText(builder.toString());
//            }
//        });
//        binding.fiveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                initZero(FIVE);
//                builder.append(FIVE);
//                canAppendZeroZero = true;
//                binding.userInputEditText.setText(builder.toString());
//            }
//        });
//        binding.sixBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                initZero(SIX);
//                builder.append(SIX);
//                canAppendZeroZero = true;
//                binding.userInputEditText.setText(builder.toString());
//            }
//        });
//        binding.sevenBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                initZero(SEVEN);
//                builder.append(SEVEN);
//                canAppendZeroZero = true;
//                binding.userInputEditText.setText(builder.toString());
//            }
//        });
//        binding.eightBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                initZero(EIGHT);
//                builder.append(EIGHT);
//                canAppendZeroZero = true;
//                binding.userInputEditText.setText(builder.toString());
//            }
//        });
//
//        binding.nineBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                initZero(NINE);
//                builder.append(NINE);
//                canAppendZeroZero = true;
//                binding.userInputEditText.setText(builder.toString());
//            }
//        });
//
//        binding.decimalPointBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                canAppendZeroZero = true;
//                if (!builder.toString().isEmpty()) {
//                    if (!canDecimal){
//                        return;
//                    }else if(checkOperator()){
//                        builder.append(ZERO);
//                    }
//                }
//                else {
//                    builder.append(ZERO);
//                }
//                builder.append(decimal_point);
//                canDecimal = false;
//                binding.userInputEditText.setText(builder.toString());
//
//            }
//        });
//
//
//
//        /* To avoid confusion
//         * The operator button logic has been implemented below
//        **/
//
//        binding.addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (checkForEmptyOrSubtract()){
//                    return;
//                }
//                replaceOperator(ADD);
//                binding.userInputEditText.setText(builder.toString());
//            }
//        });
//
//        binding.subtractBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                replaceOperator(SUBTRACT);
//                binding.userInputEditText.setText(builder.toString());
//            }
//        });
//
//        binding.multiplyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (checkForEmptyOrSubtract()){
//                    return;
//                }
//                replaceOperator(MULTIPLY);
//                binding.userInputEditText.setText(builder.toString());
//
//            }
//        });
//
//        binding.divideBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (checkForEmptyOrSubtract()){
//                    return;
//                }
//                replaceOperator(DIVIDE);
//                binding.userInputEditText.setText(builder.toString());
//
//            }
//        });
//
//
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setLength(0);
                canDecimal = true;
                binding.userInputEditText.setText(builder.toString());
            }
        });
//
//        binding.clearBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                builder.deleteCharAt(getCursorPosition());
//                binding.userInputEditText.setText(builder.toString());
//            }
//        });

    }
    protected void initZero(int number){
        if (!TextUtils.isEmpty(binding.userInputEditText.getText())
                && binding.userInputEditText.length() == 1
                && binding.userInputEditText.getText().toString().charAt(0) == '0'){

                    binding.userInputEditText.getText().clear();
//                    builder.setCharAt(0, (char) number);
        }
    }

    protected void replaceOperator(String operator){
        int lastIndex = builder.length() - 1;
        if (checkOperator()){
            builder.setCharAt(lastIndex, operator.charAt(0)); //// replacing
        } else {
            builder.append(operator);
        }
        canDecimal = true;
    }

    protected boolean checkOperator() {
        int lastIndex = builder.length() - 1;
        if (!builder.toString().isEmpty()) {
            return String.valueOf(builder.charAt(lastIndex)).equals("+")
                    || String.valueOf(builder.charAt(lastIndex)).equals("-")
                    || String.valueOf(builder.charAt(lastIndex)).equals("×")
                    || String.valueOf(builder.charAt(lastIndex)).equals("÷");
        }
        return false;
    }

    protected boolean checkForEmptyOrSubtract(){
        return builder.toString().isEmpty()
                || (builder.length() == 1 && builder.charAt(0) == '-');
    }


    protected int getCursorPosition(){
        return binding.userInputEditText.getSelectionStart();

    }

}