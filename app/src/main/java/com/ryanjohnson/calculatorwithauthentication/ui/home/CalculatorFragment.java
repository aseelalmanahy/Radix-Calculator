package com.ryanjohnson.calculatorwithauthentication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ryanjohnson.calculatorwithauthentication.DatabaseHelper;
import com.ryanjohnson.calculatorwithauthentication.MainActivity;
import com.ryanjohnson.calculatorwithauthentication.R;

public class CalculatorFragment extends Fragment {

    EditText number1;
    EditText number2;
    EditText base1;
    EditText base2;
    EditText resultBase;
    EditText calculateResult;
    Button calculateButton;
    String answer;

    DatabaseHelper myDb;

    private CalculatorViewModel calculatorViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calculatorViewModel =
                ViewModelProviders.of(this).get(CalculatorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calculator, container, false);
        final TextView textView = root.findViewById(R.id.text_calculator);
        calculatorViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // Database object to store values
        myDb = new DatabaseHelper(getActivity());

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        number1 = getView().findViewById(R.id.number1);
        number2 = getView().findViewById(R.id.number2);
        base1 = getView().findViewById(R.id.base1);
        base2 = getView().findViewById(R.id.base2);
        resultBase = getView().findViewById(R.id.resultBase);
        calculateButton = getView().findViewById(R.id.calculateButton);
        calculateResult = getView().findViewById(R.id.resultCalculate);
        calculateResult.setKeyListener(null);
        final Spinner spinner = getView().findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> operationsAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.operations_array, R.layout.spinner_layout);
        operationsAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(operationsAdapter);

        calculateButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (number1.getText().toString().isEmpty() || number2.getText().toString().isEmpty()
                        || base1.getText().toString().isEmpty() || base2.toString().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "You Are Missing One or More Fields!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                int base1input = Integer.parseInt(base1.getText().toString());
                int base2input = Integer.parseInt(base2.getText().toString());
                int resultBaseInput = Integer.parseInt(resultBase.getText().toString());
                String number1Input = number1.getText().toString();
                String number2Input = number2.getText().toString();
                if (base1input > 0 && base2input > 0 && resultBaseInput > 0) {
                    MainActivity.RadixConverter baseConverter = new MainActivity.RadixConverter();
                    int convertedNum1 = Integer.parseInt(baseConverter.convertRadix(number1Input,base1input,10));
                    int convertedNum2 = Integer.parseInt(baseConverter.convertRadix(number2Input, base2input, 10));
                    int calculatedNumber = 0;
                    String operation = String.valueOf(spinner.getSelectedItem());

                    if(operation.equals("+")) {
                        calculatedNumber = convertedNum1 + convertedNum2;
                    }

                    if(operation.equals("–")) {
                        calculatedNumber = convertedNum1 - convertedNum2;
                    }

                    if(operation.equals("x")) {
                        calculatedNumber = convertedNum1 * convertedNum2;
                    }

                    if(operation.equals("/")) {
                        calculatedNumber = convertedNum1 / convertedNum2;
                    }

                    answer = baseConverter.convertRadix(String.valueOf(calculatedNumber), 10, resultBaseInput);

                    if(!answer.isEmpty()) {
                        calculateResult.setText(answer);
                        myDb.addResult(answer, resultBaseInput, "Calculate");
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "Invalid Input. Please re-enter!",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Invalid Input. Please re-enter!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}