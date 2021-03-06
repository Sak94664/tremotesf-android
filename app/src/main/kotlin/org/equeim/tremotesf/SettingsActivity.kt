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

package org.equeim.tremotesf

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.widget.TextView

import androidx.fragment.app.transaction
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceViewHolder


class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Settings.theme)
        supportFragmentManager.transaction { replace(android.R.id.content, Fragment()) }
    }

    class Fragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }
    }
}

class MyPreferenceCategory(context: Context,
                           attrs: AttributeSet?,
                           defStyleAttr: Int,
                           defStyleRes: Int) : PreferenceCategory(context, attrs, defStyleAttr, defStyleRes) {
    constructor(context: Context,
                attrs: AttributeSet?,
                defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.preferenceCategoryStyle)
    constructor(context: Context) : this(context, null)

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val ta = holder.itemView.context.obtainStyledAttributes(intArrayOf(R.attr.colorSecondary))
            (holder.findViewById(android.R.id.title) as? TextView)?.setTextColor(ta.getColor(0, 0))
            ta.recycle()
        }
    }
}