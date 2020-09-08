package cn.sitedev.lsp.graph.improved;

public class Rectangle implements QuadRangle {
    private long height;
    private long width;

    @Override
    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    @Override
    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }
}
