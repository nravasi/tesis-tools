package model

import configuration.Command

import java.util.regex.Pattern

/**
 * Created by nmravasi on 10/9/16.
 */
class APK {

    static Pattern namePattern = Pattern.compile('^application-label:\'(.*)\'$', Pattern.MULTILINE)

    File file;
    String appname;

    APK(File file) {
        this.file = file;
        appname = getAppname(file)
    }


    static String getAppname(File file){

        def proc =  Command.run('aapt d badging ' + file.toString())

        def matcher = namePattern.matcher(proc.text)

        if (matcher.find()) {
            return matcher.group(1);
        }
    }
}
