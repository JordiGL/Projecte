package controlador.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
            holder.mIconPosition.setText(String.valueOf(currentIcona.getPosicio()));
            holder.mIconName.setText(currentIcona.getNom());
        }else{
            holder.mIconPosition.setText("");
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

        private final TextView mIconPosition;
        private final TextView mIconName;


        private ViewHolder(View itemView) {
            super(itemView);

            mIconPosition = itemView.findViewById(R.id.textIconPositionList);
            mIconName = itemView.findViewById(R.id.textIconNameList);
        }

    }
}
