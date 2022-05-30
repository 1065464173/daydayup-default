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

  private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
  private static final OkHttpClient CLIENT = new OkHttpClient();

  public static Response post(String url, String json, Map<String, String> headers)
      throws IOException {
    RequestBody body = RequestBody.create(json, MEDIA_TYPE);
    Request request =
        new Request.Builder().url(url).post(body).headers(Headers.of(headers)).build();

    return CLIENT.newCall(request).execute();
  }

  public static Response get(String url, Map<String, String> headers) throws IOException {
    Request request = new Request.Builder().url(url).get().headers(Headers.of(headers)).build();
    return CLIENT.newCall(request).execute();
  }

  public static void main(String[] args) throws IOException {
    // 获取惠农宝全部列表
    String listUrl = "https://apigw.trinablue.com/tfs-api-financial/2.0.0/prod/gfk/tft/loan/list";
    String requestJson =
        "{\"applyTimeStart\":\"\",\"applyTimeEndTime\":\"\",\"tel\":\"\",\"customerNo\":\"\",\"loanNo\":\"\",\"pageIndex\":1,\"pageSize\":50}";
    HashMap<String, String> headers =
        new HashMap<String, String>(2) {
          {
            put("Content-Type", "application/json;charset=UTF-8");
            put("authorization", "bearer D-3834935d-12b3-4bbc-8e8f-bdc716954fc8");
          }
        };
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
    // Response response1 = get(detailUrl, headers);
    // JSONObject jsonObject =
    //    (JSONObject)
    //        JSONUtil.parseObj(Objects.requireNonNull(response1.body()).string())
    //            .getByPath("data.baseInfo");
    //
    // System.out.println(jsonObject.toStringPretty());
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
