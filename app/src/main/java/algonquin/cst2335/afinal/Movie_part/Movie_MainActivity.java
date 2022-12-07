package algonquin.cst2335.afinal.Movie_part;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import algonquin.cst2335.afinal.R;


public class Movie_MainActivity extends AppCompatActivity {

    TextView tvYear,tvActors,tvDirector,tvRated, tvRuntime, tvPlot, tvTitle, tvURL;
    ImageView ivPoster;
    Button addBtn;
    EditText edMName;
    MovieInfoDAO mDAO;
    ArrayList<MovieInfo> movies = new ArrayList<>();
    SharedPreferences sharedPreferences ;



    protected RequestQueue queue = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.favorite:
                Toast.makeText(this, "You are in favorite window", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Movie_MainActivity.this, Movie_SecondActivity.class));


                break;
            case R.id.help:
                Toast.makeText(this, "You are in help window", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this,Pop.class));
                AlertDialog.Builder builder = new AlertDialog.Builder( Movie_MainActivity.this );
                builder.setMessage(getText(R.string.help_info));
                builder.setTitle(getText(R.string.help));
                builder.setPositiveButton("OK",(dialog, cl)-> {});
                builder.create().show();

                break;
            case R.id.home:
                Toast.makeText(this, "You are in home window", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Movie_MainActivity.this, Movie_MainActivity.class));
                break;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity_main);
        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.movie_activity_main);

        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        String s1 = sharedPreferences.getString("tv_title", "");






        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.movieToolbar);
        setSupportActionBar(toolbar);
        //toolbar.showOverflowMenu();

        tvURL = findViewById(R.id.imgURL);
        addBtn = findViewById(R.id.add);
        tvTitle = findViewById(R.id.tvTitle);
        edMName = findViewById(R.id.edMName);
        tvYear = findViewById(R.id.tvYear);
        tvActors = findViewById(R.id.tvActors);
        tvDirector = findViewById(R.id.tvDirector);
        tvRated = findViewById(R.id.tvRated);
        tvRuntime = findViewById(R.id.tvRuntime);
        tvPlot = findViewById(R.id.tvPlot);
        ivPoster = findViewById(R.id.ivPoster);

        edMName.setText(s1+"");

        addBtn.setOnClickListener(clk -> {
            String year1 = tvYear.getText().toString();
            String title1 = tvTitle.getText().toString();
            String actors1 = tvActors.getText().toString();
            String director1 = tvDirector.getText().toString();
            String rated1 = tvRated.getText().toString();
            String runtime1 = tvRuntime.getText().toString();
            String posterURL1 = tvURL.getText().toString();
            String plot1 = tvPlot.getText().toString();

            MovieInfo mInfo = new MovieInfo(title1,
                    year1,
                    actors1,
                    director1,
                    rated1,
                    runtime1,
                    plot1,
                    posterURL1);
            if(!movies.contains(mInfo)){
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() ->
                {
                    mDAO.insertMovie(mInfo);
                });
                movies.add(mInfo);
            }

        });




    }



    public void search(View view) throws UnsupportedEncodingException {
        String mName = edMName.getText().toString();
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("tv_title", mName);
        myEdit.apply();
        if(mName.isEmpty())
        {
            edMName.setError("Please provide movie name");
            return;
        }
        String url = "http://www.omdbapi.com/?apikey=6c9862c2&t="+URLEncoder.encode(mName, "UTF-8");

        RequestQueue queue = Volley.newRequestQueue(this);
        MovieDatabase db = Room.databaseBuilder(getApplicationContext(),MovieDatabase.class,"database-name").fallbackToDestructiveMigration().build();
        mDAO = db.miDAO();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject movie = new JSONObject(response);
                            String result = movie.getString("Response");


                            if(result.equals("True"))
                            {


                                Toast.makeText(Movie_MainActivity.this,"Found",Toast.LENGTH_SHORT).show();
                                String title = movie.getString("Title");
                                tvTitle.setText("Title: "+title);

                                String year = movie.getString("Year");
                                tvYear.setText("Year: "+year);

                                String actors = movie.getString("Actors");
                                tvActors.setText("Actors: "+actors);

                                String director = movie.getString("Director");
                                tvDirector.setText("Director: "+director);

                                String country = movie.getString("Rated");
                                tvRated.setText("Country: "+country);

                                String language = movie.getString("Runtime");
                                tvRuntime.setText("Language: "+language);

                                String plot = movie.getString("Plot");
                                tvPlot.setText("Plot: "+plot);

                                String posterURL = movie.getString("Poster");
                                tvURL.setText(posterURL);


                                if(posterURL.equals("N/A"))
                                {
                                    ivPoster.setImageResource(R.drawable.not_found);
                                }



                                else
                                {
                                    Picasso.get().load(posterURL).into(ivPoster);
                                }
                            }
                            else
                            {
                                Toast.makeText(Movie_MainActivity.this,"Movie not found",Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );
        queue.add(request);




    }


}
