package solar;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 天合光能经销商数据同步
 *
 * @author Sxuet
 * @since 2022.05.21 16:27
 */
public class SolarDataSyn {

  private static final MediaType MEDIA_TYPE_UTF8 =
      MediaType.parse("application/json; charset=utf-8");
  private static final MediaType MEDIA_TYPE_XWWW =
      MediaType.parse("application/x-www-form-urlencoded");
  private static final OkHttpClient CLIENT = new OkHttpClient();
  static HashMap<String, String> headers =
      new HashMap<String, String>(2) {
        {
          put("Transfer-Encoding", "chunked");
          // put("authorization", "bearer D-3834935d-12b3-4bbc-8e8f-bdc716954fc8");
        }
      };

  public String autoLogin(Map<String, String> headers, AutoLoginDTO autoLoginDTO)
      throws IOException {
    String postAutoLoginUrl = "https://mplat.trinasolar.com/mam-api/user/autologin";
    RequestBody requestBody =
        new MultipartBody.Builder()
            .addFormDataPart("appVersion", autoLoginDTO.getAppVersion())
            .addFormDataPart("osVersion", autoLoginDTO.getOsVersion())
            .addFormDataPart("secretKey", autoLoginDTO.getSecretKey())
            .addFormDataPart("sign", autoLoginDTO.getSign())
            .build();
    Request request =
        new Request.Builder()
            .url(postAutoLoginUrl)
            .post(requestBody)
            .headers(Headers.of(headers))
            .build();
    Response response = CLIENT.newCall(request).execute();

    String accessToken =
        JSONUtil.parseObj(Objects.requireNonNull(response.body()).string())
            .getByPath("data.accessToken", String.class);
    System.out.println(accessToken);
    return accessToken;
  }

  public static Response post(String url, String json, Map<String, String> headers)
      throws IOException {
    RequestBody body = RequestBody.create(json, MEDIA_TYPE_UTF8);
    Request request =
        new Request.Builder().url(url).post(body).headers(Headers.of(headers)).build();

    return CLIENT.newCall(request).execute();
  }

  public static Response get(String url, Map<String, String> headers) throws IOException {
    Request request = new Request.Builder().url(url).get().headers(Headers.of(headers)).build();
    return CLIENT.newCall(request).execute();
  }

  public static void main(String[] args) throws IOException {

    SolarDataSyn solarDataSyn = new SolarDataSyn();

    // 获取basic token
    String basicToken =
        solarDataSyn.loginByTfxToken(solarDataSyn.autoLogin(headers, new AutoLoginDTO()));
    // 获取惠农宝全部列表
    String listUrl = "https://apigw.trinablue.com/tfs-api-financial/2.0.0/prod/gfk/tft/loan/list";
    String requestJson =
        "{\"applyTimeStart\":\"\",\"applyTimeEndTime\":\"\",\"tel\":\"\",\"customerNo\":\"\",\"loanNo\":\"\",\"pageIndex\":1,\"pageSize\":50}";
    headers.put("authorization", "bearer " + basicToken);
    Response response = post(listUrl, requestJson, headers);

    // 获取Response中data.list，转换为数组，每个item为一个Json对象
    List<JSONObject> jsonObjectList =
        ((JSONArray)
                JSONUtil.parseObj(Objects.requireNonNull(response.body()).string())
                    .getByPath("data.list"))
            .toList(JSONObject.class);

    // 根据loadNo获取项目详情
    String detailUrl =
        "https://apigw.trinablue.com/tfs-api-financial/2.0.0/prod/gfk/detail?loanNo=";
    // 获取json对象中的loanNo（项目公文号）
    jsonObjectList.forEach(
        item -> {
          String loanNo = item.getStr("loanNo");
          try {
            detail(detailUrl + loanNo, headers);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }

  private String loginByTfxToken(String tftToken) throws IOException {
    String loginByTfxTokenUrl =
        "https://apigw.trinablue.com/tfs-api-financial/2.0.0/prod/loginByTfxToken?tftToken="
            + tftToken;
    Request request =
        new Request.Builder().url(loginByTfxTokenUrl).get().headers(Headers.of(headers)).build();
    Response response = CLIENT.newCall(request).execute();
    return JSONUtil.parse(Objects.requireNonNull(response.body()).string())
        .getByPath("data.access_token", String.class);
  }

  public static void detail(String url, HashMap<String, String> headers) throws IOException {
    Response response1 = get(url, headers);
    JSONObject jsonObject =
        (JSONObject)
            JSONUtil.parseObj(Objects.requireNonNull(response1.body()).string())
                .getByPath("data.baseInfo");
    System.out.println(jsonObject);
  }
}
