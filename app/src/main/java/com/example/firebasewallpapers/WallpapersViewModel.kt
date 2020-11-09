package com.example.firebasewallpapers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot

class WallpapersViewModel: ViewModel() {

    private val firebaseRepository: FirebaseRepository = FirebaseRepository()

    private val wallpapersList: MutableLiveData<List<WallpapersModel>> by lazy {
        MutableLiveData<List<WallpapersModel>>().also {
            loadWallpapersData()
        }
    }

    fun getWallpapersList(): LiveData<List<WallpapersModel>> {
        return wallpapersList
    }

    fun loadWallpapersData() {
        //Query data from repo
        firebaseRepository.queryWallpapers().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result
                if (result!!.isEmpty) {
                    //No more Results to load, reached at bottom of page
                } else {
                    if (wallpapersList.value == null) {
                        //Loading first page
                        wallpapersList.value = result.toObjects(WallpapersModel::class.java)
                    } else {
                        //Loading next page
                        wallpapersList.value = wallpapersList.value!!.plus(result.toObjects(WallpapersModel::class.java))
                    }

                    //Get the last Document
                    val lastItem: DocumentSnapshot = result.documents[result.size() - 1]
                    firebaseRepository.lastVisible = lastItem
                }
            } else {
                //Error
                Log.d("View_model_Log", "Error : ${it.exception!!.message}")
            }
        }
    }
}