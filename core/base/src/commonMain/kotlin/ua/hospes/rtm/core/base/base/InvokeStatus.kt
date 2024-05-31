package ua.hospes.rtm.core.base.base

sealed class InvokeStatus {
    data object Started : InvokeStatus()
    data object Success : InvokeStatus()
    data class Error(val throwable: Throwable) : InvokeStatus()
}