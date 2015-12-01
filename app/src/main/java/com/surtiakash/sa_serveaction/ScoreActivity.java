package com.surtiakash.sa_serveaction;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class ScoreActivity extends Activity {

    private Button mPlayerOne;
    private Button mPlayerTwo;
    private Button mReset;

    private String PlayerOne;
    private String PlayerTwo;

    private int PlayerOneGameScore = 0;
    private int PlayerTwoGameScore = 0;

    private int PlayerOneSetScore = 0;
    private int PlayerTwoSetScore = 0;

    private int PlayerOneSetsWon = 0;
    private int PlayerTwoSetsWon = 0;

    private int SetCount = 0;

    private int[] PlayerOneSetIds = {
            R.id.player_one_set1,
            R.id.player_one_set2,
            R.id.player_one_set3,
            R.id.player_one_set4,
            R.id.player_one_set5
    };

    private int[] PlayerTwoSetIds = {
            R.id.player_two_set1,
            R.id.player_two_set2,
            R.id.player_two_set3,
            R.id.player_two_set4,
            R.id.player_two_set5
    };

    private boolean gameOver = false;

    private boolean tiebreak = false;

    private boolean playerOneServe = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        mPlayerOne = (Button) findViewById(R.id.player_one_button);
        mPlayerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerOnePoint();
            }
        });

        mPlayerTwo = (Button) findViewById(R.id.player_two_button);
        mPlayerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerTwoPoint();
            }
        });

        mReset = (Button) findViewById(R.id.reset);
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMatch();
            }
        });

        final Spinner playerOneSpinner = (Spinner) findViewById(R.id.player_one_spinner);
        final Spinner playerTwoSpinner = (Spinner) findViewById(R.id.player_two_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.player_names_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        playerOneSpinner.setAdapter(adapter);
        playerTwoSpinner.setAdapter(adapter);

        playerOneSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        PlayerOne = playerOneSpinner.getSelectedItem().toString();
                        switchImagePlayerOne();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        PlayerOne = playerOneSpinner.getSelectedItem().toString();
                        switchImagePlayerOne();
                    }
                }
        );

        playerTwoSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        PlayerTwo = playerTwoSpinner.getSelectedItem().toString();
                        switchImagePlayerTwo();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        PlayerTwo = playerTwoSpinner.getSelectedItem().toString();
                        switchImagePlayerTwo();
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayScorePlayerOne(int score){
        TextView scoreView = (TextView) findViewById(R.id.player_one_score);
        scoreView.setText(String.valueOf(score));
    }

    private void displayAdvantagePlayerOne(){
        TextView scoreView = (TextView) findViewById(R.id.player_one_score);
        scoreView.setText("AD");
    }

    private void displayScorePlayerTwo(int score){
        TextView scoreView = (TextView) findViewById(R.id.player_two_score);
        scoreView.setText(String.valueOf(score));
    }

    private void displayAdvantagePlayerTwo(){
        TextView scoreView = (TextView) findViewById(R.id.player_two_score);
        scoreView.setText("AD");
    }

    private void newGame() {

        changeServe(playerOneServe);

        PlayerOneGameScore = 0;
        PlayerTwoGameScore = 0;

        displayScorePlayerOne(PlayerOneGameScore);
        displayScorePlayerTwo(PlayerTwoGameScore);
    }

    private void newSet() {
        PlayerOneSetScore = 0;
        PlayerTwoSetScore = 0;
    }

    private void playerOnePoint() {

        // Ensures that the button doesn't respond if the game is over
        if (gameOver == true) {
            Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show();
            return;
        }

        // In case of a tie break
        if (tiebreak) {

            PlayerOneGameScore++;
            displayScorePlayerOne(PlayerOneGameScore);

            if (PlayerOneGameScore >= 7 && PlayerOneGameScore - PlayerTwoGameScore > 1) {
                newGame();
                updateSet(true);
                tiebreak = false;
            }

            return;
        }

        // In case of a deuce, using the number 50 as advantage
        if (PlayerOneGameScore == 40 && PlayerTwoGameScore == 40) {
            PlayerOneGameScore = 50;
            displayAdvantagePlayerOne();
        }

        // When the other player has an advantage and loses the point
        else if (PlayerOneGameScore == 40 && PlayerTwoGameScore == 50) {
            PlayerTwoGameScore = 40;
            displayScorePlayerTwo(PlayerTwoGameScore);

            Toast.makeText(this, R.string.deuce_toast, Toast.LENGTH_SHORT).show();
        }

        // When the player has an advantage and wins the point
        else if (PlayerOneGameScore == 50) {
            newGame();
            updateSet(true);
        }

        // When the player wins the game
        else if (PlayerOneGameScore == 40) {
            newGame();
            updateSet(true);
        }

        // Scoring convention
        else if(PlayerOneGameScore == 30) {

            // Deuce Toast
            if (PlayerTwoGameScore == 40) {
                Toast.makeText(this, R.string.deuce_toast, Toast.LENGTH_SHORT).show();
            }

            PlayerOneGameScore = 40;
            displayScorePlayerOne(PlayerOneGameScore);
        }

        // Regular scoring
        else {
            PlayerOneGameScore = PlayerOneGameScore + 15;
            displayScorePlayerOne(PlayerOneGameScore);
        }
    }

    private void playerTwoPoint() {

        // Ensures that the button doesn't respond if the game is over
        if (gameOver) {
            Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show();
            return;
        }

        // In case of a tie break
        if (tiebreak) {

            PlayerTwoGameScore++;
            displayScorePlayerTwo(PlayerTwoGameScore);

            if (PlayerTwoGameScore >= 7 && PlayerTwoGameScore - PlayerOneGameScore > 1) {
                newGame();
                updateSet(false);
                tiebreak = false;
            }

            return;
        }

        // In case of a deuce, using the number 50 as advantage
        if (PlayerOneGameScore == 40 && PlayerTwoGameScore == 40) {
            PlayerTwoGameScore = 50;
            displayAdvantagePlayerTwo();
        }

        // When the other player has an advantage and loses the point
        else if (PlayerOneGameScore == 50 && PlayerTwoGameScore == 40) {
            PlayerOneGameScore = 40;
            displayScorePlayerOne(PlayerOneGameScore);

            Toast.makeText(this, R.string.deuce_toast, Toast.LENGTH_SHORT).show();
        }

        // When the player has an advantage and wins the point
        else if (PlayerTwoGameScore == 50) {
            newGame();
            updateSet(false);
        }

        // When the player wins the game
        else if (PlayerTwoGameScore == 40) {
            newGame();
            updateSet(false);
        }

        // Scoring convention when on 30
        else if(PlayerTwoGameScore == 30) {

            // Deuce Toast
            if(PlayerOneGameScore == 40) {
                Toast.makeText(this, R.string.deuce_toast, Toast.LENGTH_SHORT).show();
            }

            PlayerTwoGameScore = 40;
            displayScorePlayerTwo(PlayerTwoGameScore);
        }

        // Regular scoring
        else {
            PlayerTwoGameScore = PlayerTwoGameScore + 15;
            displayScorePlayerTwo(PlayerTwoGameScore);
        }
    }

    private void updateSet(boolean PlayerOneWon){
        if(PlayerOneWon == false) {
            PlayerTwoSetScore = PlayerTwoSetScore + 1;

            displaySetPlayerOne(PlayerOneSetScore);
            displaySetPlayerTwo(PlayerTwoSetScore);

            if(PlayerTwoSetScore == 7 || PlayerTwoSetScore == 6 && PlayerOneSetScore != 5 && PlayerOneSetScore != 6){
                PlayerTwoSetsWon++;
                if(PlayerTwoSetsWon != 3) {
                    // PlayerTwo = getString(R.string.player_name_two);
                    Toast.makeText(this, PlayerTwo + " has won the set.", Toast.LENGTH_SHORT).show();
                }
                nextSet();
                return;
            }
        } else {
            PlayerOneSetScore = PlayerOneSetScore + 1;

            displaySetPlayerOne(PlayerOneSetScore);
            displaySetPlayerTwo(PlayerTwoSetScore);

            if (PlayerOneSetScore == 7 || PlayerOneSetScore == 6 && PlayerTwoSetScore != 5 && PlayerTwoSetScore != 6) {
                PlayerOneSetsWon++;
                if(PlayerOneSetsWon != 3) {
                    // PlayerOne = getString(R.string.player_name_one);
                    Toast.makeText(this, PlayerOne + " has won the set.", Toast.LENGTH_SHORT).show();
                }
                nextSet();
                return;
            }
        }

        // Tiebreak
        if (PlayerOneSetScore == 6 && PlayerTwoSetScore == 6){
            Toast.makeText(this, R.string.tiebreak_toast, Toast.LENGTH_SHORT).show();
            tiebreak = true;
        }
    }

    private void displaySetPlayerOne(int score){
        TextView scoreView = (TextView) findViewById(PlayerOneSetIds[SetCount]);
        scoreView.setText(String.valueOf(score));
    }

    private void displaySetPlayerTwo(int score){
        TextView scoreView = (TextView) findViewById(PlayerTwoSetIds[SetCount]);
        scoreView.setText(String.valueOf(score));
    }

    private void nextSet(){
        if (PlayerOneSetsWon >= 3) {
            //PlayerOne = getString(R.string.player_name_one);
            Toast.makeText(this, PlayerOne + " has won the match!", Toast.LENGTH_LONG).show();
            gameOver = true;
            return;
        } else if (PlayerTwoSetsWon >= 3) {
            //String PlayerTwo = getString(R.string.player_name_two);
            Toast.makeText(this, PlayerTwo + " has won the match!", Toast.LENGTH_LONG).show();
            gameOver = true;
            return;
        } else {
            newSet();
            SetCount++;
        }
    }

    private void changeServe(boolean playerOneServing){

        ImageView playerOne = (ImageView) findViewById(R.id.serve_player_one);
        ImageView playerTwo = (ImageView) findViewById(R.id.serve_player_two);

        if (playerOneServing){
            playerOne.setVisibility(View.INVISIBLE);
            playerTwo.setVisibility(View.VISIBLE);

            playerOneServe = false;
            return;
        }

        else {
            playerOne.setVisibility(View.VISIBLE);
            playerTwo.setVisibility(View.INVISIBLE);

            playerOneServe = true;
            return;
        }
    }

    private void newMatch() {

        newGame();
        newSet();

        PlayerOneSetsWon = 0;
        PlayerTwoSetsWon = 0;

        SetCount = 0;

        for(int i = 0; i < 5; i++){
            TextView playerOneSets = (TextView) findViewById(PlayerOneSetIds[i]);
            playerOneSets.setText(String.valueOf(0));

            TextView playerTwoSets = (TextView) findViewById(PlayerTwoSetIds[i]);
            playerTwoSets.setText(String.valueOf(0));
        }

        tiebreak = false;
        gameOver = false;
        playerOneServe = false;
    }

    private void switchImagePlayerOne() {
        ImageView playerOneImage = (ImageView) findViewById(R.id.player_one_image);
        if(PlayerOne.equals("A. Seng")){
            playerOneImage.setImageResource(R.drawable.anne_seng_397_wtn);
        }
        if(PlayerOne.equals("A. Bridgett")){
            playerOneImage.setImageResource(R.drawable.abby_bridgett_397_wtn);
        }
        if(PlayerOne.equals("G. Ferro")){
            playerOneImage.setImageResource(R.drawable.gabriella_ferro_397_wtn);
        }
        if(PlayerOne.equals("G. Benton")){
            playerOneImage.setImageResource(R.drawable.gracie_benton_397_wtn);
        }
        if(PlayerOne.equals("J. Elkerton")){
            playerOneImage.setImageResource(R.drawable.janice_elkerton_397_wtn);
        }
        if(PlayerOne.equals("J. Thome")){
            playerOneImage.setImageResource(R.drawable.julia_thome_397_wtn);
        }
        if(PlayerOne.equals("S. Molly")){
            playerOneImage.setImageResource(R.drawable.shashanna_moll_397_wtn);
        }
        if(PlayerOne.equals("S. Robertson")){
            playerOneImage.setImageResource(R.drawable.sophie_robertson_397_wtn);
        }
    }

    private void switchImagePlayerTwo() {
        ImageView playerTwoImage = (ImageView) findViewById(R.id.player_two_image);
        if(PlayerTwo.equals("A. Seng")){
            playerTwoImage.setImageResource(R.drawable.anne_seng_397_wtn);
        }
        if(PlayerTwo.equals("A. Bridgett")){
            playerTwoImage.setImageResource(R.drawable.abby_bridgett_397_wtn);
        }
        if(PlayerTwo.equals("G. Ferro")){
            playerTwoImage.setImageResource(R.drawable.gabriella_ferro_397_wtn);
        }
        if(PlayerTwo.equals("G. Benton")){
            playerTwoImage.setImageResource(R.drawable.gracie_benton_397_wtn);
        }
        if(PlayerTwo.equals("J. Elkerton")){
            playerTwoImage.setImageResource(R.drawable.janice_elkerton_397_wtn);
        }
        if(PlayerTwo.equals("J. Thome")){
            playerTwoImage.setImageResource(R.drawable.julia_thome_397_wtn);
        }
        if(PlayerTwo.equals("S. Molly")){
            playerTwoImage.setImageResource(R.drawable.shashanna_moll_397_wtn);
        }
        if(PlayerTwo.equals("S. Robertson")){
            playerTwoImage.setImageResource(R.drawable.sophie_robertson_397_wtn);
        }
    }
}
