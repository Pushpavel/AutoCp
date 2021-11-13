package com.github.pushpavel.autocp.common.helpers

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

fun notifyErr(title: String, content: String) {
    Notifications.Bus.notify(Notification("autocp-notifications-group", title, content, NotificationType.ERROR))
}

fun notifyInfo(title: String, content: String) {
    Notifications.Bus.notify(Notification("autocp-notifications-group", title, content, NotificationType.INFORMATION))
}

fun notifyWarn(title: String, content: String) {
    Notifications.Bus.notify(Notification("autocp-notifications-group", title, content, NotificationType.WARNING))
}