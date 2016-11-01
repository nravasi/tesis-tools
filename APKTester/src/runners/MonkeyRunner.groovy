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
       super.beforeStart()
    }

    @Override
    void afterApk(APK apk) {
        super.afterApk(apk)
        ADB.RemoveAPK(apk.packageName);
    }

    @Override
    void testApk(APK apk) {
        println "Running MONKEY";

        def monkeyCmd = "adb shell monkey -p ${apk.packageName} -v ${20000000}";

        Command.run(monkeyCmd)

        Thread.sleep(Config.TIMEOUT_BEFORE_KILL)

        ADB.KillAPK("com.android.commands.monkey");

        println "Monkey finished OK";
        /* Command.run(monkeyCmd);*/
    }
}
