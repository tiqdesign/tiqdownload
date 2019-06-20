package com.tiqdeveloping.yasbulalim


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.CalendarView
import android.widget.Toast
import android.Manifest
import android.app.DownloadManager
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_main.*
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    private val STORAGE_PERMISSON_CODE: Int= 1000
    private var clipboard : ClipboardManager?=null
    private var clipdata : ClipData?=null
    var clipText =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fillTextBox()

        btn_add.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        btn_add.setTextColor(resources.getColor(R.color.colorForText))

        //btn_add in fonksiyonu
        btn_add.setOnClickListener{

        var note1= tb_not.text.toString()
            lb_not.text= "Notunuz: "+ note1
            lb_not.isVisible=true
            tb_not.setText("")
            Toast.makeText(getApplicationContext(), "Not Eklendi!!!", Toast.LENGTH_LONG).show()
        }

        //btn_download ın fonksiyonu
        btn_download.setOnClickListener{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                //permission denied, request it
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSON_CODE)
                }

                else{
                //permission already granted, perform download
                    startDownload()
                    val url = tb_not.text.toString()
                    val urlWithParse = Uri.parse(url)
                    Picasso.with(this).load(urlWithParse).error(R.drawable.ic_error_outline_black_24dp).into(img_search)
                    tb_not.setText("")
                }

            }

            else{
                //system os is less than Marshmallow, runtime permission not required,perform download
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fillTextBox()
    }

            //download start
    private fun startDownload() {
                val url = tb_not.text.toString()
                val request = DownloadManager.Request(Uri.parse(url))
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                request.setTitle("Tiqnot-Download")
                request.setDescription("tiqnot ile indirme işlemi başlatıldı")
                request.allowScanningByMediaScanner()
                Toast.makeText(this,"tiqnot ile indirme işlemi başlatıldı",Toast.LENGTH_LONG).show()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"${System.currentTimeMillis()}")

                val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                manager.enqueue(request)


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            STORAGE_PERMISSON_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    startDownload()
                }
                else{
                    Toast.makeText(this,"İzin Verilmedi :(",Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.bar_menu,menu)
        return true
    }
        //Secilen menuyu toast ta göster
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

       var selectedItem = ""
        when(item?.itemId){
            R.id.item_opt1 -> selectedItem="Arama"
            R.id.item_opt2 -> selectedItem="Çıkış"
        }

      Toast.makeText(this,selectedItem+" Yapıldı.",Toast.LENGTH_SHORT).show()
      return super.onOptionsItemSelected(item)
    }

    private fun fillTextBox(){
        clipboard=getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        //Clipboard a bir url ekliyse onu direk tb ye koyar.
        when(clipboard?.hasPrimaryClip()){
            true-> {clipdata = clipboard?.getPrimaryClip()
                val item =clipdata?.getItemAt(0)
                clipText= item?.text.toString()
                tb_not.setText(clipText)}

            false->{clipText=""
                tb_not.hint="Url Giriniz"}
        }
    }



}
