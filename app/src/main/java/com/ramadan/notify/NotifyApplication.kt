package com.ramadan.notify

import android.app.Application
import com.ramadan.notify.data.repository.NoteRepository
import com.ramadan.notify.data.repository.Repository
import com.ramadan.notify.data.repository.UserRepository
import com.ramadan.notify.ui.viewModel.AuthViewModelFactory
import com.ramadan.notify.ui.viewModel.NoteViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class NotifyApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@NotifyApplication))
        bind() from singleton { Repository() }
        bind() from singleton { UserRepository(instance()) }
        bind() from singleton { NoteRepository(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { NoteViewModelFactory(instance()) }

    }
}