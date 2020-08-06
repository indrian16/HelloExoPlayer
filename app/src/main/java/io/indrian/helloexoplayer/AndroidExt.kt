package io.indrian.helloexoplayer

import android.view.View

fun View.toVisible() {

    this.visibility = View.GONE
}

fun View.toInvisible() {

    this.visibility = View.INVISIBLE
}