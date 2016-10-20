package runners

import configuration.Config
import configuration.Tool
import logger.LogAnalyzer
import logger.LogDaemon
import model.APK

/**
 * Created by nmravasi on 10/8/16.
 */
public abstract class AbstractRunner {

    protected apks;
    public boolean finished = false;
    LogDaemon loggerDaemon;

    protected AbstractRunner(apks, loggerDaemon) {
        this.apks = apks;
        this.loggerDaemon = loggerDaemon;
    }

    public static AbstractRunner getRunner(apks, loggerDaemon) {
        switch (Config.TOOL_TO_USE) {
            case Tool.MONKEY:
                return new MonkeyRunner(apks, loggerDaemon);
                break
            case Tool.SAPIENZ:
                return new SapienzRunner(apks, loggerDaemon);
                break
            case Tool.DROIDMATE:
                return new DroidmaterRunner(apks, loggerDaemon);
                break
        }
    }

    public void start() {

        beforeStart();

        apks.each {
            beforeEachApk(it);
            loggerDaemon.notifyStart(it);
            testApk(it);
            loggerDaemon.notifyFinish();
            done(it)
        }

        finished = true;
    }

    def done(APK apk) {
        def analyzer = new LogAnalyzer();
        def files = analyzer.processFiles();
        println(files) // Esta es la posta
    }

    public void beforeStart() {};

    public abstract void testApk(APK apk);

    public void beforeEachApk(APK apk) {

    }
}
