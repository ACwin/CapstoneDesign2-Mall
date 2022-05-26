package com.higo.exception;

/**
 * 描述：     异常枚举
 */
public enum ImoocMallExceptionEnum {
    NEED_USER_NAME(10001, "사용자 이름을 비워 둘 수 없습니다"),
    NEED_PASSWORD(10002, "암호가 비어 있으면 안 됩니다"),
    PASSWORD_TOO_SHORT(10003, "비밀번호 길이가 8자리보다 작을 수 없습니다"),
    NAME_EXISTED(10004, "중복된 이름은 허용되지 않습니다"),
    INSERT_FAILED(10005, "삽입에 실패하였습니다. 다시 시도하십시오"),
    WRONG_PASSWORD(10006, "비밀번호가 틀렸습니다"),
    NEED_LOGIN(10007, "사용자가 로그인하지 않았습니다"),
    UPDATE_FAILED(10008, "업데이트 실패"),
    NEED_ADMIN(10009, "관리자 권한 없습니다"),
    PARA_NOT_NULL(10010, "인자가 비어 있을 수 없습니다"),
    CREATE_FAILED(10011, "추가 실패"),
    REQUEST_PARAM_ERROR(10012, "인자 오류"),
    DELETE_FAILED(10013, "삭제 실패"),
    MKDIR_FAILED(10014, "폴더 생성 실패"),
    UPLOAD_FAILED(10015, "이미지 업로드 실패"),
    NOT_SALE(10016, "상품 상태 판매 불가"),
    NOT_ENOUGH(10017, "장바구니에서 이미 선택된 상품이 비어 있습니다"),
    CART_EMPTY(10018, "장바구니에서 이미 낙점한 상품은 비어 있습니다"),
    NO_ENUM(10019, "대응되는 매거를 찾지 못습니다."),
    NO_ORDER(10020, "주문이 존재하지 않습니다"),
    NOT_YOUR_ORDER(10021, "주문서는 너의 것이 아니다"),
    WRONG_ORDER_STATUS(10022, "오더 상태가 맞지 않습니다"),
    SYSTEM_ERROR(20000, "시스템 이상, 콘솔이나 로그에서 자세한 오류 정보를 확인하십시오");
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


