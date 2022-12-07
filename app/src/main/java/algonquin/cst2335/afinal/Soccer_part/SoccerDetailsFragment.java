package algonquin.cst2335.afinal.Soccer_part;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.squareup.picasso.Picasso;

import java.util.List;

import algonquin.cst2335.afinal.R;
import algonquin.cst2335.afinal.databinding.SoccerDetailsLayoutBinding;

/**
 * Fragment for details fragment when user click on game from recyclerview list
 */
public class SoccerDetailsFragment extends Fragment {



    List<CompetitionDetails> competitionDetails;

    private SoccerDAO sDAO;

    CompetitionDetails selected;

    public SoccerDetailsFragment(CompetitionDetails game){

        selected = game;
    }

    /**
     * The onCreate method has everything needed to retrieve and parse the data for the
     * details fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return  binding.get Root
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
         super.onCreateView(inflater, container, savedInstanceState);

        SoccerDetailsLayoutBinding binding = SoccerDetailsLayoutBinding.inflate(inflater);



        binding.competitionNameTv.setText(selected.getCompetitionName());
        binding.dateTextView.setText(selected.getDate());
        binding.titleTextView.setText(selected.getTitle());
        Picasso.get().load(selected.getThumbnail()).resize(400,270).into(binding.soccerImageView);

        binding.videoView.setVideoURI(Uri.parse(selected.getVideoUrl()));

        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(binding.videoView);

        mediaController.setMediaPlayer(binding.videoView);
        binding.videoView.setMediaController(mediaController);
        binding.videoView.requestFocus();
        binding.videoView.start();


        SoccerDatabase db = Room.databaseBuilder(getContext(), SoccerDatabase.class, "database-name").allowMainThreadQueries().build();
        sDAO = db.soccerDAO();


        /**
         * Button for inserting game to database favourites
         */
        binding.saveButton.setOnClickListener(clk ->{

            SoccerGame game = new SoccerGame(selected.getTitle(),selected.getThumbnail(),
                    selected.getCompetitionName());

            /**
             * insert selected game into database
             */
            sDAO.insertGame(game);


            Toast toast = Toast.makeText(getContext(),getResources().getText(R.string.toast), Toast.LENGTH_LONG);
            toast.show();

        });


        return binding.getRoot();

    }


}
