package ua.hospes.rtm.domain

sealed class InvokeStatus {
    object Started : InvokeStatus()
    object Success : InvokeStatus()
    data class Error(val throwable: Throwable) : InvokeStatus()
}