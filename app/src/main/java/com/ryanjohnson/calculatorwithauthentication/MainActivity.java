package com.ryanjohnson.calculatorwithauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 0430;
    List<AuthUI.IdpConfig> providers;
    BottomNavigationItemView btn_sign_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        btn_sign_out = findViewById(R.id.signOut);
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                AuthUI.getInstance().signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btn_sign_out.setEnabled(false);
                                showSignInOptions();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
                // Aseel here is where I added the phone
        );

            showSignInOptions();
        }

        private void showSignInOptions() {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(providers).setTheme(R.style.MyTheme).setLogo(R.drawable.logo_final_big).build(),MY_REQUEST_CODE);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == MY_REQUEST_CODE) {

                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(resultCode == RESULT_OK) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                    btn_sign_out.setEnabled(true);
                }
                else {
                    Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

    public static class RadixConverter {

        public static String convertRadix(String num, int base1, int base2) {
            int no = convertOtherToDecimal(num, base1);
            return convertDecimalToOther(no, base2);
        }

        static String convertDecimalToOther(int num, int base) {

            if(base == 10) {
                return Integer.toString(num);
            }
            String res = "";
            int remainder;
            while (num > 0) {
                remainder = num % base;
                if (base == 16) {
                    if (remainder == 10)
                        res += 'A';
                    else if (remainder == 11)
                        res += 'B';
                    else if (remainder == 12)
                        res += 'C';
                    else if (remainder == 13)
                        res += 'D';
                    else if (remainder == 14)
                        res += 'E';
                    else if (remainder == 15)
                        res += 'F';
                    else
                        res += remainder;
                } else
                    res += remainder;

                num /= base;
            }
            return new StringBuffer(res).reverse().toString();
        }

        static int convertOtherToDecimal(String num, int base) {

            int negConvert = 1;
            if(num.charAt(0) == '-') {
                negConvert = -1;
            }

            if (base < 2 || (base > 10 && base != 16))
                return -1;

            int value = 0;
            int power = 1;

            for (int i = num.length() - 1; i >= 0; i--) {

                if(i == 0 && negConvert==-1) {
                    continue;
                }

                int digit = digitToVal(num.charAt(i));

                if (digit < 0 || digit >= base)
                    return -1;

                value += digit * power;
                power = power * base;
            }

            return value * negConvert;
        }

        static int digitToVal(char c) {
            if (c >= '0' && c <= '9')
                return (int) c - '0';
            else
                return (int) c - 'A' + 10;
        }
    }
}
