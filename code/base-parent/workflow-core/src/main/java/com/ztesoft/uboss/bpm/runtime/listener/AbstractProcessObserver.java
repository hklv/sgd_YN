package com.ztesoft.uboss.bpm.runtime.listener;

import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.activiti.engine.impl.notification.Notification;
import org.activiti.engine.impl.notification.ProcessObserver;

public abstract class AbstractProcessObserver implements ProcessObserver {

    private static final long serialVersionUID = 1L;

    private static final ZSmartLogger logger = ZSmartLogger.getLogger(AbstractProcessObserver.class);

    public void invoke(Notification notification) {
        try {
            doObserve(notification);
        } catch (Throwable t) {
            logger.error("fail to handle notification[" + notification + "]", t);
        }
    }

    protected abstract void doObserve(Notification notification);
}
