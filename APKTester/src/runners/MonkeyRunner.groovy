package runners

import configuration.Command
import configuration.Config
import model.APK
import java.util.logging.Logger


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

            println "Removing package " +  apk.packageName;

            def removeApk = Command.runAndRead("adb shell pm uninstall -k " + apk.packageName);

            if(!removeApk.contains("Success")){
                throw new RuntimeException("Remove " +  apk.packageName + " failed ")
            }

            println "Remove OK";
        }

        println "Installing "  + apk.packageName + " ....";
        // install apk
        def installApk =  Command.runAndRead("adb install " + apk.file);

        if(!installApk.contains("Success")){
            throw new RuntimeException("Something goes wrong installing " +  apk.packageName );
        }

        println "Installed OK";
    }

    @Override
    void beforeStart(){
        Command.runAndRead("adb push utils/monitor_api19.apk data/local/tmp/monitor.apk");
    }

    @Override
    void testApk(APK apk) {
        println "Running MONKEY";

       def monkeyCmd = "adb shell monkey -p "+ apk.packageName +  " -v " + 2000;
       Command.runAndKillAfterTimeout(monkeyCmd);

        println "MONKEY finished";
    }
}
