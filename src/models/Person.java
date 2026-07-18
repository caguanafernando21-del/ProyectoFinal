package models;

public class Person implements Comparable<Person>{
    private String name;
    private int age;
    
    // Constructores, Getters y Setters
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

    // Define el orden natural de las personas
    @Override
    public int compareTo(Person otra) {
        // 1. Compara primero por edad
        int compAge = Integer.compare(this.age, otra.getAge());

        if (compAge != 0)
            return compAge; // Si las edades son distintas, ordena por edad

        // 2. Si tienen la misma edad, desempata alfabéticamente por nombre
        return this.name.compareTo(otra.getName());
    }

    @Override
    public String toString() {
        return "Person [name = " + name + ", age = " + age + "]";
    }
}
