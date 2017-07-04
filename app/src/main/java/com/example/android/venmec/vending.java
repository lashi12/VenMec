package com.example.android.venmec;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by lashi on 3/23/2017.
 */

public class vending extends AppCompatActivity {

    Button star, diary, kitkat, perk ,done,about;
    ImageView paytm,freecharge;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);
        setContentView(R.layout.vending);

        star = (Button)findViewById(R.id.button1);
        diary = (Button)findViewById(R.id.button2);
        perk = (Button)findViewById(R.id.button3);
        kitkat = (Button)findViewById(R.id.button4);
        done = (Button)findViewById(R.id.button16);
        about =(Button)findViewById(R.id.about);
        paytm =(ImageView)findViewById(R.id.paytm);
        freecharge=(ImageView)findViewById(R.id.freecharge);

        new ConnectBT().execute();
        about.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent abt = new Intent(vending.this, About.class);
                startActivity(abt);
            }
        });

        paytm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(),"Rs.10 debited from your paytm account ",Toast.LENGTH_SHORT).show();
            }
        });
        freecharge.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Toast.makeText(getApplicationContext(),"Rs.10 debited from your freecharge account ",Toast.LENGTH_SHORT).show();
            }
        });
        star.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buystar();
                Toast.makeText(getApplicationContext(),"you have selected 5 star",Toast.LENGTH_SHORT).show();
            }
        });

        diary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buydiary();
                Toast.makeText(getApplicationContext(),"you have selected diary milk",Toast.LENGTH_SHORT).show();
            }
        });

        kitkat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buykitkat();
                Toast.makeText(getApplicationContext(),"you have selected kitkat",Toast.LENGTH_SHORT).show();
            }
        });

        perk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buyperk();
                Toast.makeText(getApplicationContext(),"you have selected perk",Toast.LENGTH_SHORT).show();
            }
        });

        done.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                done();
                Toast.makeText(getApplicationContext(),"Thank you for shopping",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void done()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("5".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void buystar()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("1".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void buydiary()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("2".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void buykitkat()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("3".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void buyperk()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("4".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(vending.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
