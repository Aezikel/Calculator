package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import com.example.calculator.databinding.ActivityCalculatorBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Arrays;
import java.util.Objects;


public class CalculatorActivity extends AppCompatActivity {
     ActivityCalculatorBinding binding;
     BaseInputConnection textFieldInputConnection;
     StringBuilder builder;
     boolean canDecimal;
     boolean canAppendZeroZero;
     int cursorIndex;

     int findForwardAdd, findForwardSubtract, findForwardMultiply, findForwardDivide;
     int findBackwardAdd, findBackwardSubtract, findBackwardMultiply, findBackwardDivide;
     int findForward , findBackward;





    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculatorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        builder = new StringBuilder();
        textFieldInputConnection = new BaseInputConnection(binding.userInputEditText, true); // fake delete event
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
        cursorIndex = getCursorPosition();


        binding.toolbarCalculatorActivity.getMenu().findItem(R.id.choose_theme_menu_item).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(CalculatorActivity.this)
                        .setTitle(getString(R.string.choosePtheme))
                        .setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setSingleChoiceItems(getResources().getStringArray(R.array.theme_dialog_array), ONE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                    materialAlertDialogBuilder.create().show();

                return false;
            }
        });



        log();
        Log.d("CursorIndex",String.valueOf(cursorIndex) );

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
                if (!TextUtils.isEmpty(binding.userInputEditText.getText()) && binding.userInputEditText.length() == ONE){// request focus once
                    binding.userInputEditText.requestFocus();
                    Log.d("FocusRequested", "true");
                }
                log();

            }
        });

        binding.userInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursorIndex = getCursorPosition() - 1;
                log();
                Log.d("LastIndex",String.valueOf(binding.userInputEditText.length() - 1));

                //  check forward for an operator
                //  return index of that operator

                findForwardAdd = Objects.requireNonNull(binding.userInputEditText.getText()).toString().indexOf(ADD  , getCursorPosition());
                findForwardSubtract = binding.userInputEditText.getText().toString().indexOf(SUBTRACT  , getCursorPosition());
                findForwardMultiply = binding.userInputEditText.getText().toString().indexOf(MULTIPLY  , getCursorPosition());
                findForwardDivide = binding.userInputEditText.getText().toString().indexOf(DIVIDE  , getCursorPosition());

                //  check backwards for an operator
                //  return index of that operator

                findBackwardAdd = binding.userInputEditText.getText().toString().lastIndexOf(ADD, cursorIndex);
                findBackwardSubtract = binding.userInputEditText.getText().toString().lastIndexOf(SUBTRACT, cursorIndex);
                findBackwardMultiply = binding.userInputEditText.getText().toString().lastIndexOf(MULTIPLY, cursorIndex);
                findBackwardDivide = binding.userInputEditText.getText().toString().lastIndexOf(DIVIDE, cursorIndex);

                // store forward and backward indexes in their respective arrays
                int [] findBackwardsArray = {findBackwardAdd, findBackwardSubtract, findBackwardMultiply, findBackwardDivide};
                int [] findForwardsArray = {findForwardAdd, findForwardSubtract, findForwardMultiply, findForwardDivide};

                findBackward = Arrays.stream(findBackwardsArray).summaryStatistics().getMax();// get Max backward index
                findForward = Arrays.stream(findForwardsArray).filter(i -> i >= 0).min().orElse(-1); // get MIN Positive forward index

                if (findForward == -1){
                    findForward = binding.userInputEditText.length();
                }
                if (findBackward == -1){
                    findBackward = 0;
                }

                String extractedValue = binding.userInputEditText.getText().toString().substring(findBackward, findForward);

                Log.d("foundForward" , String.valueOf(findForward));
                Log.d("foundBackward" , String.valueOf(findBackward));
                Log.d("Extracted Value", extractedValue);
