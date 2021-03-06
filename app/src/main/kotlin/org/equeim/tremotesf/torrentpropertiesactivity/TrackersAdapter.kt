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

package org.equeim.tremotesf.torrentpropertiesactivity

import java.text.Collator
import java.util.Comparator

import android.app.Dialog
import android.os.Bundle

import android.text.InputType
import android.text.format.DateUtils

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.View

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.DialogFragment
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView

import com.amjjd.alphanum.AlphanumericComparator

import org.equeim.libtremotesf.Torrent
import org.equeim.libtremotesf.Tracker
import org.equeim.tremotesf.R
import org.equeim.tremotesf.Rpc
import org.equeim.tremotesf.Selector
import org.equeim.tremotesf.utils.createTextFieldDialog

import kotlinx.android.synthetic.main.text_field_dialog.*
import kotlinx.android.synthetic.main.tracker_list_item.view.*


class TrackersAdapterItem(rpcTracker: Tracker) {
    val id = rpcTracker.id()
    val announce: String = rpcTracker.announce()

    var status = 0
    lateinit var errorMessage: String
    var peers = 0
    var nextUpdate = 0

    init {
        update(rpcTracker)
    }

    fun update(rpcTracker: Tracker) {
        status = rpcTracker.status()
        errorMessage = rpcTracker.errorMessage()
        peers = rpcTracker.peers()
        nextUpdate = rpcTracker.nextUpdate()
    }
}

class TrackersAdapter(private val activity: TorrentPropertiesActivity) : RecyclerView.Adapter<TrackersAdapter.ViewHolder>() {
    private var torrent: Torrent? = null
    private val trackers = mutableListOf<TrackersAdapterItem>()
    private val comparator = object : Comparator<TrackersAdapterItem> {
        private val stringComparator = AlphanumericComparator(Collator.getInstance())
        override fun compare(o1: TrackersAdapterItem, o2: TrackersAdapterItem) = stringComparator.compare(o1.announce,
                                                                                                          o2.announce)
    }

    val selector = Selector(activity,
                            ActionModeCallback(),
                            this,
                            trackers,
                            TrackersAdapterItem::id,
                            R.plurals.trackers_selected)

