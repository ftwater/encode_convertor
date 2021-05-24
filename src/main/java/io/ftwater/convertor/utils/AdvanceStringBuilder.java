package io.ftwater.convertor.utils;

public class AdvanceStringBuilder {
    private StringBuilder builder = new StringBuilder();
    private static final String TAB = "    ";


    public void appendEndOfNewLine(String str){
        builder.append(str);
        builder.append("\n");
    }
    public void appendStartWithTab(String str){
        builder.append(TAB);
    }

    public String toString(){
        return builder.toString();
    }
}
