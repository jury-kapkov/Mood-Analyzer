package org.communis.javawebintro.dto;

import lombok.Data;
import org.communis.javawebintro.entity.Tag;
import org.communis.javawebintro.repository.TagRepository;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Data
public class TagWrapper implements ObjectWrapper<Tag>, Serializable {
//    private Long id;

//    @NotNull
//    @Size(max = 50)
//    private String name;
//    Список поступающих тэгов
    private ArrayList<Tag> tags = new ArrayList<>(0);
//    Оценка
    @NotNull
    @Max(100)
    @Min(0)
    private Integer mark;

//    private Date dateTimeCreate;

    @Override
    public void toWrapper(Tag item) {
        if (item != null) {
            tags.add(item);
        }
    }

    @Override
    public void fromWrapper(Tag item) {
        if (item != null) {
            Date date = tags.get(0).getDateTimeCreate();
            item.setName(tags.get(0).getName());
            item.setDateTimeCreate(date == null ? item.getDateTimeCreate() : date);
        }
    }
}
