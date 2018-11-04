package jenkins.plugins.commentPlugin;

import hudson.XmlFile;
import hudson.model.*;
import hudson.util.HttpResponses;
import jenkins.model.Jenkins;
import jenkins.plugins.commentPlugin.model.Comment;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.QueryParameter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentProjectAction implements ProminentProjectAction {

    //TODO: Change System out to LOGGER
    @SuppressWarnings("rawtypes")
    private AbstractProject<? extends AbstractProject, ? extends AbstractBuild> project;
    private Comment lastComment;
    private XmlFile commentsFile;
    private XmlFile lastCommentFile;

    public CommentProjectAction(AbstractProject<? extends AbstractProject, ? extends AbstractBuild> project) {
        this.project = project;
        this.commentsFile = new XmlFile(Jenkins.XSTREAM, new File(project.getRootDir(), Comment.COMMENTS_FILENAME));
        this.lastCommentFile = new XmlFile(Jenkins.XSTREAM, new File(project.getRootDir(), Comment.LAST_COMMENTS_FILENAME));
    }

    private Comment getLastCommentFromFile() {
        try {
            return (Comment) lastCommentFile.read();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addComment(Comment comment) {
        updateLastComment(comment);
        List<Comment> commentList = loadComments();
        commentList.add(comment);
        saveComments(commentList);
    }

    private Comment getLastCommentOrNull() {
        if(lastComment == null) {
            this.lastComment = getLastCommentFromFile();
        }
        if(lastComment == null) {
            return null;
        } else {
            return this.lastComment;
        }
    }


    private List<Comment> loadComments() {
        try {
            return (List<Comment>) this.commentsFile.read();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void saveComments(List<Comment> commentList) {
        try {
            this.commentsFile.write(commentList);
            // Log successful save
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateHtmlTable() {
        StringBuffer output = new StringBuffer("<h2>Comments for " + project.getName() + "</h2>");
        output.append("<table>");
        output.append("<tr>");
        output.append("<th><b><h2>Date</h2></b></th>");
        output.append("<th><b><h2>User</h2></b></th>");
        output.append("<th><b><h2>Comment</h2></b></th>");
        output.append("</tr>");
        for (Comment c : loadComments()) {
            output.append("<tr>";
            output.append("<td>").append(c.getCreationDate()).append("</td>");
            output.append("<td>").append(c.getCreator()).append("</td>");
            output.append("<td>").append(c.getComment()).append("</td>");
            output.append("</tr>");
        }
        output.append("</table>");
        return output.toString();
    }

    private void updateLastComment(Comment lastComment) {
        this.lastComment = lastComment;
        try {
            this.lastCommentFile.write(lastComment);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return "Comment";
    }

    public String getUrlName() {
        return "comment";
    }

    public  String getLastCommentContent() {
        return getLastCommentOrNull() != null ? lastComment.getComment() : "";
    }

    public String getLastCommentDate() {
        return getLastCommentOrNull() != null ? lastComment.getCreationDate().toString() : "";
    }

    public String getLastCommentCreator() {
        return getLastCommentOrNull() != null ? lastComment.getCreator() : "";
    }

    public HttpResponse doHistory() {
        return HttpResponses.html(generateHtmlTable());
    }

    public HttpResponse doAdd(@QueryParameter("comment") String comment) {
        if (comment == null) {
            return HttpResponses.errorWithoutStack(400, "Bad request! Comment is missing.");
        }
        if (Jenkins.getAuthentication().getPrincipal().equals("anonymous")) {
            return HttpResponses.errorWithoutStack(404, "Anonymous users can't edit comments!");
        }
//        System.out.println(Jenkins.getAuthentication().getPrincipal());
        addComment(new Comment(comment, new Date(), Jenkins.getAuthentication().getPrincipal().toString()));
        return HttpResponses.html(getLastCommentContent());
    }
}
