package cc.coopersoft.common;

import cc.coopersoft.common.util.MessageBundle;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ResourceBundle;

/**
 * Created by cooper on 6/16/16.
 */
@Named
public class EnumHelper {

    @Inject
    private FacesContext facesContext;

    @Inject @MessageBundle
    private ResourceBundle bundle;

    public String getLabel(Enum value){


        return bundle.getString(value.getClass().getName() + "." + value.name());
    }
}
