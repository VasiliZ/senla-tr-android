package com.github.rtyvz.senla.tr.myapplication.tester

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD,AnnotationTarget.PROPERTY)
annotation class TesterAttribute(val info: String)