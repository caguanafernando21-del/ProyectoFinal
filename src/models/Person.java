package models;

public class Person implements Comparable<Person>{
    private String name;
    private int age;
    
    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int compareTo(Person otra) {
        // return this.name.compareTo(otra.getName());
        int compAge = Integer.compare(this.age, otra.getAge());

        if (compAge != 0)
            return compAge;

        return this.name.compareTo(otra.getName());
    }

    @Override
    public String toString() {
        return "Person [name = " + name + ", age = " + age + "]";
    }
}
