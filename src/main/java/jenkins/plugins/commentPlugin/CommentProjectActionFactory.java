package jenkins.plugins.commentPlugin;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.TransientProjectActionFactory;
import hudson.model.AbstractProject;

import java.util.Collection;
import java.util.Collections;

@Extension
public class CommentProjectActionFactory extends TransientProjectActionFactory {

    @Override
    public Collection<? extends Action> createFor(AbstractProject target) {
        System.out.println("createdFor: " + target.getName());
        return Collections.singleton(new CommentProjectAction(target));
    }

}
