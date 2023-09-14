package se.gritacademy.firestorekotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import kotlinx.coroutines.*
import kotlin.system.*

class MainActivity : AppCompatActivity() {
    var baseUrl = "https://mobilt-kotlin-22-default-rtdb.europe-west1.firebasedatabase.app/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
/*
        var url = "https://mobilt-kotlin-22-default-rtdb.europe-west1.firebasedatabase.app/users/first.json"

        val map = hashMapOf (
            //"username " to "Timearchitect",
            "password " to "12345678!",
        )
//, JSONObject(map as Map<*, *>?)


        var rq = Volley.newRequestQueue(this)
        var re = StringRequest(Request.Method.DELETE, url, { response ->
                Log.d("Alrik", " $response success!")
            }, { error ->
                Log.e("Alrik", " $error !!!")
            }
        )

        rq.add(re)*/

        //realtimeDatabasePut("users")
        callOrder2()


        //firestoreTest()

    }

    fun callOrder() = runBlocking<Unit> {//async 
        val asyncPost = async { realtimeDatabasePost("user") }
        val asyncGet = async { realtimeDatabaseGet("user") }
        //realtimeDatabasePut()
        //realtimeDatabasePatch()
        val asyncDelete = async { realtimeDatabaseDelete("") }
        asyncPost.await()
        asyncGet.await()
        asyncDelete.await()
    }

    fun callOrder2() { //async
        val job = GlobalScope.launch {
            val deferred1 = async { realtimeDatabasePost("user") }
            val deferred2 = async { realtimeDatabaseGet("user") }
            val deferred3 = async { realtimeDatabaseDelete("user") }

            deferred1.await()
            deferred2.await()
            deferred3.await()
        }

        runBlocking {
            job.join()
        }
    }

    private fun realtimeDatabaseGet(path: String) {

        var rq: RequestQueue = Volley.newRequestQueue(baseContext)
        var url = "$baseUrl$path.json"


        //Request.Method.GET,
        val sr =
            StringRequest(Request.Method.GET, url,
                { res -> Log.d("Alrik", "GET:${res}") },
                { err -> Log.d("Alrik", err.message.toString()) })
        rq.add(sr)

    }

    private fun realtimeDatabasePost(path: String) {
        // REST API POST
        //genererar en ID som key för jsonObjectet som är ovanpå objektet

        var rq: RequestQueue = Volley.newRequestQueue(this)
        var url = "$baseUrl$path.json"

        val user = hashMapOf(
            "first" to "Josef",
            "middle" to "Frans",
        )

        val sr = JsonObjectRequest(Request.Method.POST, url, JSONObject(user as Map<*, *>?),
            { res -> Log.d("Alrik", "POST:${res}") },
            { err -> Log.d("Alrik", err.message.toString()) }
        )
        rq.add(sr);
    }

    private fun realtimeDatabasePut(path: String) {
        // REST API PUT
        //skriver uppdaterar i objektet om samma key finns & lägger till fält som ej finns

        var rq: RequestQueue = Volley.newRequestQueue(this)
        var url = "$baseUrl$path.json"

        val user = hashMapOf(
            "first" to "Josef",
            "middle" to "Frans",
        )

        val sr = JsonObjectRequest(Request.Method.PUT, url, JSONObject(user as Map<*, *>?),
            { res -> Log.d("Alrik", "PUT:${res}") },
            { err -> Log.d("Alrik", err.message.toString()) }
        )
        rq.add(sr);
    }

    private fun realtimeDatabasePatch(path: String) {
        // REST API PATCH
        //skriver över i objektet om det finns fler fält så raderas dem

        var rq: RequestQueue = Volley.newRequestQueue(this)
        var url = "$baseUrl$path.json"

        val user: Map<String, String> = hashMapOf(
            "test" to "Josef",
            "middle" to "ojsan",
        )

        val sr = JsonObjectRequest(Request.Method.PATCH, url, JSONObject(user as Map<*, *>?),
            { res -> Log.d("Alrik", "PATCH${res}") },
            { err -> Log.d("Alrik", err.message.toString()) }
        )
        rq.add(sr);
    }

    private fun realtimeDatabaseDelete(path: String) {
        // REST API DELETE
        //tar bort objektet och allt under den enligt path

        var rq: RequestQueue = Volley.newRequestQueue(this)
        var url = "$baseUrl$path.json"

        val sr = StringRequest(Request.Method.DELETE, url,
            { res -> Log.d("Alrik", "DELETE:${res} success!!") },
            { err -> Log.d("Alrik", err.message.toString()) }
        )
        rq.add(sr);
    }

    private fun firestoreTest() {
        // library api
        val db = Firebase.firestore

        // Create a new user with a first and last name
        val user = hashMapOf(
            "first" to "Josef",
            "middle" to "Frans",
            "born" to 1815,
        )

        // Add a new document with a generated ID
        db.collection("users6")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("Alrik", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Alrik", "Error adding document", e)
            }
    }


}