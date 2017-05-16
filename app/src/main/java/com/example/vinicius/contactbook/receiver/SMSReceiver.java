package com.example.vinicius.contactbook.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.vinicius.contactbook.R;
import com.example.vinicius.contactbook.dao.StudentDAO;


//broadcastReceiver trata de eventos do android, como receber uma mensagem ou uma ligacao
public class SMSReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");
        byte[] pdu = (byte[]) pdus[0];

        String format = (String) intent.getSerializableExtra("format");

        SmsMessage smsMessage = SmsMessage.createFromPdu(pdu, format);

        String phone = smsMessage.getDisplayOriginatingAddress();
        StudentDAO studentDAO = new StudentDAO(context);
        boolean isStudent = studentDAO.isStudent(phone);
        if (isStudent) {
            Toast.makeText(context, "Recebido o sms", Toast.LENGTH_SHORT);
            MediaPlayer.create(context, R.raw.msg);
        }
        studentDAO.close();
    }
}
