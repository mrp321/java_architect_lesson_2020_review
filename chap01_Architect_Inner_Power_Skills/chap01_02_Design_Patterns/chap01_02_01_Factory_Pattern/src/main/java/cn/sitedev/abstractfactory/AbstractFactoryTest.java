package cn.sitedev.abstractfactory;

public class AbstractFactoryTest {
    public static void main(String[] args) {
        CourseFactory factory = new JavaCourseFactory();
        factory.createNote().edit();
        factory.craeteVideo().record();

        factory = new PythonCourseFactory();
        factory.createNote().edit();
        factory.craeteVideo().record();
    }
}
