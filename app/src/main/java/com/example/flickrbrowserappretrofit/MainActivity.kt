package com.example.flickrbrowserappretrofit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class MainActivity : AppCompatActivity() {
    private lateinit var images: ArrayList<Image>
    private lateinit var rvMain: RecyclerView
    private lateinit var rvAdapter: RV
    private lateinit var llBottom: LinearLayout
    private lateinit var etSearch: EditText
    private lateinit var btSearch: Button
    private lateinit var ivMain: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        images = arrayListOf()
        rvMain = findViewById(R.id.rvMain)
        rvAdapter = RV(this, images)
        rvMain.adapter = rvAdapter
        rvMain.layoutManager = LinearLayoutManager(this)
        llBottom = findViewById(R.id.llBottom)
        etSearch = findViewById(R.id.etSearch)
        btSearch = findViewById(R.id.btSearch)
        btSearch.setOnClickListener {
            images.clear()
            if (etSearch.text.isNotEmpty()) {
                retrofit()
            } else {
                Toast.makeText(this, "Search field is empty", Toast.LENGTH_LONG).show()
            } }

        ivMain = findViewById(R.id.ivMain)
        ivMain.setOnClickListener { closeImg() }
    }

    fun retrofit(){

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {

                apiInterface.doGetListResources(etSearch.text.toString())
                    ?.enqueue(object : Callback<PhotoData> {
                        override fun onResponse(
                            call: Call<PhotoData>,
                            response: Response<PhotoData>
                        ) {

                            Log.d("TAG", response.code().toString() + "")

                            val response = response.body()!!.photos.photo
                            for (index in response!!) {

                                val photoLink = "https://farm${index.farm}.staticflickr.com/${index.server}/${index.id}_${index.secret}.jpg"
                                images.add(Image(index.title, photoLink))

                            }
                                Log.d("intfooooooo",images.toString())
                            rvAdapter.notifyDataSetChanged()
                        }
                        override fun onFailure(call: Call<PhotoData>, t: Throwable) {

                            Log.d("err", t.message.toString())
                            call.cancel()
                        }

                    })
             }
    }

     /*    private fun requestAPI(){
        CoroutineScope(IO).launch {
            val data = async { getPhotos() }.await()
            if(data.isNotEmpty()){
                println(data)
                showPhotos(data)
            }else{
                Toast.makeText(this@MainActivity, "No Images Found", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun getPhotos(): String{
        var response = ""
        try{
            response = URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=cb0cbca5c50568f7e3189b08d8e6a89b&tags=${etSearch.text}&per_page=10&format=json&nojsoncallback=1")

                .readText(Charsets.UTF_8)


        }catch(e: Exception){
            println("ISSUE: $e")
        }
        return response
    }

    private suspend fun showPhotos(data: String){


        withContext(Main){
            val jsonObj = JSONObject(data)
            val photos = jsonObj.getJSONObject("photos").getJSONArray("photo")
            println("photos")
            println(photos.getJSONObject(0))
            println(photos.getJSONObject(0).getString("farm"))
            for(i in 0 until photos.length()){
                val title = photos.getJSONObject(i).getString("title")
                val farmID = photos.getJSONObject(i).getString("farm")
                val serverID = photos.getJSONObject(i).getString("server")
                val id = photos.getJSONObject(i).getString("id")
                val secret = photos.getJSONObject(i).getString("secret")
                val photoLink = "https://farm$farmID.staticflickr.com/$serverID/${id}_$secret.jpg"
                images.add(Image(title, photoLink)) }

        }
        rvAdapter.notifyDataSetChanged()

    }

*/

        fun openImg(link: String) {
            Glide.with(this).load(link).into(ivMain)
            ivMain.isVisible = true
            rvMain.isVisible = false
            llBottom.isVisible = false
        }

        private fun closeImg() {
            ivMain.isVisible = false
            rvMain.isVisible = true
            llBottom.isVisible = true
        }
    }