package edu.vt.converters;

import edu.vt.EntityBeans.Rider;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


// Select Item to RiderEntity Converter

@FacesConverter(value = "SelectItemToRiderEntityConverter")
public class SelectItemToRiderEntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent comp, String value) {
        Object o = null;
        if (!(value == null || value.isEmpty())) {
            o = this.getSelectedItemAsEntity(comp, value);
        }
        return o;
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
        String s = "";
        if (value != null) {
            s = ((Rider) value).getId().toString();
        }
        return s;
    }

    // get Selected Item as Entity
    private Rider getSelectedItemAsEntity(UIComponent comp, String value) {
        Rider item = null;

        List<Rider> selectItems = null;
        for (UIComponent uic : comp.getChildren()) {
            if (uic instanceof UISelectItems) {
                Integer itemId = Integer.valueOf(value);
                selectItems = (List<Rider>) ((UISelectItems) uic).getValue();

                if (itemId != null && selectItems != null && !selectItems.isEmpty()) {
                    Predicate<Rider> predicate = i -> i.getId().equals(itemId);
                    item = selectItems.stream().filter(predicate).findFirst().orElse(null);
                }
            }
        }
        return item;
    }
}


