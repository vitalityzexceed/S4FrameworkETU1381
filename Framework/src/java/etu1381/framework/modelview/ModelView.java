package etu1381.framework.modelview;
/**
 *
 * @author zexceed
 */
import java.util.HashMap;

public class ModelView {
    private String url;
    private HashMap<String, Object> data;

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public HashMap<String, Object> getData() {
        return this.data;
    }

    public ModelView(String url) {
        this.setView(url);
    }

    public String getView() {
        return url;
    }

    public void setView(String url) {
        this.url = url;
    }

    public void addItem(String key, Object value)
    {
        this.getData().put(key, value);
    }
}
