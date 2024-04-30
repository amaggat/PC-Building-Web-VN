package backend.utility;

import backend.component.model.ElectronicComponents;
import backend.pcprofile.PcProfile;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static final String URL = "http://localhost:9090/engines/pcrs_change/events";
    public static final String URL_GET = "http://localhost:9090/engines/pcrs_change/queries";
    public static final String URL_CHATBOT = "http://localhost:5000/chatbot";
    public static final String URL_CHATBOT_VOICE = "http://localhost:5000/chatbot/voice";

    public static List<String> returnPcProfileID(List<PcProfile> pcProfileList) {
        List<String> profileId = new ArrayList<>();

        for (PcProfile pcProfile : pcProfileList) {
            profileId.add(pcProfile.getId());
        }

        return profileId;
    }

    public static double to2DecimalDouble(double avg) {
        avg = avg*10;
        avg = (double)((int) avg);
        avg = avg/10;
        return avg;
    }

    public static List<String> returnComponentIdList(List<ElectronicComponents> electronicComponentsList) {
        List<String> componentId = new ArrayList<>();

        for (ElectronicComponents electronicComponents : electronicComponentsList) {
            componentId.add(electronicComponents.getId());
        }

        return componentId;
    }

    public static Chatbot returnContent(String content, String URL) throws IOException {
        HttpURLConnection con = setConnection(URL);
        String jsonChat = toJsonChatbot(content);
        doPost(con, jsonChat);
        Chatbot contentReturn = outChatbotResponse(con);

        return contentReturn;
    }

    public static Result returnReccomendedItem(String typeValue, String value, Integer id) throws IOException {
        HttpURLConnection con = setConnection(URL_GET);
        String jsonInputString = toJsonString(typeValue, value, id);
        System.out.println(jsonInputString);
        doPost(con, jsonInputString);
        Result result = outJsonResponse(con);
        return result;
    }

    public static void sendActivity(String linkQueries, String action, Integer userid, String item) throws IOException {
        HttpURLConnection con = setConnection(URL);
        String jsonInputString = toJsonRequest(action, userid, item);
        System.out.println(jsonInputString);
        doPost(con, jsonInputString);
    }

    private static Chatbot outChatbotResponse(HttpURLConnection con) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            Gson gson = new Gson();
            Chatbot result = gson.fromJson(response.toString(), Chatbot.class);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Chatbot();
    }

    private static Result outJsonResponse(HttpURLConnection con) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            Gson gson = new Gson();
            Result result = gson.fromJson(response.toString(), Result.class);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Result();
    }

    private static HttpURLConnection setConnection(String path) throws IOException {
        URL url = new URL(path);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        con.setDoOutput(true);
        return con;
    }


    private static void doPost(HttpURLConnection con, String jsonInputString) throws IOException {
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int code = con.getResponseCode();
        System.out.println(code);
    }



    public static String toJsonRequest(String action, Integer userid, String item) {
        String jsonInputString = "{\n" +
                "    \"event\": \"" + action + "\",\n" +
                "    \"entityType\": \"" + "user" + "\",\n" +
                "    \"entityId\": \"" + userid + "\",\n" +
                "    \"targetEntityType\": \"" + "item" + "\",\n" +
                "    \"targetEntityId\": \"" + item + "\",\n" +
                "    \"properties\": \" { }\",\n" +
                "    \"eventTime\": \"" + LocalDateTime.now() + "Z" + "\"\n" +
                "}";
        return jsonInputString;
    }

    private static String toJsonString(String typeValue, String value, Integer id) {
        String jsonType = new String();
        String user = new String();
        String rules = "    \"rules\": [\n" +
                "    {\n" +
                "      \"name\": \"category\",\n" +
                "      \"values\": [\"" + value + "\"],\n" +
                "      \"bias\": -1\n" +
                "    }\n" +
                "  ]\n";

        if (id != null) {
            user = "    \"user\": \"" + id + "\",\n";
        }

        if (typeValue != null) {
            jsonType = "    \"item\": \"" + typeValue + "\",\n";
        }

        String jsonInputString = new String();
        jsonInputString = "{\n" +
                jsonType +
                user +
                rules +
                "}";

        return jsonInputString;
    }

    private static String toJsonChatbot(String content) {
        String json = new String();
        json = "{\"content\": \"" + content + "\"}";
        return json;
    }

}
