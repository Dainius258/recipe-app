package com.kvk.recipeapp.topBarFragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kvk.recipeapp.R
import com.kvk.recipeapp.adapters.TagAdapter
import com.kvk.recipeapp.utils.PreferenceManager
import com.kvk.recipeapp.utils.RetroFitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class FilterSearchTopFragment : Fragment() {
    private val preferenceManager: PreferenceManager by lazy {
        PreferenceManager(requireContext())
    }
    interface OnSearchQueryListener {
        fun onSearchQuery(query: String)
        fun onSearchTextChange(query: String)
        fun onTagFilterSearch(recipeIds: List<Int>)
        fun onTagFilterSearchEmpty()
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
        val filterButton: ImageButton = rootView.findViewById(R.id.imageButtonFilter)

        filterButton.setOnClickListener{
            openTagsDialog{ recipeIds ->
                if(recipeIds.isNotEmpty()) {
                    recipeIds?.let { searchQueryListener?.onTagFilterSearch(recipeIds) }
                } else {
                    searchQueryListener?.onTagFilterSearchEmpty()
                }

            }
            //Log.d("QUERY_CHECK", getRecipeIdsByTagIds(listOf(40,44)).toString())
        }

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

    private fun getRecipeIdsByTagIds(tagIds: List<Int>, callback: (List<Int>?) -> Unit) {
        val tagIdsString = tagIds.joinToString(",")
        val call = RetroFitInstance.api.getRecipesByTags(tagIdsString)
        call.enqueue(object : Callback<List<Int>> {
            override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>) {
                if (response.isSuccessful) {
                    val recipeIds = response.body()
                    Log.d("CHECK_QUERY", "SUCCESS")
                    callback(recipeIds)
                } else {
                    Log.d("CHECK_QUERY", "NOT SUCCESS")
                    callback(null)
                }
            }
            override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                Log.e("CHECK_QUERY", "FAILURE")
                callback(null)
            }
        })
    }


    private fun openTagsDialog(callback: (List<Int>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO)  {
            val response = try {
                RetroFitInstance.api.getAllTags()
            } catch (e: IOException) {
                Log.e("Network", "IOException: ${e.message}")
                return@launch
            } catch (e: HttpException) {
                Log.e("Network", "HttpException: ${e.message}")
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val tagList = response.body()!!
                    val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_tags_layout, null)
                    val recyclerView = dialogLayout.findViewById<RecyclerView>(R.id.dialogRecyclerView)
                    recyclerView.layoutManager = GridLayoutManager(context, 3)
                    val adapter = TagAdapter(tagList, preferenceManager)
                    recyclerView.adapter = adapter
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder
                        .setView(dialogLayout)
                        .setTitle("Select tag filter")
                        .setPositiveButton("Filter") { dialog, _ ->
                            val stringSet: Set<String>? = preferenceManager.getSelectedTagIds()
                            if (stringSet != null) {
                                val integerList: List<Int> = stringSet!!.map { it.toInt() }
                                getRecipeIdsByTagIds(integerList) { recipeIds ->
                                    if (recipeIds != null) {
                                        callback(recipeIds)
                                    } else {
                                        callback(emptyList())
                                    }
                                }
                            }
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") {dialog, _ ->
                            dialog.dismiss()
                            adapter.clearSelectedTags()
                        }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            } else {
                Log.e("Network", "Response not successful")
            }
        }
    }
}