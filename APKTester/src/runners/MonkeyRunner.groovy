package runners

import configuration.ADB
import configuration.Command
import configuration.Config
import model.APK

/**
 * Created by nmravasi on 10/8/16.
 */
class MonkeyRunner extends AbstractRunner {

    protected MonkeyRunner(apks, daemon) {
        super(apks, daemon)


    }

    boolean doRun = true;

    @Override
    void beforeApk(APK apk) {
        super.beforeApk();

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
    void afterApk(APK apk) {
        super.afterApk(apk)

        ADB.RemoveAPK(apk.packageName);
    }

    @Override
    void testApk(APK apk) {
        println "Running MONKEY";

        doRun = true;

        def monkeyCmd = "adb shell monkey -p ${apk.packageName} -v ${2000}";

        Command.run(monkeyCmd)

        Thread.sleep(Config.TIMEOUT_BEFORE_KILL)

        ADB.KillAPK("com.android.commands.monkey");
        //ADB.KillAPK(apk.packageName);

        println "Monkey finished";
        /* Command.run(monkeyCmd);*/
    }

}
