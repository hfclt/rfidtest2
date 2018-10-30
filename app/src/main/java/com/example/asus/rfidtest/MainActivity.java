package com.example.asus.rfidtest;

import uhf.api.CommandType;
import uhf.api.Ware;
import com.example.asus.rfidtest.ShellUtils.CommandResult;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;



import android.view.KeyEvent;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    private TextView minfos=null;

    public static String open_1="echo 1 > /sys/devices/soc.0/xt_dev.68/xt_uhf_en";
    public static String open_2="echo 0 >/sys/devices/soc.0/xt_dev.68/xt_uart_a";
    public static String open_3="echo 1 >/sys/devices/soc.0/xt_dev.68/xt_uart_b";

    public static String close_1="echo 0 > /sys/devices/soc.0/xt_dev.68/xt_uhf_en";
    public static String close_2="echo 0 >/sys/devices/soc.0/xt_dev.68/xt_uart_a";
    public static String close_3="echo 0 >/sys/devices/soc.0/xt_dev.68/xt_uart_b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void repeat(View view) {
    }

    public void dataupdate(View view) {
    }

    public void dataupload(View view) {
    }

    public void dataSave(View view) {
    }

    public void testlink(View view) {
        {
            // TODO Auto-generated method stub
            minfos=(TextView) this.findViewById(R.id.infos);

            //打开GPIO
            Boolean ret=ShellUtils.checkRootPermission();
            Log.e("TAG", "ret="+ret);
            ShellUtils.execCommand(open_1,ret);
            ShellUtils.execCommand(open_2,ret);
            ShellUtils.execCommand(open_3,ret);


            //ShellUtils.execCommand(open,ret);
            //ShellUtils.execCommand(open1,ret);
            //ShellUtils.execCommand(open2,ret);
            //Handler handler = new Handler();

            new Thread()
            {
                public void run()
                {

                    Ware mWare=new Ware(CommandType.GET_FIRMWARE_VERSION, 0, 0, 0);

                    int count=0;
                    while(true)
                    {
                        UHFClient info=UHFClient.getInstance();
                        setTitle(" 设备获取成功1");
                        if(info!=null)
                        {
                            Boolean rett=UHFClient.mUHF.command(CommandType.GET_FIRMWARE_VERSION, mWare);
                            if(rett)
                            {
                                Log.e("TAG", "Ver."+mWare.major_version+"."+mWare.minor_version+"."+mWare.revision_version);

                                setTitle("P6010 V2 设备获取成功");
                                minfos.setText("Ver."+mWare.major_version+"."+mWare.minor_version+"."+mWare.revision_version);
                                break;
                            }
                        }

                        count++;
                        if(count>5)
                        {
                            setTitle("P6010 V2  连接失败，请重启设备");
                            break;
                        }
                    }
                }
            }.start();
        }

  //testlink结束
    }
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        finish();
        //关闭GPIO
        //打开GPIO
        Boolean ret=ShellUtils.checkRootPermission();
        Log.e("TAG", "ret="+ret);
        ShellUtils.execCommand(close_1,ret);
        ShellUtils.execCommand(close_2,ret);
        ShellUtils.execCommand(close_3,ret);

        //ShellUtils.execCommand(close,ret);
        //ShellUtils.execCommand(close1,ret);
        //ShellUtils.execCommand(close2,ret);

        android.os.Process.killProcess(android.os.Process.myPid());
    }
    //mainactivity 结束
}
