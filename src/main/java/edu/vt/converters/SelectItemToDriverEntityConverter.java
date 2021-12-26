package edu.vt.converters;

import edu.vt.EntityBeans.Driver;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.List;
import java.util.function.Predicate;

// Select Item to DriverEntity Converter

@FacesConverter(value = "SelectItemToDriverEntityConverter")
public class SelectItemToDriverEntityConverter implements Converter {

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
            s = ((Driver) value).getId().toString();
        }
        return s;
    }

    // Get Selected Item as Entity
    private Driver getSelectedItemAsEntity(UIComponent comp, String value) {
        Driver item = null;

        List<Driver> selectItems = null;
        for (UIComponent uic : comp.getChildren()) {
            if (uic instanceof UISelectItems) {
                Integer itemId = Integer.valueOf(value);

                selectItems = (List<Driver>) ((UISelectItems) uic).getValue();

                if (itemId != null && selectItems != null && !selectItems.isEmpty()) {
                    Predicate<Driver> predicate = i -> i.getId().equals(itemId);
                    item = selectItems.stream().filter(predicate).findFirst().orElse(null);
                }
            }
        }
        return item;
    }
}


