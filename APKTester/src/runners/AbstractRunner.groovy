package runners

import configuration.Config
import configuration.Tool
import model.APK

/**
 * Created by nmravasi on 10/8/16.
 */
public abstract class AbstractRunner {

    protected apks;

    protected AbstractRunner(apks) {
        this.apks = apks;
    }

    public static AbstractRunner getRunner(apks) {
        switch (Config.TOOL_TO_USE) {
            case Tool.MONKEY:
                return new MonkeyRunner(apks);
                break
            case Tool.SAPIENZ:
                return new SapienzRunner(apks);
                break
            case Tool.DROIDMATE:
                return new DroidmaterRunner(apks);
                break
        }
    }

    public void start(){
        apks.each{
            beforeEachApk(it);
            testApk(it);
            done(it)
        }
    }

    def done(APK apk) {}

    public abstract void testApk(APK apk);

    public void beforeEachApk(APK apk) {

    }
}
