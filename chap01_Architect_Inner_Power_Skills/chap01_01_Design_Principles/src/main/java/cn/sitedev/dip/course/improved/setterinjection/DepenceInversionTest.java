package cn.sitedev.dip.course.improved.setterinjection;

import cn.sitedev.dip.course.improved.JavaCourse;
import cn.sitedev.dip.course.improved.PythonCourse;

/**
 * setter方式注入
 */
public class DepenceInversionTest {
    public static void main(String[] args) {
        Tom tom = new Tom();
        tom.setCourse(new JavaCourse());
        tom.study();

        tom.setCourse(new PythonCourse());
        tom.study();
    }
}
