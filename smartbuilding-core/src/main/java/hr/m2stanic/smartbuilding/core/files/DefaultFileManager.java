package hr.m2stanic.smartbuilding.core.files;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;

@Slf4j
public class DefaultFileManager implements FileManager {

    private String absoluteRootPath;

    public DefaultFileManager(String absoluteRootPath) {

        this.absoluteRootPath = absoluteRootPath;

        try {
            File rootDir = new File(absoluteRootPath);
            if (!rootDir.exists() || !rootDir.isDirectory()) {
                rootDir.mkdirs();
            }
        } catch (Exception e) {
            log.error("Failed to create root dir for file repository!", e);
        }
    }

    @Override
    public void saveFile(String relativePath, InputStream stream) throws IOException {
        OutputStream out = null;
        try {
            File destFile = new File(absoluteRootPath, relativePath);
            if (!destFile.exists() || destFile.isDirectory()) {
                destFile.getParentFile().mkdirs();
                destFile.createNewFile();
            }
            out = new FileOutputStream(destFile);
            IOUtils.copy(stream, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    @Override
    public String saveFile(InputStream stream, String fileName) throws IOException {
        String randomFilename = RandomStringUtils.randomAlphanumeric(10);
        String ext = FilenameUtils.getExtension(fileName);
        randomFilename += ("." + ext);
        saveFile(randomFilename, stream);
        return randomFilename;
    }


    @Override
    public File getFile(String relativePath) {
        return new File(absoluteRootPath, relativePath);
    }

    @Override
    public File getFile(String relativePath, boolean createIfNotExist) throws IOException {

        File file = new File(absoluteRootPath, relativePath);
        if (createIfNotExist && (!file.exists() || file.isDirectory())) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }


    @Override
    public void deleteFile(String relativePath) {
        File file = new File(absoluteRootPath, relativePath);
        if (file.exists() && !file.isDirectory()) {
            file.delete();
        }
    }

    public String getExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index >= 0 && index + 1 < fileName.length()) {
            return fileName.substring(index + 1);
        }
        return null;
    }


}
