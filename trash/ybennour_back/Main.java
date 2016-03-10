package ybennour_back;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	    if (StringUtils.isEmpty(args[0])) {
            throw new RuntimeException("Command error : missing argument");
        }

        File file = new File(args[0]);
        JsonNode root;
        try {
            root = readFile(file);
        } catch (IOException e) {
            throw new RuntimeException("file not valid");
        }

        Core code = new Core();


    }

    private static JsonNode readFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(file);
        return node;
    }
}
