package vn.emberrise.workflow.runtime.actionhandler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ActionProvider {

    private final Map<String, ActionHandler> handlers = new HashMap<>();

    // Tự động inject tất cả các class có implement ActionHandler vào Map
    public ActionProvider(List<ActionHandler> handlerList) {
        handlerList.forEach(h -> handlers.put(h.getActionKey(), h));
    }

    public ActionHandler getHandler(String actionKey) {
        return handlers.get(actionKey);
    }
}