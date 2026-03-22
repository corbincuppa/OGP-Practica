package filesystem;

/**
 * Enum for filetypes.
 */
public enum Extension {
    PDF(".pdf"),
    JAVA(".java"),
    TXT(".txt");

    private final String extension;

    Extension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
