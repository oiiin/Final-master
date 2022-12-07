package algonquin.cst2335.afinal.Soccer_part;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import algonquin.cst2335.afinal.R;
import algonquin.cst2335.afinal.databinding.ActivityMainSoccerBinding;


/**
 * Main activity for soccer match highlights app.
 */
public class MainActivitySoccer extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<CompetitionDetails> games;
    SoccerViewModel soccerModel;

    Button favButton;
    private static String JSON_URL = "https://www.scorebat.com/video-api/v1/";
    SoccerAdapter adapter;
    ActivityMainSoccerBinding binding;
    Toolbar main_menu;

    /**
     * create soccer activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_soccer);

        binding = ActivityMainSoccerBinding.inflate(getLayoutInflater());
        main_menu = findViewById(R.id.toolbar);
        setSupportActionBar(main_menu);
        getSupportActionBar().setTitle("Soccer");

        soccerModel = new ViewModelProvider(this).get(SoccerViewModel.class);
        recyclerView = findViewById(R.id.soccerList);
        games = new ArrayList<>();

        binding.soccerList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        soccerModel.games.setValue(games = new ArrayList<CompetitionDetails>());


        /**
         * thread used to call retrieveGames method
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        retrieveGames();
                    }
                });
            }
        }).start();


        binding.soccerList.setAdapter(adapter);
        games = soccerModel.games.getValue();


        /**
         * Fragment for main detail fragment
         */
        if (soccerModel != null) {
            soccerModel.selectedGame.observe(this, (newGameValue) -> {

                SoccerDetailsFragment soccerFragment = new SoccerDetailsFragment(newGameValue);

                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();
                tx.add(R.id.fragmentLocation, soccerFragment);
                tx.addToBackStack("Back to previous activity");
                tx.commit();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, soccerFragment)
                        .commit();
            });

        }


        binding.buttonFavourite.setOnClickListener(clk -> {


            AlertDialog.Builder help = new AlertDialog.Builder(MainActivitySoccer.this);
            help.setTitle("Error");
            help.setMessage(getResources().getString(R.string.viewFav));
            help.show();
        });


    }


    /**
     * RetrieveGames method.
     * uses Volley to access json url and populate data
     */
    private void retrieveGames(){

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject gameObject = response.getJSONObject(i);

                        CompetitionDetails competitionDetails = new CompetitionDetails();
                        competitionDetails.setTitle(gameObject.getString("title").toString());
                        competitionDetails.setThumbnail(gameObject.getString("thumbnail"));
                        competitionDetails.setCompetitionName(gameObject.getJSONObject("competition").getString("name"));
                        competitionDetails.setDate(gameObject.getString("date"));

                        String embed = "";
                        // Code to parse out the video url from src attribute
                        JSONArray jsonArray = gameObject.getJSONArray("videos");
                        for (int k = 0; k < jsonArray.length(); k++){
                            JSONObject obj= jsonArray.getJSONObject(k);
                            embed = obj.getString("embed");
                            System.out.println("obj " + obj);
                        }
                        if (!embed.equals("")) {
                            String[] attrs= embed.split(" ");
                            System.out.println("src: " + attrs[2].substring(4));
                            competitionDetails.setVideoUrl(attrs[2].substring(4));
                        }
                        games.add(competitionDetails);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                adapter = new SoccerAdapter(getApplicationContext(), games, soccerModel);

                recyclerView.setAdapter(adapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag","onErrorResponse: " + error.getMessage());

            }
        });
        queue.add(jsonArrayRequest);

    }


    /**
     * Used for menu to access each persons project
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    public void startMovieActivity() {
        // add movie class
        // Intent movieActivity = new Intent(this, .class);
        // startActivity(movieActivity);
    }

    public void startTicketMasterActivity() {
        // add movie class
        // Intent ticketMasterActivity = new Intent(this, .class);
        // startActivity(ticketMasterActivity);
    }

    public void startPexelsActivity() {
        // add movie class
        //   Intent pexelsActivity = new Intent(this, .class);
        // startActivity(pexelsActivity);
    }

    public void startSoccerActivity() {

        Intent soccerActivity = new Intent(this, MainActivitySoccer.class);
        startActivity(soccerActivity);
    }


    /**
     * Used to start desired activity when clicked by user
     * @param menuItem
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case R.id.movie_info:
                startMovieActivity();
                break;
            case R.id.pexels:
                startPexelsActivity();
                break;
            case R.id.ticket_master:
                startTicketMasterActivity();
                break;
            case R.id.soccer_highlights:
                startSoccerActivity();
                break;
            case R.id.info:
                AlertDialog.Builder help = new AlertDialog.Builder(MainActivitySoccer.this);
                help.setTitle(getResources().getString(R.string.helpTitle));
                help.setMessage(getResources().getString(R.string.help));
                help.show();
                break;
        }
        return true;
    }
}