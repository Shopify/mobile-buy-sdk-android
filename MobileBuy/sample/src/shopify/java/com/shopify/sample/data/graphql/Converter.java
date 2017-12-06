package com.shopify.sample.data.graphql;

import com.shopify.buy3.Storefront;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.domain.model.Product;
import com.shopify.sample.domain.model.ProductDetail;
import com.shopify.sample.util.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.shopify.sample.util.Util.mapItems;

public final class Converter {

  public static List<Collection> convertCollections(Storefront.CollectionConnection collectionConnection) {
    List<Collection> collections = new ArrayList<>();
    for (Storefront.CollectionEdge collectionEdge : collectionConnection.getEdges()) {
      final Storefront.Collection collection = collectionEdge.getNode();
      collections.add(new Collection(
        collection.getId().toString(),
        collection.getTitle(),
        collection.getDescription(),
        collection.getImage() != null ? collection.getImage().getSrc() : null,
        collectionEdge.getCursor(),
        convertProducts(collection.getProducts())
      ));
    }
    return collections;
  }

  public static List<Product> convertProducts(Storefront.ProductConnection productConnection) {
    List<Product> products = new ArrayList<>();
    for (Storefront.ProductEdge productEdge : productConnection.getEdges()) {
      final Storefront.Product product = productEdge.getNode();
      List<Storefront.ImageEdge> images = product.getImages() != null ? product.getImages().getEdges() : null;
      List<Storefront.ProductVariantEdge> variants = product.getVariants() != null ? product.getVariants().getEdges() : null;
      products.add(new Product(
        product.getId().toString(),
        product.getTitle(),
        Util.reduce(images, (acc, val) -> val.getNode().getSrc(), null),
        Util.reduce(variants, (acc, val) -> val.getNode().getPrice(), BigDecimal.ZERO),
        productEdge.getCursor()
      ));
    }
    return products;
  }

  public static ProductDetail convertProductDetail(final Storefront.Product product) {
    return new ProductDetail(
      product.getId().toString(),
      product.getTitle(),
      product.getDescriptionHtml(),
      product.getTags(),
      mapItems(product.getImages().getEdges(), imageEdge -> imageEdge.getNode().getSrc()),
      mapItems(product.getOptions(), option -> new ProductDetail.Option(option.getId().toString(), option.getName(), option.getValues())),
      mapItems(product.getVariants().getEdges(), variantEdge -> {
        List<ProductDetail.SelectedOption> selectedOptions = mapItems(variantEdge.getNode().getSelectedOptions(), selectedOption ->
          new ProductDetail.SelectedOption(selectedOption.getName(), selectedOption.getValue()));
        return new ProductDetail.Variant(variantEdge.getNode().getId().toString(), variantEdge.getNode().getTitle(),
          (variantEdge.getNode().getAvailableForSale() == null || Boolean.TRUE.equals(variantEdge.getNode().getAvailableForSale())),
          selectedOptions, variantEdge.getNode().getPrice());
      })
    );
  }

  private Converter() {
  }
}
