package com.github.rtyvz.senla.tr.myapplication.tester

import android.util.Log

class Tester(
    @field:TesterAttribute("Some attribute")
    val param: String
) {

    protected val protParam: Int = 42

    @TesterMethod(description = "Some public method")
    public fun doPublic() {
        Log.e("TAG", "protected: $param")
    }

    protected fun doProtected() {
        Log.e("TAG", "protected: $param (${protParam})")
    }

    @TesterMethod(description = "Some private method", isInner = true)
    private fun doPrivate() {
        Log.e("TAG", "private: $param")
    }
}