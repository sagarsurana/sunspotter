package edu.uw.sagars.sunspotter_sagarsurana

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.os.Build
import android.support.annotation.RequiresApi
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import com.android.volley.*
import com.android.volley.toolbox.*
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*
import android.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val urlMain = "https://api.openweathermap.org/data/2.5/forecast?format=json&units=imperial&q="
    private val DT_FORMAT = SimpleDateFormat("EEE, h:mm a")
    private var adapter: ForecastAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val weather = ArrayList<ForecastAdapter.ForecastData>()
        adapter = ForecastAdapter(this, weather)
        listOfWeathers.adapter = adapter
        searchButton.setOnClickListener {
            val city = searchText.text.toString()
            Log.v("MainActivity", "city: $city")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (city != "") {
                    val newUrl = urlMain + city + "&appid=" + getString(R.string.OPEN_WEATHER_MAP_API_KEY)
                    recieve(newUrl)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun recieve(url: String) {
        val replyRecieved = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener<JSONObject> { it ->
            var response = it.getJSONArray("list")
            var sunnyOrNot = false
            var sunnyTime = ""
            for (i in 0..response.length() - 1) {
                var weather = response.getJSONObject(i).getJSONArray("weather")
                var dt = response.getJSONObject(i).getInt("dt")
                var main = response.getJSONObject(i).getJSONObject("main")
                var weatherCurrent = weather.getJSONObject(0)
                var mainVal = weatherCurrent.getString("main")
                var iconVal = weatherCurrent.getString("icon")
                var temp = main.getString("temp")
                Log.v("MainActivity", temp)
                var date = Date(dt * 1000L)
                var dateTwo = DT_FORMAT.format(date)
                Log.v("MainActivity", dateTwo)
                if (mainVal == "Clear") {
                    sunnyOrNot = true
                    sunnyTime = dateTwo
                }
                val temporary = resources.getIdentifier("icon" + iconVal, "drawable", packageName)
                val draw: Drawable = getDrawable(temporary)
                adapter?.add(ForecastAdapter.ForecastData(draw, mainVal, dateTwo, temp))

            }
            if (sunnyOrNot) {
                currentImage.setImageResource(R.drawable.ic_check_circle_black_24dp)
                currentImage.setColorFilter(Color.YELLOW)
                currentTitle.setText(getString(R.string.yesSun))
                currentText.setText("At $sunnyTime")
            } else {
                currentImage.setImageResource(R.drawable.ic_highlight_off_black_24dp)
                currentImage.setColorFilter(Color.GRAY)
                currentTitle.setText(getString(R.string.noSun))
                currentText.setText("")
            }
        },
        Response.ErrorListener { err ->
            throw RuntimeException(err)
        }
        )
        val queue = Volley.newRequestQueue(this)
        queue.add(replyRecieved)
    }
}
