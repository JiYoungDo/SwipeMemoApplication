package com.CarrieDo.swipememoapplication;

public class Memo {
    String title;
    String contents;

    // Constructor
    public Memo(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    // Getter
    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    //Setter
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
