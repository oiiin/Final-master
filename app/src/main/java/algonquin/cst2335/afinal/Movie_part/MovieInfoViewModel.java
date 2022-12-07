package algonquin.cst2335.afinal.Movie_part;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MovieInfoViewModel extends ViewModel {
    public MutableLiveData<ArrayList<MovieInfo>> movies = new MutableLiveData<>();
    public MutableLiveData<MovieInfo> selectedMovie = new MutableLiveData< >();
}
