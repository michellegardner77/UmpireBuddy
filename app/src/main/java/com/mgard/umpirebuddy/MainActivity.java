/* Michelle Gardner
   CS 449 Spring 2017
   Lab #1 : Ball and Strike counter
 */

package com.mgard.umpirebuddy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int strike_count = 0;

    private int ball_count = 0;

    private int out_count = 0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    // Method that creates the activity; It is also used to enable the OnClickListener on the strike and ball buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Specify the layout xml file. Located in the res directory
        setContentView(R.layout.activity_main);

        // Create new View object referencing the strike_button
        View StrikeButton = findViewById(R.id.strike_button);

        // Enables the OnClickListener on the StrikeButton view
        StrikeButton.setOnClickListener(this);

        updateStrikeCount();

        // Create new View object referencing the ball_button
        View BallButton = findViewById(R.id.ball_button);

        // Enables the OnClickListener on the BallButton view
        BallButton.setOnClickListener(this);

        updateBallCount();

        // call method to get potential saved out count
        getSharedPrefOutCount();

        // update out count
        updateOutCount();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // this will inflate the option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Handles menu item button clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // handling reset menu item click
        if(id == R.id.action_reset){
            resetOutCount();
            resetStrikeBallCounts();
            return true;
        }

        if(id == R.id.action_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    // Method used to set the StrikeCount to the strike_count global variable
    private void updateStrikeCount() {
        // Reference the strike_count_value TextView on the activity layout created a new TextView object.
        TextView strikeCountValue = (TextView) findViewById(R.id.strike_count_value);

        // Set the text of the strikeCountValue TextView to the values of the strike_count global variable.
        strikeCountValue.setText(Integer.toString(strike_count));
    }

    // Method used to set the BallCount to the ball_count global variable
    private void updateBallCount() {
        TextView ballCountValue = (TextView) findViewById(R.id.ball_count_value);
        ballCountValue.setText(Integer.toString(ball_count));
    }

    private void updateOutCount() {
        TextView outCountValue = (TextView) findViewById(R.id.out_count_value);
        outCountValue.setText(Integer.toString(out_count));

        // updating shared pref out count
        setSharedPrefOutCount();
    }

    // gets the shared preference out_count_value also sets to 0 if nonexistent
    private void getSharedPrefOutCount(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.out_count_default);
        out_count = sharedPref.getInt(getString(R.string.saved_out_count), defaultValue);
    }

    private void setSharedPrefOutCount(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_out_count), out_count);
        editor.apply();
    }

    // method to reset the counts of strike and ball
    private void resetStrikeBallCounts() {
        ball_count = 0;
        strike_count = 0;

        updateStrikeCount();
        updateBallCount();
    }

    private void resetOutCount() {
        out_count = 0;
        updateOutCount();

        // set the out count shared preference to zero
        setSharedPrefOutCount();
    }



    // Will send in variable v and choose case that is appropriate, updating either the ball or strike count
    // along with the dialog box popping up when 3 strikes or 4 balls have been reached
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.strike_button:
                // Resets counter once the count is more than 2
                if (strike_count == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Out!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            resetStrikeBallCounts();
                            out_count++;
                            updateOutCount();

                        }
                    });
                    builder.show();
                } else {
                    strike_count++;
                    updateStrikeCount();
                }
                break;
            case R.id.ball_button:
                // Resets counter once the count is more than 3
                if (ball_count == 3) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Walk!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            resetStrikeBallCounts();
                        }
                    });
                    builder.show();
                } else {
                    ball_count++;
                    updateBallCount();
                }
                break;
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}