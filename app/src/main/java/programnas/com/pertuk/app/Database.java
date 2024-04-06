package programnas.com.pertuk.app;

import org.bson.types.ObjectId;
import io.realm.Realm;
import io.realm.RealmQuery;

public class Database {

    Realm realm;

    public Database(Realm realm){
        this.realm = realm;
    }

    public void SetCategories(int id, String name, String image){
        realm.executeTransaction(r -> {
            Categories categories = r.createObject(Categories.class, new ObjectId());
            categories.SetId(id);
            categories.SetName(name);
            categories.SetImage(image);
        });
    }

    public RealmQuery<Categories> GetCategories(){
        return realm.where(Categories.class);
    }

    public Categories GetSingleCategories(int category_id){
        RealmQuery<Categories> category = realm.where(Categories.class).equalTo("id", category_id);
        return category.findAll().first();
    }

    public void SetAuthors(int id, String name, String image){
        realm.executeTransaction(r -> {
            Authors authors = r.createObject(Authors.class, new ObjectId());
            authors.SetId(id);
            authors.SetName(name);
            authors.SetImage(image);
        });
    }

    public RealmQuery<Authors> GetAuthors(){
        return realm.where(Authors.class);
    }

    public Authors GetSingleAuthors(int author_id){
        RealmQuery<Authors> authors = realm.where(Authors.class).equalTo("id", author_id);
        return authors.findAll().first();
    }

    public void SetBooks(int numbrer, int id, String name, String image, int author_id, int category_ids, String book1,
                         String book2, String book3, String book4, String book5, String book6, String book7,
                         String book8, String book9, String book10, String downloads, String views){
        realm.executeTransaction(r -> {
            Books books = r.createObject(Books.class, new ObjectId());
            books.SetNumber(numbrer);
            books.SetId(id);
            books.SetName(name);
            books.SetImage(image);
            books.SetAuthorId(author_id);
            books.SetCategoryIds(category_ids);
            books.SetBook1(book1);
            books.SetBook2(book2);
            books.SetBook3(book3);
            books.SetBook4(book4);
            books.SetBook5(book5);
            books.SetBook6(book6);
            books.SetBook7(book7);
            books.SetBook8(book8);
            books.SetBook9(book9);
            books.SetBook10(book10);
            books.SetViews(views);
            books.SetDownloads(downloads);
        });
    }

    public RealmQuery<Books> GetBooks(){
        return realm.where(Books.class);
    }

    public RealmQuery<Books> GetBooks(int category_id){
        return realm.where(Books.class).equalTo("category_ids", category_id);
    }

    public RealmQuery<Books> GetBooks(String book_name){
        return realm.where(Books.class).contains("name", book_name);
    }

    public RealmQuery<Books> GetBooks(int author_id, String references){
        return realm.where(Books.class).equalTo("author_id", author_id);
    }

    public Books GetSingleBook(int book_id){
        RealmQuery<Books> books = realm.where(Books.class).equalTo("id", book_id);
        return books.findAll().first();
    }

    public void DeleteAll(){
        this.realm.executeTransaction(r -> {
            r.deleteAll();
        });
    }

}
