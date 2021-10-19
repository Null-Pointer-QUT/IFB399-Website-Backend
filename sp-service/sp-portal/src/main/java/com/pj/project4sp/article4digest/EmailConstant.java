package com.pj.project4sp.article4digest;

public interface EmailConstant {

    String iEmailContainer = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\"/>\n" +
            "    <title>Weekly Digest</title>\n" +
            "    <meta name=\"description\" content=\"Null Pointer\"/>\n" +
            "    <meta name=\"author\" content=\"Null Pointer Team\"/>\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>\n" +
            "    <link rel='icon' href='https://z3.ax1x.com/2021/09/27/42rFFs.png'/>\n" +
            "    <link rel=\"stylesheet\" href=\"https://minio.juntao.life/ifb399bucket1/static/digest_mail_styles.css\"/>\n" +
            "</head>\n" +
            "\n" +
            "<body class=\"text-gray-50\">\n" +
            "<div>\n" +
            "    <div class=\"w-screen flex flex-col items-center space-y-3 pb-10\">\n" +
            "        %s" +
            "    </div>\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>";

    String iWeekIdStr = "<div class=\"font-bold text-2xl text-gray-700\">%s</div>";

    String iEmailTopicContainer = "        <div class='group border rounded-md bg-white hover:bg-gray-50 py-1 px-2 md:py-2 md:px-6 w-11/12 md:w-2/3 max-w-xl space-y-2'>\n" +
            "            %s" +
            "        </div>";

    String iEmailTopicNameStr = "<div class=\"font-medium text-2xl text-gray-700\">%s</div>";

    String iEmailArticleContainer = "            <div>\n" +
            "                <div class=\"w-full border-t border-gray-300\"></div>" +
            "                %s" +
            "            </div>";

    String iEmailArticleBodyContainer = "                <div class='flex flex-col md:space-y-1'>\n" +
            "                    <div class='cursor-pointer space-y-0.5'>\n" +
            "                        <div class='flex justify-between'>\n" +
            "                            <div class='text-md md:text-xl font-bold text-gray-800 most-line-1'>\n" +
            "                                %s\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <div class='flex justify-between space-x-2 items-center md:space-x-4 '>\n" +
            "                            <div class='w-24 md:w-32 h-20 flex items-center justify-center'>\n" +
            "                                <img class='object-contain h-full rounded-sm'\n" +
            "                                     src='%s'\n" +
            "                                     alt=''/>\n" +
            "                            </div>\n" +
            "                            <div class='w-full h-16 md:h-24 text-gray-800 text-left leading-4 text-sm md:text-base most-line-4'>\n" +
            "                                %s" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>";

    String iEmailArticleReadMoreContainer = "\n" +
            "                <div class=\"text-right pr-2\">\n" +
            "                    <a href=\"https://ifb399.juntao.life/explore/detail?id=%s\"\n" +
            "                       class=\"underline text-brand\">\n" +
            "                        Read More\n" +
            "                    </a>\n" +
            "                </div>";

}
