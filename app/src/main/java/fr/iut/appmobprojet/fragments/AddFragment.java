package fr.iut.appmobprojet.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.iut.appmobprojet.R;

/**
 * Fragment correspondant à la page d'ajout d'un produit
 */
public class AddFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AddFragment() {
    }

    public static AddFragment newInstance() {
        return new AddFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Spinner spinner = (Spinner) view.findViewById(R.id.addProduct_type);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.products_type_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);


        Map<String, Object> data = new HashMap<>();
        data.put("codePostal", "75016");
        data.put("dateAjout", Timestamp.now());
        data.put("donneur", "william");
        data.put("marque", "Panzani");
        data.put("peremption", new Timestamp(new Date()));
        data.put("titre", "Spaghettis");
        data.put("typeNourriture", "Féculents");


        db.collection("products")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("NewProductActivity", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("NewProductActivity", "Error adding document", e);
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sSelected = parent.getItemAtPosition(position).toString();
        Toast.makeText(getContext(), sSelected, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}