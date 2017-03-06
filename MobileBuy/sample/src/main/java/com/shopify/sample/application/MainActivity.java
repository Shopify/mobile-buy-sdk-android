package com.shopify.sample.application;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.shopify.buy3.APISchema;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.sample.R;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.run).setOnClickListener(v -> runQuery());
  }

  private void runQuery() {
    GraphCall<APISchema.QueryRoot> call = SampleApplication.graphClient()
      .queryGraph(APISchema.query(root -> root
        .shop(shop -> shop
          .name()
          .products(
            100,
            products -> products
              .edges(edges -> edges
                .node(product -> product
                  .title()
                  .handle()
                ))
          )
        )
      ));

    call.enqueue(new GraphCall.Callback<APISchema.QueryRoot>() {
      @Override public void onResponse(@NonNull final GraphResponse<APISchema.QueryRoot> response) {
        System.out.println(response);
      }

      @Override public void onFailure(@NonNull final GraphError error) {
        System.out.println(error);
      }
    });
  }
}
