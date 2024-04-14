package com.kvk.recipeapp.topBarFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.kvk.recipeapp.R

class FilterSearchTopFragment : Fragment() {
    interface OnSearchQueryListener {
        fun onSearchQuery(query: String)
        fun onSearchTextChange(query: String)
    }

    private var searchQueryListener: OnSearchQueryListener? = null
    fun setSearchQueryListener(listener: OnSearchQueryListener) {
        searchQueryListener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_filter_searchview_top, container, false)
        val searchView: SearchView = rootView.findViewById(R.id.recipeSearchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchQueryListener?.onSearchQuery(query) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchQueryListener?.onSearchTextChange(newText) }
                return true
            }
        })
        return rootView
    }
}