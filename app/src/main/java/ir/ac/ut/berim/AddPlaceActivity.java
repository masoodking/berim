package ir.ac.ut.berim;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by saeed on 1/17/2016.
 */
public class AddPlaceActivity extends AppCompatActivity{
    private Context mContext;
    private Button mSubmit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        mContext = this;

        mSubmit = (Button) findViewById(R.id.button);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
