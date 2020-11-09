package com.example.firebasewallpapers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(), (WallpapersModel) -> Unit {

    private val firebaseRepository = FirebaseRepository()
    private var navController: NavController? = null

    private var wallpapersList: List<WallpapersModel> = ArrayList()
    private val wallpapersListAdapter: WallpapersListAdapter =
        WallpapersListAdapter(wallpapersList, this)

    private var isLoading: Boolean = true

    private val wallpapersViewModel: WallpapersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Intilalize Action Bar
        (activity as AppCompatActivity).setSupportActionBar(main_toolbar)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar!!.title = "FireBase Wallpapers"

        //Initialize Nav Controller
        navController = Navigation.findNavController(view)

        //Check if user is logged in
        if (firebaseRepository.getUser() == null) {
            //User not logged, go to Register Page
            navController!!.navigate(R.id.action_homeFragment_to_registerFragment)
        }

        //Initialize Recycler View
        wallpapers_list_view.setHasFixedSize(true)
        wallpapers_list_view.layoutManager = GridLayoutManager(context, 3)
        wallpapers_list_view.adapter = wallpapersListAdapter

        //Reached bottom of Recycleview
        wallpapers_list_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //Reched at bottom and not scrolling anymore
                    if (!isLoading) {
                        //Load next page
                        wallpapersViewModel.loadWallpapersData()
                        isLoading = true

                        Log.d("HOME_FRAGMENT_LOG", "Reached Bottom: Loading new Content")
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        wallpapersViewModel.getWallpapersList().observe(viewLifecycleOwner, Observer {
            wallpapersList = it
            wallpapersListAdapter.wallpapersList = wallpapersList
            wallpapersListAdapter.notifyDataSetChanged()

            //Loading Complete
            isLoading = false
        })
    }

    override fun invoke(wallpapers: WallpapersModel) {
        //Clicked on wallpapers Item from the list, Navigation to details fragment
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(wallpapers.image)
        navController!!.navigate(action)
    }


}