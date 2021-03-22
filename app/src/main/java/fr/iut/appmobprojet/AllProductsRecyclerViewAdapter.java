package fr.iut.appmobprojet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.iut.appmobprojet.data.model.Product;

/**
 * Adapter permettant d'afficher tous les produits non possédés (page d'accueil)
 */
public class AllProductsRecyclerViewAdapter extends RecyclerView.Adapter<AllProductsRecyclerViewAdapter.ViewHolder> {

    private final List<Product> mValues;

    public AllProductsRecyclerViewAdapter(List<Product> items) {
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
        holder.mCodePostalView.setText(mValues.get(position).getCodePostal());
        holder.mReserverButton.setOnClickListener(v -> reserver(mValues.get(position), v , position));
        holder.mDeleteButton.setVisibility(View.GONE);
        holder.mContactButton.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void reserver(Product p, View v, int position) {
        FirebaseFirestore fDb = FirebaseFirestore.getInstance();
        fDb.collection("products").document(p.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                if (doc.getString("reservePar") == null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("reservePar", v.getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", null));
                    fDb.collection("products").document(p.getId()).update(data).addOnSuccessListener(aVoid -> {
                        Snackbar.make(v.getRootView().findViewById(R.id.home), R.string.reservation_complete, Snackbar.LENGTH_LONG).setAction(R.string.undo, v1 -> annulerReservation(p, v, position)).show();
                        mValues.remove(position);
                        this.notifyItemRemoved(position);
                    });
                } else {
                    Toast.makeText(v.getContext(), "Ce produit est déjà réservé", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void annulerReservation(Product p, View v, int position) {
        Map<String, Object> data = new HashMap<>();
        data.put("reservePar", FieldValue.delete());
        FirebaseFirestore.getInstance().collection("products").document(p.getId()).update(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mValues.add(position, p);
                this.notifyItemInserted(position);
                Toast.makeText(v.getContext(), "Reservation annulée", Toast.LENGTH_SHORT).show();
            }
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

        }

        @NonNull
        @Override
        public String toString() {
            return super.toString();
        }
    }
}