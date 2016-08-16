/**
 * Copyright 2014-16 by Riccardo Massera (TheCoder4.Eu) and Stephan Rauh (http://www.beyondjava.net).
 * <p>
 * This file is part of BootsFaces.
 * <p>
 * BootsFaces is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * BootsFaces is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with BootsFaces. If not, see <http://www.gnu.org/licenses/>.
 */

package cc.coopersoft.component.selectOneMenu;

import net.bootsfaces.component.ajax.AJAXRenderer;
import net.bootsfaces.component.icon.Icon;
import net.bootsfaces.render.CoreRenderer;
import net.bootsfaces.render.H;
import net.bootsfaces.render.R;
import net.bootsfaces.render.Tooltip;

import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/** This class generates the HTML code of &lt;b:SelectOneMenu /&gt;. */
@FacesRenderer(componentFamily = "cc.coopersoft.component", rendererType = "cc.coopersoft.component.selectOneMenu.SelectOneMenu")
public class SelectOneMenuRenderer extends CoreRenderer {

    /** Receives the value from the client and sends it to the JSF bean. */
    @Override
    public void decode(FacesContext context, UIComponent component) {
        SelectOneMenu menu = (SelectOneMenu) component;
        if (menu.isDisabled() || menu.isReadonly()) {
            return;
        }
        String outerClientId = menu.getClientId(context);
        String clientId = outerClientId + "Inner";
        String submittedOptionValue = (String) context.getExternalContext().getRequestParameterMap().get(clientId);

        List<Object> items = collectOptions(context, menu);

        if (null != submittedOptionValue) {
            for (int index = 0; index < items.size(); index++) {
                Object currentOption = items.get(index);
                String currentOptionValueAsString;
                Object currentOptionValue = null;
                if (currentOption instanceof SelectItem) {
                    if (!((SelectItem) currentOption).isDisabled()) {
                        currentOptionValue = ((SelectItem) currentOption).getValue();
                        if (null == currentOptionValue) // use the label as
                            // fall-back
                            currentOptionValue = ((SelectItem) currentOption).getLabel();
                    }
                } else {
                    if (!((UISelectItem) currentOption).isItemDisabled()) {
                        currentOptionValue = ((UISelectItem) currentOption).getItemValue();
                        if (null == currentOptionValue) // use the label as
                            // fall-back
                            currentOptionValue = ((UISelectItem) currentOption).getItemLabel();
                    }
                }
                if (currentOptionValue instanceof String) {
                    currentOptionValueAsString = (String) currentOptionValue;
                } else
                    currentOptionValueAsString = String.valueOf(index);
                if (submittedOptionValue.equals(currentOptionValueAsString)) {
                    menu.setSubmittedValue(currentOptionValue);
                    menu.setValid(true);
                    menu.validateValue(context, submittedOptionValue);
                    new AJAXRenderer().decode(context, component, clientId);
                    return;
                }
            }
            menu.validateValue(context, null);
            menu.setSubmittedValue(null);
            menu.setValid(false);
            return;
        }

        menu.setValid(true);
        menu.validateValue(context, submittedOptionValue);
        menu.setSubmittedValue(submittedOptionValue);
        new AJAXRenderer().decode(context, component, clientId);
    }

    /** Generates the HTML code for this component. */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        SelectOneMenu menu = (SelectOneMenu) component;

        if (!menu.isRendered()) {
            return;
        }
        ResponseWriter rw = context.getResponseWriter();
        String outerClientId = menu.getClientId(context);
        String clientId = outerClientId + "Inner";
        int span = startColSpanDiv(rw, menu);
        rw.startElement("div", menu);
        Tooltip.generateTooltip(context, menu, rw);

        if (menu.isInline()) {
            rw.writeAttribute("class", "form-inline", "class");
        } else {
            rw.writeAttribute("class", "form-group", "class");
        }
        rw.writeAttribute("id", outerClientId, "id");
        writeAttribute(rw, "dir", menu.getDir(), "dir");

        addLabel(rw, clientId, menu);

        UIComponent prependingAddOnFacet = menu.getFacet("prepend");
        UIComponent appendingAddOnFacet = menu.getFacet("append");
        final boolean hasAddon = startInputGroupForAddOn(rw, (prependingAddOnFacet != null),
                (appendingAddOnFacet != null), menu);

        addPrependingAddOnToInputGroup(context, rw, prependingAddOnFacet, (prependingAddOnFacet != null), menu);
        renderSelectTag(context, rw, clientId, menu);
        addAppendingAddOnToInputGroup(context, rw, appendingAddOnFacet, (appendingAddOnFacet != null), menu);

