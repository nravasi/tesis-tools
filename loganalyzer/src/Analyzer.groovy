import java.util.regex.Pattern

/**
 * Created by nmravasi on 9/22/16.
 */
pattern = Pattern.compile(".*objCls: ([^ ]*) mthd: ([^ ]*) .*")

res = new HashMap();

def getMethodName(String s) {
    def match = pattern.matcher(s);
    if (match.matches()) {
        return "${match.group(1)}.${match.group(2)}"
    }
}

def processFile(String fname) {
    def methods = new HashSet()

    new File('../input/' + fname).eachLine {
        methods << getMethodName(it);
    }
    println fname
    println  fname.split()
    output = new File("../output/" + fname.split(Pattern.quote('.'))[0] + '-uniq.txt')

    methods.each {
        output << it + '\n'
    }

    res.put(fname.split(Pattern.quote('.'))[0], methods.size());
}


def list = new File('../input').list()
list.each {
    if (!it.startsWith('.')) processFile(it)
}


def file = new File("../output/summary.txt")
res.each {
    file << it.key + '\t' + it.value + '\n'
}