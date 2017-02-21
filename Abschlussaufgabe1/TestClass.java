package edu.kit.informatik;

import static edu.kit.informatik.Terminal.*;

public class TestClass {

    public static void basicTest() {
        initTestSession(() -> CommandExecutor.main(new String[0]));
        testOutput("add author test,author", "Ok");
        testOutput("add author second,author", "Ok");
        testOutput("add conference series TEST", "Ok");
        testOutput("add conference TEST,2000,Testheim", "Ok");
        testOutput("add conference TEST,2010,Testhausen", "Ok");
        testOutput("add journal testPAPERS,the Testers", "Ok");
        nextInput("all publications");
        testOutput("add article to series TEST:test00,2000,first test", "Ok");
        testOutput("add article to series TEST:test01,2000,second test", "Ok");
        testOutput("add article to journal testPAPERS:test02,2002,third test", "Ok");

        testList("in proceedings TEST,2000", false, true, "test00", "test01");
        testList("all publications", false, true, "test00", "test01", "test02");
        testList("list invalid publications", false, true, "test00", "test01", "test02");
        testOutput("written-by test00,test author", "Ok");
        testOutput("written-by test01,test author", "Ok");
        testOutput("written-by test01,second author", "Ok");
        testOutput("list invalid publications", "test02");
        testOutput("publications by second author", "test01");
        testList("publications by test author;second author", false, true, "test00", "test01");
        testPrefix("written-by test00,test author;second author", "Error, ");
        testPrefix("written-by test01,test author", "Error, ");

        testOutput("quit", "Ok");
        assertExit();
        System.out.println("--- Basic Test finished ---");
        System.out.println();
    }

    public static void syntaxTest() {
        initTestSession(() -> CommandExecutor.main(new String[0]));
        testOutput("add author test,author", "Ok");
        testOutput("add author second,author", "Ok");
        testOutput("add conference series TEST", "Ok");
        testOutput("add conference TEST,2000,Testheim", "Ok");
        testOutput("add journal testPAPERS,the Testers", "Ok");
        testOutput("add article to series TEST:test00,2000,first test", "Ok");
        testOutput("add article to journal testPAPERS:test02,2002,third test", "Ok");

        testPrefix("add author Troll", "Error, ");
        testPrefix("illegal -", "add author T-T,Troll", "Error, ");
        testPrefix("add conference TEST,2002,", "Error, ");
        testPrefix("trailing whitespace", "all publications ", "Error, ");
        testPrefix("leading whitespace", " list invalid publications", "Error, ");
        testPrefix("whitespace between", "add  author third,author", "Error, ");
        testPrefix("publications by ", "Error, ");
        testPrefix("publications by test author;second author;", "Error, ");
        testPrefix("publications by ;test author;second author", "Error, ");
        testPrefix("find keywords ;", "Error, ");
        testPrefix("publications by test author,second author", "Error, ");
        testPrefix("find keywords test:second", "Error, ");
        testPrefix("publications by test author;Troll ", "Error, ");
        testPrefix("add article to series TEST,test01,2000,second test", "Error, ");

        testPrefix("add conference TEST,999,Testheim", "Error, ");
        testPrefix("add conference TEST,999.0,Testheim", "Error, ");
        testPrefix("add conference TEST,-1999,Testheim", "Error, ");
        testPrefix("add conference TEST,10000,Testheim", "Error, ");
        testPrefix("add conference TEST,0999,Testheim", "Error, ");
        testPrefix("leading zero", "add conference TEST,02000,Testheim", "Error, ");
        testPrefix("direct h-index -1;0", "Error, ");
        testPrefix("direct h-index 01", "Error, ");
        testPrefix("direct h-index 1;1.0", "Error, ");
        testPrefix("direct print conference ieee:T Test,,,Flow Control,TCS,KIT,999", "Error, ");
        testPrefix("direct print conference ieee:T Test,,,Flow Control,TCS,KIT,-1999", "Error, ");
        testPrefix("leading zero", "direct print conference ieee:T Test,,,Flow Control,TCS,KIT,02000", "Error, ");

        testPrefix("add conference TES,2000,Testheim", "Error, ");
        testPrefix("add article to journal test:xyz,2005,x", "Error, ");
        testPrefix("add article to journal testPAPERS:testX,2010,X", "Error, ");
        testPrefix("cites test00,xyz", "Error, ");
        testPrefix("add keywords to pub test:a;b", "Error, ");
        testPrefix("in proceedings TEST,1999", "Error, ");
        testPrefix("coauthors of X Y", "Error, ");
        testPrefix("print bibliography ieee:test00;test02;test", "Error, ");

        testOutput("add article to series TEST:test01,2000,second test", "Ok");
        testList("in proceedings TEST,2000", false, true, "test00", "test01");
        testList("all publications", false, true, "test00", "test01", "test02");
        testList("list invalid publications", false, true, "test00", "test01", "test02");

        testOutput("add journal OO: Architecture & Design,By: The Nerds", "Ok");
        testOutput("add article to journal OO: Architecture & Design:ooad,1000,X:treme", "Ok");
        testOutput("add keywords to journal OO: Architecture & Design:hc", "Ok");
        testPrefix("add article to journal OO: Architecture & Design xxad,2000,X:treme", "Error, ");

        testOutput("quit", "Ok");
        assertExit();
        System.out.println("--- Syntax Test finished ---");
        System.out.println();
    }

