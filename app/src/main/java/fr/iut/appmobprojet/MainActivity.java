package fr.iut.appmobprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import fr.iut.appmobprojet.fragments.AccountFragment;
import fr.iut.appmobprojet.fragments.AddFragment;
import fr.iut.appmobprojet.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment homeF = new HomeFragment();
        Fragment addF = new AddFragment();
        Fragment accF = new AccountFragment();

        // Lors d'un clic sur la navbar :
        // On change le fragment par celui correspondant
        // --> On redirige l'utilisateur sur la bonne page
        makeCurrentFragment(homeF);
        BottomNavigationView navbar = findViewById(R.id.bottom_navbar);
        navbar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.ic_home:
                    makeCurrentFragment(homeF);
                    break;
                case R.id.ic_add:
                    makeCurrentFragment(addF);
                    break;
                case R.id.ic_account:
                    makeCurrentFragment(accF);
                    break;
            }
            return true;
        });
    }

    /**
     * Redirige vers le fragment choisi
     *
     * @param f
     */
    private void makeCurrentFragment(Fragment f){
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_wrapper, f).commit();
    }

    public void disconnect(View v){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "Déconnecté", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}