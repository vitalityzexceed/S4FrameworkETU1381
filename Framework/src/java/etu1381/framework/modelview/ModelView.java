package etu1381.framework.modelview;

/**
 *
 * @author zexceed
 */

public class ModelView {
    private String url;

    public ModelView(String url) {
        this.setView(url);
    }

    public String getView() {
        return url;
    }

    public void setView(String url) {
        this.url = url;
    }
}
