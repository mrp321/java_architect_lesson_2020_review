package cn.sitedev.simplefactory.improved.reflect.clazz;

import cn.sitedev.simplefactory.ICourse;

public class CourseFactory {
    public ICourse create(Class<? extends ICourse> courseCls) {
        try {
            if (courseCls != null) {
                return courseCls.newInstance();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("不受支持的课程类型");
    }
}