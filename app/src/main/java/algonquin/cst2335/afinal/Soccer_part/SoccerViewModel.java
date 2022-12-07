package algonquin.cst2335.afinal.Soccer_part;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.afinal.Soccer_part.CompetitionDetails;

public class SoccerViewModel extends ViewModel {

    public MutableLiveData<ArrayList<CompetitionDetails>> games = new MutableLiveData< >();
    public MutableLiveData<CompetitionDetails> selectedGame = new MutableLiveData< >();
}