        closeInputGroupForAddOn(rw, hasAddon);
        rw.endElement("div"); // form-group
        closeColSpanDiv(rw, span);
        Tooltip.activateTooltips(context, menu);
    }

    /**
     * Renders components added seamlessly behind the input field.
     *
     * @param context
     *            the FacesContext
     * @param rw
     *            the response writer
     * @param appendingAddOnFacet
     *            optional facet behind the field. Can be null.
     * @param hasAppendingAddOn
     *            optional facet in front of the field. Can be null.
     * @throws IOException
     *             may be thrown by the response writer
     */
    protected void addAppendingAddOnToInputGroup(FacesContext context, ResponseWriter rw,
                                                 UIComponent appendingAddOnFacet, boolean hasAppendingAddOn, SelectOneMenu menu) throws IOException {
        if (hasAppendingAddOn) {
            if (appendingAddOnFacet.getClass().getName().endsWith("Button") || (appendingAddOnFacet.getChildCount() > 0
                    && appendingAddOnFacet.getChildren().get(0).getClass().getName().endsWith("Button"))) {
                rw.startElement("div", menu);
                rw.writeAttribute("class", "input-group-btn", "class");
                appendingAddOnFacet.encodeAll(context);
                rw.endElement("div");
            } else {
                if (appendingAddOnFacet instanceof Icon)
                    ((Icon) appendingAddOnFacet).setAddon(true); // modifies the id of the icon
                rw.startElement("span", menu);
                rw.writeAttribute("class", "y input-group-addon", "class");
                appendingAddOnFacet.encodeAll(context);
                rw.endElement("span");
            }
        }
    }

    /**
     * Renders the optional label. This method is protected in order to allow
     * third-party frameworks to derive from it.
     *
     * @param rw
     *            the response writer
     * @param clientId
     *            the id used by the label to refernce the input field
     * @throws IOException
     *             may be thrown by the response writer
     */
    protected void addLabel(ResponseWriter rw, String clientId, SelectOneMenu menu) throws IOException {
        String label = menu.getLabel();
        {
            if (!menu.isRenderLabel()) {
                label = null;
            }
        }
        if (label != null) {
            rw.startElement("label", menu);
            rw.writeAttribute("for", clientId, "for");
            generateErrorAndRequiredClass(menu, rw, clientId);
            rw.writeText(label, null);
            rw.endElement("label");
        }
    }

    /**
     * Renders components added seamlessly in front of the input field.
     *
     * @param context
     *            the FacesContext
     * @param rw
     *            the response writer
     * @param prependingAddOnFacet
     * @param hasPrependingAddOn
     * @throws IOException
     *             may be thrown by the response writer
     */
    protected void addPrependingAddOnToInputGroup(FacesContext context, ResponseWriter rw,
                                                  UIComponent prependingAddOnFacet, boolean hasPrependingAddOn, SelectOneMenu menu) throws IOException {
        if (hasPrependingAddOn) {
            if (prependingAddOnFacet.getClass().getName().endsWith("Button")
                    || (prependingAddOnFacet.getChildCount() > 0
                    && prependingAddOnFacet.getChildren().get(0).getClass().getName().endsWith("Button"))) {
                rw.startElement("div", menu);
                rw.writeAttribute("class", "input-group-btn", "class");
                prependingAddOnFacet.encodeAll(context);
                rw.endElement("div");
            } else {
                if (prependingAddOnFacet instanceof Icon)
                    ((Icon) prependingAddOnFacet).setAddon(true); // modifies the id of the icon
                rw.startElement("span", menu);
                rw.writeAttribute("class", "input-group-addon", "class");
                prependingAddOnFacet.encodeAll(context);
                rw.endElement("span");
            }
        }
    }

    /**
     * Terminate the column span div (if there's one). This method is protected
     * in order to allow third-party frameworks to derive from it.
     *
     * @param rw
     *            the response writer
     * @param span
     *            the width of the components (in BS columns).
     * @throws IOException
     *             may be thrown by the response writer
     */
    protected void closeColSpanDiv(ResponseWriter rw, int span) throws IOException {
        if (span > 0) {
            rw.endElement("div");
        }
    }

    /**
     * Terminates the input field group (if there's one). This method is
     * protected in order to allow third-party frameworks to derive from it.
     *
     * @param rw
     *            the response writer
     * @param hasAddon
     *            true if there is an add-on in front of or behind the input
     *            field
     * @throws IOException
     *             may be thrown by the response writer
     */
    protected void closeInputGroupForAddOn(ResponseWriter rw, final boolean hasAddon) throws IOException {
        if (hasAddon) {
            rw.endElement("div");
        }
    }

    /**
     * Algorithm works as follows; - If it's an input component, submitted value
     * is checked first since it'd be the value to be used in case validation
     * errors terminates jsf lifecycle - Finally the value of the component is
     * retrieved from backing bean and if there's a converter, converted value
     * is returned
     *
     * @param context
     *            FacesContext instance
     * @return End text
     */
