package controlador.fragment;

import android.content.Context;
import android.graphics.drawable.Icon;
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

import controlador.gestor.OnIconInteractionListener;
import jordigomez.ioc.cat.escoltam.R;
import model.Icona;

public class PanellRecyclerAdapter extends RecyclerView.Adapter<PanellRecyclerAdapter.ViewHolder> {

    private List<Icona> mIcones;
    private Context mContext;
    private OnIconInteractionListener mListener;

    public PanellRecyclerAdapter(List<Icona> icones, Context context, OnIconInteractionListener listener) {
        this.mIcones = icones;
        this.mContext = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public PanellRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PanellRecyclerAdapter.ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.recyclerview_icon_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PanellRecyclerAdapter.ViewHolder holder, int position) {
        Icona currentIcona = mIcones.get(position);
        holder.bindTo(currentIcona);

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
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView mIconName;
        private final TextView mIconId;
        private final ImageView mIconImage;

        private ViewHolder(View itemView) {
            super(itemView);

            mIconName = itemView.findViewById(R.id.textIconNameList);
            mIconImage = itemView.findViewById(R.id.iconImage);
            mIconId = itemView.findViewById(R.id.textIconIdList);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        /**
         * Carrega la informació i la imatge del recyclerview.
         * @param currentIcona
         */
        void bindTo(Icona currentIcona){

            if(mIcones != null){

                mIconName.setText(currentIcona.getNom());
                mIconId.setText(String.valueOf(currentIcona.getId()));

                if(currentIcona.getImatge() != null){

                    if(currentIcona.getImatge().length > 3){
                        Glide.with(mContext).load(currentIcona.getImatge())
                                .asBitmap()
                                .into(mIconImage);
                    }
                }
            }else{
               mIconName.setText("");
            }
        }

        @Override
        public void onClick(View v) {
            mListener.onIconClicked(mIconName.getText().toString());
        }


        @Override
        public boolean onLongClick(View v) {

            mListener.onIconLongClicked(v, mIconId.getText().toString());
            return true;
        }
    }
}
