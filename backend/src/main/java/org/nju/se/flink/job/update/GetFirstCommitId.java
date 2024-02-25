package org.nju.se.flink.job.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetFirstCommitId {

    public static void main(String[] args) {
        // Replace 'YOUR_GITHUB_TOKEN' with your actual GitHub token if the repositories are private
        String githubToken = "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";

        // Replace the following array with your GitHub repository URLs
        String[] repositoryUrls = {
                "https://github.com/openshopio/openshop.io-android",

                "https://github.com/irccloud/android",

                "https://github.com/kontalk/androidclient",

                "https://github.com/Anasthase/TintBrowser",

                "https://github.com/js-labs/WalkieTalkie",

                "https://github.com/pocmo/Yaaic",

                "https://github.com/zulip/zulip-android",

                "https://github.com/Nightonke/LeeCo",

                "https://github.com/bither/bither-android",

                "https://github.com/Nightonke/CoCoin",

                "https://github.com/codinguser/gnucash-android",

                "https://github.com/moneymanagerex/android-money-manager-ex",

                "https://github.com/WillWcchan/Simple-Random-Stock",

                "https://github.com/jonasoreland/runnerup",

                "https://github.com/AntennaPod/AntennaPod",

                "https://github.com/k3b/APhotoManager/",

                "https://github.com/google/ExoPlayer",

                "https://github.com/TeamNewPipe/NewPipe",

                "https://github.com/Swati4star/Images-to-PDF",

                "https://github.com/kabouzeid/Phonograph",
//
//
//
                "https://github.com/bill1012/microservice",

                "https://github.com/zlt2000/microservices-platform",

                "https://github.com/techa03/goodsKill",

                "https://github.com/chillzhuang/blade-tool",

                "https://github.com/FJ-OMS/oms-erp",

                "https://github.com/OptionalDay/spring-cloud-vue",

                "https://github.com/maventalker/simplemall",

                "https://github.com/Saseke/poseidon",

                "https://github.com/junneyang/xxproject",

                "https://github.com/sunweiguo/MMall",

                "https://github.com/Linter-cn/OASys",

                "https://github.com/274056675/springboot-openai-chatgpt",

                "https://github.com/macrozheng/mall-swarm",

                "https://github.com/newbee-ltd/newbee-mall-cloud",

                "https://github.com/paascloud/paascloud-master",

                "https://github.com/u014427391/taoshop",

                "https://github.com/2227324689/gpmall",

                "https://github.com/Grootzz/dis-seckill",
//
//
//
                "https://github.com/YGcatswhite/MAODS_Java",

                "https://github.com/dataease/dataease",

                "https://github.com/apache/maven",

                "https://github.com/gradle/gradle",

                "https://github.com/apache/ant",

                "https://github.com/testng-team/testng",

                "https://github.com/findbugsproject/findbugs",

                "https://github.com/checkstyle/checkstyle",

                "https://github.com/spotbugs/spotbugs",

                "https://github.com/jenkinsci/jenkins",

                "https://github.com/projectlombok/lombok",

                "https://github.com/swagger-api/swagger-core",

                "https://github.com/jhipster/generator-jhipster",

                "https://github.com/flyway/flyway",

                "https://github.com/qos-ch/logback",

                "https://github.com/jacoco/jacoco",

                "https://github.com/mapstruct/mapstruct",

                "https://github.com/jOOQ/jOOQ",

                "https://github.com/liquibase/liquibase",

                "https://github.com/mock-server/mockserver"

        };
        // 仓库总cimmit 条数
        int[] commitOffsets = {
                79,
                3152,
                7165,
                242,
                106,
                1067,
                1086,
                75,
                1951,
                104,
                1730,
                4406,
                55,
                3140,
                8635,
                1128,
                18865,
                10985,
                677,
                1615,

                23,
                743,
                845,
                197,
                85,
                7,
                186,
                46,
                127,
                90,
                226,
                103,
                459,
                14,
                46,
                369,
                479,
                71,


                2,
                10618,
                12015,
                110329,
                14977,
                5308,
                15375,
                13037,
                17195,
                34423,
                3629,
                4251,
                49652,
                3144,
                4464,
                1843,
                1679,
                12674,
                12908,
                4188

        };

        for (int i = 0; i < repositoryUrls.length; i++) {
            String repoUrl = repositoryUrls[i];
            String firstCommitId = getLastCommitId(repoUrl, githubToken);
            if (firstCommitId != null) {
                //System.out.println("Initial commit ID for " + repoUrl + ": " + commitId);
                String initialCommitId = getInitialCommitId(repoUrl, firstCommitId, commitOffsets[i], githubToken);
                if(initialCommitId != null){
                    String sql = "insert into code_repo_info (repo_name, username, repo_classification, latest_commit_id) values (";
                    String[] splits = repoUrl.split("/");
                    sql+="'"+splits[splits.length-1]+"', '"+splits[splits.length-2]+"', ";
                    if(i<20){
                        sql+="'android', ";
                    }else if(i<38){
                        sql+="'microservice', ";
                    }else {
                        sql+="'util', ";
                    }
                    sql+="'"+initialCommitId+"')";
                    System.out.println(sql);
                }
                else {
                    System.out.println("Failed to retrieve initial commit ID for " + repoUrl);
                }
            } else {
                System.out.println("Failed to retrieve first commit ID for " + repoUrl);
            }
        }
    }

    private static String getLastCommitId(String repoUrl, String githubToken) {
        try {
            URL url = new URL(repoUrl + "/commits");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request headers
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (githubToken != null && !githubToken.isEmpty()) {
                connection.setRequestProperty("Authorization", "Token " + githubToken);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // 定义正则表达式
                String regex = "\"Copy the full SHA\" value=\"(.*?)\"";

                // 创建Pattern对象
                Pattern pattern = Pattern.compile(regex);

                // 创建Matcher对象
                Matcher matcher = pattern.matcher(response.toString());

                // 查找匹配
                if (matcher.find()) {
                    // 提取匹配到的内容
                    String extractedValue = matcher.group(1);
                    //System.out.println("Extracted value: " + extractedValue);
                    return extractedValue;
                } else {
                    System.out.println("Value not found in the given string.");
                    return null;
                }
            } else {
                System.out.println("Error accessing GitHub API. Response Code: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    private static String getInitialCommitId(String repoUrl, String firstId, int commitOffset, String githubToken) {
        try {
            URL url = new URL(repoUrl + "/commits/?after="+firstId+"+"+(commitOffset-2));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request headers
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (githubToken != null && !githubToken.isEmpty()) {
                connection.setRequestProperty("Authorization", "Token " + githubToken);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // 定义正则表达式
                String regex = "\"Copy the full SHA\" value=\"(.*?)\"";

                // 创建Pattern对象
                Pattern pattern = Pattern.compile(regex);

                // 创建Matcher对象
                Matcher matcher = pattern.matcher(response.toString());

                // 查找匹配
                if (matcher.find()) {
                    // 提取匹配到的内容
                    String extractedValue = matcher.group(1);
                    //System.out.println("Extracted value: " + extractedValue);
                    return extractedValue;
                } else {
                    System.out.println("Value not found in the given string.");
                    return null;
                }
            } else {
                System.out.println("Error accessing GitHub API. Response Code: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}

