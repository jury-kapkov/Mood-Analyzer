package org.communis.javawebintro.dto;

import lombok.Data;
import org.communis.javawebintro.entity.Tag;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Data
public class TagWrapper implements ObjectWrapper<Tag>, Serializable {
//    Список поступающих тэгов
   @NotNull
    private ArrayList<Tag> tags = new ArrayList<>(0);
//    Оценка
    @NotNull
    @Max(100)
    @Min(0)
    private Integer mark;

    @Override
    public void toWrapper(Tag item) {
        if (item != null) {
            tags.add(item);
        }
    }

    @Override
    public void fromWrapper(Tag item) {
        if (item != null && !tags.isEmpty()) {
            Date date = tags.get(0).getDateTimeCreate();
            item.setName(tags.get(0).getName());
            item.setDateTimeCreate(date == null ? item.getDateTimeCreate() : date);
        }
    }
}
