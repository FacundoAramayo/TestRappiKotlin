package com.facundoaramayo.testrappikotlin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.balysv.materialripple.MaterialRippleLayout
import com.nostra13.universalimageloader.core.ImageLoader

import java.util.ArrayList

import com.facundoaramayo.testrappikotlin.R
import com.facundoaramayo.testrappikotlin.data.Constant
import com.facundoaramayo.testrappikotlin.model.Images

class AdapterImageList// Proporcionar un constructor adecuado (depende del tipo de dataset)
(items: List<Images>) : RecyclerView.Adapter<AdapterImageList.ViewHolder>() {

    private var items = ArrayList<Images>()
    private val imgloader = ImageLoader.getInstance()
    private var onItemClickListener: OnItemClickListener? = null

    private val lastPosition = -1

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var image: ImageView
        var lyt_parent: MaterialRippleLayout

        init {
            image = v.findViewById(R.id.image)
            lyt_parent = v.findViewById(R.id.lyt_parent)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    init {
        this.items = items as ArrayList<Images>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = items[position].imageUrl
        imgloader.displayImage(Constant.getURLimgPlace(p), holder.image)
        holder.lyt_parent.setOnClickListener { v ->
            // Give some delay to the ripple to finish the effect
            onItemClickListener!!.onItemClick(v, p, position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, viewModel: String, pos: Int)
    }
}