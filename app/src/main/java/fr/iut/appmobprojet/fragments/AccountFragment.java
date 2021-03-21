package fr.iut.appmobprojet.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import fr.iut.appmobprojet.AllProductsFragment;
import fr.iut.appmobprojet.LoginActivity;
import fr.iut.appmobprojet.R;


/**
 * Fragment correspond à la page profil
 */
public class AccountFragment extends Fragment {
    private String username;
    private String email;
    private String statut;

    public AccountFragment() {
    }

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseFirestore fDb = FirebaseFirestore.getInstance();

        DocumentReference docRef = fDb.collection("users").document(getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", null));
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                username = (String) document.get("username");
                email = (String) document.get("email");
                statut = (String) document.get("statut");
                Fragment currentFragment = getFragmentManager().findFragmentByTag("currentFragment");
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.detach(currentFragment);
                fragmentTransaction.attach(currentFragment);
                fragmentTransaction.commit();
            }
        });

        Fragment allProductsFragment = AllProductsFragment.newInstance(true);
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().replace(R.id.all_owned_fram_layout, allProductsFragment, "allproducts").commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView usernameV = view.findViewById(R.id.username_profil);
        TextView emailV = view.findViewById(R.id.email_profil);
        TextView statutV = view.findViewById(R.id.statut_profil);

        usernameV.setText(username);
        emailV.setText(email);
        statutV.setText(statut);

        view.findViewById(R.id.settings_btn_profile).setOnClickListener(v ->
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.allproducts_home, new SettingsFragment(), "currentFragment").commit()
        );

        view.findViewById(R.id.disconnect_btn).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getContext(), "Déconnecté", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });
    }
}