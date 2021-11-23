package eu.qped.umr.configs;

import java.util.ArrayList;

public final class ConfiguratorUtility {


    private ConfiguratorUtility() {
    }

    private final static ArrayList<String> begCodeWords = new ArrayList<>();
    private final static ArrayList<String> advCodeWords = new ArrayList<>();
    private final static ArrayList<String> proCodeWords = new ArrayList<>();

    static {
        begCodeWords.add("b");
        begCodeWords.add("beginner");
        begCodeWords.add("anfänger");
        begCodeWords.add("1");
        begCodeWords.add("beg");
        begCodeWords.add("anf");

        advCodeWords.add("a");
        advCodeWords.add("advanced");
        advCodeWords.add("adv");
        advCodeWords.add("2");
        advCodeWords.add("fortgeschritten");
        advCodeWords.add("fort");

        proCodeWords.add("profi");
        proCodeWords.add("professional");
        proCodeWords.add("3");
        proCodeWords.add("pro");
        proCodeWords.add("parseXML");

    }

    public static ArrayList<String> getBegCodeWords() {
        return begCodeWords;
    }

    public static ArrayList<String> getAdvCodeWords() {
        return advCodeWords;
    }

    public static ArrayList<String> getProCodeWords() {
        return proCodeWords;
    }


}
