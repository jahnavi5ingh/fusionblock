
package com.sumit.aistudio.backend.ptl;

import java.lang.reflect.Method;

public class TemplateParserTest {

    public static void main(String[] args) {
        try {
            // Create an instance of the TemplateParserTest class
            TemplateParserTest testInstance = new TemplateParserTest();

            // Get all methods declared in the TemplateParserTest class
            Method[] methods = TemplateParserTest.class.getDeclaredMethods();

            // Iterate over the methods and invoke those that match "testCase" prefix
            for (Method method : methods) {
                if (method.getName().startsWith("testCase")) {
                    System.out.println("Running " + method.getName() + "...");
                    method.invoke(testInstance);
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void testCase1_1() {
        String input = "//#START:temp2:template3:model1";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase1_2() {
        String input = "//#START:1:template(\"m\",true,[\"a\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }
    public void testCase1() {
        String input = "//#START:1:template(\"m\",true,[\"a\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase2() {
        String input = "//#START:2:template2(\"model1\",false,[\"tag1\",\"tag2\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase3() {
        String input = "//#START:3:template3(\"x\",true,[\"alpha\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase4() {
        String input = "//#START:4:template4(\"model2\",false,[\"beta\",\"gamma\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase5() {
        String input = "//#START:5:template5(\"model3\",true,[\"delta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase6() {
        String input = "//#START:6:template6(\"y\",false,[\"epsilon\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase7() {
        String input = "//#START:7:template7(\"model4\",true,[\"zeta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase8() {
        String input = "//#START:8:template8(\"model5\",false,[\"eta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase9() {
        String input = "//#START:9:template9(\"a\",true,[\"theta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase10() {
        String input = "//#START:10:template10(\"model6\",false,[\"iota\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase11() {
        String input = "//#START:11:template11(\"model7\",true,[\"kappa\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase12() {
        String input = "//#START:12:template12(\"b\",false,[\"lambda\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase13() {
        String input = "//#START:13:template13(\"model8\",true,[\"mu\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase14() {
        String input = "//#START:14:template14(\"model9\",false,[\"nu\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase15() {
        String input = "//#START:15:template15(\"c\",true,[\"xi\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase16() {
        String input = "//#START:16:template16(\"model10\",false,[\"omicron\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase17() {
        String input = "//#START:17:template17(\"model11\",true,[\"pi\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase18() {
        String input = "//#START:18:template18(\"d\",false,[\"rho\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase19() {
        String input = "//#START:19:template19(\"model12\",true,[\"sigma\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase20() {
        String input = "//#START:20:template20(\"model13\",false,[\"tau\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase21() {
        String input = "//#START:abc:template21(\"model14\",true,[\"alpha\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase22() {
        String input = "//#START:def:template22(\"model15\",false,[\"beta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase23() {
        String input = "//#START:ghi:template23(\"model16\",true,[\"gamma\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase24() {
        String input = "//#START:jkl:template24(\"model17\",false,[\"delta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase25() {
        String input = "//#START:mno:template25(\"model18\",true,[\"epsilon\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase26() {
        String input = "//#START:pqr:template26(\"model19\",false,[\"zeta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase27() {
        String input = "//#START:stu:template27(\"model20\",true,[\"eta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase28() {
        String input = "//#START:vwx:template28(\"model21\",false,[\"theta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase29() {
        String input = "//#START:yz:template29(\"model22\",true,[\"iota\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase30() {
        String input = "//#START:ABC:template30(\"model23\",false,[\"kappa\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }
    public void testCase31() {
        String input = "//#START:a1b2:template31(\"model24\",true,[\"alpha\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase32() {
        String input = "//#START:123abc:template32(\"model25\",false,[\"beta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase33() {
        String input = "//#START:xyz789:template33(\"model26\",true,[\"gamma\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase34() {
        String input = "//#START:456def:template34(\"model27\",false,[\"delta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase35() {
        String input = "//#START:ghi012:template35(\"model28\",true,[\"epsilon\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase36() {
        String input = "//#START:abc123xyz:template36(\"model29\",false,[\"zeta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase37() {
        String input = "//#START:7up:template37(\"model30\",true,[\"eta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase38() {
        String input = "//#START:m1n2:template38(\"model31\",false,[\"theta\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase39() {
        String input = "//#START:abc999:template39(\"model32\",true,[\"iota\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    public void testCase40() {
        String input = "//#START:0abc:template40(\"model33\",false,[\"kappa\"]);";
        ParsedTemplate result = PromptTemplateCmdParser.parseTemplate(input);
        printResult(result);
    }

    // Method to print the result
    private void printResult(ParsedTemplate result) {
        System.out.println(result);
    }
}
