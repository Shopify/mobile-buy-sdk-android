package com.shopify.sample.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shopify.sample.R;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

//    findViewById(R.id.run).setOnClickListener(v -> runQuery());
  }
}
