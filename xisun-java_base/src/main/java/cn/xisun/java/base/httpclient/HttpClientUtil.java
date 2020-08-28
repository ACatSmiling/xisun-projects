package cn.xisun.java.base.httpclient;

import org.apache.commons.io.FileUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XiSun
 * @Date 2020/8/21 11:13
 */
public class HttpClientUtil {
    /**
     * 执行无参数的get请求
     *
     * @param url 请求地址
     * @return
     * @throws IOException
     */
    public static String getRequest(String url) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http get请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                // 请求体内容
                String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
                // 将返回内容写入文件
                FileUtils.writeStringToFile(new File("D:\\getRequest.html"), responseContent, "UTF-8");
                return responseContent;
            }
        } finally {
            if (response != null) {
                response.close();
            }
            // 相当于关闭浏览器
            httpclient.close();
        }
        return "failed";
    }

    /**
     * 执行带参数的get请求，两种方式：
     * 1.直接将参数拼接到url后面，如：?wd=java
     * 2.使用URI的方法设置参数，如：setParameter("wd", "java")
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String getParamRequest(String url, Map<String, String> params) throws IOException, URISyntaxException {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 定义get请求的参数
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                uriBuilder.setParameter(param.getKey(), param.getValue());
            }
        }
        URI uri = uriBuilder.build();
        // 创建http get请求
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                // 请求体内容
                String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
                // 将返回内容写入文件
                FileUtils.writeStringToFile(new File("D:\\getParamsRequest.html"), responseContent, "UTF-8");
                return responseContent;
            }
        } finally {
            if (response != null) {
                response.close();
            }
            // 相当于关闭浏览器
            httpclient.close();
        }
        return "failed";
    }

    /**
     * 执行无参数的post请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String postRequest(String url) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http post请求
        HttpPost httpPost = new HttpPost(url);
        // 伪装浏览器请求
        httpPost.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        CloseableHttpResponse response = null;
        try {
            // 执行post请求
            response = httpclient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
                // 将返回内容写入文件
                FileUtils.writeStringToFile(new File("E:\\devtest\\oschina.html"), responseContent, "UTF-8");
                return responseContent;
            }
        } finally {
            if (response != null) {
                response.close();
            }
            // 相当于关闭浏览器
            httpclient.close();
        }
        return "failed";
    }

    /**
     * 执行带参数的post请求
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String postParamRequest(String url, Map<String, String> params) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建http post请求
        HttpPost httpPost = new HttpPost(url);
        // 伪装浏览器请求
        httpPost.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        // 设置post参数
        List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                parameters.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
        }
        CloseableHttpResponse response = null;
        try {
            // 构造一个form表单式的实体
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
            // 执行post请求
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得response的实体内容
                String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
                // 将返回内容写入文件
                FileUtils.writeStringToFile(new File("D:\\11.html"), responseContent, "UTF-8");
                return responseContent;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
            // 相当于关闭浏览器
            httpClient.close();
        }
        return "failed";
    }

    public static void main(String[] args) throws IOException {
        String url = "http://chemicaltagger.ch.cam.ac.uk/submit";
        Map<String, String> params = new HashMap<String, String>();
        params.put("ChemistryType", "Organic");
        String p = "Specific examples of compounds of formula I may be selected from the group consisting of\n" +
                "\t\t\t\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(1,3-benzodioxol-4-yl)ethanamine (compound 1000),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(1,3-benzodioxol-5-yl)ethanamine (compound 1001),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(1-methyl-5-phenyl-pyrazol-3-yl)ethanamine (compound 1002),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(2,2-difluoro-1,3-benzodioxol-4-yl)ethanamine (compound 1003),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(2,3-dihydro-1,4-benzodioxin-5-yl)ethanamine (compound 1004).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(2,3-dihydrobenzofuran-5-yl)ethanamine (compound 1005),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(2,3-dimethoxyphenyl)ethanamine (compound 1006),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(2-fluoro-3-methoxy-phenyl)ethanamine (compound 1007),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(2-fluoro-5-methoxy-phenyl)ethanamine (compound 1008),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(2-methylthiazol-4-yl)ethanamine (compound 1009),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(3-chloro-4-fluoro-phenyl)ethanamine (compound 1010),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(3-ethoxyphenyl)ethanamine (compound 1011),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(3-fluoro-5-methoxyphenyl)ethanamine (compound 1012).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(4-fluoro-2-methoxyphenyl)ethanamine (compound 1013),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1014),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(4-quinolyl)ethanamine (compound 1015),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(5-chloro-2-thienyl)ethanamine (compound 1016),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(8-quinolyl)ethanamine (compound 1017),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-[3-(methylethoxy)phenyl]ethanamine (compound 1018),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-[4-fluoro-3-(trifluoromethyl)phenyl]ethanamine (compound 1019),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-cyclopropylethanamine (compound 1020),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-imidazo[1,2-a]pyridin-3-ylethanamine (compound 1021),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-imidazo[1,5-a]pyridin-3-ylethanamine (compound 1022).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-pyrazolo[1,5-a]pyridin-3-ylethanamine (compound 1023),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-cyclohexylethanamine (compound 1024),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(3-fluoro-4-methoxyphenyl)ethanamine (compound 1025),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(3-fluorophenyl)ethanamine (compound 1026),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R/S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(3-methoxyphenyl)ethanamine (compound 1027),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-[3-(trifluoromethyl)phenyl]ethanamine (compound 1028),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-benzo[b]thiophen-3-yl-ethanamine (compound 1029),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(3-cyano-4-fluorophenyl)ethanamine (compound 1030),\n" +
                "\t\t\t\t(1R/S)—(N)-[(1R/S,3S)-3-(4-methanesulfonylphenyl)cyclopentyl]-1-(3,4-dihydro-3-oxo-[2H]-1,4-benzoxazin-6-yl)ethanamine (compound 1031).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(1H-tetrazol-5-yl)phenyl]cyclopentyl]-1-(1,3-benzodioxol-4-yl)ethanamine (compound 1032),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(1H-tetrazol-5-yl)phenyl]cyclopentyl]-1-(2,3-dihydro-1,4-benzodioxin-5-yl)ethanamine (compound 1033),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(1H-tetrazol-5-yl)phenyl]cyclopentyl]-1-(4-fluoro-2-methoxyphenyl)ethanamine (compound 1034),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(1H-tetrazol-5-yl)phenyl]cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1035),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(1H-tetrazol-5-yl)phenyl]cyclopentyl]-1-(3-chlorophenyl)ethanamine (compound 1036),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-1-(1,3-benzodioxol-4-yl)ethanamine (compound 1037),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-1-(1-isoquinolyl)ethanamine (compound 1038),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-1-(3,4-dichlorophenyl)ethanamine (compound 1039),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-1-(3,4-difluorophenyl)ethanamine (compound 1040),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-1-(3,4-dihydro-2H-1,5-benzodioxepin-6-yl)ethanamine (compound 1041),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-1-(3,4-dimethoxyphenyl)ethanamine (compound 1042),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-1-(3,5-dimethoxyphenyl)ethanamine (compound 1043),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-1-(3-chloro-4-fluoro-phenyl)ethanamine (compound 1044),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-1-(3-fluoro-5-methoxyphenyl)ethanamine (compound 1045).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-1-(4-fluoro-2-methoxyphenyl)ethanamine (compound 1046),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1047),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-1-pyrazolo[1,5-a]pyridin-3-ylethanamine (compound 1048),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-1-benzo[b]thiophen-3-yl-ethanamine (compound 1049),\n" +
                "\t\t\t\t(1R)—(N)-[(3S)-(1R/S,3S)-3-[4-[3-hydroxypyrrolidin-1-ylcarbonyl]phenyl]-cyclopentyl]-1-(3-ethoxyphenyl)ethanamine (compound 1050),\n" +
                "\t\t\t\t(1R)—(N)-[(3S)-(1R/S,3S)-3-[4-[3-hydroxypyrrolidin-1-ylcarbonyl]phenyl]-cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1051),\n" +
                "\t\t\t\t(1R)—(N)-[(3S)-(1R/S,3S)-3-[4-[3-hydroxypyrrolidin-1-ylcarbonyl]phenyl]-cyclopentyl]-1-(3-chlorophenyl)ethanamine (compound 1052),\n" +
                "\t\t\t\t(1R)—(N)-[(3S)-(1R/S,3S)-3-[4-[3-hydroxypyrrolidin-1-ylcarbonyl]phenyl]-cyclopentyl]-1-benzo[b]thiophen-3-yl-ethanamine (compound 1053),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R/S)-3-[4-acetamidophenyl]cyclopentyl]-1-(3-methoxyphenyl)-ethanamine (compound 1054).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R/S)-3-[4-methanesulfonylaminophenyl]cyclopentyl]-1-(3-methoxyphenyl)ethanamine (compound 1055),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-[4-[aminocarbonylmethoxy]phenyl]cyclopentyl]-1-(1,3-benzodioxol-4-yl)ethanamine (compound 1056),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-[4-[aminocarbonylmethoxy]phenyl]cyclopentyl]-1-(3-chloro-4-fluoro-phenyl)ethanamine (compound 1057),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-[4-[aminocarbonylmethoxy]phenyl]cyclopentyl]-1-(3-ethoxyphenyl)ethanamine (compound 1058),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-[4-(aminocarbonylmethoxy)-phenyl]cyclopentyl]-1-(4-fluoro-2-methoxyphenyl)ethanamine (compound 1059),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-[4-(aminocarbonylmethoxy)-phenyl]cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1060),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-[4-[aminocarbonylmethoxy]phenyl]cyclopentyl]-1-(3-chlorophenyl)ethanamine (compound 1061),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R/S)-3-[3-methylsulfonylaminophenyl]cyclopentyl]-1-(3-methoxyphenyl)ethanamine (compound 1062),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R/S)-3-[4-morpholinosulfonylphenyl]cyclopentyl]-1-(3-methoxyphenyl)ethanamine (compound 1063),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R/S)-3-[4-hydroxymethylphenyl]cyclopentyl]-1-(3-methoxyphenyl)ethanamine (compound 1064),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R/S)-3-[4-methanesulfonylaminophenyl]cyclopentyl]-1-(3-methoxyphenyl)ethanamine (compound 1065),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-(aminosulfonyl)ethylaminocarbonyl)phenyl]-cyclopentyl]-1-(1,3-benzodioxol-4-yl)ethanamine (compound 1066),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-(aminosulfonyl)ethylaminocarbonyl)phenyl]-cyclopentyl]-1-(3-ethoxyphenyl)ethanamine (compound 1067),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-(aminosulfonyl)ethylaminocarbonyl)phenyl]-cyclopentyl]-1-(4-fluoro-2-methoxyphenyl)ethanamine (compound 1068),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-(aminosulfonyl)ethylaminocarbonyl)phenyl]-cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1069),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-(aminosulfonyl)ethylaminocarbonyl)phenyl]-cyclopentyl]-1-(3-chlorophenyl)ethanamine (compound 1070),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-(aminosulfonyl)ethylaminocarbonyl)phenyl]cyclopentyl]-1-benzo[b]thiophen-3-yl-ethanamine (compound 1071),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(3-oxo-piperazinylcarbonyl)phenyl]cyclopentyl]-1-(1,3-benzodioxol-4-yl)ethanamine (compound 1072),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(3-oxo-piperazinylcarbonyl)phenyl]cyclopentyl]-1-(3-ethoxyphenyl)ethanamine (compound 1073),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(3-oxo-piperazinylcarbonyl)phenyl]cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1074).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(3-oxo-piperazinylcarbonyl)phenyl]cyclopentyl]-1-(3-chlorophenyl)ethanamine (compound 1075),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(3-oxo-piperazinylcarbonyl)phenyl]cyclopentyl]-1-benzo[b]thiophen-3-yl-ethanamine (compound 1076),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R/S)-3-[4-(acetamidomethyl)phenyl]cyclopentyl]-1-(3-methoxyphenyl)ethanamine (compound 1077),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(1,3-benzodioxol-4-yl)ethanamine (compound 1078),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(1,3-benzodioxol-5-yl)ethanamine (compound 1079),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(1-methyl-5-phenyl-pyrazol-3-yl)ethanamine (compound 1080),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(2,3-dihydro-1,4-benzodioxin-5-yl)ethanamine (compound 1081),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(2,3-dihydrobenzofuran-5-yl)ethanamine (compound 1082),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(2,3-dimethoxyphenyl)ethanamine (compound 1083),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(2-fluoro-3-methoxy-phenyl)ethanamine (compound 1084),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(2-fluoro-5-methoxy-phenyl)ethanamine (compound 1085).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(2-methylthiazol-4-yl)ethanamine (compound 1086),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(3-fluoro-5-methoxyphenyl)ethanamine (compound 1087),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(4-fluoro-2-methoxyphenyl)ethanamine (compound 1088),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1089),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(8-quinolyl)ethanamine (compound 1090),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-cyclopropylethanamine (compound 1091),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-imidazo[1,2-a]pyridin-3-ylethanamine (compound 1092),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-pyrazolo[1,5-a]pyridin-3-ylethanamine (compound 1093).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-cyclohexylethanamine (compound 1094),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(3-chlorophenyl)ethanamine (compound 1095),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(3-fluoro-4-methoxyphenyl)ethanamine (compound 1096),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(3-fluorophenyl)ethanamine (compound 1097),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-[3-(trifluoromethyl)phenyl]ethanamine (compound 1098),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-benzo[b]thiophen-3-yl-ethanamine (compound 1099),\n" +
                "\t\t\t\t(1R/S)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-pyrimidin-4-ylethanamine (compound 1100),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[2-hydroxypyridin-5-yl]cyclopentyl]-1-(3-cyano-4-fluorophenyl)ethanamine (compound 1101),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-(2-(acetamido)ethylaminocarbonyl)phenyl)-cyclopentyl]-1-(1,3-benzodioxol-4-yl)ethanamine (compound 1102).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-(2-(acetamido)ethylaminocarbonyl)phenyl)-cyclopentyl]-1-(3-ethoxyphenyl)ethanamine (compound 1103),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-(2-(acetamido)ethylaminocarbonyl)phenyl)-cyclopentyl]-1-(4-fluoro-2-methoxyphenyl)ethanamine (compound 1104),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-(2-(acetamido)ethylaminocarbonyl)phenyl)-cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1105),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-(2-(acetamido)ethylaminocarbonyl)phenyl)-cyclopentyl]-1-(3-chlorophenyl)ethanamine (compound 1106),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-(4-(2-(acetamido)ethylaminocarbonyl)phenyl)-cyclopentyl]-1-benzo[b]thiophen-3-yl-ethanamine (compound 1107).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(aminocarbonylmethylaminocarbonyl)phenyl]-cyclopentyl]-1-(3-ethoxyphenyl)ethanamine (compound 1108),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(aminocarbonylmethylaminocarbonyl)phenyl]-cyclopentyl]-1-(4-fluoro-2-methoxyphenyl)ethanamine (compound 1109),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(aminocarbonylmethylaminocarbonyl)phenyl]-cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1110),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(aminocarbonylmethylaminocarbonyl)phenyl]-cyclopentyl]-1-benzo[b]thiophen-3-yl-ethanamine (compound 1111),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-hydroxyethylaminosulfonyl)phenyl]cyclopentyl]-1-(3,4-difluorophenyl)ethanamine (compound 1112\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-hydroxyethylaminosulfonyl)phenyl]cyclopentyl]-1-(3,4-dihydro-2H-1,5-benzodioxepin-6-yl)ethanamine (compound 1113),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-hydroxyethylaminosulfonyl)phenyl]cyclopentyl]-1-(3,4-dimethoxyphenyl)ethanamine (compound 1114),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-hydroxyethylaminosulfonyl)phenyl]cyclopentyl]-1-(3-fluoro-5-methoxyphenyl)ethanamine (compound 1115),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-hydroxyethylaminosulfonyl)phenyl]cyclopentyl]-1-(4-fluoro-2-methoxyphenyl)ethanamine (compound 1116).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-hydroxyethylaminosulfonyl)phenyl]cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1117),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-hydroxyethylaminocarbonyl)phenyl]cyclopentyl]-1-(3-chloro-4-fluoro-phenyl)ethanamine (compound 1118),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-hydroxyethylaminocarbonyl)phenyl]cyclopentyl]-1-(3-ethoxyphenyl)ethanamine (compound 1119),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-hydroxyethylaminocarbonyl)phenyl]cyclopentyl]-1-(3-fluoro-5-methoxyphenyl)ethanamine (compound 1120),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-hydroxyethylaminocarbonyl)phenyl]cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1121),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-hydroxyethylaminocarbonyl)phenyl]cyclopentyl]-1-(3-chlorophenyl)ethanamine (compound 1122),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-hydroxyethylaminocarbonyl)phenyl]cyclopentyl]-1-benzo[b]thiophen-3-yl-ethanamine (compound 1123),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(cyanomethylaminocarbonyl)phenyl]cyclopentyl]-1-(3-chloro-4-fluoro-phenyl)ethanamine (compound 1124),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(cyanomethylaminocarbonyl)phenyl]cyclopentyl]-1-(3-ethoxyphenyl)ethanamine (compound 1125),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(cyanomethylaminocarbonyl)phenyl]cyclopentyl]-1-(3-fluoro-5-methoxyphenyl)ethanamine (compound 1126),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(cyanomethylaminocarbonyl)phenyl]cyclopentyl]-1-(4-fluoro-2-methoxyphenyl)ethanamine (compound 1127).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(cyanomethylaminocarbonyl)phenyl]cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1128),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(cyanomethylaminocarbonyl)phenyl]cyclopentyl]-1-(3-chlorophenyl)ethanamine (compound 1129),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(cyanomethylaminocarbonyl)phenyl]cyclopentyl]-1-benzo[b]thiophen-3-yl-ethanamine (compound 1130).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-(methanesulfonylamino)-ethylaminocarbonyl)-phenyl]cyclopentyl]-1-(3-ethoxyphenyl)ethanamine (compound 1131),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-(methanesulfonylamino)-ethylaminocarbonyl)-phenyl]cyclopentyl]-1-(3-fluoro-5-methoxyphenyl)ethanamine (compound 1132),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-(methanesulfonylamino)-ethylaminocarbonyl)-phenyl]cyclopentyl]-1-(4-fluoro-2-methoxyphenyl)ethanamine (compound 1133),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-(methanesulfonylamino)-ethylaminocarbonyl)-phenyl]cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1134).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-(methanesulfonylamino)-ethylaminocarbonyl)-phenyl]cyclopentyl]-1-(3-chlorophenyl)ethanamine (compound 1135),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-(2-(methanesulfonylamino)-ethylaminocarbonyl)-phenyl]cyclopentyl]-1-benzo[b]thiophen-3-yl-ethanamine (compound 1136),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-[4-[methanesulfonylaminocarbonylmethoxy]phenyl]-cyclopentyl]-1-(1,3-benzodioxol-4-yl)ethanamine (compound 1137),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-[4-[methanesulfonylaminocarbonylmethoxy]phenyl]-cyclopentyl]-1-(3-chloro-4-fluoro-phenyl)ethanamine (compound 1138),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-[4-[methanesulfonylaminocarbonylmethoxy]phenyl]-cyclopentyl]-1-(3-ethoxyphenyl)ethanamine (compound 1139),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-[4-(methanesulfonylaminocarbonylmethoxy)-phenyl]cyclopentyl]-1-(3-fluoro-5-methoxyphenyl)ethanamine (compound 1140).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-[4-(methanesulfonylaminocarbonylmethoxy)-phenyl]cyclopentyl]-1-(4-fluoro-2-methoxyphenyl)ethanamine (compound 1141),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3R)-3-[4-(methanesulfonylaminocarbonylmethoxy)-phenyl]cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1142),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(1,3-benzodioxol-4-yl)ethanamine (compound 1143),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(1,3-benzodioxol-5-yl)ethanamine (compound 1144),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(1-methyl-5-phenyl-pyrazol-3-yl)ethanamine (compound 1145),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(2,2-difluoro-1,3-benzodioxol-4-yl)ethanamine (compound 1146),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(2,3-dihydro-1,4-benzodioxin-5-yl)ethanamine (compound 1147),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(2,3-dihydrobenzofuran-5-yl)ethanamine (compound 1148),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(2,3-dimethoxyphenyl)ethanamine (compound 1149),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(2-fluoro-3-methoxy-phenyl)ethanamine (compound 1150).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(2-fluoro-5-methoxy-phenyl)ethanamine (compound 1151),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(2-methylthiazol-4-yl)ethanamine (compound 1152),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(3-chloro-4-fluoro-phenyl)ethanamine (compound 1153),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(3-ethoxyphenyl)ethanamine (compound 1154),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(3-fluoro-5-methoxyphenyl)ethanamine (compound 1155),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(4-fluoro-2-methoxyphenyl)ethanamine (compound 1156),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(4-fluoro-3-methoxyphenyl)ethanamine (compound 1157),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(5-chloro-2-thienyl)ethanamine (compound 1158),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(5-fluoroimidazo[1,2-a]pyridin-2-yl)ethanamine (compound 1159),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(8-quinolyl)ethanamine (compound 1160),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-[3-(methylethoxy)phenyl]ethanamine (compound 1161),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-[4-fluoro-3-(trifluoromethyl)phenyl]ethanamine (compound 1162),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-cyclopropylethanamine (compound 1163).\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-imidazo[1,5-a]pyridin-3-ylethanamine (compound 1164),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-pyrazolo[1,5-a]pyridin-3-ylethanamine (compound 1165),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(3-chlorophenyl)ethanamine (compound 1166),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(3-fluoro-4-methoxyphenyl)ethanamine (compound 1167),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(3-fluorophenyl)ethanamine (compound 1168),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-[3-(trifluoromethyl)phenyl]ethanamine (compound 1169),\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-benzo[b]thiophen-3-yl-ethanamine (compound 1170),\n" +
                "\t\t\t\t(1R/S)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-pyrimidin-4-ylethanamine (compound 1171), or\n" +
                "\t\t\t\t(1R)—(N)-[(1R/S,3S)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-1-(3-cyano-4-fluorophenyl)ethanamine (compound 1172).\n" +
                "\t\t\t\t(4-{(1R,3S)-3-[(1R)-1-(4-Fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-phenyl)-(3-hydroxy-azetidin-1-yl)-methanone (compound 1173),\n" +
                "\t\t\t\t4-{(1R,3S)-3-[(1R)-1-(4-Fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-N-methyl-N-oxetan-3-yl-benzamide (compound 1174),\n" +
                "\t\t\t\t(4-{(1R,3S)-3-[(1R)-1-(4-Fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-phenyl)-isoxazolidin-2-yl-methanone (compound 1175),\n" +
                "\t\t\t\t4-{(1R,3S)-3-[(1R)-1-(4-Fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-N-(2-hydroxy-ethyl)-benzamide (compound 1176),\n" +
                "\t\t\t\t(4-{(1R,3S)-3-[(1R)-1-(4-Fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-phenyl)-(3-hydroxy-pyrrolidin-1-yl)-methanone (compound 1177),\n" +
                "\t\t\t\t4-{(1R,3S)-3-[(1R)-1-(4-Fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-N-(2-methanesulfonylamino-ethyl)-benzamide hydrochloride (compound 1178).\n" +
                "\t\t\t\t4-(4-{(1R,3S)-3-[(1R)-1-(4-Fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-benzoyl)-piperazin-2-one (compound 1179),\n" +
                "\t\t\t\t(1R/S,3R)—N-[(1R)-1-(4-fluoro-3-methoxy-phenyl)ethyl]-3-[4-(2-morpholinosulfonylethyl)-phenyl]cyclopentanamine (mixture of 2 isomers) (compound 1180),\n" +
                "\t\t\t\tN-[[4-[(1R/S,3R/S)-3-[[(1R)-1-(4-fluoro-3-methoxy-phenyl)ethyl]amino]cyclopentyl]phenyl]methyl]methanesulfonamide (mixture of 4 isomers) (compound 1181),\n" +
                "\t\t\t\t(1S,3R)—N-[(1R)-1-(4-fluoro-3-methoxy-phenyl)ethyl]-3-[4-(1H-tetrazol-5-yl)phenyl]cyclopentanamine (compound 1182),\n" +
                "\t\t\t\t[4-[(1S,3R/S)-3-[[(1R)-1-(3-ethoxyphenyl)ethyl]amino]cyclopentyl]phenyl]-morpholino-methanone (compound 1183),\n" +
                "\t\t\t\t[4-[(1S,3R/S)-3-[[(1R)-1-(3-chlorophenyl)ethyl]amino]cyclopentyl]phenyl]-morpholino-methanone (compound 1184),\n" +
                "\t\t\t\t[4-[(1S,3R/S)-3-[[(1R)-1-(4-fluoro-3-methoxy-phenyl)ethyl]amino]cyclopentyl]phenyl]-morpholino-methanone (compound 1185),\n" +
                "\t\t\t\tN-(3-amino-3-oxo-propyl)-4-[(1S,3R/S)-3-[[(1R)-1-(1,3-benzodioxol-4-yl)ethyl]amino]cyclopentyl]benzamide (compound 1186),\n" +
                "\t\t\t\t4-[(1R,3S)-3-[[(1R)-1-(4-fluoro-3-methoxy-phenyl)ethyl]amino]cyclopentyl]-N-(2-hydroxyethyl)benzenesulfonamide (compound 1187),\n" +
                "\t\t\t\tN-(3-amino-3-oxo-propyl)-4-[(1S,3R/S)-3-[[(1R)-1-(3-ethoxyphenyl)ethyl]amino]-cyclopentyl]benzamide (compound 1188),\n" +
                "\t\t\t\tN-(3-amino-3-oxo-propyl)-4-[(1S,3R/S)-3-[[(1R)-1-(4-fluoro-3-methoxy-phenyl)ethyl]amino]cyclopentyl]benzamide (compound 1189),\n" +
                "\t\t\t\tN-{2-[Bis-(2-hydroxy-ethyl)-amino]-ethyl}-4-{(1R,3S)-3-[(1R)-1-(4-fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-benzamide (Compound 1190),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-(1R)-1-(4-fluoro-3-methoxyphenyl)ethylamine (Compound 1191),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-(1R)-1-(3-ethoxyphenyl)-ethanamine (Compound 1192),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-(1R)-1-(3-chlorophenyl)ethanamine (Compound 1193),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-(1R)-1-(1,3-benzodioxol-4-yl)ethanamine (Compound 1194),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-(1R)-1-(3-fluoro-5-methoxy-phenyl)-ethanamine (Compound 1195),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(4-hydroxypiperidine-1-carbonyl)phenyl]cyclopentyl]-(1R)-1-(2,3-Dihydro-benzo[1,4]dioxin-5-yl)-ethylamine (Compound 1196).\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-[(3S)-3-hydroxypyrrolidin-1-ylcarbonyl]phenyl]cyclopentyl]-(1R)-1-(4-fluoro-3-methoxyphenyl)ethylamine (Compound 1197),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-[(3S)-3-hydroxypyrrolidin-1-ylcarbonyl]phenyl]cyclopentyl]-(1R)-1-(3-ethoxyphenyl)-ethanamine (Compound 1198),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-[(3S)-3-hydroxypyrrolidin-1-ylcarbonyl]phenyl]cyclopentyl]-(1R)-1-(3-chlorophenyl)ethanamine (Compound 1199),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-[(3S)-3-hydroxypyrrolidin-1-ylcarbonyl]phenyl]cyclopentyl]-(1R)-1-(1,3-benzodioxol-4-yl)ethanamine (Compound 1200).\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-[(3S)-3-hydroxypyrrolidin-1-ylcarbonyl]phenyl]cyclopentyl]-(1R)-1-(3-fluoro-5-methoxy-phenyl)-ethanamine (Compound 1201),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-[(3S)-3-hydroxypyrrolidin-1-ylcarbonyl]phenyl]cyclopentyl]-(1R)-1-(2,3-Dihydro-benzo[1,4]dioxin-5-yl)-ethylamine (Compound 1202),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(2-(aminosulfonyl)ethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(4-fluoro-3-methoxyphenyl)ethylamine (Compound 1203),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(2-(aminosulfonyl)ethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(3-ethoxyphenyl)-ethanamine (Compound 1204),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(2-(aminosulfonyl)ethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(3-chlorophenyl)ethanamine (Compound 1205).\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(2-(aminosulfonyl)ethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(1,3-benzodioxol-4-yl)ethanamine (Compound 1206),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(2-(aminosulfonyl)ethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(3-fluoro-5-methoxy-phenyl)-ethanamine (Compound 1207),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(2-(aminosulfonyl)ethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(2,3-Dihydro-benzo[1,4]dioxin-5-yl)-ethylamine (Compound 1208),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(3-oxo-piperazinylcarbonyl)phenyl]cyclopentyl]-(1R)-1-(3-fluoro-5-methoxy-phenyl)-ethanamine (Compound 1209),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(3-oxo-piperazinylcarbonyl)phenyl]cyclopentyl]-(1R)-1-(3-ethoxyphenyl)-ethanamine (Compound 1210),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(3-oxo-piperazinylcarbonyl)phenyl]cyclopentyl]-(1R)-1-(3-chlorophenyl)ethanamine (Compound 1211),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(3-oxo-piperazinylcarbonyl)phenyl]cyclopentyl]-(1R)-1-(1,3-benzodioxol-4-yl)ethanamine (Compound 1212),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(3-oxo-piperazinylcarbonyl)phenyl]cyclopentyl]-(1R)-1-(2,3-Dihydro-benzo[1,4]dioxin-5-yl)-ethylamine (Compound 1213),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(aminocarbonylmethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(4-fluoro-3-methoxyphenyl)ethylamine (Compound 1214),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(aminocarbonylmethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(3-ethoxyphenyl)-ethanamine (Compound 1215),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(aminocarbonylmethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(3-chlorophenyl)ethanamine (Compound 1216).\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(aminocarbonylmethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(1,3-benzodioxol-4-yl)ethanamine (Compound 1217),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(aminocarbonylmethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(3-fluoro-5-methoxy-phenyl)-ethanamine (Compound 1218),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(aminocarbonylmethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(2,3-Dihydro-benzo[1,4]dioxin-5-yl)-ethylamine (Compound 1219),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(2-(methanesulfonylamino)-ethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(3-ethoxyphenyl)-ethanamine (Compound 1220),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(2-(methanesulfonylamino)-ethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(3-chlorophenyl)ethanamine (Compound 1221),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(2-(methanesulfonylamino)-ethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(1,3-benzodioxol-4-yl)ethanamine (Compound 1222),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(2-(methanesulfonylamino)-ethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(3-fluoro-5-methoxy-phenyl)-ethanamine (Compound 1223),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-(2-(methanesulfonylamino)-ethylaminocarbonyl)phenyl]cyclopentyl]-(1R)-1-(2,3-Dihydro-benzo[1,4]dioxin-5-yl)-ethylamine (Compound 1224),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-(1R)-1-(4-fluoro-3-methoxyphenyl)ethylamine (Compound 1225),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-(1R)-1-(3-ethoxyphenyl)-ethanamine (Compound 1226).\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-(1R)-1-(3-chlorophenyl)ethanamine (Compound 1227),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-(1R)-1-(1,3-benzodioxol-4-yl)ethanamine (Compound 1228),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-(1R)-1-(3-fluoro-5-methoxy-phenyl)-ethanamine (Compound 1229),\n" +
                "\t\t\t\t(N)-[(1S,3R)-3-[4-[piperidin-4-ylaminocarbonyl]phenyl]cyclopentyl]-(1R)-1-(2,3-Dihydro-benzo[1,4]dioxin-5-yl)-ethylamine (Compound 1230),\n" +
                "\t\t\t\t4-{(1R,3S)-3-[(1R)-1-(4-Fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-N-[2-(2-hydroxy-ethylamino)-ethyl]-benzamide (Compound 1231),\n" +
                "\t\t\t\t(4-{(1R,3S)-3-[(1R)-1-(4-Fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-phenyl)-[4-(2-hydroxy-ethyl)-piperazin-1-yl]-methanone (Compound 1232),\n" +
                "\t\t\t\t(4-{(1R,3S)-3-[(1R)-1-(4-Fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-phenyl)-piperazin-1-yl-methanone (Compound 1233),\n" +
                "\t\t\t\t(4-{(1R,3S)-3-[(1R)-1-(4-Fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-phenyl)-(4-methanesulfonyl-piperazin-1-yl)-methanone (Compound 1234),\n" +
                "\t\t\t\tN-(2-Amino-ethyl)-4-{(1R,3S)-3-[(1R)-1-(4-fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-benzamide (Compound 1235), or\n" +
                "\t\t\t\t4-{(1R,3S)-3-[(1R)-1-(4-Fluoro-3-methoxy-phenyl)-ethylamino]-cyclopentyl}-N-[1-(2-hydroxy-ethyl)-piperidin-4-yl]-benzamide (Compound 1236).";
        params.put("body", p);
        String responseContent = HttpClientUtil.postParamRequest(url, params);
        if (!"failed".equals(responseContent)) {
            String[] strs = responseContent.split("<textarea style=\"display:none\" rows=\"20\" cols=\"10\" name=\"xml\" type=\"hidden\">");
            String[] strs2 = strs[1].split("</textarea>");
            String result = strs2[0];
            result = result.replaceAll("&lt;", "<");
            result = result.replace("&gt;", ">");
            result = result.replace("&quot;", "\"");
            FileUtils.writeStringToFile(new File("D:\\33.xml"), result, "UTF-8");
            System.out.println("result = " + result);
        }
    }
}
