package filesystem;

/**
 * Enum for filetypes.
 */
public enum FileType {
    PDF(".pdf"),
    JAVA(".java"),
    TXT(".txt");

    /**
     * The extension of the file type.
     */
    private final String extension;

    /**
     * Create a new file type with a given extension
     * @param extension
     *        Extension of the file type.
     */
    FileType(String extension) {
        this.extension = extension;
    }

    /**
     * Returns the extension of the file type.
     */
    public String getExtension() {
        return extension;
    }
}
