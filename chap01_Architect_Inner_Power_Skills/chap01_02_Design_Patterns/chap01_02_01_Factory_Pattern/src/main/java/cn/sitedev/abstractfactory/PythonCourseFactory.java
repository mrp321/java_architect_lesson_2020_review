package cn.sitedev.abstractfactory;

public class PythonCourseFactory extends CourseFactory {
    @Override
    protected INote createNote() {
        return new PythonNote();
    }

    @Override
    protected IVideo craeteVideo() {
        return new PythonVideo();
    }
}
