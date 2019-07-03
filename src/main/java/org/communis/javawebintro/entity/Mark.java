package org.communis.javawebintro.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "marks")

public class Mark {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mark")
    private Integer mark;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time_create")
    private Date dateTimeCreate;

}
