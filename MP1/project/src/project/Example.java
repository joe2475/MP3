package project;

abstract class Animal {
    public abstract void saySomething();
}

class Cat extends Animal {
    public void saySomething() {
        System.out.println("purr");
    }
}

class Dog extends Animal {
    public void saySomething() {
        System.out.println("woof");
    }
}

class Fish extends Animal {
    public void saySomething() {
        System.out.println("...");
    }
}

class Car {  // not an Animal
    public void saySomething() {
        System.out.println("honk!");
    }
}

public class Example {
    static Animal neverCalled() {
        return new Fish();
    }

    static Animal selectAnimal() {
        return new Cat();
    }

    public static void main(String[] args) {
    	System.out.println("calling selectAnimal"); // Used for part 3
        Animal animal = selectAnimal();
        System.out.println("calling saySomething");
        animal.saySomething();
    
 

}
}