package Example;

public class Coffee {
    boolean water = false;
    boolean cup = false;
    boolean coffeeBean = false;

    public Coffee(boolean a,boolean b,boolean c){
        this.water = a;
        this.cup = b;
        this.coffeeBean = c;
    }

    public boolean isCoffee(){
        boolean a = this.water;
        boolean b = this.cup;
        boolean c = this.coffeeBean;

        if((a == true) && (b == true) && (c == true)){
            return true;
        }
        else if ((a == true) && (b == true) && (c == false)){
            return true;
        }
        else if ((a == true) && (b == false) && (c == true)){
            return false;
        }
        else if ((a == true) && (b == false) && (c == false)){
            return false;
        }
        else if ((a == false) && (b == true) && (c == false)){
            return true;
        }
        else if ((a == false) && (b == true) && (c == true)){
            return false;
        }
        else if ((a == false) && (b == false) && (c == false)){
            return false;
        }
        else{
            return false;
        }

    }
}
