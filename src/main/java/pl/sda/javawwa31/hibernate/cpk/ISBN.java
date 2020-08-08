package pl.sda.javawwa31.hibernate.cpk;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ISBN implements Serializable {
    @Column(name="group_number")    //"group" jest słowem kluczowym SQL, stąd zmieniam domyślną nazwę kolumny
    int group;
    int publisher;
    int title;
    int checkdigit;

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getPublisher() {
        return publisher;
    }

    public void setPublisher(int publisher) {
        this.publisher = publisher;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getCheckdigit() {
        return checkdigit;
    }

    public void setCheckdigit(int checkdigit) {
        this.checkdigit = checkdigit;
    }
}
