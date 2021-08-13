package com.example.tekotest2.asyns
 interface IWorkCallback<T> {
    fun run() : T
    fun onFinish(obj : T)
}