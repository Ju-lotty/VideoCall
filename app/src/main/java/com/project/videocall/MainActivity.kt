package com.project.videocall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.videocall.adapter.UserAdapter
import com.project.videocall.dto.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerviewinit()
        getUserList()
        videoCall()
    }
    private fun recyclerviewinit() {
        userRecyclerview = findViewById(R.id.userRecyclerView)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)
        userArrayList = arrayListOf()
    }

    private fun getUserList() {
        lifecycleScope.launch {
            val await = Firebase.database.reference.child("Users").get().await()
            for(i in await.children) {
                val email = i.child("email").value.toString()
                userArrayList.add(User(email))
            }
            userRecyclerview.adapter = UserAdapter(userArrayList)
        }
    }

    private fun videoCall() {
        val joinText = findViewById<EditText>(R.id.roomEditTextView)
        val serverUrl: URL
        try {
            serverUrl = URL("https://meet.jit.si")
            val options = JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverUrl)
                .build()
            JitsiMeet.setDefaultConferenceOptions(options)
        }
        catch (e: MalformedURLException) {
            Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
        }
        val gotoRoom = findViewById<Button>(R.id.gotoRoomButton)
        gotoRoom.setOnClickListener {
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(joinText.text.toString())
                .build()
            JitsiMeetActivity.launch(this, options)
        }
    }
}

