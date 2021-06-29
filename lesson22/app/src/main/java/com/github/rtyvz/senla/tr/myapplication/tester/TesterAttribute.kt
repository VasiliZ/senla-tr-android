package com.github.rtyvz.senla.tr.myapplication.tester

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class TesterAttribute(val info: String)