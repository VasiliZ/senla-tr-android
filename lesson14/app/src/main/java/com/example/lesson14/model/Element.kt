package com.example.lesson14.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Externalizable
import java.io.ObjectInput
import java.io.ObjectOutput

@Parcelize
class Element(var countValue: Int = 0, var title: String = "") : Externalizable, Parcelable {

    override fun readExternal(input: ObjectInput?) {
        countValue = input?.readInt() ?: 0
        title = input?.readUTF() ?: ""
    }

    override fun writeExternal(out: ObjectOutput?) {
        out?.writeInt(countValue)
        out?.writeUTF(title)
    }
}