package runners

import configuration.Command
import configuration.Config
import model.APK

/**
 * Created by nmravasi on 10/8/16.
 */
class MonkeyRunner extends AbstractRunner {

    protected MonkeyRunner(apks) {
        super(apks)
    }

    @Override
    void beforeEachApk(APK apk) {
        super.beforeEachApk()

        def data = Command.runAndRead("adb shell pm list packages | grep " + apk.packageName);

        //remove the apk before install it, if it exists
        if(data.contains("package:"+ apk.packageName)){

            def removeApk = Command.runAndRead("adb shell pm uninstall -k " + apk.packageName);

            if(!removeApk.contains("Success")){
                throw new RuntimeException("Remove " +  apk.packageName + " failed ")
            }

        }

        // install apk
        def installApk =  Command.runAndRead("adb install " + apk.file);

        if(!installApk.contains("Success")){
            throw new RuntimeException("Something goes wrong installing " +  apk.packageName );
        }


        //necesito firmar primero el monitor , no pude hacer funcionar esto
        //def monitor = Command.run("adb install /monitor/monitor_api19.apk ");

    }

    @Override
    void testApk(APK apk) {
       def monkeyCmd = "adb shell monkey -p "+ apk.packageName +  " -v 500";
       def execute = Command.runAndRead(monkeyCmd);
    }
}
