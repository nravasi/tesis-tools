package runners

import configuration.Command
import configuration.Config
import model.APK

/**
 * Created by nmravasi on 10/8/16.
 */
class SapienzRunner extends AbstractRunner {
    protected SapienzRunner(apks) {
        super(apks)
    }


    @Override
    void beforeStart(){
        def monitorInstalled = Command.runAndRead("adb push utils/monitor_api19.apk data/local/tmp/monitor.apk");
    }

    @Override
    void testApk(APK apk) {

    }

    @Override
    void beforeEachApk(APK apk) {
        super.beforeEachApk()

        def rmSd = 'adb shell rm -f ' + Config.SD_PATH

        Command.run(rmSd + 'motifcore*', rmSd + '*strings.xml', rmSd + 'activity.coverage', rmSd + 'skin.coverage')
    }
}
