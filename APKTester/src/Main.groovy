import configuration.ADB
import configuration.Command
import configuration.Config
import logger.LogDaemon
import model.APK
import org.apache.commons.io.FileUtils
import runners.AbstractRunner

import java.nio.file.Paths


/**
 * Created by nmravasi on 10/8/16.
 */
class Main {

    public static void main(String[] args) {

        if (!ADB.IsDeviceUp()){
            ADB.RunEmulator();

//        Command.run('adb shell rm -rf ' + Config.SD_PATH + 'logs')
        File apksPath = new File(Config.APKS_PATH);

        if (Config.shouldInline) {
            File inlinePath = Paths.get(Config.DROIDMATE_DIR, 'apks', 'inlined').toFile()
            inlinePath.deleteDir();
            FileUtils.copyDirectory(apksPath, inlinePath)
            println("Copied to " + inlinePath.getAbsolutePath())


            def argsFile = Paths.get(Config.DROIDMATE_DIR, 'args.txt').toFile()
            def originalArgs = argsFile.text

            if (!originalArgs.contains('-inline')) {
                println('Modifying args.txt to add inline command')
                def newArgs = originalArgs + ' -inline';
                argsFile.write(newArgs);
            } else {
                originalArgs = originalArgs.replace('-inline', '')
            }

            def droidmateCmd = Config.DROIDMATE_DIR + 'gradlew -p ' + Config.DROIDMATE_DIR + ' :p:com:run'
            println("Inlining with command " + droidmateCmd)
            def inliner = Command.run(droidmateCmd);

            def res = inliner.waitFor()

            if (res != 0) {
                println("Inlining failed")
                println(inliner.text)
                throw new RuntimeException()
            }


            println('Inlined successfuly')
            argsFile.write(originalArgs);

            println('Copying apk back to path')

            FileUtils.copyDirectory(inlinePath, apksPath)
            FileUtils.deleteDirectory(new File(apksPath, 'originals'))
        }

        def apks = apksPath.listFiles().findAll {
            it.name.endsWith('inlined.apk')
        }.collect { new APK(it) }

        def loggerDaemon = new LogDaemon();
        def runner = AbstractRunner.getRunner(apks, loggerDaemon);

        runner.start();

        System.exit(0)
    }
}
