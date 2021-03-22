package fr.iut.appmobprojet.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import fr.iut.appmobprojet.R;

/**
 * Fragment correspondant à la page d'ajout d'un produit
 */
public class AddFragment extends Fragment implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private static TextView textPeremption;
    private static String typeNourriture;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Timestamp datePeremptionBdd;

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

        Spinner spinner = view.findViewById(R.id.addProduct_type);
        textPeremption = view.findViewById(R.id.textPeremption);
        Button buttonPeremption = view.findViewById(R.id.buttonPeremption);
        Button buttonValiderAddProduct = view.findViewById(R.id.buttonValiderAddProduct);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.products_type_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        buttonPeremption.setOnClickListener(v -> showDatePickerDialog());

        EditText titre, marque, codePostal;

        titre = view.findViewById(R.id.addProduct_titre);
        marque = view.findViewById(R.id.addProduct_marque);
        codePostal = view.findViewById(R.id.addProduct_codePostal);

        buttonValiderAddProduct.setOnClickListener(v -> {
            Map<String, Object> data = new HashMap<>();
            data.put("codePostal", codePostal.getText().toString().trim());
            data.put("dateAjout", Timestamp.now());
            data.put("donneur", getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", null));
            data.put("marque", marque.getText().toString().trim());
            data.put("peremption", datePeremptionBdd);
            data.put("titre", titre.getText().toString().trim());
            data.put("typeNourriture", typeNourriture);

            db.collection("products")
                    .add(data)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("NewProductActivity", "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getContext(), "Produit ajouté !", Toast.LENGTH_SHORT).show();
                        titre.getText().clear();
                        marque.getText().clear();
                        codePostal.getText().clear();
                        textPeremption.setText("Date de péremption");
                    });
        });

    }

    private void showDatePickerDialog() {
        DatePickerDialog dpd = new DatePickerDialog(getContext(), this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        typeNourriture = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        String date = dayOfMonth + "/" + month + "/" + year;
        textPeremption.setText(date);
        try {
            datePeremptionBdd = new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(dayOfMonth + "/" + month + "/" + year));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}