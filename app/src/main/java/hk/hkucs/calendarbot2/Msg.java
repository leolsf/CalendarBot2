package hk.hkucs.calendarbot2;

public class Msg {
    public static final int TYPE_RECEIVE = 0;
    public static final int TYPE_SEND = 1;
    private String content;

    public Msg(String content, int picture, int type) {
        this.content = content;
        this.picture = picture;
        this.type = type;
    }

    private int type;
    private int picture;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPicture() {
        return picture;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