//	public Object getValue2Render(FacesContext context, SelectOneMenu menu) {
//		Object sv = menu.getSubmittedValue();
//		if (sv != null) {
//			return sv;
//		}
//
//		Object val = menu.getValue();
//		if (val != null) {
//			Converter converter = menu.getConverter();
//
//			if (converter != null)
//				return converter.getAsString(context, menu, val);
//			else
//				return val;
//
//		} else {
//			// component is a value holder but has no value
//			return null;
//		}
//	}

    /** Renders the select tag. */
    protected void renderSelectTag(FacesContext context, ResponseWriter rw, String clientId, SelectOneMenu menu)
            throws IOException {
        renderSelectTag(rw, menu);
        renderSelectTagAttributes(rw, clientId, menu);
        //Object selectedOption = getValue2Render(context, menu);
        renderOptions(context, rw, menu);

        renderInputTagEnd(rw);
    }

    /**
     * Copied from the InputRenderer class of PrimeFaces 5.1.
     *
     * @param context
     * @param uiSelectItems
     * @param value
     * @param label
     * @return
     */
    protected SelectItem createSelectItem(FacesContext context, UISelectItems uiSelectItems, Object value,
                                          Object label) {
        String var = (String) uiSelectItems.getAttributes().get("var");
        Map<String, Object> attrs = uiSelectItems.getAttributes();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

        if (var != null) {
            requestMap.put(var, value);
        }

        Object itemLabelValue = attrs.get("itemLabel");
        Object itemValue = attrs.get("itemValue");
        String description = (String) attrs.get("itemDescription");
        Object itemDisabled = attrs.get("itemDisabled");
        Object itemEscaped = attrs.get("itemLabelEscaped");
        Object noSelection = attrs.get("noSelectionOption");

        if (itemValue == null) {
            itemValue = value;
        }

        if (itemLabelValue == null) {
            itemLabelValue = label;
        }

        String itemLabel = itemLabelValue == null ? String.valueOf(value) : String.valueOf(itemLabelValue);
        boolean disabled = itemDisabled == null ? false : Boolean.valueOf(itemDisabled.toString());
        boolean escaped = itemEscaped == null ? false : Boolean.valueOf(itemEscaped.toString());
        boolean noSelectionOption = noSelection == null ? false : Boolean.valueOf(noSelection.toString());

        if (var != null) {
            requestMap.remove(var);
        }

        return new SelectItem(itemValue, itemLabel, description, disabled, escaped, noSelectionOption);
    }

    /**
     * Parts of this class are an adapted version of
     * InputRenderer#getSelectItems() of PrimeFaces 5.1.
     *
     * @param rw
     * @throws IOException
     */
    protected void renderOptions(FacesContext context, ResponseWriter rw, SelectOneMenu menu)
            throws IOException {
        List<Object> items = collectOptions(context, menu);

        for (int index = 0; index < items.size(); index++) {
            Object option = items.get(index);
            if (option instanceof SelectItem) {
                renderOption(context, menu, rw, (SelectItem) option, index);
            } else {
                renderOption(context, menu, rw, (UISelectItem) option, index);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private List<Object> collectOptions(FacesContext context, SelectOneMenu menu) {
        List<Object> items = new ArrayList<Object>();

        List<UIComponent> selectItems = menu.getChildren();
        for (UIComponent kid : selectItems) {
            if (kid instanceof UISelectItem) {
                UISelectItem option = (UISelectItem) kid;
                items.add(option);
            } else if (kid instanceof UISelectItems) {

                UISelectItems uiSelectItems = ((UISelectItems) kid);
                Object value = uiSelectItems.getValue();

                if (value != null) {
                    if (value instanceof SelectItem) {
                        items.add(value);

                    } else {
                        if (value.getClass().isArray()) {
                            for (int i = 0; i < Array.getLength(value); i++) {
                                Object item = Array.get(value, i);

                                if (item instanceof SelectItem)
                                    items.add(item);
                                else
                                    items.add(createSelectItem(context, uiSelectItems, item, null));
                            }
                        } else if (value instanceof Map) {
                            Map map = (Map) value;

                            for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
                                Object key = it.next();

                                items.add(createSelectItem(context, uiSelectItems, map.get(key), String.valueOf(key)));
                            }
                        } else if (value instanceof Collection) {
                            Collection collection = (Collection) value;

                            for (Iterator it = collection.iterator(); it.hasNext(); ) {
                                Object item = it.next();
                                if (item instanceof SelectItem)
                                    items.add(item);
                                else
                                    items.add(createSelectItem(context, uiSelectItems, item, null));
                            }
                        }
                    }
                }

            }
        }
        return items;
    }

    private Converter findImplicitConverter(FacesContext context, UIComponent component) {
        ValueExpression ve = component.getValueExpression("value");

        if (ve != null) {
            Class<?> valueType = ve.getType(context.getELContext());

            if (valueType != null)
                return context.getApplication().createConverter(valueType);
        }

        return null;
    }

    private String getOptionAsString(FacesContext context, SelectOneMenu menu, Object value, Converter converter) throws ConverterException {

        if (converter == null) {
            if (value == null) {
                return "";
            } else if (value instanceof String) {
                return (String) value;
            } else {
                Converter implicitConverter = findImplicitConverter(context, menu);

                return implicitConverter == null ? value.toString() : implicitConverter.getAsString(context, menu, value);
            }
        } else {
            return converter.getAsString(context, menu, value);
        }
    }

    private Object coerceToModelType(FacesContext ctx, Object value, Class itemValueType) {
        Object newValue;
        try {
            ExpressionFactory ef = ctx.getApplication().getExpressionFactory();
            newValue = ef.coerceToType(value, itemValueType);
        } catch (ELException ele) {
            newValue = value;
        } catch (IllegalArgumentException iae) {
            newValue = value;
        }

        return newValue;
    }

    private boolean isSelected(FacesContext context, SelectOneMenu menu, Object value, Object itemValue, Converter converter) {
        if (itemValue == null && value == null) {
            return true;
        }

        if (value != null) {
            Object compareValue;
            if (converter == null) {
                compareValue = coerceToModelType(context, itemValue, value.getClass());
            } else {
                compareValue = itemValue;

                if (compareValue instanceof String && !(value instanceof String)) {
                    compareValue = converter.getAsObject(context, menu, (String) compareValue);
                }
            }
            if (value.equals(compareValue)) {
                return true;
            }


        }

        return false;
    }

    /**
     * Renders a single &lt;option&gt; tag. For some reason,
     * <code>SelectItem</code> and <code>UISelectItem</code> don't share a
     * common interface, so this method is repeated twice.
     *
     * @param rw
     *            The response writer
     * @param selectItem
     *            The current SelectItem
     * @throws IOException
     *             thrown if something's wrong with the response writer
     */
    protected void renderOption(FacesContext context, SelectOneMenu menu, ResponseWriter rw, SelectItem selectItem, int index)
            throws IOException {

        String itemLabel = selectItem.getLabel();
        final String description = selectItem.getDescription();
        final Object itemValue = selectItem.getValue();

        renderOption(context, menu, rw, index, itemLabel, description, itemValue, selectItem.isDisabled());
    }

    /**
     * Renders a single &lt;option&gt; tag. For some reason,
     * <code>SelectItem</code> and <code>UISelectItem</code> don't share a
     * common interface, so this method is repeated twice.
     *
     * @param rw
     *            The response writer
     * @param selectItem
     *            The current SelectItem
     * @throws IOException
     *             thrown if something's wrong with the response writer
     */
    protected void renderOption(FacesContext context, SelectOneMenu menu, ResponseWriter rw, UISelectItem selectItem, int index)
            throws IOException {

        String itemLabel = selectItem.getItemLabel();
        final String itemDescription = selectItem.getItemDescription();
        final Object itemValue = selectItem.getItemValue();

        renderOption(context, menu, rw, index, itemLabel, itemDescription, itemValue, selectItem.isItemDisabled());
    }

    private void renderOption(FacesContext context, SelectOneMenu menu, ResponseWriter rw, int index, String itemLabel,
                              final String description, final Object itemValue, boolean isDisabled) throws IOException {

        Object submittedValue = menu.getSubmittedValue();
        Object selectedOption;
        Object optionValue;
        Converter converter = menu.getConverter();
        String itemValueAsString = getOptionAsString(context, menu, itemValue, converter);

        if (submittedValue != null) {
            selectedOption = submittedValue;
            optionValue = itemValueAsString;
        } else {
            selectedOption = menu.getValue();
            optionValue = itemValue;
        }

        boolean isItemLabelBlank = itemLabel == null || itemLabel.trim().isEmpty();
        itemLabel = isItemLabelBlank ? "&nbsp;" : itemLabel;

        rw.startElement("option", null);
        rw.writeAttribute("data-label", itemLabel, null);
        if (description != null) {
            rw.writeAttribute("title", description, null);
        }
        if (itemValue != null) {
            String value;
            if (itemValue instanceof String) {
                value = (String) itemValue;
            } else
                value = String.valueOf(index);
            rw.writeAttribute("value", value, "value");
            if (isSelected(context, menu, selectedOption, optionValue, converter)) {
                rw.writeAttribute("selected", "true", "selected");
            }
        } else if (itemLabel.equals(selectedOption)) {
            rw.writeAttribute("selected", "true", "selected");
        }
        if (isDisabled)
            rw.writeAttribute("disabled", "disabled", "disabled");

        rw.write(itemLabel);

        rw.endElement("option");
    }

    /**
     * Renders the start of the input tag. This method is protected in order to
     * allow third-party frameworks to derive from it.
     *
     * @param rw
     *            the response writer
     * @throws IOException
     *             may be thrown by the response writer
     */
    protected void renderSelectTag(ResponseWriter rw, SelectOneMenu menu) throws IOException {
        rw.write("\n");
        rw.startElement("select", menu);
    }

    /**
     * Renders the attributes of the input tag. This method is protected in
     * order to allow third-party frameworks to derive from it.
     *
     * @param rw
     *            the response writer
     * @param clientId
     *            the client id (used both as id and name)
     * @throws IOException
     *             may be thrown by the response writer
     */
    protected void renderSelectTagAttributes(ResponseWriter rw, String clientId, SelectOneMenu menu)
            throws IOException {
        rw.writeAttribute("id", clientId, null);
        //Tooltip.generateTooltip(FacesContext.getCurrentInstance(), menu, rw);
        rw.writeAttribute("name", clientId, null);

        StringBuilder sb;
        String s;
        sb = new StringBuilder(20); // optimize int
        sb.append("form-control");
        String fsize = menu.getFieldSize();

        if (fsize != null) {
            sb.append(" input-").append(fsize);
        }
        String cssClass = menu.getStyleClass();
        if (cssClass != null) {
            sb.append(" ").append(cssClass);
        }
        sb.append(" ").append(getErrorAndRequiredClass(menu, clientId));

        s = sb.toString().trim();
        if (s != null && s.length() > 0) {
            rw.writeAttribute("class", s, "class");
        }

        if (menu.isDisabled()) {
            rw.writeAttribute("disabled", "disabled", null);
        }
        if (menu.isReadonly()) {
            rw.writeAttribute("readonly", "readonly", null);
        }

        AJAXRenderer.generateBootsFacesAJAXAndJavaScript(FacesContext.getCurrentInstance(), menu, rw);

        // Encode attributes (HTML 4 pass-through + DHTML)
        R.encodeHTML4DHTMLAttrs(rw, menu.getAttributes(), H.SELECT_ONE_MENU);
    }

    /**
     * Closes the input tag. This method is protected in order to allow
     * third-party frameworks to derive from it.
     *
     * @param rw
     *            the response writer
     * @throws IOException
     *             may be thrown by the response writer
     */
    protected void renderInputTagEnd(ResponseWriter rw) throws IOException {
        rw.endElement("select");
    }

    /**
     * Start the column span div (if there's one). This method is protected in
     * order to allow third-party frameworks to derive from it.
     *
     * @param rw
     *            the response writer
     * @throws IOException
     *             may be thrown by the response writer
     */
    protected int startColSpanDiv(ResponseWriter rw, SelectOneMenu menu) throws IOException {
        int span = menu.getSpan();
        if (span > 0) {
            rw.startElement("div", menu);
            rw.writeAttribute("class", "col-md-" + span, "class");
        }
        return span;
    }

    /**
     * Starts the input field group (if needed to display a component seamlessly
     * in front of or behind the input field). This method is protected in order
     * to allow third-party frameworks to derive from it.
     *
     * @param hasAppendingAddOn
     *
     * @param rw
     *            the response writer
     * @param hasPrependingAddOn
     * @return true if there is an add-on in front of or behind the input field
     * @throws IOException
     *             may be thrown by the response writer
     */
    protected boolean startInputGroupForAddOn(ResponseWriter rw, boolean hasPrependingAddOn, boolean hasAppendingAddOn,
                                              SelectOneMenu menu) throws IOException {
        final boolean hasAddon = hasAppendingAddOn || hasPrependingAddOn;
        if (hasAddon) {
            rw.startElement("div", menu);
            rw.writeAttribute("class", "input-group", "class");
        }
        return hasAddon;
    }

}
