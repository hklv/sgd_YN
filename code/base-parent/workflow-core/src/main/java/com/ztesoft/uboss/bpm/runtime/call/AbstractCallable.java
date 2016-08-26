package com.ztesoft.uboss.bpm.runtime.call;

import com.ztesoft.uboss.bpm.runtime.client.ServiceProvider;
import com.ztesoft.zsmart.core.i18n.I18NThreadContext;

import java.util.concurrent.Callable;

public abstract class AbstractCallable<T> implements Callable<T> {
    protected ServiceProvider sp;

    protected String localeName;

    public AbstractCallable(ServiceProvider sp) {
        this.sp = sp;
        localeName = I18NThreadContext.getLocaleName();
    }

    public T call() {
        I18NThreadContext.setLocaleName(localeName);
        return doTask();
    }

    public abstract T doTask();
}
