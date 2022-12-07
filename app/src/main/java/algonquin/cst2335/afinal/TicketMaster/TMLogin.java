package algonquin.cst2335.afinal.TicketMaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.afinal.databinding.ActivityTmloginBinding;

/**
 * This class is the first one the user sees.
 * It requests the user to input a City and Radius
 * to continue to the next page
 */
public class TMLogin extends AppCompatActivity {

    private ActivityTmloginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTmloginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("Data", Context.MODE_PRIVATE); // initializing SharedPreferences file
        SharedPreferences.Editor editor = prefs.edit(); // creating editor for SharedPreferences

        binding.inputCity.setText(prefs.getString("city","")); // setting the city valued found in the SharedPreferences file
        binding.inputRadius.setText(prefs.getString("radius","")); // setting the radius valued found in the SharedPreferences file

        binding.loginButton.setOnClickListener(clk -> { // Listener for the 'Continue' button
            String city = binding.inputCity.getText().toString(); // getting info from the editTexts
            String radius = binding.inputRadius.getText().toString();

            if(!city.isEmpty() || !radius.isEmpty()){ // if both city and radius are not empty
                editor.putString("city",city); // storing new info into SharedPreferences file
                editor.putString("radius",radius);
                editor.apply();

                Intent nextPage = new Intent(TMLogin.this, TicketMaster.class); // creating intent to move to the TicketMaster activity
                nextPage.putExtra("city",city); // storing the user inputs in the intent
                nextPage.putExtra("radius",radius);
                startActivity(nextPage); // starting the TicketMaster activity
            }
        });
    }
}