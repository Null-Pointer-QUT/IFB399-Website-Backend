package com.pj;

import com.pj.project4sp.user4notify.NotificationSocket;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class NotificationTest {

    @Test
    public void testSendMsg() throws IOException {
        NotificationSocket.sendMsg(2L, "123");
    }
}
