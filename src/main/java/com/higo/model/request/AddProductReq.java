package com.higo.model.request;

import java.util.Date;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.mapstruct.MapperConfig;

public class AddProductReq {

    @NotNull(message = "상품 이름은 null이 될 수 없습니다")
    private String name;

    @NotNull(message = "상품 그림은 null이 될 수 없습니다")
    private String image="";

    private String detail;

    @NotNull(message = "상품 분류가 null이 될 수 없습니다")
    private Integer categoryId;

    @NotNull(message = "상품 가격은 null이 될 수 없다")
    @Min(value = 1, message = "가격은 1 센트 이하여서는 안 된다")
    private Integer price;

    @NotNull(message = "상품 재고품은 null이 될 수 없습니다")
    @Max(value = 10000, message = "재고가 10000을 넘으면 안 된다")
    private Integer stock;

    private Integer status;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AddProductReq{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", detail='" + detail + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", stock=" + stock +
                ", status=" + status +
                '}';
    }
}