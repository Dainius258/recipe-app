package com.kvk.recipeapp.topBarFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.kvk.recipeapp.R

class FilterSearchTopFragment : Fragment() {

    private var searchQuerySubmitListener: OnSearchQuerySubmitListener? = null
    interface OnSearchQuerySubmitListener {
        fun onSearchQuerySubmit(query: String)
    }

    fun setSearchQuerySubmitListener(listener: OnSearchQuerySubmitListener) {
        searchQuerySubmitListener = listener
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
                query?.let { searchQuerySubmitListener?.onSearchQuerySubmit(query) }
                Log.d("SEARCH_QUERY", "WORKS")
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                // This method is called when the query text changes (e.g., user types)
                // You might handle filtering the recipes here
                Log.d("SEARCH_QUERY", "WORKS")
                return true
            }
        })
        return rootView
    }
}