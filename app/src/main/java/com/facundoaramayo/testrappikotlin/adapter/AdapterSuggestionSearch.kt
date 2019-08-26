package com.facundoaramayo.testrappikotlin.adapter

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.google.gson.Gson

import java.io.Serializable
import java.util.ArrayList
import java.util.Collections

import com.facundoaramayo.testrappikotlin.R

class AdapterSuggestionSearch(context: Context) : RecyclerView.Adapter<AdapterSuggestionSearch.ViewHolder>() {

    private var items: List<String> = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null
    private val prefs: SharedPreferences

    private val searchHistory: MutableList<String>
        get() {
            val json = prefs.getString(SEARCH_HISTORY_KEY, "")
            if (json == "") return ArrayList()
            val searchObject = Gson().fromJson(json, SearchObject::class.java)
            return searchObject.items
        }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var title: TextView
        var lyt_parent: LinearLayout

        init {
            title = v.findViewById(R.id.title)
            lyt_parent = v.findViewById(R.id.lyt_parent)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    init {
        prefs = context.getSharedPreferences("PREF_RECENT_SEARCH", Context.MODE_PRIVATE)
        this.items = searchHistory
        Collections.reverse(this.items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_suggestion, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = items[position]
        holder.title.text = p
        holder.lyt_parent.setOnClickListener { v -> onItemClickListener!!.onItemClick(v, p, position) }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, viewModel: String, pos: Int)
    }

    fun refreshItems() {
        this.items = searchHistory
        Collections.reverse(this.items)
        notifyDataSetChanged()
    }

    private inner class SearchObject(items: MutableList<String>) : Serializable {

        var items: MutableList<String> = ArrayList()

        init {
            this.items = items
        }
    }

    /**
     * To save last state request
     */
    fun addSearchHistory(s: String) {
        val searchObject = SearchObject(searchHistory)
        searchObject.items.remove(s)
        searchObject.items.add(s)
        if (searchObject.items.size > MAX_HISTORY_ITEMS) searchObject.items.removeAt(0)
        val json = Gson().toJson(searchObject, SearchObject::class.java)
        prefs.edit().putString(SEARCH_HISTORY_KEY, json).apply()
    }

    companion object {

        private val SEARCH_HISTORY_KEY = "_SEARCH_HISTORY_KEY"
        private val MAX_HISTORY_ITEMS = 5
    }
}