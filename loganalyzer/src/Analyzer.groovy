import java.util.regex.Pattern

/**
 * Created by nmravasi on 9/22/16.
 */
pattern = Pattern.compile(".*objCls: ([^ ]*) mthd: ([^ ]*) .*")

def getMethodName(String s) {
    def match = pattern.matcher(s);
    if (match.matches()) {
        return "${match.group(1)}.${match.group(2)}"
    }
}

def methods = new HashSet()

new File('../logsout.txt').eachLine {
    methods << getMethodName(it);
}

output = new File("../analyzed.txt")

methods.each {
    output << it + '\n'
}

