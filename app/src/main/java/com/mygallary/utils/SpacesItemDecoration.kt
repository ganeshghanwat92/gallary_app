package com.mygallary.utils

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Ganesh on 30/11/20.
 * ganeshghanwat92@gmail.com
 */
class SpacesItemDecoration(private val space : Int) : RecyclerView.ItemDecoration() {

   override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
        outRect.top = space
    }

}