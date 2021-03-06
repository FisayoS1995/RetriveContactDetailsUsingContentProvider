package com.example.retrievecontactsdatakotlin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.contact_child.view.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResults.layoutManager = LinearLayoutManager(this)

        btnSave.setOnClickListener {

            val contactList: MutableList<ContactDTO> = ArrayList()
            val contacts =
                contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
            while (contacts.moveToNext()) {
                val name =
                    contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val obj = ContactDTO()
                obj.name = name
                obj.number = number

                val photoUri =
                    contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                if (photoUri != null) {
                    obj.image = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(photoUri))
                }
                contactList.add(obj)
            }
            tvResults.adapter = ContactAdapter(contactList, this)
            contacts.close()
        }
    }

    class ContactAdapter(items: List<ContactDTO>, ctx: Context) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {


        private var list = items
        private var context = ctx

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int) {
            holder.name.text = list[position].name
            holder.number.text = list[position].number


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_child,parent, false))
        }

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val name: TextView = v.tvName!!
            val number: TextView = v.tvNumber!!
            val profile: ImageView = v.iv_profile!!
        }


    }
}
