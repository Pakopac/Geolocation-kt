package com.example.geolocation.ui.geoloc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geolocation.R
import com.example.geolocation.model.Geoloc
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.ObservableSnapshotArray


class GeolocAdapter(options: FirestoreRecyclerOptions<Geoloc>): FirestoreRecyclerAdapter<Geoloc, GeolocAdapter.ViewHolder>(options) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var longitude = itemView.findViewById<TextView>(R.id.longitude)
        var latitude = itemView.findViewById<TextView>(R.id.latitude)
        var btn_delete = itemView.findViewById<TextView>(R.id.btn_delete)
    }

        override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Geoloc) {
            holder.longitude.text = model.longitude.toString()
            holder.latitude.text = model.latitude.toString()
            holder.btn_delete.setOnClickListener {
                val observableSnapshotArray: ObservableSnapshotArray<Geoloc> = snapshots
                val documentReference =
                    observableSnapshotArray.getSnapshot(position).reference

                documentReference.delete()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_geoloc, parent, false)
            return ViewHolder(view)
        }
}