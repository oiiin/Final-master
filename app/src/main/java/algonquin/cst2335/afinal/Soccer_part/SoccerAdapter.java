package algonquin.cst2335.afinal.Soccer_part;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import algonquin.cst2335.afinal.R;


public  class SoccerAdapter extends RecyclerView.Adapter <SoccerAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<CompetitionDetails> competitionDetails;
    SoccerViewModel soccerModel;

    /**
     *
     * @param ctx
     * @param competitionDetails
     * @param soccerModel
     */
    public SoccerAdapter (Context ctx, List<CompetitionDetails> competitionDetails, SoccerViewModel soccerModel){
        this.inflater = LayoutInflater.from(ctx);
        this.competitionDetails = competitionDetails;
        this.soccerModel = soccerModel;
    }


    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public SoccerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.soccer_list_layout, parent, false);
        return new ViewHolder(view);
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull SoccerAdapter.ViewHolder holder, int position) {
        // bind the data

            holder.gameTitle.setText(competitionDetails.get(position).getTitle());
            Picasso.get().load(competitionDetails.get(position).getThumbnail()).into(holder.thumbnail);

    }

    /**
     *
     * @return competitiondetails size
     */
    @Override
    public int getItemCount() {
        return competitionDetails.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView gameTitle;
        ImageView thumbnail;


        /**
         * used for layout of recycler view
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            gameTitle = itemView.findViewById(R.id.gameTitle);
            thumbnail = itemView.findViewById(R.id.thumbnail);


            /**
             * on click listener to go to detail fragment when user clicks on
             * recycler item
             */
            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();

                CompetitionDetails selected = competitionDetails.get(position);

                soccerModel.selectedGame.postValue(selected);
            });

        }
    }

}


