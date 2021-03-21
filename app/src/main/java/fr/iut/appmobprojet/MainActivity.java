package fr.iut.appmobprojet;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fr.iut.appmobprojet.fragments.AccountFragment;
import fr.iut.appmobprojet.fragments.AddFragment;
import fr.iut.appmobprojet.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "a channel id";

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
     * @param f
     */
    private void makeCurrentFragment(Fragment f) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_wrapper, f, "currentFragment").commit();
    }
}