    public static void entityTest() {
        initTestSession(() -> CommandExecutor.main(new String[0]));
        testOutput("add conference series series", "Ok");
        testOutput("add conference series,2000,conf@1", "Ok");
        testOutput("add article to series series:cona1,2000,first at conference", "Ok");
        testOutput("in proceedings series,2000", "cona1");
        testOutput("add conference series series test", "Ok");
        testOutput("add conference series test,2000,Test", "Ok");
        testOutput("add article to series series test:test,2000,second", "Ok");
        testOutput("in proceedings series test,2000", "test");

        testOutput("add conference series,2002,conf@2", "Ok");
        testOutput("add journal conference journal,@-@", "Ok");
        testOutput("add journal journal@series,J.Smith & co.", "Ok");
        testOutput("add article to series series:cona2,2000,second at conference", "Ok");
        testOutput("add article to series series:cona3,2002,third at conference", "Ok");
        testOutput("add article to journal conference journal:joura1,2005,journal_first", "Ok");
        testOutput("add article to journal conference journal:joura2,1997,journal_second", "Ok");
        testOutput("add article to journal journal@series:joura3,2003,journal_third", "Ok");

        testOutput("add keywords to pub joura1:jrnl", "Ok");
        testOutput("add keywords to journal conference journal:jrnl", "Ok");
        testOutput("add keywords to series series:ser;mass", "Ok");
        testOutput("add keywords to conference series,2000:confi", "Ok");
        testOutput("add keywords to pub joura1:confi", "Ok");
        testOutput("add keywords to pub cona1:jrnl;mass", "Ok");

        testList("find keywords jrnl", false, true, "joura1", "joura2", "cona1");
        testList("find keywords ser;mass", false, true, "cona3", "cona2", "cona1");
        testList("find keywords confi", false, true, "joura1", "cona2", "cona1");
        testOutput("find keywords ser;mass;jrnl", "cona1");

        testPrefix("add keywords to conference conference journal:jrnl", "Error, ");
        testPrefix("add keywords to conference series:ser;mass", "Error, ");
        testPrefix("add keywords to journal series:ser;mass", "Error, ");
        testPrefix("add keywords to series series,2000:confi", "Error, ");
        testPrefix("add keywords to pubjoura1:confi", "Error, ");
        testPrefix("add keywords to publication cona1:jrnl", "Error, ");
        testPrefix("add keywords to pub joura2:Confi", "Error, ");

        testOutput("quit", "Ok");
        assertExit();
        System.out.println("--- Entity Test finished ---");
        System.out.println();
    }

