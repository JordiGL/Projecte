package controlador.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jordigomez.ioc.cat.escoltam.R;
import model.Usuari;

public class UsuariListAdapter extends RecyclerView.Adapter<UsuariListAdapter.ViewHolder> {

    private List<Usuari> mUsuaris;
    private Context mContext;

    public UsuariListAdapter(List<Usuari> mUsuaris, Context mContext) {
        this.mUsuaris = mUsuaris;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UsuariListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    /**
     * Calsse holder que crea i gestiona l'item del recyclerview.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mUsuariText;
        private TextView mVoiceText;
        private TextView mRoleText;

        private ViewHolder(View itemView) {
            super(itemView);

            mUsuariText = itemView.findViewById(R.id.usernameListText);
            mVoiceText = itemView.findViewById(R.id.voiceListText);
            mRoleText = itemView.findViewById(R.id.roleListText);

        }

        /**
         * Carrega la informació  del recyclerview.
         * @param currentusuari
         */
        void bindTo(Usuari currentusuari){

            mUsuariText.setText(currentusuari.getEmail());
            mVoiceText.setText(currentusuari.getVoice());
//            mRoleText.setText(currentusuari.getRole());
        }

    }
}
