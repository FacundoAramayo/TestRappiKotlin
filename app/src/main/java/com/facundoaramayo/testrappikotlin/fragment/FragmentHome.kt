package com.facundoaramayo.testrappikotlin.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.facundoaramayo.testrappikotlin.R

import android.widget.ProgressBar


/**
 * A simple [Fragment] subclass.
 */
class FragmentHome : Fragment() {

    private val lat: Float = 0.toFloat()
    private val lng: Float = 0.toFloat()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }


}
