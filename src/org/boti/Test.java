package org.boti;

import de.java2html.Java2Html;

public class Test
{
    public static void main(String []args){
        String egyik = Java2Html.convertToHtml("if('egyik'.equals){System.out.println('masik');}");
        System.out.println(egyik);
    }
}
