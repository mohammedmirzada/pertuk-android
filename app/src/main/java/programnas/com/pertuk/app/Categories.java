package programnas.com.pertuk.app;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Categories extends RealmObject implements Serializable {
    @PrimaryKey
    public ObjectId _id;

    public int id;
    public String name;
    public String image;

    public Categories(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Categories() {}

    public int GetId() {
        return id;
    }

    public void SetId(int id) {
        this.id = id;
    }

    public String GetName() {
        return name;
    }

    public void SetName(String name) {
        this.name = name;
    }

    public String GetImage() {
        return image;
    }

    public void SetImage(String image) {
        this.image = image;
    }

}
