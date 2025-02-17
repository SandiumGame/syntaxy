package org.sandium.syntaxy.backend;

import java.nio.file.Path;
import java.util.Collection;

public class ExecutionContext {

    private Path projectDir;
    private Path selectedOpenFile;
    private Collection<Path> openFiles;

    public Path getProjectDir() {
        return projectDir;
    }

    public void setProjectDir(Path projectDir) {
        this.projectDir = projectDir;
    }

    public Path getSelectedOpenFile() {
        return selectedOpenFile;
    }

    public void setSelectedOpenFile(Path selectedOpenFile) {
        this.selectedOpenFile = selectedOpenFile;
    }

    public Collection<Path> getOpenFiles() {
        return openFiles;
    }

    public void setOpenFiles(Collection<Path> openFiles) {
        this.openFiles = openFiles;
    }
}
