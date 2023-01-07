package com.example.lucho

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.nfc.Tag
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.textfield.TextInputEditText
import java.nio.channels.Channel

class MainActivity : AppCompatActivity() {


    private val chanelID = "chanelID"
    private val chanelName = "chanelName"
    private val notificationID = 0

    companion object{
        const val INTENT_REQUEST = 0
        const val BUTTON_INTENT_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Servicio
        findViewById<Button>(R.id.btn_iniciarServicio).setOnClickListener {
            Thread{
                Intent(this, ServicioLucho::class.java).also {
                    startService(it)
                    findViewById<TextView>(R.id.textView_servicio).text = "Servicio Iniciado"
                }


            }.start()



        }

        findViewById<Button>(R.id.btn_finalizarServicio).setOnClickListener {
            Intent(this, ServicioLucho::class.java).also {
                stopService(it)
                findViewById<TextView>(R.id.textView_servicio).text = "Servicio Finalizado"
            }
        }

        findViewById<Button>(R.id.btn_sendMessage).setOnClickListener {
            Intent(this, ServicioLucho::class.java).also {
                it.putExtra("EXTRA MENSAJE", findViewById<TextInputEditText>(R.id.textInputEditText_message).text.toString())
                startService(it)
                findViewById<TextInputEditText>(R.id.textInputEditText_message).setText("")
                findViewById<TextView>(R.id.textView_servicio).text = "Servicio Iniciado"
            }

        }



        //Entrada Notificaciones

        createNotioficationChanel()

        val intent = Intent(this, SecondActivity::class.java)
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(INTENT_REQUEST, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val buttonIntent = Intent(this, SecondActivity::class.java)
        buttonIntent.putExtra("EXTRA_ARGS", "Botón Presionado")
        val buttonPendingIntent = PendingIntent.getActivity(
            this,
            BUTTON_INTENT_REQUEST,
            buttonIntent,
            PendingIntent.FLAG_ONE_SHOT

        )

        val action = NotificationCompat.Action.Builder(
            R.drawable.ic_messagelucho,
            "Botón",
            buttonPendingIntent
        ).build()

        val notification = NotificationCompat.Builder(this,chanelID).also {
            it.setContentTitle("Titulo de Notificación")
            it.setContentText("Este es el contenido")
            it.setSmallIcon(R.drawable.ic_messagelucho)
            it.setPriority(NotificationCompat.PRIORITY_HIGH)
            it.setContentIntent(pendingIntent)
            it.addAction(action)
            it.setAutoCancel(true)
        }.build()


        val notificationManager = NotificationManagerCompat.from(this)


        findViewById<Button>(R.id.btn_newNotification).setOnClickListener{
            notificationManager.notify(notificationID, notification)
        }
    }

    private fun createNotioficationChanel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(chanelID, chanelName, importance).apply {
                lightColor = Color.RED
                enableLights(true)

            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

             manager.createNotificationChannel(channel)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("MiServicio", "onDestroy Activity...")
    }
}