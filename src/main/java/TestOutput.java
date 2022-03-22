/** @author Ken Lok Chan
 * this class help to write test output
 * parameter could be editted by the user later on
 **/
import java.util.*;

public class TestOutput{

    List<String> resultList;
    String className = "exampleClass";
    String method = "exampleMethod";

    public TestOutput(List<String> resultList, String className, String method){
        this.resultList = resultList;
        this.className = className;
        this.method = method;
    }

    public TestOutput(List<String> resultList){
        this.resultList = resultList;
    }

    private void changeClassName(String name){
        this.className = name;
    }

    private String showClassName(){
        return this.className;
    }

    // typing the pseudo method for later on
    private String writeTestI(String method, int number){
        String methodTestName = "exampleTest" + number; // this is fixed
        String res = "@Test" + "\n" + "public void "+methodTestName+"(){"+"\n\t"+method+"\n"+"}" ;
        return res;
    }


    // typing in the condition to create conditions of the results
    private List<String> createMethod(String parameter,String result){
        List<String> list=new ArrayList<>();
        String className = this.className;

        String method1 = "assertTrue("+ result + " == " + className + "." + this.method + "(" + parameter + "));";
        String method2 = "assertFalse("+ result + " != " + className + "." + this.method + "(" + parameter + "));";

        list.add(method1);
        list.add(method2);

        return list;
    }

    // to generate strings for user to type default
    public String generateTestOutput(){
        String result = "public class "+ this.showClassName()+" { \n\n";
        List<String> finalList = new ArrayList<>();

        for( String res : this.resultList){
            List<String> tempList = new ArrayList<>();
            tempList = this.createMethod("parameter", res);
            finalList.addAll(tempList);
        }

        int i = 0;
        for( String f : finalList){
            String temp = this.writeTestI(f, i);
            i++;
            result = result + temp + "\n";
        }

        result = result + "\n}";

        return result;
    }

}
