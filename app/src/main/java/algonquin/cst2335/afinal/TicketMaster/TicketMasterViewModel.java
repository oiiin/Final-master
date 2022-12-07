package algonquin.cst2335.afinal.TicketMaster;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * ViewModel that holds variables for TicketMaster.java
 */
public class TicketMasterViewModel extends ViewModel {
    public MutableLiveData<ArrayList<EventObject>> savedEvents = new MutableLiveData<>();
    public MutableLiveData<EventObject> selectedEvent = new MutableLiveData<>();
}
