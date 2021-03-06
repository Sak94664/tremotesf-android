/*
 * Copyright (C) 2017-2018 Alexey Rochev <equeim@gmail.com>
 *
 * This file is part of Tremotesf.
 *
 * Tremotesf is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Tremotesf is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.equeim.tremotesf.serversettingsactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ListView

import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.ListFragment
import androidx.fragment.app.transaction

import com.google.android.material.snackbar.Snackbar

import org.jetbrains.anko.contentView
import org.jetbrains.anko.design.indefiniteSnackbar

import org.equeim.libtremotesf.BaseRpc
import org.equeim.tremotesf.BaseActivity
import org.equeim.tremotesf.R
import org.equeim.tremotesf.Rpc
import org.equeim.tremotesf.Settings

import kotlinx.android.synthetic.main.server_settings_placeholder_fragment.*


class ServerSettingsActivity : BaseActivity() {
    private lateinit var inputManager: InputMethodManager

    private val rpcStatusListener = fun(status: Int) {
        when (status) {
            BaseRpc.Status.Disconnected -> {
                if (supportFragmentManager.findFragmentByTag(PlaceholderFragment.TAG) == null) {
                    supportFragmentManager.popBackStack()
                    supportFragmentManager.transaction {
                        replace(android.R.id.content,
                                PlaceholderFragment(),
                                PlaceholderFragment.TAG)
                    }
                }
            }
            BaseRpc.Status.Connected -> {
                supportFragmentManager.transaction { replace(android.R.id.content, MainFragment()) }
            }
            else -> {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Settings.theme)

        inputManager = getSystemService<InputMethodManager>()!!

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                title = getString(R.string.server_settings)
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.transaction { replace(android.R.id.content, MainFragment()) }
        }

        Rpc.instance.addStatusListener(rpcStatusListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        Rpc.instance.removeStatusListener(rpcStatusListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            return true
        }
        return super.onSupportNavigateUp()
    }

    fun hideKeyboard() {
        currentFocus?.let { inputManager.hideSoftInputFromWindow(it.windowToken, 0) }
    }

    class PlaceholderFragment : Fragment() {
        companion object {
            const val TAG = "org.equeim.tremotesf.ServerSettingsActivity.PlaceholderFragment"
        }

        private var snackbar: Snackbar? = null

        private var rpcStatusListener: (Int) -> Unit = { status ->
            placeholder.text = Rpc.instance.statusString
            when (status) {
                BaseRpc.Status.Disconnected -> {
                    snackbar = requireActivity().contentView?.indefiniteSnackbar("", getString(R.string.connect)) {
                        snackbar = null
                        Rpc.instance.connect()
                    }
                    progress_bar.visibility = View.GONE
                }
                BaseRpc.Status.Connecting -> {
                    if (snackbar != null) {
                        snackbar!!.dismiss()
                        snackbar = null
                    }
                    progress_bar.visibility = View.VISIBLE
                }
                BaseRpc.Status.Connected -> {
                }
            }
        }

        override fun onCreateView(inflater: LayoutInflater,
                                  container: ViewGroup?,
                                  savedInstanceState: Bundle?): View {
            return inflater.inflate(R.layout.server_settings_placeholder_fragment,
                                    container,
                                    false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            rpcStatusListener(Rpc.instance.status())
            Rpc.instance.addStatusListener(rpcStatusListener)
        }

        override fun onDestroyView() {
            super.onDestroyView()
            Rpc.instance.removeStatusListener(rpcStatusListener)
            snackbar = null
        }
    }

    class MainFragment : ListFragment() {
        private lateinit var items: Array<String>

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            items = resources.getStringArray(R.array.server_settings_items)
            listAdapter = ArrayAdapter(requireContext(),
                                       R.layout.server_settings_activity_main_fragment_list_item,
                                       items)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            listView.divider = null
            listView.setSelector(android.R.color.transparent)
        }

        override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
            val fragment = when (position) {
                0 -> DownloadingFragment()
                1 -> SeedingFragment()
                2 -> QueueFragment()
                3 -> SpeedFragment()
                4 -> NetworkFragment()
                else -> return
            }
            requireFragmentManager().transaction {
                replace(android.R.id.content,
                        fragment,
                        if (position == 3) SpeedFragment.TAG else null)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                addToBackStack(null)
            }
        }
    }
}