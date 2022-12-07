package algonquin.cst2335.afinal.Movie_part;import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import algonquin.cst2335.afinal.databinding.MovieDetailsLayoutBinding;

public class MovieDetailsFragment extends Fragment {

    MovieInfo selected;
    public MovieDetailsFragment(MovieInfo m){

        selected=m;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        MovieDetailsLayoutBinding binding=MovieDetailsLayoutBinding.inflate(inflater);
        binding.titleD.setText(selected.title);
        binding.actorsD.setText(selected.actors);
        binding.directorD.setText(selected.director);
        binding.ratedD.setText(selected.rated);
        binding.plotD.setText(selected.plot);
        Picasso.get().load(selected.image).into(binding.imgD);
        return binding.getRoot();
    }
}