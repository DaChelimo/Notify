package com.ramadan.notify.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.ramadan.notify.data.model.WrittenNote
import io.reactivex.Completable

class Repository {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    var status = "null"

    fun logout() = auth.signOut()

    fun currentUser() = auth.currentUser

    fun login(email: String, password: String) = Completable.create { emitter ->
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun register(email: String, password: String) = Completable.create { emitter ->
        val data = hashMapOf("email" to email, "password" to password)
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) {
                    val userID = auth.currentUser?.uid
                    db.collection("user").document(userID!!).set(data)
                    emitter.onComplete()
                } else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun insertNote(data: HashMap<String, Any?>) {
        FirebaseFirestore.getInstance().collection("user").document(auth.currentUser!!.uid)
            .collection("note").document(data["noteID"].toString()).set(data, SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                }
            }.addOnFailureListener {
                status = it.message.toString()
                println(it.toString())
            }
    }


    fun updateNote(data: HashMap<String, Any?>) {
        FirebaseFirestore.getInstance().collection("user").document(auth.currentUser!!.uid)
            .collection("note").document(data["noteID"].toString()).update(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                }
            }.addOnFailureListener { status = it.message.toString() }
    }


    fun deleteNote(ID: String) {
        FirebaseFirestore.getInstance().collection("user").document(auth.currentUser!!.uid)
            .collection("note").document(ID).delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                }
            }.addOnFailureListener { status = it.message.toString() }

    }


    fun fetchNotes(): MutableLiveData<MutableList<WrittenNote>> {
        val mutableData = MutableLiveData<MutableList<WrittenNote>>()
        FirebaseFirestore.getInstance().collection("user").document(auth.currentUser!!.uid)
            .collection("note").orderBy("noteID", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { result ->
                val dataList: MutableList<WrittenNote> = mutableListOf<WrittenNote>()
                for (document: QueryDocumentSnapshot in result) {
                    val noteID: String? = document.getString("noteID")
                    val noteDate: String? = document.getString("noteDate")
                    val noteName: String? = document.getString("noteName")
                    val noteContent: String? = document.getString("noteContent")
                    val noteColor: Long? = document.getLong("noteColor")
                    val writtenNote = WrittenNote(
                        noteID!!, noteDate!!, noteName!!,
                        noteContent!!, noteColor!!.toInt()
                    )
                    dataList.add(writtenNote)
                }
                mutableData.value = dataList
            }.addOnFailureListener { e -> println("Error!! $e") }
        return mutableData
    }

    fun fetchNote(ID: String): MutableLiveData<WrittenNote> {
        val mutableData = MutableLiveData<WrittenNote>()
        FirebaseFirestore.getInstance().collection("user").document(auth.currentUser!!.uid)
            .collection("note").document(ID).get()
            .addOnSuccessListener { document ->
                val noteID: String? = document.getString("noteID")
                val noteDate: String? = document.getString("noteDate")
                val noteName: String? = document.getString("noteName")
                val noteContent: String? = document.getString("noteContent")
                val noteColor: Long? = document.getLong("noteColor")
                mutableData.value = WrittenNote(
                    noteID!!, noteDate!!, noteName!!,
                    noteContent!!, noteColor!!.toInt()
                )
            }.addOnFailureListener { e -> println("Error!! $e") }
        return mutableData
    }


}