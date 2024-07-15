package com.example.blerefrigerator

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import java.util.UUID

class GetBLE {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner
    private var scanCallback: ScanCallback? = null

    @OptIn(UnstableApi::class)
    @SuppressLint("MissingPermission")
    fun startScan() {
        var count = 0
//        var door = false //true = ドアが空いている
        bluetoothLeScanner?.let {
            if (scanCallback == null) {
                scanCallback = object : ScanCallback() {
                    @OptIn(UnstableApi::class)
                    override fun onScanResult(callbackType: Int, result: ScanResult) {
                        super.onScanResult(callbackType, result)
                        val device: BluetoothDevice = result.device
                        val uuids = result.scanRecord?.serviceUuids
                        val receiveRssi = result.rssi
                        uuids?.forEach { _ ->
                            val address = device.address
                            if (address == "DC:0D:30:15:9A:AF") {
                                if (receiveRssi > -64) {
                                    if (count < 5) {
                                        count+=1
                                    } else
                                        if (count == 5){
                                        door.value = true
                                    }
                                } else{
                                    if (count > 0){
                                        count --
                                    }else if(count == 0){
                                        door.value = false
                                    }
                                }
//                                val uuidString = uuid.uuid.toString()
//                                val logMessage = "Device: ${device.address}, UUID: $uuidString, RSSI: $receiveRssi"
//                                Log.d("GetBLE", logMessage) // リアルタイムでログに出力
                            val doorString = door.toString()
                            Log.d("Door", "$doorString, RSSI: $receiveRssi, Count:$count")
                            }
                        }
                    }

                    @OptIn(UnstableApi::class)
                    override fun onScanFailed(errorCode: Int) {
                        super.onScanFailed(errorCode)
                        Log.e("GetBLE", "Scan failed with error code $errorCode")
                    }
                }
                // スキャンのセッティング
                val scanSettings = ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build()

                val mUuid = UUID.fromString("0000fef5-0000-1000-8000-00805f9b34fb") // ここにサービスのUUIDを設定

                val scanFilters = mutableListOf<ScanFilter>()
                val filter = ScanFilter.Builder()
                    .setServiceUuid(ParcelUuid(mUuid))
                    .build()
                scanFilters.add(filter)

                bluetoothLeScanner.startScan(scanFilters, scanSettings, scanCallback)

                Log.d("GetBLE", "scan started")
            } else {
                Log.d("GetBLE", "Scan already running")
            }
        } ?: run {
            Log.e("GetBLE", "BluetoothLeScanner is not initialized")
        }
    }


    @OptIn(UnstableApi::class)
    @SuppressLint("MissingPermission")
    fun stopScan() {
        scanCallback?.let { bluetoothLeScanner?.stopScan(it) }
        scanCallback = null
        Log.d("GetBLE", "scan stopped")
    }
}