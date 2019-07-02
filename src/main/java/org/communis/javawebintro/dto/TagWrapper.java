package org.communis.javawebintro.dto;

import lombok.Data;
import org.communis.javawebintro.entity.Tag;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class TagWrapper implements ObjectWrapper<Tag>, Serializable {
    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    private Date dateTimeCreate;

    @Override
    public void toWrapper(Tag item) {
        if (item != null) {
            id = item.getId();
            name = item.getName();
            dateTimeCreate = item.getDateTimeCreate();
        }
    }

    @Override
    public void fromWrapper(Tag item) {
        if (item != null) {
            item.setName(name);
            item.setDateTimeCreate(dateTimeCreate);
        }
    }
}
