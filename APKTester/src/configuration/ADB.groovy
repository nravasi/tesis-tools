package configuration

import static configuration.Config.*


/**
 * Created by Ignacio on 20-Oct-16.
 */
class ADB {

    public static void InstallAPK(String packageName, String file){
        println "Installing " + packageName + " ....";

        def installApk = Command.runAndRead("adb install " + file);

        if (!installApk.contains("Success")) {
            throw new RuntimeException("Something goes wrong installing " + packageName);
        }

        println "Installed OK";
    }

    public static Boolean IsAPKInstalled(String packageName){

        def data = Command.runAndRead("adb shell pm list packages | grep " + packageName);

        return data.contains("package:" + packageName);
    }

    public static void RemoveAPK(String packageName){

        println "Removing package " + packageName;

        def removeApk = Command.runAndRead("adb shell pm uninstall -k " + packageName);

        if (!removeApk.contains("Success")) {
            throw new RuntimeException("Remove " + packageName + " failed ")
        }

        println "Remove OK";
    }

    public static Boolean IsDeviceUp(){

        def emulatorRunning = Command.run("adb shell getprop sys.boot_completed").text.readLines();

        return emulatorRunning.contains("1");
    }

    public static void RunEmulator(){

        Command.run("${SDK}/emulator -avd ${ADV_NAME}");

        def isRunning = false;

        while(!isRunning){
            isRunning = IsDeviceUp()

            if(isRunning){
                //wait 10 seconds just in case..
                Thread.sleep(10000);
            }

            //wait 2 second to avoid followed  commands
            Thread.sleep(2000);
        }
    }
}
