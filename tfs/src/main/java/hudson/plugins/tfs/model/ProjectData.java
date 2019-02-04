package hudson.plugins.tfs.model;

import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.ParametersAction;
import hudson.model.Run;
import hudson.plugins.tfs.util.BuildVariableResolver;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.Serializable;

/**
 * @author <a href="mailto:grigor.meliksetyan@gmail.com">Grish Meliksetyan</a>
 */

public final class ProjectData implements Serializable {

    public final String projectPath;
    private String localPath;

    @DataBoundConstructor
    public ProjectData(final String projectPath, final String localPath) {
        this.projectPath = Util.removeTrailingSlash(Util.fixNull(projectPath).trim());
        this.localPath = Util.fixEmptyAndTrim(localPath);
    }

    public String getProjectPath() {
        return projectPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(final String localPath) {
        this.localPath = localPath;
    }

    /**
     * Returns the project server path.
     */
    public String getProjectPath(final Run<?, ?> run) {
        return Util.replaceMacro(substituteBuildParameter(run, projectPath), new BuildVariableResolver(run.getParent()));
    }

    /**
     * Returns the substituted build parameter.
     */
    public static String substituteBuildParameter(final Run<?, ?> run, final String text) {
        if (run instanceof AbstractBuild<?, ?>) {
            AbstractBuild<?, ?> build = (AbstractBuild<?, ?>) run;
            if (build.getAction(ParametersAction.class) != null) {
                return build.getAction(ParametersAction.class).substitute(build, text);
            }
        }
        return text;
    }

    /**
     * Returns all projects that mapped in the workspace.
     */
    public static ProjectData[] getProjects(final String projectPath, final String localPath, final ProjectData[] projects) {
        ProjectData[] newProjects = new ProjectData[projects.length + 1];
        newProjects[0] = new ProjectData(projectPath, localPath);
        for (int ndx = 0; ndx < projects.length; ++ndx) {
            newProjects[ndx + 1] = new ProjectData(projects[ndx].getProjectPath(), projects[ndx].getLocalPath());
        }

        return newProjects;
    }

    /**
     * Returns the URL to the team project collection.
     */
    public static ProjectData[] getProjects(final Run<?, ?> run, final String projectPath, final String localPath, final ProjectData[] projects) {
        ProjectData[] newProjects = new ProjectData[projects.length + 1];
        newProjects[0] = new ProjectData(
                Util.replaceMacro(substituteBuildParameter(run, projectPath), new BuildVariableResolver(run.getParent())),
                Util.replaceMacro(substituteBuildParameter(run, localPath), new BuildVariableResolver(run.getParent())));
        for (int ndx = 0; ndx < projects.length; ++ndx) {
            newProjects[ndx + 1] = new ProjectData(projects[ndx].getProjectPath(run), projects[ndx].getLocalPath());
        }

        return newProjects;
    }
}
