package org.communis.javawebintro.dto;

import lombok.Data;
import org.communis.javawebintro.entity.Mark;
import org.communis.javawebintro.entity.Tag;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class MarkWrapper implements ObjectWrapper<Mark>, Serializable {

//    id
    Long id;

//    Оценка
    @NotNull
    @Max(100)
    @Min(0)
    private Integer mark;

//    date of create
    Date dateOfCreate;

    @Override
    public void toWrapper(Mark item) {
        id = item.getId();
        dateOfCreate = item.getDateTimeCreate();
        mark = item.getMark();
    }

    @Override
    public void fromWrapper(Mark item) {
        if (item != null) {
            item.setMark(mark);
            item.setDateTimeCreate(dateOfCreate == null ? item.getDateTimeCreate() : dateOfCreate);
        }
    }
}
