package controlador.fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import jordigomez.ioc.cat.escoltam.R;
import model.Icona;

public class PanellRecyclerAdapter extends RecyclerView.Adapter<PanellRecyclerAdapter.ViewHolder> {

    private List<Icona> mIcones;
    private Context mContext;

    public PanellRecyclerAdapter(List<Icona> icones, Context context) {
        this.mIcones = icones;
        this.mContext = context;
    }

    @NonNull
    @Override
    public PanellRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PanellRecyclerAdapter.ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.recyclerview_icon_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PanellRecyclerAdapter.ViewHolder holder, int position) {
        if(mIcones != null){
            Icona currentIcona = mIcones.get(position);
            holder.mIconName.setText(currentIcona.getNom());

            if(currentIcona.getImatge() != null){
                Log.i("Info", "Entra");
                if(currentIcona.getImatge().length > 3){
                    Glide.with(mContext).load(currentIcona.getImatge())
                            .asBitmap()
                            .into(holder.mIconImage);
                }
            }
        }else{
            holder.mIconName.setText("");
        }

    }

    @Override
    public int getItemCount() {
        if (mIcones != null)
            return mIcones.size();
        else return 0;
    }


    /**
     * Calsse holder que crea i gestiona l'item del recyclerview.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mIconName;
        private final ImageView mIconImage;

        private ViewHolder(View itemView) {
            super(itemView);

            mIconName = itemView.findViewById(R.id.textIconNameList);
            mIconImage = itemView.findViewById(R.id.iconImage);
        }

    }
}
