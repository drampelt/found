package com.seversion.found.ui.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.mosby.mvp.MvpFragment
import kotlinx.android.synthetic.main.fragment_track.*
import com.seversion.found.R
import com.seversion.found.data.models.Location
import com.seversion.found.inflate
import com.seversion.found.ui.FragmentLifecycle
import com.seversion.found.ui.activities.SettingsActivity
import com.seversion.found.ui.presenters.TrackPresenter
import com.seversion.found.ui.views.TrackView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.onClick
import org.jetbrains.anko.support.v4.startActivity

/**
 * Created by Daniel on 2016-04-19.
 */

class TrackFragment : MvpFragment<TrackView, TrackPresenter>(), TrackView, FragmentLifecycle, AnkoLogger {

    companion object {
        fun newInstance(): TrackFragment = TrackFragment()
    }

    override fun createPresenter() = TrackPresenter()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) = container?.inflate(R.layout.fragment_track)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tryAgain.onClick { presenter.startTracking() }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.stopTracking()
    }

    override fun onPauseFragment() {
        presenter.stopTracking()
    }

    override fun onResumeFragment() {
        presenter.startTracking()
    }

    override fun setLocation(location: Location) {
        currentLocation.text = location.name
    }

    override fun showError(error: String, showSettings: Boolean) {
        val snackbar = Snackbar.make(root, error, if (showSettings) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG)
        if (showSettings) {
            snackbar.setAction(R.string.main_action_open_settings, {
                startActivity<SettingsActivity>()
            })
        }
        snackbar.show()
    }

    override fun disableTracking() {
        progress.visibility = View.INVISIBLE
        tryAgain.visibility = View.VISIBLE
        currentLocation.text = resources.getString(R.string.track_label_error)
    }

    override fun enableTracking() {
        progress.visibility = View.VISIBLE
        tryAgain.visibility = View.INVISIBLE
        currentLocation.text = resources.getString(R.string.track_label_determining)
    }
}
