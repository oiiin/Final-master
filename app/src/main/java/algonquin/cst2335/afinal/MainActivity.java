package algonquin.cst2335.afinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import algonquin.cst2335.afinal.Movie_part.Movie_MainActivity;
import algonquin.cst2335.afinal.Movie_part.Movie_SecondActivity;
import algonquin.cst2335.afinal.Soccer_part.MainActivitySoccer;

public class MainActivity extends AppCompatActivity {

    Toolbar main_menu;

    String description = "Choose an icon to view your desired app. App created by Ibrahim, Ahmed, Selman and Ridhwan " ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       main_menu = findViewById(R.id.toolbar);
       setSupportActionBar(main_menu);
    }

// **methods for each persons proejct to switch activities, once completed fill in the code required***

//
    public void startMovieActivity() {
        startActivity(new Intent(MainActivity.this, Movie_MainActivity.class));

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
        // add soccer class
        Intent soccerActivity = new Intent(this, MainActivitySoccer.class);
        startActivity(soccerActivity);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


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
                AlertDialog.Builder help = new AlertDialog.Builder(MainActivity.this);
                help.setTitle("Help");
                help.setMessage(description);
                help.show();
                break;
        }
        return true;
    }

}