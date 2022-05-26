package com.higo.common;

/**
 * Created by Intellij IDEA.
 *
 * @author ACwin YANG
 * @date 2022/05/20
 */

import java.util.Set;
import com.higo.exception.ImoocMallException;
import com.higo.exception.ImoocMallExceptionEnum;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 描述：     常量值
 */

@Component
public class Constant {
    public static final String HIGO_USER = "higo_user";
    public static final String SALT = "8svbsvjkweDF,.03[";


    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    public interface ProductListOrderBy {

        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc", "price asc");
    }

    public interface SaleStatus {

        int NOT_SALE = 0;//商品下架状态
        int SALE = 1;//商品上架状态
    }

    public interface Cart {

        int UN_CHECKED = 0;//购物车未选中状态
        int CHECKED = 1;//购物车选中状态
    }

    public enum OrderStatusEnum {
        CANCELED(0, "사용자가 이미 취소했습니다"),
        NOT_PAID(10, "아직 결제하지 않습니다"),
        PAID(20, "이미 결제했습니다."),
        DELIVERED(30, "발송되었습니다"),
        FINISHED(40, "거래완료");

        private String value;
        private int code;

        OrderStatusEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ENUM);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }


}