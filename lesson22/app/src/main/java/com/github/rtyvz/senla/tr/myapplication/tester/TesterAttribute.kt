package com.github.rtyvz.senla.tr.myapplication.tester

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class TesterAttribute(val info: String)