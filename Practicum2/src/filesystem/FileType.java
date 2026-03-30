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

    /**
     * Check if this extension is a legal extension.
     *
     * @return	True if the given string is effective, not
     * 			empty and consisting only of lower-case letters; false otherwise.
     * 			| result ==
     * 			|	(getExtension() != null) && getExtension().matches("[a-z]+")
     */
    protected boolean isValidExtension() {
        if (this.getExtension() != null && this.getExtension().matches("[a-z]+")) {
            return true;
        }
        else{
            return false;
        }
    }
}