    override fun getItemCount(): Int {
        return trackers.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(selector,
                          LayoutInflater.from(parent.context).inflate(R.layout.tracker_list_item,
                                                                      parent,
                                                                      false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tracker = trackers[position]

        holder.item = tracker
        holder.nameTextView.text = tracker.announce
        holder.statusTextView.text = when (tracker.status) {
            Tracker.Status.Inactive -> activity.getString(R.string.tracker_inactive)
            Tracker.Status.Active -> activity.getString(R.string.tracker_active)
            Tracker.Status.Queued -> activity.getString(R.string.tracker_queued)
            Tracker.Status.Updating -> activity.getString(R.string.tracker_updating)
            else -> {
                if (tracker.errorMessage.isEmpty()) {
                    activity.getString(R.string.error)
                } else {
                    activity.getString(R.string.tracker_error, tracker.errorMessage)
                }
            }
        }

        if (tracker.status == Tracker.Status.Error) {
            holder.peersTextView.visibility = View.GONE
        } else {
            holder.peersTextView.text = activity.resources.getQuantityString(R.plurals.peers_plural,
                                                                             tracker.peers,
                                                                             tracker.peers)
            holder.peersTextView.visibility = View.VISIBLE
        }

        if (tracker.nextUpdate < 0) {
            holder.nextUpdateTextView.visibility = View.GONE
        } else {
            holder.nextUpdateTextView.text = activity.getString(R.string.next_update,
                                                                DateUtils.formatElapsedTime(tracker.nextUpdate.toLong()))
            holder.nextUpdateTextView.visibility = View.VISIBLE
        }

        holder.updateSelectedBackground()
    }

    fun update() {
        val torrent = activity.torrent

        if (torrent == null) {
            if (this.torrent == null) {
                return
            }
            this.torrent = null
            val count = itemCount
            trackers.clear()
            notifyItemRangeRemoved(0, count)
            selector.actionMode?.finish()
            return
        }

        this.torrent = torrent

        val rpcTrackers = torrent.trackers()
        val newTrackers = mutableListOf<TrackersAdapterItem>()
        for (i in 0 until rpcTrackers.size()) {
            val rpcTracker = rpcTrackers[i.toInt()]
            val id = rpcTracker.id()
            var tracker = trackers.find { it.id == id }
            if (tracker == null) {
                tracker = TrackersAdapterItem(rpcTracker)
            } else {
                tracker.update(rpcTracker)
            }
            newTrackers.add(tracker)
        }

        run {
            var i = 0
            while (i < trackers.size) {
                if (newTrackers.contains(trackers[i])) {
                    i++
                } else {
                    trackers.removeAt(i)
                    notifyItemRemoved(i)
                }
            }
        }

        for ((i, tracker) in newTrackers.sortedWith(comparator).withIndex()) {
            if (trackers.getOrNull(i) === tracker) {
                notifyItemChanged(i)
            } else {
                val index = trackers.indexOf(tracker)
                if (index == -1) {
                    trackers.add(i, tracker)
                    notifyItemInserted(i)
                } else {
                    trackers.removeAt(index)
                    trackers.add(i, tracker)
                    notifyItemMoved(index, i)
                    notifyItemChanged(i)
                }
            }
        }
    }

    inner class ViewHolder(selector: Selector<TrackersAdapterItem, Int>,
                           itemView: View) : Selector.ViewHolder<TrackersAdapterItem>(selector, itemView) {
        override lateinit var item: TrackersAdapterItem
        val nameTextView = itemView.name_text_view!!
        val statusTextView = itemView.status_text_view!!
        val peersTextView = itemView.peers_text_view!!
        val nextUpdateTextView = itemView.next_update_text_view!!

        override fun onClick(view: View) {
            if (selector.actionMode == null) {
                if (activity.supportFragmentManager.findFragmentByTag(EditTrackerDialogFragment.TAG) == null) {
                    val fragment = EditTrackerDialogFragment()
                    fragment.arguments = bundleOf(EditTrackerDialogFragment.TRACKER_ID to item.id,
                                                  EditTrackerDialogFragment.ANNOUNCE to item.announce)
                    fragment.show(activity.supportFragmentManager, EditTrackerDialogFragment.TAG)
                }
            } else {
                super.onClick(view)
            }
        }
    }

    class EditTrackerDialogFragment : DialogFragment() {
        companion object {
            const val TAG = "org.equeim.tremotesf.TrackerAdapter.EditTrackerDialogFragment"
            const val TRACKER_ID = "trackerId"
            const val ANNOUNCE = "announce"
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val trackerId = arguments?.getInt(TRACKER_ID) ?: -1

            return createTextFieldDialog(requireContext(),
                                         if (trackerId == -1) R.string.add_tracker else R.string.edit_tracker,
                                         null,
                                         getString(R.string.tracker_announce_url),
                                         InputType.TYPE_TEXT_VARIATION_URI,
                                         arguments?.getString(ANNOUNCE)) {
                val torrent = (activity as TorrentPropertiesActivity).torrent
                val textField = dialog.text_field!!
                if (trackerId == -1) {
                    Rpc.instance.torrentAddTracker(torrent, textField.text.toString())
                } else {
                    Rpc.instance.torrentSetTracker(torrent, trackerId, textField.text.toString())
                }
            }
        }
    }

    private inner class ActionModeCallback : Selector.ActionModeCallback<TrackersAdapterItem>() {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.trackers_context_menu, menu)
            return true
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            if (super.onActionItemClicked(mode, item)) {
                return true
            }

            if (item.itemId == R.id.remove) {
                RemoveDialogFragment.create(selector.selectedItems.map(TrackersAdapterItem::id).toIntArray())
                        .show(activity.supportFragmentManager, RemoveDialogFragment.TAG)
                return true
            }

            return false
        }
    }

    class RemoveDialogFragment : DialogFragment() {
        companion object {
            const val TAG = "org.equeim.tremotesf.TorrentsAdapter.RemoveDialogFragment"
            private const val IDS = "ids"

            fun create(ids: IntArray): RemoveDialogFragment {
                val fragment = RemoveDialogFragment()
                fragment.arguments = bundleOf(IDS to ids)
                return fragment
            }
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val ids = arguments!!.getIntArray(IDS)!!

            return AlertDialog.Builder(requireContext())
                    .setMessage(resources.getQuantityString(R.plurals.remove_trackers_message,
                                                            ids.size,
                                                            ids.size))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(R.string.remove) { _, _ ->
                        val activity = this.activity as TorrentPropertiesActivity
                        Rpc.instance.torrentRemoveTrackers(activity.torrent, ids)
                        activity.actionMode?.finish()
                    }
                    .create()
        }
    }
}