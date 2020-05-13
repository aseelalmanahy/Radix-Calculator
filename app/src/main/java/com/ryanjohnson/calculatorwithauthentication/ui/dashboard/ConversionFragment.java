package com.ryanjohnson.calculatorwithauthentication.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ConversionFragment extends Fragment {

    EditText fromBaseBox;
    EditText toBaseBox;
    EditText numberInput;
    EditText convertResult;
    TextView solutionSteps;
    Button convertButton;
    String answer;

    // Database object to store values
    DatabaseHelper myDb;

    private ConversionViewModel conversionViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        conversionViewModel =
                ViewModelProviders.of(this).get(ConversionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_conversion, container, false);
        final TextView textView = root.findViewById(R.id.text_conversion);
        conversionViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        myDb = new DatabaseHelper(getActivity());
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        fromBaseBox = getView().findViewById(R.id.fromBase);
        toBaseBox = getView().findViewById(R.id.toBase);
        convertButton = getView().findViewById(R.id.button_convert);
        numberInput = getView().findViewById(R.id.numberToConvert);
        convertResult = getView().findViewById(R.id.resultConvert);
        convertResult.setKeyListener(null);
        solutionSteps = getView().findViewById(R.id.solutionStepsConvert);

        convertButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (fromBaseBox.getText().toString().isEmpty() || toBaseBox.getText().toString().isEmpty()
                        || numberInput.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "You Are Missing One or More Fields!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                int fromBase = Integer.parseInt(fromBaseBox.getText().toString());
                int toBase = Integer.parseInt(toBaseBox.getText().toString());
                String numberToConvert = numberInput.getText().toString();
                if (fromBase > 0 && toBase > 0) {
                    MainActivity.RadixConverter baseConverter = new MainActivity.RadixConverter();
                    answer = baseConverter.convertRadix(numberToConvert,fromBase,toBase);
                    if(!answer.isEmpty()) {

                        convertResult.setText(answer);
                        myDb.addResult(answer, toBase, "Convert");
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