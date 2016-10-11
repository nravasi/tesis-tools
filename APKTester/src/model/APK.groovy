package model

import configuration.Command

import java.util.regex.Pattern

/**
 * Created by nmravasi on 10/9/16.
 */
class APK {

    static Pattern packageNamePattern = Pattern.compile('^package: name=\'(.*)\' versionCode.*$', Pattern.MULTILINE)
    static Pattern appnamePattern = Pattern.compile('^application-label:\'(.*)\'$', Pattern.MULTILINE)

    File file;
    String appName;
    String packageName;

    APK(File file) {
        this.file = file;
        setNames(file)
    }


    private String setNames(File file){

        def proc =  Command.run('aapt d badging ' + file.toString())

        def text = proc.text
        def matcher = appnamePattern.matcher(text)

        if (matcher.find()) {
            appName = matcher.group(1);
        }

        matcher = packageNamePattern.matcher(text)

        if (matcher.find()) {
            packageName = matcher.group(1);
        }
    }
}
