package cn.sitedev.factorymethod;

public class FactoryMethodTest {
    public static void main(String[] args) {
        ICourseFactory courseFactory = new JavaCourseFactory();
        courseFactory.create().record();

        courseFactory = new PythonCourseFactory();
        courseFactory.create().record();
    }
}
