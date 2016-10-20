package runners

import configuration.ADB
import configuration.Command
import configuration.Config
import model.APK

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by nmravasi on 10/8/16.
 */
class MonkeyRunner extends AbstractRunner {

    protected MonkeyRunner(apks, daemon) {
        super(apks, daemon)


    }

    boolean doRun = true;

    @Override
    void beforeEachApk(APK apk) {
        super.beforeEachApk();

        //remove the apk before install it, if it exists
        if (ADB.IsAPKInstalled(apk.packageName)) {

            ADB.RemoveAPK(apk.packageName);
        }

        ADB.InstallAPK(apk.packageName, apk.file.toString());
    }

    @Override
    void beforeStart() {
//        Command.runAndRead("adb push utils/monitor_api19.apk data/local/tmp/monitor.apk");
    }

    @Override
    void testApk(APK apk) {
        println "Running MONKEY";

        doRun = true;

        def monkeyCmd = "adb shell monkey -p ${apk.packageName} -v ${2000}";


        Executors.newScheduledThreadPool(1).schedule(new Runnable() {
            @Override
            void run() {
                doRun =false;
            }
        }, Config.minutes, TimeUnit.MINUTES);

        while (doRun) {
            println("Iterating monkey")
            def run = Command.run(monkeyCmd);
            run.waitFor()
        }

        println("Monkey finished")
        /* Command.run(monkeyCmd);*/
    }

}
