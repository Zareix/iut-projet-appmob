package fr.iut.appmobprojet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fr.iut.appmobprojet.data.model.Product;

/**
 * A fragment representing a list of Items.
 */
public class AllProductsFragment extends Fragment {


    private List<Product> allProducts = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AllProductsFragment() {
    }

    public static AllProductsFragment newInstance(int columnCount) {
        AllProductsFragment fragment = new AllProductsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            recyclerView.setAdapter(new AllProductsRecyclerViewAdapter(allProducts));
        }
        return view;
    }

    private void searchAllProducts() {
        FirebaseFirestore fDb = FirebaseFirestore.getInstance();

        fDb.collection("products")
                .whereNotEqualTo("donneur", getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", null))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                    }
                });
    }
}