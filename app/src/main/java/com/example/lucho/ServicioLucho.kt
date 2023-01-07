package com.example.lucho

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ServicioLucho: Service(){

    private val chanelID = "chanelID"
    private val chanelName = "chanelName"
    private val notificationID = 0

    private val TAG: String = "MiServicio"

    init {
        Log.d(TAG, "Services running...")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate...Lucho")
    }
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
     /*   val extraStringS = intent?.getStringExtra("EXTRA MENSAJE")
        extraStringS?.let {
            Log.d(TAG, "Mensaje: $it")
            if(it == "FINALIZAR"){
                stopSelf()
            }
        }

*/

        val runable = Runnable {
            for(i in 1..20){
                Log.d(TAG , "Acciones del servicio" + i.toString())
                Thread.sleep(1000)
            }
            val extraStringS = intent?.getStringExtra("EXTRA MENSAJE")

            //Entrada Notificaciones

            createNotioficationChanel()

            val intent = Intent(this, SecondActivity::class.java)
            val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(MainActivity.INTENT_REQUEST, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            val buttonIntent = Intent(this, SecondActivity::class.java)
            buttonIntent.putExtra("EXTRA_ARGS", "Botón Presionado")
            val buttonPendingIntent = PendingIntent.getActivity(
                this,
                MainActivity.BUTTON_INTENT_REQUEST,
                buttonIntent,
                PendingIntent.FLAG_ONE_SHOT

            )

            val action = NotificationCompat.Action.Builder(
                R.drawable.ic_messagelucho,
                "Botón",
                buttonPendingIntent
            ).build()

            val notification = NotificationCompat.Builder(this,chanelID).also {
                it.setContentTitle("Finalizó el proceso")
                it.setContentText("Contenido deseado")
                it.setSmallIcon(R.drawable.ic_messagelucho)
                it.setPriority(NotificationCompat.PRIORITY_HIGH)
                it.setContentIntent(pendingIntent)
                it.addAction(action)
                it.setAutoCancel(true)
            }.build()

            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(notificationID, notification)


            if (extraStringS != null) {
                Log.d(TAG , "extraStringS")
            }

        }
        val thread = Thread(runable)
        thread.start()

        return START_NOT_STICKY
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
        Log.d(TAG, "Services destroyed...")
    }

}