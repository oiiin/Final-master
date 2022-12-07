package algonquin.cst2335.afinal.TicketMaster;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.afinal.R;
import algonquin.cst2335.afinal.databinding.EventDetailedBinding;

/**
 * This class extends Fragment and is used by
 * the TicketMaster.java class to display event details
 */
public class EventDetailed extends Fragment {

    private EventDetailedBinding binding;
    private EventObject selectedEvent;
    private EventObjectDAO eDAO;
    private Executor thread = Executors.newSingleThreadExecutor();

    /**
     * Constructor to store the SelectedEvent
     * @param selectedEvent Event selected by the user
     */
    public EventDetailed(EventObject selectedEvent){
        this.selectedEvent = selectedEvent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        binding = EventDetailedBinding.inflate(inflater);

        // Creating connection to the database
        EventDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), EventDatabase.class, "favorite-events").build();
        eDAO = db.evDAO();

        if(selectedEvent.isFav()){ // if the selected event is favorited, set the checkbox and text accordingly
            binding.addToFavs.setText(getString(R.string.favHeader2));
            binding.addToFavs.setChecked(true);
        }

        binding.addToFavs.setOnCheckedChangeListener((btn, state)->{listener(state);}); // setting the Listener for the checkbox

        thread.execute(()->{ // Thread used to get and store the image as a bitmap
            Bitmap img = getImg();
            getActivity().runOnUiThread(()->binding.image.setImageBitmap(img)); // placing the bitmap image into the ImageView
        });

        binding.date.setText(selectedEvent.getDate());
        binding.name.setText(selectedEvent.getName());

        if(selectedEvent.getMaxPrice()>0) // if the max price is higher than 0
            binding.priceRange.setText("Min: "+selectedEvent.getMinPrice()+"\nMax: "+selectedEvent.getMaxPrice());

        binding.tickets.setOnClickListener(clk -> { // setting Listener for the 'click here to purchase tickets' textView
            Intent ticketLink = new Intent(Intent.ACTION_VIEW);
            ticketLink.setData(Uri.parse(selectedEvent.getTicketsURL())); // setting the url to search
            startActivity(ticketLink); // beginning the search activity
        });

        return binding.getRoot();
    }

    /**
     * This method is called whenever the user clicks the 'Add to Favorite' checkbox
     * @param state Weather the checkbox is Checked or Unchecked (true or false)
     */
    public void listener(boolean state){
        // If the box is checked
        if(state){
            selectedEvent.setFav(true);
            thread.execute(()->eDAO.insertEvent(selectedEvent)); // Adding the event to the database
            binding.addToFavs.setText(getString(R.string.favHeader2)); // Changing the text beside the checkbox

            Toast.makeText(getContext(),getString(R.string.favToast),Toast.LENGTH_SHORT).show(); // Creating and displaying Toast

        // If the box is unchecked
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext()) // Creating an AlertDialog
                    .setMessage(getString(R.string.alertDia)+"\n"+selectedEvent.getName())
                    .setTitle("Question")
                    .setNegativeButton(getString(R.string.no),((dialog, cl) ->{
                        binding.addToFavs.setOnCheckedChangeListener(null); // Setting the Listener to 'null' so the app doesn't react when the setChecked is changed
                        binding.addToFavs.setChecked(true);
                        binding.addToFavs.setOnCheckedChangeListener((btn, btnState)->{listener(btnState);}); // Reinstating the Listener after the change
                    }))
                    .setPositiveButton(getString(R.string.yes),((dialog, cl) ->{
                        selectedEvent.setFav(false);
                        thread.execute(()->eDAO.removeEvent(selectedEvent)); // Removing the event from the database
                        binding.addToFavs.setText(getString(R.string.favHeader1)); // Changing the text beside the checkbox

                            Snackbar.make(binding.addToFavs, getString(R.string.removed),Snackbar.LENGTH_LONG).setAction(getString(R.string.undo), clk -> { // Creating snackbar to allow the user to Undo
                                selectedEvent.setFav(true);
                                thread.execute(()->eDAO.insertEvent(selectedEvent)); // Adding event to the database
                                binding.addToFavs.setText(getString(R.string.favHeader2)); // Changing the text beside the checkbox

                                Toast.makeText(getContext(),getString(R.string.favToast),Toast.LENGTH_SHORT).show(); // Creating and displaying Toast

                                binding.addToFavs.setOnCheckedChangeListener(null);
                                binding.addToFavs.setChecked(true);
                                binding.addToFavs.setOnCheckedChangeListener((btn, btnState)->{listener(btnState);});
                            }).show();
                    }));
            alert.create().show();
        }
    }

    /**
     * Method to convert an online image to a Bitmap
     * @return The image found or null if the process fails
     */
    public Bitmap getImg(){
        try{
            URL url = new URL(selectedEvent.getImgLink()); // storing the image's link as a URL variable
            Bitmap img = BitmapFactory.decodeStream(url.openConnection().getInputStream()); // converting the image into a Bitmap
            return img; // returning the bitmap
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}