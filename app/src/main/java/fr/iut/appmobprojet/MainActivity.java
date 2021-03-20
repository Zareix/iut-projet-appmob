package fr.iut.appmobprojet;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import fr.iut.appmobprojet.data.model.Product;
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

    public void reserver(Product p) {
        FirebaseFirestore fDb = FirebaseFirestore.getInstance();
        DocumentReference docRef = fDb.collection("products").document(p.getId());
        if (docRef.get().getResult().getDocumentReference("reservePar") == null) {

        } else {
            Toast.makeText(getApplicationContext(), "Ce produit est déjà réservé", Toast.LENGTH_SHORT).show();
        }
    }
}