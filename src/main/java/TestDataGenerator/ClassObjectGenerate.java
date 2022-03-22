package TestDataGenerator;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Random;

/**
 * @author Ken Lok Chan
 * ClassObjectGenerator.java
 *
 * class that generate random data with fields
 * of the class.
 */
public class ClassObjectGenerate {

    private Random r = new Random();
    private BasicTestGeneration bT = new BasicTestGeneration();

    private <T> T createAndFill(Class<T> clazz) throws Exception {
        T instance = clazz.newInstance();
        for(Field field: clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = getRandomValueForField(field);
            field.set(instance, value);
        }
        return instance;
    }

    //Detect object type and return the value
    public Object getRandomValueForField(Field field) throws Exception {
        Class<?> type = field.getType();

        if(type.isEnum()) {
            Object[] enumValues = type.getEnumConstants();
            return enumValues[r.nextInt(enumValues.length)];
        } else if(type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return r.nextInt();
        } else if(type.equals(Long.TYPE) || type.equals(Long.class)) {
            return r.nextLong();
        } else if(type.equals(Double.TYPE) || type.equals(Double.class)) {
            return r.nextDouble();
        } else if(type.equals(Float.TYPE) || type.equals(Float.class)) {
            return r.nextFloat();
        } else if(type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
            return r.nextBoolean();
        } else if(type.equals(Character.TYPE)) {
            return bT.randomChar(true);
        } else if(type.equals(String.class)) {
            return bT.randomString(6,false);
        } else if(type.equals(BigInteger.class)){
            return BigInteger.valueOf(r.nextInt());
        }
        return createAndFill(type);
    }

    // putting Classname.class as parameter create an array of object
    // user have to create loops in order to show the data in the object[]
    public Object[] getRandomValueForFieldByClass(Class<?> t) throws Exception {
        Object instance = t.newInstance();
        Field[] fields = instance.getClass().getFields();
        Object[] result = new Object[fields.length];
        int i = 0;
        for (Field apl :fields) {
            result[i] = this.getRandomValueForField(apl);
            i++;
        }
        return result;
    }
}
