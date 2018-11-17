package dk.nodes.template.util

import android.arch.lifecycle.*
import androidx.lifecycle.*
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

class LifecycleObservable<T>() {
    constructor(v : T) : this()  {
        value = v
    }
    var value : T? = null
        set(value) {
            field = value
            changed = true
            notifyObservers()
        }

    private var entryMap : ConcurrentHashMap<Entry, Observer<T>> = ConcurrentHashMap()
    var changed = false

    fun call() {
        value = null
    }

    private fun notifyObservers() {
        if(changed) {
            for (kv in entryMap) {
                if(kv.key.shouldTrigger)
                    kv.value.onChanged(value)
            }
            changed = false
        }
    }

    fun removeObserver(observer: Observer<T>) {
        var entry : Entry? = null
        for(kv in entryMap) {
            if(kv.value == observer) {
                entry = kv.key
                break
            }
        }
        entry?.let { entryMap.remove(it) }
    }

    fun observe(owner : LifecycleOwner, observer: Observer<T>) {
        val entry = Entry()
        owner.lifecycle.addObserver(entry)
        entryMap[entry] = observer
    }

    inner class Entry : LifecycleObserver {
        var shouldTrigger = true


        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onStartLifecycle() {

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onStopLifecycle() {
        }


        // TODO deliver queued changes upon resume
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResumeLifecycle() {
            shouldTrigger = true
        }

        // TODO queue changes while paused
        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPauseLifecycle() {
            shouldTrigger = false
        }

        /*
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreateLifecycle() {

        }
        */

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroyLifecycle() {
            entryMap.remove(this)
        }
    }
}