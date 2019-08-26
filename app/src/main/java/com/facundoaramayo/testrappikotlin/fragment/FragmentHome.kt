package com.facundoaramayo.testrappikotlin.fragment

<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
=======

>>>>>>> Stashed changes
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< Updated upstream
<<<<<<< Updated upstream

import com.facundoaramayo.testrappikotlin.R


=======
=======
>>>>>>> Stashed changes
import android.widget.ProgressBar

import com.facundoaramayo.testrappikotlin.R

<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
/**
 * A simple [Fragment] subclass.
 */
class FragmentHome : Fragment() {

<<<<<<< Updated upstream
<<<<<<< Updated upstream
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
=======
=======
>>>>>>> Stashed changes
    private val lat: Float = 0.toFloat()
    private val lng: Float = 0.toFloat()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes

        return inflater.inflate(R.layout.fragment_home, container, false)
    }


}// Required empty public constructor
