package etu1381.framework.modelview;
/**
 *
 * @author zexceed
 */
import java.util.HashMap;
import java.util.List;

public class ModelView {
    private String url;
    private HashMap<String, Object> data;
    private HashMap<String, Object> sessiontoadd;
    private boolean isJson;
    private boolean invalidateSession;
    private List<String> sessionToRemove;

    public ModelView() {
    }
    
    public ModelView(String url) {
            this.setView(url);
        }
    public boolean isJson() {
        return isJson;
    }

    public void setJson(boolean isJson) {
        this.isJson = isJson;
    }

    public HashMap<String, Object> getSessiontoadd() {
        return sessiontoadd;
    }

    public void setSessiontoadd(HashMap<String, Object> sessiontoadd) {
        this.sessiontoadd = sessiontoadd;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public HashMap<String, Object> getData() {
        return this.data;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isInvalidateSession() {
        return invalidateSession;
    }

    public void setInvalidateSession(boolean invalidateSession) {
        this.invalidateSession = invalidateSession;
    }

    public List<String> getSessionToRemove() {
        return sessionToRemove;
    }

    public void setSessionToRemove(List<String> sessionToRemove) {
        this.sessionToRemove = sessionToRemove;
    }
}
