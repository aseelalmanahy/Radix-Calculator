package com.ryanjohnson.calculatorwithauthentication.ui.notifications;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ryanjohnson.calculatorwithauthentication.DatabaseHelper;
import com.ryanjohnson.calculatorwithauthentication.R;
import com.ryanjohnson.calculatorwithauthentication.ui.home.CalculatorFragment;

import java.util.ArrayList;

public class DatabaseFragment extends Fragment {

    private DatabaseViewModel databaseViewModel;
    private GridView mGridView;
    private GridView mGridColumnHeaders;
    private Button deleteDatabaseButton;
    DatabaseHelper myDb;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        databaseViewModel =
                ViewModelProviders.of(this).get(DatabaseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_database, container, false);
        final TextView textView = root.findViewById(R.id.text_database);
        databaseViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        myDb = new DatabaseHelper(getActivity());
        mGridView = getView().findViewById(R.id.gridView);
        deleteDatabaseButton = getView().findViewById(R.id.deleteDatabase);

        mGridColumnHeaders = getView().findViewById(R.id.gridColumnHeaders);
        ArrayList<String> columnHeaders = new ArrayList<>();
        columnHeaders.add("Operation");
        columnHeaders.add("Result");
        columnHeaders.add("Result Base");
        columnHeaders.add("Date");
        ArrayAdapter columnAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,columnHeaders);
        mGridColumnHeaders.setAdapter(columnAdapter);

        Cursor data = myDb.getData();
        final ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            listData.add(data.getString(0));
            listData.add(data.getString(1));
            listData.add(data.getString(2));
            listData.add(data.getString(3));
        }

        final ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,listData);
        mGridView.setAdapter(adapter);

        deleteDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDb.clearDatabase();
            }
        });
    }

}