//              Log.d("CursorQuery", String.valueOf(binding.userInputEditText.getText().charAt(cursorIndex)));
                canDecimal = !extractedValue.contains(".");

            }
        });


        binding.zeroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(binding.userInputEditText.getText())) {
                    if (binding.userInputEditText.length() == ONE
                            && binding.userInputEditText.getText().toString().charAt(ZERO) == '0') {
                                return;
                    }
                    if (checkOperator()){
                        builder.append(ZERO);// ignore
                        insertOrAppend(String.valueOf(ZERO));
                        canAppendZeroZero = false;
                        return;

                    }

                    if (!canAppendZeroZero){
                        return;
                    }
                }
                builder.append(ZERO);// ignore
                insertOrAppend(String.valueOf(ZERO));
            }
        });
        binding.doubleZeroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(binding.userInputEditText.getText())) {
                    if (binding.userInputEditText.length() == ONE
                            && binding.userInputEditText.getText().toString().charAt(ZERO) == '0') {
                            return; // end code if true
                    }

                    if (checkOperator()){
                        builder.append(ZERO); // ignore
                        insertOrAppend(String.valueOf(ZERO));
                        canAppendZeroZero = false;
                        return;
                    }

                    if (!canAppendZeroZero){
                        return;
                    }

                    builder.append(ZERO);// ignore
                    insertOrAppend(String.valueOf(ZERO));

                }
                // continue code
                builder.append(ZERO); // ignore
                insertOrAppend(String.valueOf(ZERO));

            }
        });

        binding.oneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(ONE);// replace initial zero with one
                builder.append(ONE);// ignore
                insertOrAppend(String.valueOf(ONE));
                canAppendZeroZero = true;
            }
        });

        binding.twoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(TWO);
                builder.append(TWO);
                insertOrAppend(String.valueOf(TWO));
                canAppendZeroZero = true;
            }
        });

        binding.threeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(THREE);
                builder.append(THREE);
                insertOrAppend(String.valueOf(THREE));
                canAppendZeroZero = true;
            }
        });

        binding.fourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(FOUR);
                builder.append(FOUR);
                insertOrAppend(String.valueOf(FOUR));
                canAppendZeroZero = true;
            }
        });

        binding.fiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(FIVE);
                builder.append(FIVE);
                insertOrAppend(String.valueOf(FIVE));
                canAppendZeroZero = true;
            }
        });

        binding.sixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(SIX);
                builder.append(SIX);
                insertOrAppend(String.valueOf(SIX));
                canAppendZeroZero = true;
            }
        });

        binding.sevenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(SEVEN);
                builder.append(SEVEN);
                insertOrAppend(String.valueOf(SEVEN));
                canAppendZeroZero = true;
            }
        });

        binding.eightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(EIGHT);
                builder.append(EIGHT);
                insertOrAppend(String.valueOf(EIGHT));
                canAppendZeroZero = true;
            }
        });

        binding.nineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero(NINE);
                builder.append(NINE);
                insertOrAppend(String.valueOf(NINE));
                canAppendZeroZero = true;
            }
        });

        binding.decimalPointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canAppendZeroZero = true;
                if (!TextUtils.isEmpty(binding.userInputEditText.getText())) {
                    if (!canDecimal){
                        return;
                    }else if(checkOperator()){
                        builder.append(ZERO);// ignore
                        insertOrAppend(String.valueOf(ZERO));
                    }
                }
                else {
                    builder.append(ZERO); // ignore
                    insertOrAppend(String.valueOf(ZERO));
                }
                builder.append(decimal_point);//ignore
                insertOrAppend(decimal_point);
                canDecimal = false;
            }
        });


        /* To avoid confusion
         * The operator button logic has been implemented below
        **/

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForEmptyOrSubtract()){
                    return;
                }
                replaceOperator(ADD);
//              binding.userInputEditText.setText(builder.toString());
            }
        });

        binding.subtractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceOperator(SUBTRACT);
            }
        });

        binding.multiplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForEmptyOrSubtract()){
                    return;
                }
                replaceOperator(MULTIPLY);
            }
        });

        binding.divideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForEmptyOrSubtract()){
                    return;
                }
                replaceOperator(DIVIDE);
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.userInputEditText.getText())){
                    return;
                }
                builder.setLength(0);
                binding.userInputEditText.getText().clear();
                canDecimal = true;
            }
        });

        binding.clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.userInputEditText.isFocused()){
                    if (TextUtils.equals(String.valueOf(Objects.requireNonNull(binding.userInputEditText.getText()).charAt(getCursorPosition() - 1)) , decimal_point)){
                        canDecimal = true;
                    }
//                  builder.deleteCharAt(builder.length() - 1);
                    textFieldInputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                }
            }
        });
    }

    // checks
    protected void initZero(int number){
        if (!TextUtils.isEmpty(binding.userInputEditText.getText())
                && binding.userInputEditText.length() == 1
                && binding.userInputEditText.getText().toString().charAt(0) == '0'){
                    binding.userInputEditText.getText().clear();
                    builder.setCharAt(0, (char) number);
        }
    }

    protected void replaceOperator(String operator){
        Log.d("Operator found", String.valueOf(checkOperator()));
        if (isCursorPositionEqualsLength() && checkOperator()){
            //textFieldInputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            Objects.requireNonNull(binding.userInputEditText.getText())
                    .delete(binding.userInputEditText.length() - 1, binding.userInputEditText.length());
        }
        insertOrAppend(operator);
        canDecimal = true;
    }

    protected boolean checkOperator() {
        int lastIndex = binding.userInputEditText.length() - 1;
        if (!TextUtils.isEmpty(binding.userInputEditText.getText())) {
            return binding.userInputEditText.getText().charAt(lastIndex) == '+'
                    || binding.userInputEditText.getText().charAt(lastIndex) == '-'
                    || binding.userInputEditText.getText().charAt(lastIndex) == '÷'
                    || binding.userInputEditText.getText().charAt(lastIndex) == '×';
        }
        return false;
    }

    protected boolean checkForEmptyOrSubtract(){
        return TextUtils.isEmpty(binding.userInputEditText.getText())
                || (binding.userInputEditText.length() == 1 && binding.userInputEditText.getText().toString().charAt(0) == '-');
    }

    protected int getCursorPosition(){
        return binding.userInputEditText.getSelectionStart();

    }

    protected boolean isCursorPositionEqualsLength(){
        return getCursorPosition() == binding.userInputEditText.length();
    }
    protected void log(){
        Log.d("cursorAt", String.valueOf(getCursorPosition()));
        Log.d("length", String.valueOf(binding.userInputEditText.length()));
    }

    protected void insertOrAppend(String input){

        if (!TextUtils.isEmpty(binding.userInputEditText.getText())
                && !isCursorPositionEqualsLength()){
            binding.userInputEditText.getText().insert(getCursorPosition(), input);
        }
        else {
            binding.userInputEditText.append(input);
        }
    }
}