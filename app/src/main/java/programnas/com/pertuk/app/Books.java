package programnas.com.pertuk.app;

import org.bson.types.ObjectId;
import java.io.Serializable;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Books extends RealmObject implements Serializable {
    @PrimaryKey
    public ObjectId _id;

    public int number;
    public int id;
    public String name;
    public String image;
    public int author_id;
    public int category_ids;
    public String book1, book2, book3, book4, book5, book6, book7, book8, book9, book10;
    public String downloads;
    public String views;

    public Books(int number, int id, String name, String image, int author_id, int category_ids, String book1,
                 String book2, String book3, String book4, String book5, String book6, String book7,
                 String book8, String book9, String book10, String downloads, String views) {
        this.number = number;
        this.id = id;
        this.name = name;
        this.image = image;
        this.author_id = author_id;
        this.category_ids = category_ids;
        this.book1 = book1;
        this.book2 = book2;
        this.book3 = book3;
        this.book4 = book4;
        this.book5 = book5;
        this.book6 = book6;
        this.book7 = book7;
        this.book8 = book8;
        this.book9 = book9;
        this.book10 = book10;
        this.downloads = downloads;
        this.views = views;
    }

    public Books() {}

    public int GetNumber() {
        return number;
    }

    public void SetNumber(int number) {
        this.number = number;
    }

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

    public void SetAuthorId(int author_id) {
        this.author_id = author_id;
    }

    public int GetAuthorId() {
        return author_id;
    }

    public void SetCategoryIds(int category_ids) {
        this.category_ids = category_ids;
    }

    public int GetCategoryIds() {
        return category_ids;
    }

    public void SetBook1(String book1) {
        this.book1 = book1;
    }

    public String GetBook1() {
        return book1;
    }

    public void SetBook2(String book2) {
        this.book2 = book2;
    }

    public String GetBook2() {
        return book2;
    }

    public void SetBook3(String book3) {
        this.book3 = book3;
    }

    public String GetBook3() {
        return book3;
    }

    public void SetBook4(String book4) {
        this.book4 = book4;
    }

    public String GetBook4() {
        return book4;
    }

    public void SetBook5(String book5) {
        this.book5 = book5;
    }

    public String GetBook5() {
        return book5;
    }

    public void SetBook6(String book6) {
        this.book6 = book6;
    }

    public String GetBook6() {
        return book6;
    }

    public void SetBook7(String book7) {
        this.book7 = book7;
    }

    public String GetBook7() {
        return book7;
    }

    public void SetBook8(String book8) {
        this.book8 = book8;
    }

    public String GetBook8() {
        return book8;
    }

    public void SetBook9(String book9) {
        this.book9 = book9;
    }

    public String GetBook9() {
        return book9;
    }

    public void SetBook10(String book10) {
        this.book10 = book10;
    }

    public String GetBook10() {
        return book10;
    }

    public void SetDownloads(String downloads) {
        this.downloads = downloads;
    }

    public String GetDownloads() {
        return downloads;
    }

    public void SetViews(String views) {
        this.views = views;
    }

    public String GetViews() {
        return views;
    }

}
