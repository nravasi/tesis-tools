package logger

import configuration.Command
import configuration.Config
import model.APK

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Created by nmravasi on 10/16/16.
 */
class LogDaemon  {

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> future;


    def notifyStart(APK apk) {
        println("Instantiating daemon")
        def retriever = new LogRetriever(apk)
        future = executor.scheduleAtFixedRate(retriever, 10, 10, TimeUnit.SECONDS)
    }

    def notifyFinish() {
        future.cancel(false);
    }

    private class LogRetriever implements Runnable {

        APK apk;

        LogRetriever(APK apk){
            this.apk = apk;
        }

        int timestamp = 0; //This represents the nth minute where it's run

        @Override
        void run() {
            timestamp++;
            println("Executing retriever for iteration no.$timestamp")

            def cmd = "adb pull \"${Config.SD_PATH}logs/${apk.appName}.txt\" \"${Config.THIS_PATH}tmp"
            println(cmd)
            println(Command.run("pwd").text)
            def run = Command.run(cmd);
            println(run.text)
        }
    }
}
