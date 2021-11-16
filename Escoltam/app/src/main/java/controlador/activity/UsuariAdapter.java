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
import model.Usuari;

public class UsuariAdapter extends RecyclerView.Adapter<UsuariAdapter.ViewHolder> {

    private List<Usuari> mUsuaris;
    private Context mContext;

    public UsuariAdapter(List<Usuari> mUsuaris, Context mContext) {
        this.mUsuaris = mUsuaris;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UsuariAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(mUsuaris != null){
            Usuari currentUsuari = mUsuaris.get(position);
            holder.mUsuariText.setText(currentUsuari.getEmail());
            holder.mVoiceText.setText(currentUsuari.getVoice());
            holder.mRoleText.setText(currentUsuari.getRole().getName());
        }else{
            holder.mUsuariText.setText("");
            holder.mVoiceText.setText("");
            holder.mRoleText.setText("");
        }

    }

    @Override
    public int getItemCount() {
        if (mUsuaris != null)
            return mUsuaris.size();
        else return 0;
    }


    /**
     * Calsse holder que crea i gestiona l'item del recyclerview.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mUsuariText;
        private final TextView mVoiceText;
        private final TextView mRoleText;

        private ViewHolder(View itemView) {
            super(itemView);

            mUsuariText = itemView.findViewById(R.id.usernameListText);
            mVoiceText = itemView.findViewById(R.id.voiceListText);
            mRoleText = itemView.findViewById(R.id.roleListText);

        }
//
//        /**
//         * Carrega la informació  del recyclerview.
//         * @param currentusuari
//         */
//        void bindTo(Usuari currentusuari){
//
//            mUsuariText.setText(currentusuari.getEmail());
//            mVoiceText.setText(currentusuari.getVoice());
//            mRoleText.setText(currentusuari.getRole().getName());
//        }

    }
}
