package org.thesis.nrip.simplepoc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button first;
    Button second;
    EditText input;
    TextView output;
    private static String TAG = "mainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        first = (Button) findViewById(R.id.first_button);
//        second = (Button) findViewById(R.id.second_button);
        input = (EditText) findViewById(R.id.input);
        output = (TextView) findViewById(R.id.output);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();

                if(text.equals("1")){
                    output.setText("Ganaste!");
                }else{
                    output.setText("Perdiste!");
                }

                output.setVisibility(View.VISIBLE);
            }
        });

//        second.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                throw new RuntimeException("Hiciste click donde no tenias que");
//            }
//        });
    }


}
