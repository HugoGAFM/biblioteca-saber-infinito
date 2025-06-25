package org.project.database;

import java.io.*;
import java.util.logging.Logger;

public class FilesUtil {
    private static final Logger LOG = Logger.getLogger(FilesUtil.class.getName());

    public static String loadTextFile(String filename) throws IOException {
        InputStream is = BDconnection.class.getResourceAsStream("/" + filename);
        if (is == null) {
            throw new FileNotFoundException("Arquivo não encontrado no classpath: " + filename);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
