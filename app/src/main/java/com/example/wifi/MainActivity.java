package com.example.wifi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private Button btnTurnOn, btnTurnOff, btnListWiFi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WifiManager.class);

        btnTurnOn = findViewById(R.id.btnTurnOn);
        btnTurnOff = findViewById(R.id.btnTurnOff);
        btnListWiFi = findViewById(R.id.btnListWiFi);

        btnTurnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnWiFi();
            }
        });

        btnTurnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOffWiFi();
            }
        });

        btnListWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listWiFiNetworks();
            }
        });
    }

    private void turnOnWiFi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            Toast.makeText(getApplicationContext(), "WiFi Turned On", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "WiFi is already On", Toast.LENGTH_SHORT).show();
        }
    }

    private void turnOffWiFi() {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            Toast.makeText(getApplicationContext(), "WiFi Turned Off", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "WiFi is already Off", Toast.LENGTH_SHORT).show();
        }
    }

    private void listWiFiNetworks() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
            return;
        }

        // Proceed with listing WiFi networks
        List<ScanResult> scanResults = wifiManager.getScanResults();
        StringBuilder sb = new StringBuilder();
        sb.append("WiFi Networks:\n");
        for (ScanResult scanResult : scanResults) {
            sb.append(scanResult.SSID).append("\n");
        }
        Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
    }

    // Add this constant for the permission request code
    private static final int PERMISSION_REQUEST_LOCATION = 123;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with listing WiFi networks
                listWiFiNetworks();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
