package runners

import configuration.Command
import configuration.Config
import model.APK
import org.apache.commons.io.FileUtils

import java.nio.file.Paths
import java.util.regex.Pattern

/**
 * Created by nmravasi on 10/8/16.
 */
class DroidmaterRunner extends AbstractRunner {

    def timeLimitPattern = Pattern.compile(".*timeLimit (\\d+).*")
    private File apksDir

    protected DroidmaterRunner(apks, daemon) {
        super(apks, daemon)
    }

    @Override
    void testApk(APK apk) {
        println("Executing droidmate for ${apk.appName}")
        def droidmateCmd = Config.DROIDMATE_DIR + 'gradlew -p ' + Config.DROIDMATE_DIR + ' :p:com:run'
        def inliner = Command.run(droidmateCmd);

        def res = inliner.waitFor()

        println("Finished testing apk ${apk.appName}")

    }

    @Override
    void beforeStart() {
        super.beforeStart()

        println("Modifying args file to specify running minutes")

        def argsFile = Paths.get(Config.DROIDMATE_DIR, 'args.txt').toFile()
        def contents = argsFile.text;

        def matcher = timeLimitPattern.matcher(contents)
        if (matcher.matches()) {
            contents = contents.replace(matcher.group(1), Integer.toString(Config.minutes * 60));
        } else {
            contents += " -timeLimit ${Integer.toString(Config.minutes * 60)}"
        }

        argsFile.write(contents);

        println("Finished modifying droidmate args file")

        println("Cleaning all files in apk/inlined directory")


        apksDir = Paths.get(Config.DROIDMATE_DIR, 'apks', 'inlined').toFile()
        apksDir.listFiles()
                .findAll { it.name.endsWith('apk') }.each { it.delete() }
    }

    @Override
    void beforeEachApk(APK apk) {
        super.beforeEachApk(apk)

        println("Copying ${apk.file.name} to droidmate apks directory")

        FileUtils.copyFileToDirectory(apk.file, apksDir);
    }
}