    public static void jaccardTest() {
        initTestSession(() -> CommandExecutor.main(new String[0]));
        testOutput("jaccard a;b a", "0.500");
        testOutput("jaccard key;word abc;def;key", "0.250");
        testOutput("jaccard key;word a;b", "0.000");

        testOutput("add conference series HofIndex", "Ok");
        testOutput("add conference HofIndex,2000,Jaccardhausen", "Ok");
        testOutput("add article to series HofIndex:low,2000,a b", "Ok");
        testOutput("add article to series HofIndex:medium,2000,b c key word", "Ok");
        testOutput("add article to series HofIndex:high,2000,a b key word jacc", "Ok");
        testOutput("add article to series HofIndex:mix,2000,c key jacc", "Ok");
        testOutput("add keywords to pub medium:b;c;key;word", "Ok");
        testOutput("add keywords to pub high:b;word;a;key;jacc", "Ok");
        testOutput("similarity low,medium", "0.000");
        testOutput("similarity mix,low", "1.000");

        testOutput("add keywords to pub low:a;b", "Ok");
        testOutput("add keywords to pub mix:c;key;jacc", "Ok");
        testOutput("similarity low,medium", "0.200");
        testOutput("similarity mix,low", "0.000");
        testOutput("similarity high,medium", "0.500");
        testOutput("similarity high,mix", "0.333");
        testOutput("similarity low,high", "0.400");
        testOutput("similarity medium,mix", "0.400");

        testOutput("quit", "Ok");
        assertExit();
        System.out.println("--- Jaccard Test finished ---");
        System.out.println();
    }

    public static void hIndexTest() {
        initTestSession(() -> CommandExecutor.main(new String[0]));
        testOutput("direct h-index 2;3;1", "2");
        testOutput("direct h-index 4;7;2;9;5", "4");
        testOutput("direct h-index 0;0;0", "0");
        testOutput("direct h-index 100", "1");

        testOutput("add journal HofIndex,The Indexers", "Ok");
        testOutput("add article to journal HofIndex:zero1,2010, 0", "Ok");
        testOutput("add article to journal HofIndex:zero2,2010, 0", "Ok");
        testOutput("add article to journal HofIndex:zero3,2010, 0", "Ok");
        testOutput("add article to journal HofIndex:low,2008, 1", "Ok");
        testOutput("add article to journal HofIndex:medium,2005, 3", "Ok");
        testOutput("add article to journal HofIndex:high,2003, 4", "Ok");
        testOutput("add article to journal HofIndex:max,2000, 5", "Ok");
        testOutput("cites zero1,medium", "Ok");
        testOutput("cites zero1,high", "Ok");
        testOutput("cites zero1,max", "Ok");
        testOutput("cites zero2,high", "Ok");
        testOutput("cites zero2,max", "Ok");
        testOutput("cites zero3,low", "Ok");
        testOutput("cites zero3,medium", "Ok");
        testOutput("cites zero3,high", "Ok");
        testOutput("cites low,medium", "Ok");
        testOutput("cites low,max", "Ok");
        testOutput("cites medium,high", "Ok");
        testOutput("cites medium,max", "Ok");
        testOutput("cites high,max", "Ok");

        testOutput("add author casual,author", "Ok");
        testOutput("add author pro,author", "Ok");
        testOutput("h-index casual author", "0");
        testOutput("written-by zero1,casual author;pro author", "Ok");
        testOutput("written-by zero2,casual author", "Ok");
        testOutput("h-index casual author", "0");
        testOutput("written-by low,casual author", "Ok");
        testOutput("h-index casual author", "1");
        testOutput("written-by medium,pro author;casual author", "Ok");
        testOutput("written-by high,pro author", "Ok");
        testOutput("h-index pro author", "2");
        testOutput("written-by max,pro author;casual author", "Ok");
        testOutput("written-by zero3,pro author", "Ok");
        testOutput("h-index pro author", "3");
        testOutput("h-index casual author", "2");

        testOutput("quit", "Ok");
        assertExit();
        System.out.println("--- h-index Test finished ---");
        System.out.println();
    }

