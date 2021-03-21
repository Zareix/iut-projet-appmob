package fr.iut.appmobprojet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import fr.iut.appmobprojet.data.model.Product;

/**
 * Fragment pour afficher l'ensemble des produits
 */
public class AllProductsFragment extends Fragment {

    private static final String ARG_ISDONNEUR = "isDonneur";
    private boolean isDonneur;

    private final List<Product> allProducts = new ArrayList<>();

    public AllProductsFragment() {
    }

    public static AllProductsFragment newInstance(boolean isDonneur) {
        AllProductsFragment fragment = new AllProductsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_ISDONNEUR, isDonneur);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        isDonneur = args.getBoolean(ARG_ISDONNEUR);
        searchAllProducts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (isDonneur)
                recyclerView.setAdapter(new AllProductsOwnedRecyclerViewAdapter(allProducts));
            else
                recyclerView.setAdapter(new AllProductsRecyclerViewAdapter(allProducts));
        }
        return view;
    }

    private void searchAllProducts() {
        FirebaseFirestore fDb = FirebaseFirestore.getInstance();

        if (isDonneur) {
            fDb.collection("products")
                    .whereEqualTo("donneur", getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", null))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            allProducts.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("reservePar") != null)
                                    allProducts.add(new Product(document.getId(), document.getString("codePostal"), document.getDate("dateAjout"), document.getString("donneur"), document.getString("marque"), document.getDate("peremption"), document.getString("titre"), document.getString("typeNourriture"), document.getString("typeNourriture"), document.getString("reservePar")));
                                else
                                    allProducts.add(new Product(document.getId(), document.getString("codePostal"), document.getDate("dateAjout"), document.getString("donneur"), document.getString("marque"), document.getDate("peremption"), document.getString("titre"), document.getString("typeNourriture"), document.getString("typeNourriture")));
                            }
                            Fragment currentFragment = getFragmentManager().findFragmentByTag("allproducts");
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.detach(currentFragment);
                            fragmentTransaction.attach(currentFragment);
                            fragmentTransaction.commit();
                        }
                    });
        } else {
            fDb.collection("products")
                    .whereNotEqualTo("donneur", getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", null))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            allProducts.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("reservePar") == null)
                                    allProducts.add(new Product(document.getId(), document.getString("codePostal"), document.getDate("dateAjout"), document.getString("donneur"), document.getString("marque"), document.getDate("peremption"), document.getString("titre"), document.getString("typeNourriture"), document.getString("typeNourriture")));
                            }
                            Fragment currentFragment = getFragmentManager().findFragmentByTag("allproducts");
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.detach(currentFragment);
                            fragmentTransaction.attach(currentFragment);
                            fragmentTransaction.commit();
                        }
                    });
        }
    }
}