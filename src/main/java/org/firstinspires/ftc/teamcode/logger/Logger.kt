package org.firstinspires.ftc.teamcode.logger

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.qualcomm.robotcore.util.WebHandlerManager
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoHTTPD.Response
import fi.iki.elonen.NanoHTTPD.Response.Status
import org.firstinspires.ftc.ftccommon.external.WebHandlerRegistrar
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import java.io.File

object Logger {
    val rootDir: File = File(AppUtil.FIRST_FOLDER, "turtle_logger")

    init { if (!rootDir.exists()) rootDir.mkdirs() }

    @WebHandlerRegistrar
    @JvmStatic
    fun register(ctx: Context, manager: WebHandlerManager) {
        manager.register("/logger") { s -> serveWebsite(s, ctx.assets) }
        manager.register("/api/list", ::getFileList)
        manager.register("/api/data", ::getFileData)
        manager.register("/api/del", ::delFile)
    }

    fun serveWebsite(session: IHTTPSession, assets: AssetManager): Response =
        NanoHTTPD.newChunkedResponse(
            Status.OK,
            NanoHTTPD.MIME_HTML,
            assets.open("turtle_logger/index.html")
        )
    fun getFileData(session: IHTTPSession): Response {
        val name = session.parameters["file"]?.get(0)
        val f = File(rootDir, name ?: error("File name not provided"))
        return if (f.exists()) {
            NanoHTTPD.newFixedLengthResponse(
                Status.OK,
                "application/json",
                Gson().toJson(f.readLines())
            )
        } else {
            NanoHTTPD.newFixedLengthResponse(
                Status.NOT_FOUND,
                NanoHTTPD.MIME_PLAINTEXT,
                "File doesn't exist"
            )
        }
    }
    fun getFileList(session: IHTTPSession): Response =
        NanoHTTPD.newFixedLengthResponse(
            Status.OK,
            "application/json",
            Gson().toJson(rootDir.list())
        )
    fun delFile(session: IHTTPSession): Response {
        File(rootDir, session.parameters["file"]?.get(0) ?: error("File name not provided")).delete()
        return NanoHTTPD.newFixedLengthResponse(
            Status.OK,
            NanoHTTPD.MIME_PLAINTEXT,
            ""
        )
    }
}