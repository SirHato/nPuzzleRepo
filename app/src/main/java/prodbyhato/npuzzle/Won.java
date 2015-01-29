package prodbyhato.npuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class Won extends Activity {

   private int totalTaps = 0;

    // This function is called when this activity starts.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);
        Bundle bundle = getIntent().getExtras();
        totalTaps = bundle.getInt("id");
        TextView textView = (TextView) findViewById(R.id.textViewWon);
        textView.setText("CONGRATULATIONS! \n YOU SOLVED IT! \n Amount of moves: " + totalTaps);
    }

    // This function handles clicks on the button.
    public void onClick(View view)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
