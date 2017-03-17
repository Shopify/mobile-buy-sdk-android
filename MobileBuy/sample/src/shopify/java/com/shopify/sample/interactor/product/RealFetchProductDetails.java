package com.shopify.sample.interactor.product;

import android.support.annotation.NonNull;

import com.shopify.buy3.APISchema;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.graphql.support.ID;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.presenter.product.Product;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.util.Util.mapItems;

public final class RealFetchProductDetails implements FetchProductDetails {
  private final GraphClient graphClient = SampleApplication.graphClient();

  @NonNull @Override public Single<Product> call(final String productId) {
    GraphCall<APISchema.QueryRoot> call = graphClient.queryGraph(APISchema.query(
      root -> root
        .node(new ID(productId), node -> node
          .onProduct(product -> product
            .title()
            .descriptionHtml()
            .tags()
            .images(250, imageConnection -> imageConnection
              .edges(imageEdge -> imageEdge
                .node(APISchema.ImageQuery::src)
              )
            )
            .options(option -> option
              .name()
              .values()
            )
            .variants(250, variantConnection -> variantConnection
              .edges(variantEdge -> variantEdge
                .node(variant -> variant
                  .title()
                  .available()
                  .selectedOptions(selectedOption -> selectedOption
                    .name()
                    .value()
                  )
                  .price()
                )
              )
            )
          )
        )
    ));
    return Single.fromCallable(call::execute)
      .map(queryRoot -> ((APISchema.Product) queryRoot.data().getNode()))
      .map(RealFetchProductDetails::convert)
      .subscribeOn(Schedulers.io());
  }

  private static Product convert(final APISchema.Product product) {
    List<String> images = mapItems(product.getImages().getEdges(), imageEdge -> imageEdge.getNode().getSrc());
    List<Product.Option> options = mapItems(product.getOptions(), option -> new Product.Option(option.getId().toString(), option.getName(),
      option.getValues()));
    List<Product.Variant> variants = mapItems(product.getVariants().getEdges(),
      variantEdge -> {
        List<Product.SelectedOption> selectedOptions = mapItems(variantEdge.getNode().getSelectedOptions(), selectedOption ->
          new Product.SelectedOption(selectedOption.getName(), selectedOption.getValue()));
        return new Product.Variant(variantEdge.getNode().getId().toString(), variantEdge.getNode().getTitle(),
          (variantEdge.getNode().getAvailable() == null || Boolean.TRUE.equals(variantEdge.getNode().getAvailable())),
          selectedOptions, variantEdge.getNode().getPrice());
      });
    return new Product(product.getId().toString(), product.getTitle(), product.getDescriptionHtml(), product.getTags(), images, options,
      variants);
  }
}
