package com.higo.service;

import com.github.pagehelper.PageInfo;
import com.higo.model.pojo.Category;
import com.higo.model.pojo.Product;
import com.higo.model.request.AddCategoryReq;
import com.higo.model.request.AddProductReq;
import com.higo.model.request.ProductListReq;
import com.higo.model.vo.CategoryVO;
import java.util.List;

/**
 * 描述：     商品Service
 */
public interface ProductService {

    void add(AddProductReq addProductReq);

    void update(Product updateProduct);

    void delete(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    Product detail(Integer id);

    PageInfo list(ProductListReq productListReq);
}
