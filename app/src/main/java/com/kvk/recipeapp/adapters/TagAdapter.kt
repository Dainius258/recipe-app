package com.kvk.recipeapp.adapters

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kvk.recipeapp.R
import com.kvk.recipeapp.data.Tag
import com.kvk.recipeapp.utils.PreferenceManager


class TagAdapter(private val tagList: List<Tag>, private val preferenceManager: PreferenceManager) :
    RecyclerView.Adapter<TagAdapter.ViewHolder>() {
    private val selectedTagIds = preferenceManager.getSelectedTagIds()?.map { it.toInt() }?.toMutableSet() ?: mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val tagView = inflater.inflate(R.layout.item_tag, parent, false)
        return ViewHolder(tagView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tag = tagList[position]
        holder.bind(tag)
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val btnTagName: Button = itemView.findViewById(R.id.btnTagName)
        private lateinit var tag: Tag

        init {
            btnTagName.setOnClickListener {
                toggleTagSelection()
            }
        }

        fun bind(tag: Tag) {
            this.tag = tag
            btnTagName.text = tag.tag_name
            updateButtonColor()
        }

        private fun toggleTagSelection() {
            val tagId = tag.id
            if (selectedTagIds.contains(tagId)) {
                selectedTagIds.remove(tagId)
            } else {
                selectedTagIds.add(tagId)
            }
            preferenceManager.saveSelectedTagIds(selectedTagIds.map { it.toString() }.toSet())
            updateButtonColor()
            Log.d("SELECT_TAG", getSelectedTagIds().toString())
        }

        fun updateButtonColor() {
            val isSelected = selectedTagIds.contains(tag.id)
            val color = if (isSelected) R.color.green else R.color.mint
            btnTagName.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, color))
        }
    }
    fun clearSelectedTags() {
        selectedTagIds.clear()
        preferenceManager.saveSelectedTagIds(emptySet())
        notifyDataSetChanged()
    }

    fun getSelectedTagIds(): Set<Int> {
        return selectedTagIds
    }
}

