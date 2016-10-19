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

    def processFile(String fname) {
        def methods = new HashSet()

        new File("tmp/${fname}").eachLine {
            def name = getMethodName(it)
            name && methods << name;
        }
        /*output = new File("../output/" + fname.split(Pattern.quote('.'))[0] + '-uniq.txt')

        methods.each {
            output << it + '\n'
        }*/

        res.put(fname.split(Pattern.quote('.'))[0].split(Pattern.quote("_")).last(), methods.size());
    }


    def processFiles() {
        def list = new File('./tmp').list()
        list.each {
            if (!it.startsWith('.')) processFile(it)
        }

        return res;
    }
}
