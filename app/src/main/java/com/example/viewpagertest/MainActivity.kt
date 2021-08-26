package com.example.viewpagertest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.marginRight
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL

class MainActivity : AppCompatActivity() {
    val wordList = mutableListOf<String>("TAB1", "TAB2", "TAB3", "TAB4", "TAB5")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<View>(R.id.indicatorView)
        val layoutParams = view.layoutParams

        findViewById<TextView>(R.id.indicatorTextView).text = "1/${wordList.size}"

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.apply {
            adapter = ViewPagerAdapter().apply {
                items = wordList
            }
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 1
            setPageTransformer { page, position ->
                val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
                val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
                val viewPager = page.parent.parent as ViewPager2
                val offset = position * -(2 * offsetPx + pageMarginPx)
                if(viewPager.orientation == ORIENTATION_HORIZONTAL) {
                    if(ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.translationX = -offset
                    } else {
                        page.translationX = offset
                    }
                } else {
                    page.translationY = offset
                }
            }
        }

        viewPager.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                layoutParams.width = viewPager.width / wordList.size
                view.layoutParams = layoutParams

                viewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                findViewById<TextView>(R.id.indicatorTextView).text = "${position + 1}/${wordList.size}"
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                view.x = (position + positionOffset) * view.width
            }
        })
    }

}