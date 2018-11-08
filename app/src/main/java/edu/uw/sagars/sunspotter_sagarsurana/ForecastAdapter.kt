package edu.uw.sagars.sunspotter_sagarsurana

import java.util.*
import android.view.*
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import edu.uw.sagars.sunspotter_sagarsurana.R.layout


class ForecastAdapter(context: Context, data: ArrayList<ForecastData>) :
    ArrayAdapter<ForecastAdapter.ForecastData>(context, 0, data) {

    class ForecastData(var icon: Drawable, var weather: String, var date: String, var temp: String)

    class ViewHolder {
        internal var text: TextView? = null
        internal var icon: ImageView? = null
        internal var position: Int = 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView: View? = convertView
        val holderView: ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout.single_cell_view, parent, false)
            holderView = ViewHolder()
            holderView.icon = convertView.findViewById(R.id.icon_cell) as ImageView
            holderView.text = convertView.findViewById(R.id.text_cell) as TextView
            convertView?.tag = convertView.findViewById(R.id.text_cell) as TextView
        } else {
            holderView = convertView.tag as ViewHolder
        }
        holderView.icon?.setImageDrawable(getItem(position)?.icon)
        holderView.text?.text = "${getItem(position)?.weather} @ ${getItem(position)?.date} (${getItem(position)?.temp}°)"
        return convertView
    }
}