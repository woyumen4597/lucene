package cn.jrc.domain;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/17 10:35
 */
public class Book {
    private String id;
    private String name;
    private String author;
    private String image;

    public Book(String id, String name, String author, String image) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.image = image;
    }

    public Book() {

    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
