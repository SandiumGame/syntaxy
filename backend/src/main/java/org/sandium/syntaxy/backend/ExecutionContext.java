package org.sandium.syntaxy.backend;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Locale;

public class ExecutionContext {

    private Locale userLocale;
    private Path projectDir;
    private Path selectedOpenFile;
    private Collection<Path> openFiles;

    public Locale getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(Locale userLocale) {
        this.userLocale = userLocale;
    }

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
