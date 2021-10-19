package com.pj.project4sp.user4notify;

import cn.hutool.json.JSONUtil;
import com.pj.project4sp.user4notify.vo.WsMessageVo;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;


/**
 * definition for our encoder
 *
 */
public class ServerEncoder implements Encoder.Text<WsMessageVo> {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(EndpointConfig arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public String encode(WsMessageVo wsMessageVo) {
        return JSONUtil.parseObj(wsMessageVo).toString();
    }
}