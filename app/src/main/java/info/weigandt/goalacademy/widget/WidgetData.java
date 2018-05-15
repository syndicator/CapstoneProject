package info.weigandt.goalacademy.widget;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class WidgetData implements Serializable {
    public Map<Integer,List<String>> criticalEvents;
    public Map<Integer,List<String>> normalEvents;
}
