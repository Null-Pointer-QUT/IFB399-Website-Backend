package com.pj;

import cn.hutool.extra.mail.MailUtil;
import com.pj.project4sp.utils.CheckEmailOK;
import com.pj.project4sp.utils.CheckEmailObj;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MailTest {

    @Resource
    private CheckEmailOK ce;

    @Test
    public void test() {
        if(ce.checkEmail("stephen.zrt@qq.com")){
            System.out.println("true");
        }else{
            System.out.println("false");
        }
        System.out.println("1258455347@qq.com Outcome: "
                + CheckEmailObj.checkEmail("1258455347@qq.com"));
        System.out.println("synthiapanzj@gmail.com Outcome: "
                + CheckEmailObj.checkEmail("synthiapanzj@gmail.com"));
        System.out.println("n10889299@qut.edu.au Outcome: "
                + CheckEmailObj.checkEmail("n10889299@qut.edu.au"));
    }

    @Test
    @SneakyThrows
    public void testSend() {
        String emailContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\"/>\n" +
                "    <title>Weekly Digest</title>\n" +
                "    <meta name=\"description\" content=\"Null Pointer\"/>\n" +
                "    <meta name=\"author\" content=\"Null Pointer Team\"/>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>\n" +
                "    <link rel='icon' href='https://z3.ax1x.com/2021/09/27/42rFFs.png'/>\n" +
                "    <link rel=\"stylesheet\" href=\"https://ifb399-minio.halocampus.com:40443/ifb399bucket1/static/digest_mail_styles.css\"/>\n" +
                "</head>\n" +
                "\n" +
                "<body class=\"text-gray-50\">\n" +
                "<div>\n" +
                "    <div class=\"w-screen flex flex-col items-center space-y-3 pb-10\">\n" +
                "        <div class=\"font-bold text-2xl text-gray-700\">Null Pointer Weekly Digest</div>\n" +
                "        <div class=\"font-bold text-2xl text-gray-700\">2021-09-27</div>\n" +
                "\n" +
                "        <div class='group border rounded-md bg-white hover:bg-gray-50 py-1 px-2 md:py-2 md:px-6 w-11/12 md:w-2/3 max-w-xl space-y-2'>\n" +
                "            <div class=\"font-medium text-2xl text-gray-700\">C++</div>\n" +
                "            <div>\n" +
                "                <div class=\"w-full border-t border-gray-300\"></div>\n" +
                "                <div class='flex flex-col md:space-y-1'>\n" +
                "                    <div class='cursor-pointer space-y-0.5'>\n" +
                "                        <div class='flex justify-between'>\n" +
                "                            <div class='text-md md:text-xl font-bold text-gray-800 most-line-1'>\n" +
                "                                Pointers in C / C++\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                        <div class='flex justify-between space-x-2 items-center md:space-x-4 '>\n" +
                "                            <div class='w-24 md:w-32 h-20 flex items-center justify-center'>\n" +
                "                                <img class='object-contain h-full rounded-sm'\n" +
                "                                     src='https://ifb399-minio.halocampus.com:40443/ifb399bucket1/test/1630338225860_np_logo.png'\n" +
                "                                     alt=''/>\n" +
                "                            </div>\n" +
                "                            <div class='w-full h-16 md:h-24 text-gray-800 text-left leading-4 text-sm md:text-base most-line-4'>\n" +
                "                                Pointers in C and C++ are often challenging to understand. In this course, they will be\n" +
                "                                demystified, allowing you to use pointers more effectively in your code. The concepts\n" +
                "                                you learn in this course apply to both C and C++.\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "\n" +
                "                <div class=\"text-right pr-2\">\n" +
                "                    <a href=\"https://ifb399.juntao.life/explore/detail?id=1432047793888563200\"\n" +
                "                       class=\"underline text-brand\">\n" +
                "                        Read More\n" +
                "                    </a>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "\n" +
                "            <div>\n" +
                "                <div class=\"w-full border-t border-gray-300\"></div>\n" +
                "                <div class='flex flex-col md:space-y-1'>\n" +
                "                    <div class='cursor-pointer space-y-0.5'>\n" +
                "                        <div class='flex justify-between'>\n" +
                "                            <div class='text-md md:text-xl font-bold text-gray-800 most-line-1'>\n" +
                "                                Pointers in C / C++\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                        <div class='flex justify-between space-x-2 items-center md:space-x-4 '>\n" +
                "                            <div class='w-24 md:w-32 h-20 flex items-center justify-center'>\n" +
                "                                <img class='object-contain h-full rounded-sm'\n" +
                "                                     src='https://ifb399-minio.halocampus.com:40443/ifb399bucket1/test/1630338225860_np_logo.png'\n" +
                "                                     alt=''/>\n" +
                "                            </div>\n" +
                "                            <div class='w-full h-16 md:h-24 text-gray-800 text-left leading-4 text-sm md:text-base most-line-4'>\n" +
                "                                Pointers in C and C++ are often challenging to understand. In this course, they will be\n" +
                "                                demystified, allowing you to use pointers more effectively in your code. The concepts\n" +
                "                                you learn in this course apply to both C and C++.\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "\n" +
                "                <div class=\"text-right pr-2\">\n" +
                "                    <a href=\"https://ifb399.juntao.life/explore/detail?id=1432047793888563200\"\n" +
                "                       class=\"underline text-brand\">\n" +
                "                        Read More\n" +
                "                    </a>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class='group border rounded-md bg-white hover:bg-gray-50 py-1 px-2 md:py-2 md:px-6 w-11/12 md:w-2/3 max-w-xl space-y-2'>\n" +
                "            <div class=\"font-medium text-2xl text-gray-700\">C</div>\n" +
                "            <div>\n" +
                "                <div class=\"w-full border-t border-gray-300\"></div>\n" +
                "                <div class='flex flex-col md:space-y-1'>\n" +
                "                    <div class='cursor-pointer space-y-0.5'>\n" +
                "                        <div class='flex justify-between'>\n" +
                "                            <div class='text-md md:text-xl font-bold text-gray-800 most-line-1'>\n" +
                "                                Pointers in C / C++\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                        <div class='flex justify-between space-x-2 items-center md:space-x-4 '>\n" +
                "                            <div class='w-24 md:w-32 h-20 flex items-center justify-center'>\n" +
                "                                <img class='object-contain h-full rounded-sm'\n" +
                "                                     src='https://ifb399-minio.halocampus.com:40443/ifb399bucket1/test/1630338225860_np_logo.png'\n" +
                "                                     alt=''/>\n" +
                "                            </div>\n" +
                "                            <div class='w-full h-16 md:h-24 text-gray-800 text-left leading-4 text-sm md:text-base most-line-4'>\n" +
                "                                Pointers in C and C++ are often challenging to understand. In this course, they will be\n" +
                "                                demystified, allowing you to use pointers more effectively in your code. The concepts\n" +
                "                                you learn in this course apply to both C and C++.\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "\n" +
                "                <div class=\"text-right pr-2\">\n" +
                "                    <a href=\"https://ifb399.juntao.life/explore/detail?id=1432047793888563200\"\n" +
                "                       class=\"underline text-brand\">\n" +
                "                        Read More\n" +
                "                    </a>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "\n" +
                "            <div>\n" +
                "                <div class=\"w-full border-t border-gray-300\"></div>\n" +
                "                <div class='flex flex-col md:space-y-1'>\n" +
                "                    <div class='cursor-pointer space-y-0.5'>\n" +
                "                        <div class='flex justify-between'>\n" +
                "                            <div class='text-md md:text-xl font-bold text-gray-800 most-line-1'>\n" +
                "                                Pointers in C / C++\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                        <div class='flex justify-between space-x-2 items-center md:space-x-4 '>\n" +
                "                            <div class='w-24 md:w-32 h-20 flex items-center justify-center'>\n" +
                "                                <img class='object-contain h-full rounded-sm'\n" +
                "                                     src='https://ifb399-minio.halocampus.com:40443/ifb399bucket1/test/1630338225860_np_logo.png'\n" +
                "                                     alt=''\n" +
                "                                />\n" +
                "                            </div>\n" +
                "                            <div class='w-full h-16 md:h-24 text-gray-800 text-left leading-4 text-sm md:text-base most-line-4'>\n" +
                "                                Pointers in C and C++ are often challenging to understand. In this course, they will be\n" +
                "                                demystified, allowing you to use pointers more effectively in your code. The concepts\n" +
                "                                you learn in this course apply to both C and C++.\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "\n" +
                "                <div class=\"text-right pr-2\">\n" +
                "                    <a href=\"https://ifb399.juntao.life/explore/detail?id=1432047793888563200\"\n" +
                "                       class=\"underline text-brand\">\n" +
                "                        Read More\n" +
                "                    </a>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
        MailUtil.send("n10889299@qut.edu.au", "测试", emailContent, true);

//        MailAccount account = new MailAccount();
//        account.setHost("smtp.yeah.net");
//        account.setPort(465);
//        account.setAuth(true);
//        account.setFrom("n_pointer@yeah.net");
//        account.setUser("n_pointer");
//        account.setPass("VURROUESBZLEVPIU");
//        account.setStarttlsEnable(true);
//        account.setSslEnable(true);
//////        account.setSslProtocols("STARTTLS");
////
//
//
//////        account.setSslProtocols("STARTTLS");
//        MailUtil.send(account, CollUtil.newArrayList("n10889299@qut.edu.au"), "测试", "邮件来自测试", false);
    }
}