    public static void coauthorCitationTest() {
        initTestSession(() -> CommandExecutor.main(new String[0]));
        testOutput("add journal BigJ,The Indexers", "Ok");
        testOutput("add article to journal BigJ:pub1,2010, a b", "Ok");
        testOutput("add article to journal BigJ:pub2,2010, b", "Ok");
        testOutput("add article to journal BigJ:pub3,2010, c", "Ok");
        testOutput("add article to journal BigJ:low,2008, d", "Ok");
        testOutput("add article to journal BigJ:medium,2005, a", "Ok");
        testOutput("add article to journal BigJ:high,2003, b c", "Ok");
        testOutput("add article to journal BigJ:max,2000, b c", "Ok");
        testOutput("add author author,A", "Ok");
        testOutput("add author author,B", "Ok");
        testOutput("add author author,C", "Ok");
        testOutput("add author author,D", "Ok");

        nextInput("coauthors of author B");
        nextInput("foreign citations of author A");
        testOutput("written-by pub1,author A;author B", "Ok");
        testOutput("written-by pub2,author B", "Ok");
        testOutput("written-by pub3,author C", "Ok");
        testOutput("written-by low,author D", "Ok");
        testOutput("written-by medium,author A", "Ok");
        testOutput("written-by high,author C;author B", "Ok");
        testOutput("written-by max,author C;author B", "Ok");
        testList("coauthors of author A", false, true, "author B");
        testList("coauthors of author B", false, true, "author A", "author C");
        testList("coauthors of author C", false, true, "author B");
        nextInput("coauthors of author D");
        nextInput("foreign citations of author C");

        testOutput("cites pub1,low", "Ok");
        testOutput("cites pub1,high", "Ok");
        testOutput("cites pub1,max", "Ok");
        nextInput("foreign citations of author B");
        testOutput("foreign citations of author D", "pub1");
        testOutput("cites pub2,max", "Ok");
        testOutput("cites pub2,low", "Ok");
        testOutput("cites pub3,low", "Ok");
        testOutput("cites pub3,medium", "Ok");
        testOutput("cites low,max", "Ok");
        testOutput("cites medium,high", "Ok");
        testOutput("cites high,max", "Ok");
        testOutput("foreign citations of author A", "pub3");
        testOutput("foreign citations of author B", "low");
        testList("foreign citations of author C", false, true, "low", "medium");
        testList("foreign citations of author D", false, true, "pub1", "pub2", "pub3");
        testPrefix("cites low,low", "Error, ");
        testPrefix("cites high,medium", "Error, ");
        testPrefix("cites high,max", "Error, ");
        testPrefix("cites pub2,low", "Error, ");

        testOutput("quit", "Ok");
        assertExit();
        System.out.println("--- Coauthors/Citation Test finished ---");
        System.out.println();
    }

