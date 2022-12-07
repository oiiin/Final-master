package algonquin.cst2335.afinal.TicketMaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.afinal.R;
import algonquin.cst2335.afinal.databinding.ActivityTicketMasterBinding;
import algonquin.cst2335.afinal.databinding.EventBinding;

/**
 * This class is the heart of the Ticket Master section
 * it creates the RecycleView and Database as well as
 * displaying details to the user using fragments
 */
public class TicketMaster extends AppCompatActivity {

    private ActivityTicketMasterBinding binding;
    private ArrayList<EventObject> eventList = new ArrayList<>();
    private ArrayList<EventObject> savedEvents = new ArrayList<>();
    private boolean showFavs = false;
    private RecyclerView.Adapter myAdapter;
    private TicketMasterViewModel model;
    private EventObjectDAO eDAO;
    private Executor thread = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketMasterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.TicketMasterToolBar);

        model = new ViewModelProvider(this).get(TicketMasterViewModel.class); // Setting the model

        EventDatabase db = Room.databaseBuilder(getApplicationContext(), EventDatabase.class, "favorite-events").build(); // Creating connection to the database
        eDAO = db.evDAO();

        savedEvents = model.savedEvents.getValue();

        if(savedEvents == null){
            model.savedEvents.postValue(savedEvents = new ArrayList<>());
            thread.execute(()->{savedEvents.addAll(eDAO.getFavorites());}); // Getting list of saved events from the database
        }

        recyclerViewSetup(); // calling method to setup the recyclerView

        jsonProcess(); // calling method to connect to the server and retrieve the event information

        model.selectedEvent.observe(this, (newValue)->{ // Observer for the selectedEvent variable
            EventDetailed eventFragment = new EventDetailed(newValue); // create EventDetailed class object
            getSupportFragmentManager().beginTransaction().replace(binding.fragmentLocation.getId(), eventFragment).commit(); // build and create fragment for event details
        });

        binding.favorites.setOnCheckedChangeListener((btn, isSelected)->{ // Listener for the 'Display Favorites' switch
            if(isSelected){
                showFavs = true;
            } else{
                showFavs = false;
            }
            myAdapter.notifyDataSetChanged(); // calling method to update the RecyclerView
        });
    }

    /**
     * Class to instantiate information for recyclerView objects
     */
    class MyRowHolder extends RecyclerView.ViewHolder{
        TextView eventTitle;
        TextView eventDate;
        TextView favTag;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventName);
            eventDate = itemView.findViewById(R.id.eventDate);
            favTag = itemView.findViewById(R.id.favText);

            itemView.setOnClickListener(clk -> { // Listener for the Event class object being placed on the RecyclerView
                int position = getAbsoluteAdapterPosition();
                EventObject selected = eventList.get(position);
                model.selectedEvent.postValue(selected); // posting the selected event
            });
        }
    }

    /**
     * Method that sets up the recyclerView
     */
    public void recyclerViewSetup(){
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this)); // Setting the Layout Manager
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                EventBinding binding = EventBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot()); // Returning the layout of event.xml
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.eventTitle.setText("");
                holder.eventDate.setText("");

                holder.itemView.setVisibility(View.VISIBLE); // setting the view to be visible

                if(showFavs){ // If the user chooses to only show favorite events
                    if(eventList.get(position).isFav()){ // if the event is favorited
                        holder.eventTitle.setText(eventList.get(position).getName());
                        holder.eventDate.setText(eventList.get(position).getDate());
                        holder.favTag.setText(getString(R.string.favHeader2));
                    }else{ // if the event is not favorited
                        holder.itemView.setVisibility(View.INVISIBLE); // make the event invisible
                    }
                }else{
                    holder.eventTitle.setText(eventList.get(position).getName());
                    holder.eventDate.setText(eventList.get(position).getDate());
                    if(eventList.get(position).isFav()){
                        holder.favTag.setText(getString(R.string.favHeader2));
                    }
                }
            }

            @Override
            public int getItemCount() {
                return eventList.size();
            }
        });
    }

    /**
     * Method that connects to the Server and gets the Event information
     */
    public void jsonProcess(){
        /* API URL creation */
        Intent fromPrevious = getIntent();
        String city = fromPrevious.getStringExtra("city");
        String radius = fromPrevious.getStringExtra("radius");
        String url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=mhfAwsR8NGA2jUuuClXl86EJpUnavblt&city="+ URLEncoder.encode(city)+"&radius="+URLEncoder.encode(radius);

        /* JSON Request and information storing */
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                (response) -> {
                    try {
                        JSONArray events = response.getJSONObject("_embedded").getJSONArray("events"); // getting the JSON array of events
                        for(int i=0; i<events.length(); i++){ // for loop to increment through the array of events
                            JSONObject event = events.getJSONObject(i); // current event

                            String id = event.getString("id");
                            String name = event.getString("name");
                            String tickets = event.getString("url");
                            String date = event.getJSONObject("dates").getJSONObject("start").getString("localDate");
                            float minPrice = 0;
                            float maxPrice = 0;
                            if(event.has("priceRanges")){ // if the event has the priceRanges array
                                minPrice = event.getJSONArray("priceRanges").getJSONObject(0).getInt("min");
                                maxPrice = event.getJSONArray("priceRanges").getJSONObject(0).getInt("max");
                            }
                            String imgLink = event.getJSONArray("images").getJSONObject(1).getString("url");

                            boolean isFav = false;

                            for(int j=0; j<savedEvents.size(); j++){ // for loop to increment through the savedEvents
                                if(savedEvents.get(j).getId().equals(id))  // if the current event is the same as one of the saved events
                                    isFav = true;
                            }

                            EventObject ev = new EventObject(id, name, tickets, date, minPrice, maxPrice, imgLink, isFav); // creating the event object

                            if(isFav){ // if the event is favorited, place it at the beginning
                                eventList.add(0,ev);
                            }else{ // if the event is not, place it at the end
                                eventList.add(ev);
                            }
                            myAdapter.notifyItemInserted(eventList.size()-1); // calling method to add the event to the RecyclerView
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                (error) -> {
                    error.printStackTrace();
                });
        queue.add(request);
    }

    /**
     * Method that creates the menu
     * @param menu The Menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.ticket_master_menu, menu);
        return true;
    }

    /**
     * Method that handles interaction with the menu
     * @param item The menuItem that was clicked
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        switch (item.getItemId())
        {
            case R.id.TM_help:
                AlertDialog.Builder alert = new AlertDialog.Builder(this) // Creating an AlertDialog
                        .setMessage(getString(R.string.TM_helpMessage))
                        .setTitle(getString(R.string.TM_help));
                alert.create().show(); // Displaying alert dialog
                break;
        }

        return true;
    }
}