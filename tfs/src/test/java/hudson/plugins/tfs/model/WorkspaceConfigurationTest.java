package hudson.plugins.tfs.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class WorkspaceConfigurationTest {

    private static final List<String> EMPTY_CLOAKED_PATHS_LIST = Collections.emptyList();

    @Test public void assertConfigurationsEquals() {
        final List<String> cloakList = Collections.singletonList("cloak");

        ProjectData[] projects = new ProjectData[0];
        WorkspaceConfiguration one = new WorkspaceConfiguration("server", "workspace", ProjectData.getProjects("project", "workfolder", projects), cloakList);
        WorkspaceConfiguration two = new WorkspaceConfiguration("server", "workspace", ProjectData.getProjects("project", "workfolder", projects), cloakList);
        assertThat(one, is(two));
        assertThat(two, is(one));
        assertThat(one, is(one));
        assertThat(one, not(new WorkspaceConfiguration("aserver", "workspace", ProjectData.getProjects("project", "workfolder", projects), cloakList)));
        assertThat(one, not(new WorkspaceConfiguration("server", "aworkspace", ProjectData.getProjects("project", "workfolder", projects), cloakList)));
        assertThat(one, not(new WorkspaceConfiguration("server", "workspace", ProjectData.getProjects("aproject", "workfolder", projects), cloakList)));
        assertThat(one, not(new WorkspaceConfiguration("server", "workspace", ProjectData.getProjects("project", "aworkfolder", projects), cloakList)));
        assertThat(one, not(new WorkspaceConfiguration("server", "workspace", ProjectData.getProjects("project", "workfolder", projects), EMPTY_CLOAKED_PATHS_LIST)));
    }
}
