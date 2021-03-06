package team10.ldcc.com.smartconsumer.Data;

import java.io.Serializable;

/**
 * Created by Gelgel on 2017. 7. 27..
 */
public class Product implements Serializable{
    private String cart_code;
    private String product_code;
    private String product_name;
    private String product_info;
    private String product_price;
    private String product_count;
    private String product_image;
    private String productDetail_code;
    private String productBase_code;
    private String productBase_weight;
    private String product_delivery;
    private Boolean product_check;

    @Override
    public String toString() {
        return "Product{" +
                "product_code='" + product_code + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_info='" + product_info + '\'' +
                ", product_price='" + product_price + '\'' +
                ", product_count='" + product_count + '\'' +
                ", product_image='" + product_image + '\'' +
                ", productDetail_code='" + productDetail_code + '\'' +
                ", productBase_code='" + productBase_code + '\'' +
                ", productBase_weight='" + productBase_weight + '\'' +
                ", product_delivery='" + product_delivery + '\'' +
                '}';
    }


    public String getCart_code() {
        return cart_code;
    }

    public void setCart_code(String cart_code) {
        this.cart_code = cart_code;
    }

    public Boolean getProduct_check() {
        return product_check;
    }

    public void setProduct_check(Boolean product_check) {
        this.product_check = product_check;
    }

    public String getProduct_delivery() {
        return product_delivery;
    }

    public void setProduct_delivery(String product_delivery) {
        this.product_delivery = product_delivery;
    }

    public String getProductBase_weight() {
        return productBase_weight;
    }

    public void setProductBase_weight(String productBase_weight) {
        this.productBase_weight = productBase_weight;
    }

    public String getProductDetail_code() {
        return productDetail_code;
    }

    public void setProductDetail_code(String productDetail_code) {
        this.productDetail_code = productDetail_code;
    }

    public String getProductBase_code() {
        return productBase_code;
    }

    public void setProductBase_code(String productBase_code) {
        this.productBase_code = productBase_code;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_info() {
        return product_info;
    }

    public void setProduct_info(String product_info) {
        this.product_info = product_info;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_count() {
        return product_count;
    }

    public void setProduct_count(String product_count) {
        this.product_count = product_count;
    }
}
