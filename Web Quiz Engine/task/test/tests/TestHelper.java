package tests;

import com.google.gson.*;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.hyperskill.hstest.v6.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.v6.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.v6.testcase.CheckResult;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.BiFunction;

import static org.hyperskill.hstest.v6.mocks.web.request.HttpRequestExecutor.packUrlParams;

class WrongAnswer extends RuntimeException {
    public WrongAnswer(String msg) {
        super(msg);
    }
}

class HttpResp {
    private String url;
    private String method;
    private HttpResponse resp;

    HttpResp(HttpResponse resp, String url, String method) {
        this.url = url;
        this.resp = resp;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getRequest() {
        return getMethod() + " " + getUrl();
    }

    public int getStatusCode() {
        return resp.getStatusCode();
    }

    public Map<String, String> getHeaders() {
        return resp.getHeaders();
    }

    public byte[] getRawContent() {
        return resp.getRawContent();
    }

    public String getContent() {
        return resp.getContent();
    }

    public JsonElement getJson() {
        return resp.getJson();
    }
}

public class TestHelper {
    // Function just to be able to throw WrongAnswer
    // as the way to fail the test
    static <T> BiFunction<String, T, CheckResult> wrap(
        BiFunction<String, T, CheckResult> original) {

        return (r, a) -> {
            try {
                return original.apply(r, a);
            } catch (WrongAnswer ex) {
                return CheckResult.FALSE(ex.getMessage());
            }
        };
    }

    static void checkStatusCode(HttpResp resp, int status) {
        if (resp.getStatusCode() != status) {
            throw new WrongAnswer(
                    resp.getRequest() +
                    " should respond with status code " + status + ", " +
                    "responded: " + resp.getStatusCode() + "\n\n" +
                    "Response body:\n\n" + resp.getContent()
            );
        }
    }

    static void checkHeader(HttpResp resp, String header, String value) {
        Map<String, String> headers = resp.getHeaders();
        if (!headers.containsKey(header)) {
            throw new WrongAnswer(
                resp.getRequest() +
                    " should respond with header \"Content-Type\", " +
                    "but this header is not found in the response."
            );
        }
        String actualValue = headers.get(header);
        if (!actualValue.equals(value)) {
            throw new WrongAnswer(
                resp.getRequest() +
                    " should respond with header \"Content-Type\" being " +
                    "equal to " + value + " but in the response header " +
                    "\"Content-Type\" is equal to " + actualValue + "."
            );
        }
    }

    static JsonElement getJson(HttpResp resp) {
        checkHeader(resp,
            HttpHeaders.CONTENT_TYPE,
            ContentType.APPLICATION_JSON.getMimeType()
        );
        try {
            return resp.getJson();
        } catch (Exception ex) {
            throw new WrongAnswer(
                resp.getRequest() + " should return a valid JSON"
            );
        }
    }

    static JsonElement getJson(String json) {
        return new JsonParser().parse(json);
    }

    static private String constructUrl(String address) {
        if (!address.startsWith("/")) {
            address = "/" + address;
        }
        return "http://localhost:8889" + address;
    }

    static public HttpRequest post(String address, Map<String, String> params) {
        return new HttpRequest("POST")
            .setUri(constructUrl(address))
            .setContent(packUrlParams(params))
            .setContentType(ContentType.APPLICATION_FORM_URLENCODED);
    }

    static public HttpRequest put(String address, Map<String, String> params) {
        return new HttpRequest("PUT")
            .setUri(constructUrl(address))
            .setContent(packUrlParams(params))
            .setContentType(ContentType.APPLICATION_FORM_URLENCODED);
    }
}
