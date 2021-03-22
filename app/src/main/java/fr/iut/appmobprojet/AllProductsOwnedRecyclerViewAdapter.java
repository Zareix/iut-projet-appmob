package fr.iut.appmobprojet;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.iut.appmobprojet.data.model.Product;

/**
 * Adapter permettant d'afficher tous les produits possédés (page profile)
 */
public class AllProductsOwnedRecyclerViewAdapter extends RecyclerView.Adapter<AllProductsOwnedRecyclerViewAdapter.ViewHolder> {

    private final List<Product> mValues;

    public AllProductsOwnedRecyclerViewAdapter(List<Product> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTitleView.setText(mValues.get(position).getTitre());
        if (mValues.get(position).getMarque().equals("")) {
            holder.mMarqueTitleView.setVisibility(View.GONE);
        } else
            holder.mMarqueView.setText(mValues.get(position).getMarque());
        holder.mCategorieView.setText(mValues.get(position).getCategorie());
        holder.mAddedDateView.setText(mValues.get(position).getDateAjout());
        holder.mPermeateDateView.setText(mValues.get(position).getPeremption());
        if (mValues.get(position).getPeremptionDate().before(new Date())) {
            holder.mPermeateDateView.setTextColor(ContextCompat.getColor(holder.mPermeateDateView.getContext(), R.color.red));
            holder.mTitleView.setTextColor(ContextCompat.getColor(holder.mPermeateDateView.getContext(), R.color.red));
        }
        holder.mCodePostalView.setText(mValues.get(position).getCodePostal());
        holder.mReserverButton.setVisibility(View.GONE);

        holder.mDeleteButton.setOnClickListener(v -> supprimerProduit(mValues.get(position), v, position));

        if (!mValues.get(position).getReservePar().equals("")) {
            holder.mContactButton.setOnClickListener(v -> contacterReceveur(mValues.get(position), v));
        } else {
            holder.mContactButton.setVisibility(View.GONE);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.constraintLayout);
            constraintSet.connect(R.id.suppr_btn_item_product, ConstraintSet.TOP, R.id.linearLayout_item_product, ConstraintSet.BOTTOM, 8);
            constraintSet.applyTo(holder.constraintLayout);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void contacterReceveur(Product p, View v) {
        FirebaseFirestore.getInstance().collection("users").document(p.getReservePar()).get().addOnCompleteListener(task -> {
            DocumentSnapshot receiverInfo = task.getResult();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{receiverInfo.getString("email")});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Freelicious - Votre réservation de " + p.getTitre());
            intent.putExtra(Intent.EXTRA_TEXT, "Bonjour,\nVous avez réservé mon produit " + p.getTitre() + " sur Freelicious.\n Quand êtes-vous disponible pour le récupérer ?\n\nCordialement,\n" + p.getDonneur());
            v.getContext().startActivity(Intent.createChooser(intent, ""));
        });
    }

    private void supprimerProduit(Product p, View v, int position) {
        FirebaseFirestore.getInstance()
                .collection("products")
                .document(p.getId())
                .delete()
                .addOnSuccessListener(command -> {
                    Snackbar.make(v.getRootView().findViewById(R.id.account_fragment), "Produit supprimé !", Snackbar.LENGTH_LONG).setAction(R.string.undo, v1 -> annulerSuppression(p, v, position)).show();
                    mValues.remove(position);
                    this.notifyItemRemoved(position);
                });
    }

    private void annulerSuppression(Product p, View v, int position) {
        Map<String, Object> data = new HashMap<>();
        data.put("codePostal", p.getCodePostal());
        data.put("dateAjout", p.getDateAjoutDate());
        data.put("donneur", p.getDonneur());
        data.put("marque", p.getMarque());
        data.put("peremption", p.getPeremptionDate());
        data.put("titre", p.getTitre());
        data.put("typeNourriture", p.getTypeNourriture());
        if (!p.getReservePar().equals(""))
            data.put("reservePar", p.getReservePar());

        FirebaseFirestore.getInstance()
                .collection("products")
                .document(p.getId())
                .set(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(v.getContext(), "Suppression annulée !", Toast.LENGTH_SHORT).show();
                    mValues.add(position, p);
                    this.notifyItemInserted(position);
                });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mMarqueView;
        public final TextView mMarqueTitleView;
        public final TextView mCategorieView;
        public final TextView mAddedDateView;
        public final TextView mPermeateDateView;
        public final TextView mCodePostalView;
        public final Button mReserverButton;
        public final Button mDeleteButton;
        public final Button mContactButton;
        public final ConstraintLayout constraintLayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.titre_product_item);
            mMarqueView = view.findViewById(R.id.marque_product_item);
            mMarqueTitleView = view.findViewById(R.id.brand_title_list_item);
            mCategorieView = view.findViewById(R.id.categorie_list_item);
            mAddedDateView = view.findViewById(R.id.added_date_item_product);
            mPermeateDateView = view.findViewById(R.id.permeate_date_item_product);
            mCodePostalView = view.findViewById(R.id.code_postal_item_product);
            mReserverButton = view.findViewById(R.id.reserve_btn_item_product);
            mDeleteButton = view.findViewById(R.id.suppr_btn_item_product);
            mContactButton = view.findViewById(R.id.contact_btn_item_product);
            constraintLayout = view.findViewById(R.id.main_constraint_layout_item_product);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString();
        }
    }
}