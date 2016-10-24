package logger

import java.util.regex.Pattern

/**
 * Created by nmravasi on 10/19/16.
 */
class LogAnalyzer {

    def pattern = Pattern.compile(".*objCls: ([^ ]*) mthd: ([^ ]*) .*")
    def res = new HashMap();

    def getMethodName(String s) {
        def match = pattern.matcher(s);
        if (match.matches()) {
            return "${match.group(1)}.${match.group(2)}"
        }
    }

    def processFile(File file) {
        def methods = new HashSet()

        file.eachLine {
            def name = getMethodName(it)
            name && methods << name;
        }
        /*output = new File("../output/" + fname.split(Pattern.quote('.'))[0] + '-uniq.txt')

        methods.each {
            output << it + '\n'
        }*/

        res.put(file.name.split(Pattern.quote('.'))[0].split(Pattern.quote("_")).last(), methods.size());

        file.delete()
    }


    def processFiles() {
        def list = new File('./tmp').listFiles()
        list.each {
            if (!it.name.startsWith('.')) processFile(it)
        }

        return res;
    }
}
