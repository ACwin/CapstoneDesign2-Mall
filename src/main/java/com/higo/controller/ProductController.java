package com.higo.controller;

import cn.hutool.json.JSONObject;
import com.github.pagehelper.PageInfo;

import com.higo.common.ApiRestResponse;
import com.higo.model.pojo.Product;
import com.higo.model.request.ProductListReq;
import com.higo.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：     前台商品Controller
 */
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @ApiOperation("product details")
    @PostMapping("product/detail")
    public ApiRestResponse detail(@RequestBody JSONObject json) {
        Integer id = json.getInt("id");
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    @ApiOperation("product details")
    @PostMapping("product/list")
    public ApiRestResponse list(@RequestBody JSONObject json){
        ProductListReq productListReq = new ProductListReq();
        productListReq.setPageNum(json.getInt("pagenum"));
        productListReq.setPageSize(json.getInt("pagesize"));
        PageInfo list = productService.list(productListReq);
        return ApiRestResponse.success(list);
    }
}
