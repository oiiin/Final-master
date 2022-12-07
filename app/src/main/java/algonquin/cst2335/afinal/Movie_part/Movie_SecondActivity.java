package algonquin.cst2335.afinal.Movie_part;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import algonquin.cst2335.afinal.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.afinal.databinding.MovieActivitySecondBinding;

public class Movie_SecondActivity extends AppCompatActivity {

    private MovieActivitySecondBinding binding;
    ArrayList<MovieInfo> movies;
    private RecyclerView.Adapter myAdapter;
    MovieInfoViewModel movieModel;
    MovieInfoDAO mDAO;
    Button search_fav_btn;
    SharedPreferences sharedPreferences;

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
                startActivity(new Intent(Movie_SecondActivity.this, Movie_SecondActivity.class));
                break;
            case R.id.help:
                Toast.makeText(this, "You are in help window", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(SecondActivity.this,Pop.class));
                AlertDialog.Builder builder = new AlertDialog.Builder( Movie_SecondActivity.this );
                builder.setMessage(getText(R.string.help_info));
                builder.setTitle("Help info:");
                builder.setPositiveButton("OK",(dialog, cl)-> {});
                builder.create().show();
                break;
            case R.id.home:
                Toast.makeText(this, "You are in home window", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Movie_SecondActivity.this, Movie_MainActivity.class));
                break;
        }

        return true;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MovieActivitySecondBinding.inflate(getLayoutInflater());
        search_fav_btn = binding.searchFavButton;
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        String s1 = sharedPreferences.getString("tv_title_fav", "");
        binding.searchFav.setText(s1);
        RecyclerView recyclerView = binding.recycleView;
        movieModel = new ViewModelProvider(this).get(MovieInfoViewModel.class);
        movies = movieModel.movies.getValue();
        MovieDatabase db = Room.databaseBuilder(getApplicationContext(),MovieDatabase.class,"database-name").fallbackToDestructiveMigration().build();
        mDAO = db.miDAO();

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.movieToolbar2);
        setSupportActionBar(toolbar);

        movieModel = new ViewModelProvider(this).get(MovieInfoViewModel.class);
        movies = movieModel.movies.getValue();

        if(movies == null)
        {
            //chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());
            movieModel.movies.setValue(movies = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                movies.addAll((Collection<? extends MovieInfo>) mDAO.getAllMovies()); //Once you get the data from database
                binding.recycleView.setAdapter( myAdapter ); //You can then load the RecyclerView
            });
        }

        binding.recycleView.setAdapter(myAdapter=new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View root = getLayoutInflater().inflate(R.layout.movie_titles, parent, false);
                return new MyRowHolder(root);

            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                MovieInfo obj = movies.get(position);
                holder.movieTitle.setText(obj.getTitle());

            }

            @Override
            public int getItemCount() {
                return movies.size();
            }


        });

        movieModel.selectedMovie.observe(this, (newMessageValue) -> {
            MovieDetailsFragment movieFragment = new MovieDetailsFragment( newMessageValue );
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.replace(R.id.fragment_location, movieFragment);
            tx.commit();
        });

        binding.searchFavButton.setOnClickListener(clk->{
            String movieTitleSearch=binding.searchFav.getText().toString();
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("tv_title_fav", movieTitleSearch);
            myEdit.apply();
            MovieInfo[] movieList=new MovieInfo[movies.size()];
            movieList=movies.toArray(movieList);
            for(MovieInfo resultMovie:movieList){
                if(!resultMovie.getTitle().contains(movieTitleSearch)){
                    movies.remove(resultMovie);
                }
            }
            recyclerView.getLayoutManager().removeAllViews();
            myAdapter.notifyItemChanged(movies.size()-1);
        });

        RecyclerView.LayoutManager LayoutManager = new LinearLayoutMW(this, LinearLayoutManager.VERTICAL, false);
        binding.recycleView.setLayoutManager(LayoutManager);
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView movieTitle;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk ->{
                int position=getAbsoluteAdapterPosition();
                MovieInfo selected = movies.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder( Movie_SecondActivity.this );
                builder.setMessage("Do you want to delete this movie or show its details: "+movieTitle.getText());
                builder.setTitle("Question:");
                builder.setPositiveButton("Yes",(dialog, cl)->{
                    MovieInfo m=movies.get(position);
                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() ->
                    {
                        mDAO.deleteMovie(m);
                    });

                    movies.remove(position);
                    myAdapter.notifyItemRemoved(position);
                    Snackbar.make(movieTitle, "You deleted movie #"+position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", undo->{

                                thread.execute(() ->
                                {
                                    mDAO.insertMovie(m);
                                });
                                movies.add(position,m);
                                myAdapter.notifyItemInserted(position);

                            }).show();
                });
                builder.setNegativeButton("Details",(dialog, cl)->{
                    movieModel.selectedMovie.postValue(selected);
                });
                builder.create().show();

            });

            movieTitle = itemView.findViewById (R.id.title_rec);

        }
    }

    public class LinearLayoutMW extends LinearLayoutManager {

        public LinearLayoutMW(Context context){
            super(context);
        }
        public LinearLayoutMW(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public LinearLayoutMW(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }
}
