package com.heyy.com.mutetask.common;

/**
 * Created by mo on 16-11-6.
 */

public interface ICallback<T> {
    void onResult(Throwable error,T data);
}
