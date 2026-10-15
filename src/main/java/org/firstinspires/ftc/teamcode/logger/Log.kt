package org.firstinspires.ftc.teamcode.logger

import android.content.Context
import android.content.res.AssetManager
import com.qualcomm.robotcore.util.WebHandlerManager
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.Response.Status
import org.firstinspires.ftc.ftccommon.external.WebHandlerRegistrar
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import java.io.File

object Log {
    val rootDir: File = File(AppUtil.FIRST_FOLDER, "turtle_logger")

    @WebHandlerRegistrar
    fun register(ctx: Context, manager: WebHandlerManager) {
        val assets = ctx.assets

        manager.register("/logger", serveWebsite(assets))
        manager.register("/logger/api/list", {TODO()})
        manager.register("/logger/api/data", {TODO()})
        manager.register("/logger/api/del", {TODO()})
        // main website
        // get list of logs
        // get log data
        // delete log
    }

    fun serveWebsite(assets: AssetManager) = {
        session: NanoHTTPD.IHTTPSession ->
            if (session.method != NanoHTTPD.Method.GET)
                NanoHTTPD.newFixedLengthResponse(Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "")
            else
                NanoHTTPD.newChunkedResponse(Status.OK, NanoHTTPD.MIME_HTML, assets.open("/turtle_logger/index.html"))
    }


}