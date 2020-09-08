package cn.sitedev.ocp.course;

public class JavaDiscountCourse extends JavaCourse {

    public JavaDiscountCourse(Integer id, String name, Double price) {
        super(id, name, price);
    }

    public Double getOriginalPrice() {
        return getPrice();
    }

    @Override
    public Double getPrice() {
        return super.getPrice() * 0.61;
    }
}
