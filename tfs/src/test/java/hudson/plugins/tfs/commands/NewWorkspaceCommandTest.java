package hudson.plugins.tfs.commands;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.versioncontrol.WorkspaceLocation;
import com.microsoft.tfs.core.clients.versioncontrol.WorkspaceOptions;
import com.microsoft.tfs.core.clients.versioncontrol.soapextensions.WorkingFolder;
import hudson.plugins.tfs.model.ProjectData;
import hudson.plugins.tfs.model.Server;
import hudson.remoting.Callable;
import org.junit.Test;

import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

public class NewWorkspaceCommandTest extends AbstractCallableCommandTest {

    private static final List<String> EMPTY_CLOAKED_PATHS = Collections.emptyList();

    @Test public void assertLogging() throws Exception {
        ProjectData[] projects = new ProjectData[0];
        projects = ProjectData.getProjects(null, null, projects);
        when(server.getUserName()).thenReturn("snd\\user_cp");
        when(vcc.createWorkspace(aryEq((WorkingFolder[]) null),
                isA(String.class),
                isA(String.class),
                isA(String.class),
                isA(String.class),
                isA(WorkspaceLocation.class),
                isA(WorkspaceOptions.class))).thenReturn(null);
        final NewWorkspaceCommand command = new NewWorkspaceCommand(server, "TheWorkspaceName", projects, EMPTY_CLOAKED_PATHS) {
            @Override
            public Server createServer() {
                return server;
            }

            @Override
            protected void updateCache(final TFSTeamProjectCollection connection) {
                // no-op for tests
            }
        };
        final Callable<Void, Exception> callable = command.getCallable();

        callable.call();

        assertLog(
                "Creating workspace 'TheWorkspaceName' owned by 'snd\\user_cp'...",
                "Created workspace 'TheWorkspaceName'."
        );
    }

    @Test public void assertLoggingWhenAlsoMapping() throws Exception {
        final List<String> cloakedPaths = Collections.singletonList("$/Stuff/Hide");
        ProjectData[] projects = new ProjectData[0];
        projects = ProjectData.getProjects("$/Stuff", "/home/jenkins/jobs/stuff/workspace", projects);
        when(server.getUserName()).thenReturn("snd\\user_cp");
        when(vcc.createWorkspace(aryEq((WorkingFolder[]) null),
                isA(String.class),
                isA(String.class),
                isA(String.class),
                isA(String.class),
                isA(WorkspaceLocation.class),
                isA(WorkspaceOptions.class))).thenReturn(null);
        final NewWorkspaceCommand command = new NewWorkspaceCommand(server, "TheWorkspaceName", projects, cloakedPaths) {
            @Override
            public Server createServer() {
                return server;
            }

            @Override
            protected void updateCache(final TFSTeamProjectCollection connection) {
                // no-op for tests
            }
        };
        final Callable<Void, Exception> callable = command.getCallable();

        callable.call();

        assertLog(
                "Creating workspace 'TheWorkspaceName' owned by 'snd\\user_cp'...",
                "Mapping '$/Stuff' to local folder '/home/jenkins/jobs/stuff/workspace' in workspace 'TheWorkspaceName'...",
                "Cloaking '$/Stuff/Hide' in workspace 'TheWorkspaceName'...",
                "Created workspace 'TheWorkspaceName'."
        );
    }

    @Override protected AbstractCallableCommand createCommand(final ServerConfigurationProvider serverConfig) {
        ProjectData[] projects = new ProjectData[0];
        projects = ProjectData.getProjects("$/serverPath", "local/path", projects);

        return new NewWorkspaceCommand(serverConfig, "workspaceName", projects, EMPTY_CLOAKED_PATHS);    }
}
