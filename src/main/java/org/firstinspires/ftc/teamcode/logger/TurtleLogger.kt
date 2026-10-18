package org.firstinspires.ftc.teamcode.logger

import android.content.Context
import android.os.Environment
import com.google.gson.Gson
import com.qualcomm.robotcore.hardware.VoltageSensor
import com.qualcomm.robotcore.util.WebHandlerManager
import fi.iki.elonen.NanoHTTPD
import org.firstinspires.ftc.ftccommon.external.WebHandlerRegistrar
import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler
import java.io.File
import java.io.FileWriter
import java.util.concurrent.ConcurrentHashMap

object TurtleLogger {
    private val gson = Gson()
    private val logDir = File(Environment.getExternalStorageDirectory(), "FIRST/turtle_logger")
    private val sessionData = ConcurrentHashMap<Long, Map<String, Any>>()
    private var currentPacket = LoggerPacket()
    private var activeSessionName: String? = null
    private var isLogging = false
    private var voltageSensor: VoltageSensor? = null

    init {
        if (!logDir.exists()) {
            logDir.mkdirs()
        }
    }

    class LoggerPacket {
        private val entries = mutableMapOf<String, Any>()

        fun addEntry(key: String, value: Any) {
            entries[key] = value
        }

        fun getEntries(): Map<String, Any> = entries

        fun clear() {
            entries.clear()
        }
    }

    fun startSession(sessionName: String = "session_${System.currentTimeMillis()}", sensor: VoltageSensor? = null) {
        activeSessionName = sessionName
        voltageSensor = sensor
        sessionData.clear()
        currentPacket.clear()
        isLogging = true
    }

    fun log(key: String, value: Any) {
        if (!isLogging) return
        currentPacket.addEntry(key, value)
    }

    fun update() {
        if (!isLogging) return

        // Automatically log battery voltage if sensor is registered
        voltageSensor?.let { sensor ->
            try {
                currentPacket.addEntry("battery_voltage", sensor.voltage)
            } catch (e: Exception) {
                // Ignore sensor read errors
            }
        }

        val timestamp = System.currentTimeMillis()
        val entries = currentPacket.getEntries()
        if (entries.isNotEmpty()) {
            sessionData[timestamp] = HashMap(entries)
            currentPacket = LoggerPacket()
        }
    }

    fun stopSession() {
        if (!isLogging || activeSessionName == null) return
        isLogging = false
        update() // capture any remaining entries
        saveCurrentSession()
    }

    private fun saveCurrentSession() {
        val name = activeSessionName ?: return
        val file = File(logDir, "$name.json")
        try {
            FileWriter(file).use { writer ->
                gson.toJson(sessionData, writer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLogFiles(): List<String> {
        return logDir.listFiles { _, name -> name.endsWith(".json") }
            ?.map { it.name }
            ?.sortedDescending() ?: emptyList()
    }

    fun getLogContent(fileName: String): String {
        val file = File(logDir, fileName)
        if (!file.exists()) return "{}"
        return file.readText()
    }

    fun deleteLog(fileName: String): Boolean {
        val file = File(logDir, fileName)
        return file.delete()
    }

    @WebHandlerRegistrar
    @JvmStatic
    fun registerWebHandlers(context: Context, manager: WebHandlerManager) {
        manager.register("logger", WebHandler { session ->
            val uri = session.uri ?: ""
            val parms = session.parameters
            when {
                uri.contains("/api/list") -> {
                    val files = getLogFiles()
                    NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", gson.toJson(files))
                }
                uri.contains("/api/get") -> {
                    val fileName = parms["file"]?.get(0) ?: ""
                    val content = getLogContent(fileName)
                    NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", content)
                }
                uri.contains("/api/delete") -> {
                    val fileName = parms["file"]?.get(0) ?: ""
                    val success = deleteLog(fileName)
                    NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", "{\"success\":$success}")
                }
                else -> {
                    try {
                        val assetManager = context.assets
                        val inputStream = assetManager.open("turtle_logger/index.html")
                        val html = inputStream.bufferedReader().use { it.readText() }
                        NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", html)
                    } catch (e: Exception) {
                        NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "Logger UI not found: ${e.message}")
                    }
                }
            }
        })
        manager.register("graph", WebHandler { _ ->
            try {
                val assetManager = context.assets
                val inputStream = assetManager.open("turtle_logger/graph.html")
                val html = inputStream.bufferedReader().use { it.readText() }
                NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", html)
            } catch (e: Exception) {
                NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "Graph UI not found: ${e.message}")
            }
        })
    }
}
