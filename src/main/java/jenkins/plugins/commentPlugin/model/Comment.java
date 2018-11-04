package jenkins.plugins.commentPlugin.model;

import java.io.File;
import java.util.Date;

public class Comment {

    public static final transient String COMMENTS_DIR = "comments";
    public static final transient String COMMENTS_FILENAME = COMMENTS_DIR + File.pathSeparator + "comments.xml";
    public static final transient String LAST_COMMENTS_FILENAME = COMMENTS_DIR + File.pathSeparator + "comment-last.xml";

    private final String comment;
    private final Date creationDate;
    private final String creator;

    public Comment(String comment, Date creationDate, String creator) {
        this.comment = comment;
        this.creationDate = creationDate;
        this.creator = creator;
    }

    public String getComment() {
        return comment;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getCreator() {
        return creator;
    }

    @Override
    public String toString() {
        return "Comment: " + this.comment + "; creationDate: " + this.creationDate + "; creator: " + this.creator;
    }
}
