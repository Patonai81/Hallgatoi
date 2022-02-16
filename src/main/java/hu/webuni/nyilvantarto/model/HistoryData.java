package hu.webuni.nyilvantarto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.envers.RevisionType;

import java.util.Date;

@Data
@AllArgsConstructor
public class HistoryData<T> {
    private T entity;
    private RevisionType revisionType;
    private int revision;
    private Date date;

}
