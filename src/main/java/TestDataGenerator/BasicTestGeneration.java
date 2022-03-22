package TestDataGenerator;

import java.util.Random;
/**
 * @author Ken Lok Chan
 * TestDataGenerator.BasicTestGeneration.java
 *
 * class that generate random data with random module
 * it enable user to generate basic method without initialize random.
 */

public class BasicTestGeneration {

    private Random r = new Random();

    public int randomData() {
        return r.nextInt();
    }

    // data that is random in a range
    public int rangeDataInt(int lower, int upper){
        if(lower == Integer.MIN_VALUE && upper == Integer.MAX_VALUE) {
            return r.nextInt();
        }else {
            return r.ints(lower,upper+1).findFirst().getAsInt();
        }
    }

    public float rangeDataFloat(float lower, float upper){
        if(lower == Integer.MIN_VALUE && upper == Integer.MAX_VALUE) {
            return r.nextFloat();
        }else {
            float res = lower + r.nextFloat() * (upper-lower);
            return res;
        }
    }

    public Long rangeDataLong(Long lower, Long upper){
        if(lower == Integer.MIN_VALUE && upper == Integer.MAX_VALUE) {
            return r.nextLong();
        }else {
            Long res = lower + r.nextLong() * (upper-lower);
            return res;
        }
    }

    public double rangeDataDouble(double lower, double upper){
        if(lower == Integer.MIN_VALUE && upper == Integer.MAX_VALUE) {
            return r.nextDouble();
        }else {
            double res = lower + r.nextDouble() * (upper-lower);
            return res;
        }
    }

    // data with other type
    public float randomDataFloat(){
        return r.nextFloat();
    }

    public Long randomDataLong(){
        return r.nextLong();
    }

    public boolean randomDataBoolean(){
        return r.nextBoolean();
    }

    public double randomDataDouble(){
        return r.nextDouble();
    }


    // generate random char true for lower case, false for upper case
    public char randomChar(boolean condition){
        char c;
        if(condition) {
            c = (char) (r.nextInt(26) + 'a');
        }
        else{
            c = (char) (this.rangeDataInt(65,91));
        }
        return c;
    }

    // generate random char
    public char randomCharRange(char a, char b){
        char c;
        int first = (int) a;
        int second = (int) b;
            c = (char) (this.rangeDataInt(first,second));
        return c;
    }

    // generate string with certain length, input parameter , length of string and true for lowerCase
    public String randomString(int i, boolean b){
        int start = 'a';
        int end = 'z';
        if(!b){
            start = 'A';
            end = 'Z';
        }
        Random r = new Random();
        String genS = r.ints(start,end+1).limit(i)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return genS;
    }

}
