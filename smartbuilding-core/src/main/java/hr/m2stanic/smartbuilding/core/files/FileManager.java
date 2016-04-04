package hr.m2stanic.smartbuilding.core.files;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileManager {

    /**
     * @param relativePath file path (including file name) relative to repository root folder
     * @param stream       - source stream for the file
     * @throws IOException
     */
    public void saveFile(String relativePath, InputStream stream) throws IOException;

    /**
     * @param stream   - source stream for the file
     * @param fileName - original name of the file
     * @return path of saved file relative to file repository root
     * @throws IOException
     */
    public String saveFile(InputStream stream, String fileName) throws IOException;


    /**
     * @param relativePath - file path (including file name) relative to repository root folder
     * @return file if found, otherwise null
     */
    public File getFile(String relativePath);

    /**
     * @param relativePath     - file path (including file name) relative to repository root folder
     * @param createIfNotExist - weather to create file on that path if it doesn't exist
     * @return file object for given path
     */
    public File getFile(String relativePath, boolean createIfNotExist) throws IOException;

    /**
     * @param relativePath file path (including file name) relative to repository root folder
     */
    public void deleteFile(String relativePath);


    public String getExtension(String fileName);


}
