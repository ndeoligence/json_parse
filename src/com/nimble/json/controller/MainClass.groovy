package com.nimble.json.controller

import com.nimble.json.model.JsonParse
import groovy.json.JsonOutput

public class MainClass {
    static final String path='resources/input'
    static List<String> nodeCaptions=[];
    static file_out=new File('resources/output/out.txt')

    public static void main(String[] args) {
        def dir = new File(path)
        dir.eachFile {
            getOutcomeCaptions(it,nodeCaptions)
            it.write(prettyfy(it.text))
        }
        removeDuplicates(nodeCaptions)
        file_out.write(""); // clears previous file contents
        listToFile(nodeCaptions, file_out)

    }
    private static void getOutcomeCaptions(File inf,Collection lst) {
        lst.addAll(new JsonParse(inf.text).getOutcomes())
    }
    private static String prettyfy(String jsonStr) {
        return JsonOutput.prettyPrint(jsonStr)
    }
    private static void listToFile(Collection lst, File outf) {
        lst.each {
            outf.append(it.toString())
            outf.append("\n")
        }
    }
    private static void removeDuplicates(Collection<String> lst) {
        Set<String> set=new HashSet()
        set.addAll lst
        lst.clear()
        lst.addAll set
    }
}
