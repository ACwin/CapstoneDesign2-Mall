package com.higo.controller;

import com.higo.common.ApiRestResponse;
import com.higo.filter.UserFilter;
import com.higo.model.vo.CartVO;
import com.higo.service.CartService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：     购物车Controller
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/list")
    @ApiOperation("쇼핑 카트 목록")
    public ApiRestResponse list() {
        //内部获取用户ID，防止横向越权
        List<CartVO> cartList = cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(cartList);
    }

    @PostMapping("/add")
    @ApiOperation("상품을 쇼핑카트에 추가")
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count) {
        List<CartVO> cartVOList = cartService.add(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/update")
    @ApiOperation("쇼핑 카트 업데이트.")
    public ApiRestResponse update(@RequestParam Integer productId, @RequestParam Integer count) {
        List<CartVO> cartVOList = cartService
                .update(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/delete")
    @ApiOperation("쇼핑 카트 삭제")
    public ApiRestResponse delete(@RequestParam Integer productId) {
        //不能传入userID，cartID，否则可以删除别人的购物车
        List<CartVO> cartVOList = cartService.delete(UserFilter.currentUser.getId(), productId);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/select")
    @ApiOperation("쇼핑 카트에 넣을 물건을 선택/선택하지 않습니다")
    public ApiRestResponse select(@RequestParam Integer productId, @RequestParam Integer selected) {
        //不能传入userID，cartID，否则可以删除别人的购物车
        List<CartVO> cartVOList = cartService
                .selectOrNot(UserFilter.currentUser.getId(), productId, selected);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/selectAll")
    @ApiOperation("쇼핑 카트에서 물건을 선택하지 않음")
    public ApiRestResponse selectAll(@RequestParam Integer selected) {
        //不能传入userID，cartID，否则可以删除别人的购物车
        List<CartVO> cartVOList = cartService
                .selectAllOrNot(UserFilter.currentUser.getId(), selected);
        return ApiRestResponse.success(cartVOList);
    }
}
