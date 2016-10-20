package configuration
/**
 * Created by nmravasi on 10/8/16.
 */
public class Config {

    public static String DROIDMATE_DIR =  '/Users/nmravasi/dev/thesis/droidmate/dev/droidmate/';
    public static String THIS_PATH =  '/Users/nmravasi/dev/thesis/tesis-tools/APKTester/';
    public static String SAPIENZ_DIR =  '';
    public static String MONKEY_RUNNER_DIR =  '';
    public static String MONITOR_APK_DIR_IN_DEVICE =  '';
    public static String SD_PATH =  '/data/media/0/';
    public static String APKS_PATH =  'apks';
    public static String SDK = "C:\\Users\\Ignacio\\AppData\\Local\\Android\\Sdk\\tools";
    public static String ADV_NAME = "Nexus_7_2012_Edited_API_19";

    public static int minutes =  3;

    public static boolean shouldInline = false;
    public static Tool TOOL_TO_USE = Tool.MONKEY;

    //Way until kill monkey 30 min would be 18000000 (in milliseconds )
    public static Long TIMEOUT_BEFORE_KILL = 10000;
}
