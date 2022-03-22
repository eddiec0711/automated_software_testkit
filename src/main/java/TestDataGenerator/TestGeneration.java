package TestDataGenerator;

import java.util.Random;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author Ken Lok Chan
 * TestDataGenerator.TestGeneration.java
 *
 * class that generate random data
 * based on the inputs
 */

public class TestGeneration{

    private int numOfParameter;
    private Object[] typeOfParameter;
    private Object[][] rangeOfParameter = null;
    private int iteration = 100;

    private Random r = new Random();
    private BasicTestGeneration bT = new BasicTestGeneration();

    //first is the number of parameter
    //second is an array of the type name of the parameter
    //third is 2D array of the range type of parameter
    //fourth is setting own iteration for test data generation
    public TestGeneration(int numParameter, Object[] typeParameter){
        this.numOfParameter = numParameter;
        this.typeOfParameter = typeParameter;
    }

    public TestGeneration(int numParameter, Object[] typeParameter, Object[][] rangeParameter){
        this(numParameter, typeParameter);
        this.rangeOfParameter = rangeParameter;
    }

    public TestGeneration(int numParameter, Object[] typeParameter, Object[][] rangeParameter, int i){
        this(numParameter, typeParameter, rangeParameter);
        this.iteration = i;
    }

    // detecting an object type but with range
    private Object detectObjectType(Object e, Object[] range) {

        if ((e instanceof Integer) && (range != null)) {
            int a = (int) range[0];
            int b = (int) range[1];
            return bT.rangeDataInt(a, b);
        }else if ((e instanceof Long) && (range != null)) {
            Long a = (Long) range[0];
            Long b = (Long) range[1];
            return bT.rangeDataLong(a,b);
        }else if ((e instanceof Double) && (range != null)) {
            double a = (double) range[0];
            double b = (double) range[1];
            return bT.rangeDataDouble(a,b);
        }else if ((e instanceof Float) && (range != null)) {
            float a = (float) range[0];
            float b = (float) range[1];
            return bT.rangeDataFloat(a,b);
        } else if(e instanceof Character && (range != null)) {
            char b = (char) range[0];
            char c = (char) range[1];
            return bT.randomCharRange(b,c);
        } else if(e instanceof String && (range != null)) {
            boolean a = (boolean) range[0];
            int b = (int) range[1];
            return bT.randomString(b, a);
        }
        else {
            return detectObjectType(e);
        }

    }

    // detecting object type with no range
    private Object detectObjectType(Object e) {

        if(e instanceof Enum) {
            return r.nextInt();
        } else if(e instanceof Integer) {
            return r.nextInt();
        } else if(e instanceof Long) {
            return r.nextLong();
        } else if(e instanceof Double) {
            return r.nextDouble();
        } else if(e instanceof Float) {
            return r.nextFloat();
        } else if(e instanceof Boolean) {
            return r.nextBoolean();
        } else if(e instanceof Character) {
            return bT.randomChar(true);
        } else if(e instanceof String) {
            return bT.randomString(6,false);
        } else if(e instanceof BigInteger){
            return BigInteger.valueOf(r.nextInt());
        } else{
            return "Type is not clear";
        }

    }

    // finalize and generate data base on arraylist
    public ArrayList<ArrayList<Object>> generateRandomTestData() {
        ArrayList<ArrayList<Object>> result = new ArrayList<>();
        for(int i = 0; i < iteration ; i++){
            ArrayList<Object> smaller = new ArrayList<>();
            for(int j = 0; j < this.numOfParameter ; j++){
                if(rangeOfParameter == null) {
                    smaller.add(this.detectObjectType(typeOfParameter[j]));
                }else{
                    smaller.add(this.detectObjectType(typeOfParameter[j], rangeOfParameter[j]));
                }
            }
            result.add(smaller);
        }
        return result;
    }

}
