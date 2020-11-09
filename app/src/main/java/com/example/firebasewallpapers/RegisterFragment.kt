package com.example.firebasewallpapers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setup nav Controller
        navController = Navigation.findNavController(view)

        //Check if User Already Loggedin
        if (firebaseAuth.currentUser == null) {
            //Not Logged in, Create new User
            register_text.text = "Buat Akun Baru"
            firebaseAuth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful) {
                    //Account created, go to home
                    register_text.text = "Membuat Akun, Masuk"
                    navController!!.navigate(R.id.action_registerFragment_to_homeFragment)
                } else {
                    //Error
                    register_text.text = "Error : ${it.exception!!.message}"
                }
            }
        } else {
            //Logged in, send back to homepage
            navController!!.navigate(R.id.action_registerFragment_to_homeFragment)
        }
    }
}