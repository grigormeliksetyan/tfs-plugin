package hudson.plugins.tfs.model;

import java.io.IOException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Workspace {

    private final Server server;
    private final String name;
    private final String computer;
    private final String owner;
    private final String comment;

    public Workspace(Server server, String name, String computer, String owner, String comment) {
        this.server = server;
        this.name = name;
        this.computer = computer;
        this.owner = owner;
        this.comment = comment;
    }
    
    public Workspace(Server server, String name) {
        this(server, name, "", "", "");
    }

    public String getName() {
        return name;
    }

    public String getComputer() {
        return computer;
    }

    public String getOwner() {
        return owner;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 27).append(name).append(owner).append(computer).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        final Workspace other = (Workspace) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.name, other.name);
        builder.append(this.owner, other.owner);
        builder.append(this.computer, other.computer);
        return builder.isEquals();
    }
}
