package br.java.app_ecommerce_firebase.sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import br.java.app_ecommerce_firebase.R;
import br.java.app_ecommerce_firebase.activities.MainActivity;


public class VendedorHomeActivity extends AppCompatActivity {

    private TextView mTextMessage;


    public boolean OnNavigationItemReselected (@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.navigation_home:
                mTextMessage.setText(R.string.title_home);
                return true;
            case R.id.navigation_add:
                mTextMessage.setText(R.string.title_dashboard);
                return true;
            case R.id.navigation_logout:

                final FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent = new Intent(VendedorHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

                return true;
        }

        return false;

    };

}