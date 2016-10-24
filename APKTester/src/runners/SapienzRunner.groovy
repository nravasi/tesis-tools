package runners

import configuration.Command
import configuration.Config
import model.APK

/**
 * Created by nmravasi on 10/8/16.
 */
class SapienzRunner extends AbstractRunner {
    protected SapienzRunner(apks, daemon) {
        super(apks, daemon)
    }


    @Override
    void beforeStart() {
//        def monitorInstalled = Command.runAndRead("adb push utils/monitor_api19.apk data/local/tmp/monitor.apk");
    }

    @Override
    void testApk(APK apk) {
        println("Starting sapienz for apk ${apk.appName}")

        def process = Command.run("python ${Config.SAPIENZ_DIR}main.py ${apk.file.absolutePath}")
        process.waitForOrKill(Config.minutes * 60000)
        println("Terminating sapienz for apk ${apk.appName}")

    }

    @Override
    void beforeApk(APK apk) {
        super.beforeApk()

        def rmSd = 'adb shell rm -f ' + Config.SD_PATH

        Command.run(rmSd + 'motifcore*', rmSd + '*strings.xml', rmSd + 'activity.coverage', rmSd + 'skin.coverage')
    }
}