    public static void bibliographyTest() {
        initTestSession(() -> CommandExecutor.main(new String[0]));
        testOutput("add author Sergey,Brin", "Ok");
        testOutput("add author Lawrence,Page", "Ok");
        testOutput("add author Richard,Rhinelander", "Ok");
        testOutput("add author Emiola,Lowry", "Ok");
        testOutput("add conference series JAVA CON", "Ok");
        testOutput("add journal Computer Science,KIT", "Ok");
        testOutput("add conference JAVA CON,2000,Berlin", "Ok");
        testOutput("add conference JAVA CON,2010,Karlsruhe", "Ok");
        testOutput("add article to series JAVA CON:b00,2000,Basics", "Ok");
        testOutput("written-by b00,Lawrence Page", "Ok");
        testOutput("add article to journal Computer Science:oo08,2008,Object Orientated Programming", "Ok");
        testOutput("written-by oo08,Lawrence Page", "Ok");
        testOutput("add article to journal Computer Science:f02,2002,Functional Programming", "Ok");
        testOutput("written-by f02,Lawrence Page;Sergey Brin", "Ok");
        testOutput("add article to series JAVA CON:d10,2010,Design", "Ok");
        testOutput("written-by d10,Richard Rhinelander;Lawrence Page;Sergey Brin", "Ok");
        testOutput("add article to series JAVA CON:a10,2010,Architecture", "Ok");
        testOutput("written-by a10,Richard Rhinelander;Emiola Lowry;Lawrence Page;Sergey Brin", "Ok");
        testOutput("add article to journal Computer Science:t99,1999,Troll", "Ok");

        testOutput("direct print conference ieee:Sergey Brin,Richard Rhinelander,,Flow Control,TCS,KIT Karlsruhe,2012",
                "[1] S. Brin and R. Rhinelander, \"Flow Control,\" in Proceedings of TCS, KIT Karlsruhe, 2012.");
        testOutput("direct print journal chicago:Lawrence Page,,,Data Management,KIT Journal,2015",
                "(Page, 2015) Page, Lawrence. \"Data Management.\" KIT Journal (2015).");
        testOutput(
                "direct print conference chicago:Sam Brin,Law Page,Emiola Lowry,Syntax and Semantik,TCS,Karlsruhe,2018",
                "(Brin, 2018) Brin, Sam, Page, Law, and Lowry, Emiola. \"Syntax and Semantik.\" Paper presented at TCS, 2018, Karlsruhe.");
        testList("print bibliography ieee:d10;b00;oo08", false, false,
                "[1] L. Page, \"Basics,\" in Proceedings of JAVA CON, Berlin, 2000.",
                "[2] L. Page, \"Object Orientated Programming,\" Computer Science, 2008.",
                "[3] R. Rhinelander et al., \"Design,\" in Proceedings of JAVA CON, Karlsruhe, 2010.");
        testList("print bibliography chicago:a10;oo08;f02", false, false,
                "(Page, 2008) Page, Lawrence. \"Object Orientated Programming.\" Computer Science (2008).",
                "(Page, 2002) Page, Lawrence, and Brin, Sergey. \"Functional Programming.\" Computer Science (2002).",
                "(Rhinelander, 2010) Rhinelander, Richard, Lowry, Emiola, Page, Lawrence, and Brin, Sergey. \"Architecture.\" Paper presented at JAVA CON, 2010, Karlsruhe.");
        testPrefix("Invalid publication contained!", "print bibliography ieee:a10;d10;t99", "Error, ");
        testPrefix("zero authors", "direct print journal ieee:,,,Author Test,ZERO,1000", "Error, ");
        testPrefix("Illegal year value!", "direct print journal chicago:Lawrence Page,,,Year Test,Medieval,800",
                "Error, ");
        testPrefix("Illegal style value!", "print bibliography ieeee:a10", "Error, ");

        testOutput("quit", "Ok");
        assertExit();
        System.out.println("--- Bibliography Test finished ---");
        System.out.println();
    }

    public static void main(String[] args) {
        setPrintPolicy(PRINT_FAILURES);
        // setCancelPolicy(CANCEL_AT_MISMATCH);
        runCancelingTest(TestClass::basicTest);
        runCancelingTest(TestClass::syntaxTest);
        runCancelingTest(TestClass::entityTest);
        runCancelingTest(TestClass::jaccardTest);
        runCancelingTest(TestClass::hIndexTest);
        runCancelingTest(TestClass::coauthorCitationTest);
        runCancelingTest(TestClass::bibliographyTest);
    }
}
