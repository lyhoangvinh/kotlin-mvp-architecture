package com.dev.lyhoangvinh.mvparchitecture.utils.progressbutton

import android.animation.Animator
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.graphics.drawable.Animatable
import android.view.View
import android.widget.TextView
import java.lang.ref.WeakReference
import java.util.*

internal val attachedViews = WeakHashMap<TextView, TextChangeAnimatorParams>()
internal val activeAnimations = WeakHashMap<TextView, MutableList<Animator>>()
internal val activeViews = WeakHashMap<TextView, DrawableViewData>()

/**
 * Binds your buttons to component lifecycle for the correct data recycling
 * This method is required for all buttons that show progress/drawable
 * @receiver lifecycle owner to which to bin (eg. Activity, Fragment or other)
 * @param button button instance to bind
 */
fun LifecycleOwner.bindProgressButton(button: TextView) {
    lifecycle.addObserver(ProgressButtonHolder(WeakReference(button)))
}

fun TextView.cleanUpDrawable() {
    if (activeViews.containsKey(this)) {
        activeViews[this]?.drawable?.apply {
            if (this is Animatable) {
                stop()
            }
            callback = null
        }
        activeViews.remove(this)
    }
}

private class ProgressButtonHolder(private val textView: WeakReference<TextView>) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        textView.get()?.let {
            it.cancelAnimations()
            it.cleanUpDrawable()
            it.removeTextAnimationAttachViewListener()
            it.removeDrawableAttachViewListener()
            attachedViews.remove(it)
        }
    }
}

internal fun TextView.addTextAnimationAttachViewListener() {
    addOnAttachStateChangeListener(textAnimationsAttachListener)
}

internal fun TextView.removeTextAnimationAttachViewListener() {
    removeOnAttachStateChangeListener(textAnimationsAttachListener)
}

internal fun TextView.addDrawableAttachViewListener() {
    addOnAttachStateChangeListener(drawablesAttachListener)
}

private fun TextView.removeDrawableAttachViewListener() {
    removeOnAttachStateChangeListener(drawablesAttachListener)
}

private val textAnimationsAttachListener = object : View.OnAttachStateChangeListener {
    override fun onViewDetachedFromWindow(v: View) {
        if (attachedViews.containsKey(v)) {
            (v as TextView).cancelAnimations()
        }
    }

    override fun onViewAttachedToWindow(v: View?) {
    }
}

private val drawablesAttachListener = object : View.OnAttachStateChangeListener {
    override fun onViewDetachedFromWindow(v: View?) {
        if (activeViews.containsKey(v)) {
            activeViews[v]?.drawable?.apply {
                if (this is Animatable) {
                    stop()
                }
            }
        }
    }

    override fun onViewAttachedToWindow(v: View?) {
        if (activeViews.containsKey(v)) {
            activeViews[v]?.drawable?.apply {
                if (this is Animatable) {
                    start()
                }
            }
        }
    }
}
