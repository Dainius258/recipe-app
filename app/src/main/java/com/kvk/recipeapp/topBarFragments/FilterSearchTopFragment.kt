package com.kvk.recipeapp.topBarFragments

import android.os.Bundle
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
        fun onSearchQueryNewText(query: String)
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
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchQuerySubmitListener?.onSearchQueryNewText(newText) }
                return true
            }
        })
        return rootView
    }
}