package com.github.rtyvz.senla.tr.myapplication.tester

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TesterMethod(val description: String, val isInner:Boolean=false)