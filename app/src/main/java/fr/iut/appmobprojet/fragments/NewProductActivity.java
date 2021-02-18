package fr.iut.appmobprojet.fragments;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.iut.appmobprojet.R;

public class NewProductActivity  extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add);

        Map<String, Object> data = new HashMap<>();
        data.put("codePostal", 75016);
        data.put("dateAjout", new Timestamp(new Date()));
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

}
