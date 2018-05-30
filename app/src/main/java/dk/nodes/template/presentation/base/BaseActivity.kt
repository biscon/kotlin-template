package dk.nodes.template.presentation.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dk.nodes.arch.presentation.base.BaseView
import dk.nodes.nstack.kotlin.inflater.NStackBaseContext

abstract class BaseActivity : AppCompatActivity(), BaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Make sure all of our dependencies get injected
        injectDependencies()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(NStackBaseContext(newBase))
    }

    override fun onStart() {
        super.onStart()

        setupTranslations()
    }

    protected abstract fun injectDependencies()
}