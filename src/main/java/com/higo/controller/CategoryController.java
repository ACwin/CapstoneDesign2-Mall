package com.higo.controller;

import com.github.pagehelper.PageInfo;

import com.higo.common.ApiRestResponse;
import com.higo.common.Constant;
import com.higo.exception.ImoocMallExceptionEnum;
import com.higo.model.pojo.Category;
import com.higo.model.pojo.User;
import com.higo.model.request.AddCategoryReq;
import com.higo.model.request.UpdateCategoryReq;
import com.higo.model.vo.CategoryVO;
import com.higo.service.CategoryService;
import com.higo.service.UserService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 描述：     目录Controller
 */
@Controller
public class CategoryController {

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    /**
     * 后台添加目录
     */
    @ApiOperation("백 엔드 디렉토리 추가")
    @PostMapping("admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session,
            @Valid @RequestBody AddCategoryReq addCategoryReq) {
        User currentUser = (User) session.getAttribute(Constant.HIGO_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
        }
        //校验是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (adminRole) {
            //是管理员，执行操作
            categoryService.add(addCategoryReq);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
        }
    }

    @ApiOperation("백 엔드 업데이트 디렉토리")
    @PostMapping("admin/category/update")
    @ResponseBody
    public ApiRestResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq,
            HttpSession session) {
        User currentUser = (User) session.getAttribute(Constant.HIGO_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
        }
        //校验是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (adminRole) {
            //是管理员，执行操作
            Category category = new Category();
            BeanUtils.copyProperties(updateCategoryReq, category);
            categoryService.update(category);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
        }
    }

    @ApiOperation("백 엔드 디렉토리 삭제")
    @PostMapping("admin/category/delete")
    @ResponseBody
    public ApiRestResponse deleteCategory(@RequestParam Integer id) {
        categoryService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("백 엔드 디렉터리 목록")
    @GetMapping("admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("프런트 디렉터리 목록")
    @GetMapping("category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForCustomer() {
        List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(0);
        return ApiRestResponse.success(categoryVOS);
    }
}
