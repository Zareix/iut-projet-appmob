package fr.iut.appmobprojet;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.iut.appmobprojet.fragments.AccountFragment;
import fr.iut.appmobprojet.fragments.AddFragment;
import fr.iut.appmobprojet.fragments.HomeFragment;

/**
 * Activité principal, change de fragment selon la page choisi (en cliquant sur la navbar)
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment homeF = new HomeFragment();
        Fragment addF = new AddFragment();
        Fragment accF = new AccountFragment();

        makeCurrentFragment(homeF);
        BottomNavigationView navbar = findViewById(R.id.bottom_navbar);
        navbar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
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
     * @param f : fragment à afficher
     */
    private void makeCurrentFragment(Fragment f) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.for_fragment_main, f, "currentFragment").commit();
    }
}