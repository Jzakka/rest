package com.ll.rest.base.rsData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.rest.boundarycontext.article.controller.ApiV1ArticlesController;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
public class RsData<T> {
    private static final String PROCESSING = "P";
    public static final String EXCEPTION = "E";
    private String resultCode;
    private String msg;
    private T data;

    public static <T> RsData<T> of(String resultCode, String msg, T data) {
        return new RsData<>(resultCode, msg, data);
    }

    public static <T> RsData<T> of(String resultCode, String msg) {
        return new RsData<>(resultCode, msg, null);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return resultCode.startsWith("S-");
    }

    @JsonIgnore
    public boolean isFail() {
        return resultCode.startsWith("F-");
    }

    @JsonIgnore
    public boolean isProcessing() {
        return resultCode.startsWith(PROCESSING);
    }

    public RsData<T> then(Function<RsData<T>, RsData<T>> constrain) {
        if (isSuccess() || isProcessing()) {
            return constrain.apply(this);
        }
        return this;
    }

    public RsData<T> catchEx(Function<RsData<T>, RsData<T>> handler) {
        if (this.getResultCode().equals(EXCEPTION)) {
            return handler.apply(this);
        }
        return this;
    }

    public static <T> RsData<T> produce(Class<T> entity) {
        return RsData.of(PROCESSING, "Not Completed");
    }

    public <D> RsData<D> mapToDto(Class<D> dtoClass) {
        try {
            D dto = dtoClass.getConstructor(this.data.getClass()).newInstance(this.data);
            return RsData.of(this.getResultCode(), this.getMsg(), dto);
        } catch (Exception e) {
            return RsData.of("F-500", "예상치 못한 오류가 발생했습니다.");
        }
    }
}
