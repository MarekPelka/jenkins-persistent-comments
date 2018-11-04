package jenkins.plugins.commentPlugin;

import hudson.Extension;
import hudson.XmlFile;
import hudson.model.Job;
import hudson.views.ListViewColumn;
import hudson.views.ListViewColumnDescriptor;
import jenkins.model.Jenkins;
import jenkins.plugins.commentPlugin.model.Comment;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;

public class LastComment extends ListViewColumn {

    @DataBoundConstructor
    public LastComment() {
        super();
    }

    public String getLastComment(@SuppressWarnings("rawtypes") Job job) {
        try {
            //TODO: Firstly check cache with last comment in CommentProjectAction, if empty read from disk.
            Comment c = (Comment) new XmlFile(Jenkins.XSTREAM, new File(job.getRootDir(), Comment.LAST_COMMENTS_FILENAME)).read();
            return c.getComment();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Extension
    public static class DescriptorImpl extends ListViewColumnDescriptor {

        @Override
        public boolean shownByDefault() {
            return false;
        }

        @Override
        public String getDisplayName() {
            return "Comment";
        }

    }

}
