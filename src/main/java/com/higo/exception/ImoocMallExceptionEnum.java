package com.higo.exception;

/**
 * 描述：     异常枚举
 */
public enum ImoocMallExceptionEnum {
    NEED_USER_NAME(10001, "username cannot be empty"),
    NEED_PASSWORD(10002, "password cannot be empty"),
    PASSWORD_TOO_SHORT(10003, " password cannot be less than 8 bits long"),
    NAME_EXISTED(10004, "Duplicate names are not allowed"),
    INSERT_FAILED(10005, "Insert failed, please try again"),
    WRONG_PASSWORD(10006, "Wrong password"),
    NEED_LOGIN(10007, "User not logged in"),
    UPDATE_FAILED(10008, "Update failed"),
    NEED_ADMIN(10009, "No administrator privileges"),
    PARA_NOT_NULL(10010, "parameter cannot be null"),
    CREATE_FAILED(10011, "Add failed"),
    REQUEST_PARAM_ERROR(10012, "Parameter error"),
    DELETE_FAILED(10013, "removal failed"),
    MKDIR_FAILED(10014, "Folder creation failure"),
    UPLOAD_FAILED(10015, "Image upload failed"),
    NOT_SALE(10016, "goods are not available for sale"),
    NOT_ENOUGH(10017, "Shortage of goods in stock"),
    CART_EMPTY(10018, "shopping cart is empty with selected items"),
    NO_ENUM(10019, "No enumeration found"),
    NO_ORDER(10020, "Order does not exist"),
    NOT_YOUR_ORDER(10021, "Not your order"),
    WRONG_ORDER_STATUS(10022, "Order status error"),
    SYSTEM_ERROR(20000, "System error, please check the specific error message from the console or log");
    /**
     * 异常码
     */
    Integer code;
    /**
     * 异常信息
     */
    String msg;

    ImoocMallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}


