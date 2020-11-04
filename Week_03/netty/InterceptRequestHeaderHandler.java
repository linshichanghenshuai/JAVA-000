package com.lsc.geek.netty;

import com.lsc.geek.netty.filter.HttpRequestFilter;
import com.lsc.geek.netty.filter.NettyHeadersFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @program: hello
 * @description:
 * @author: 林师昌
 */
public class InterceptRequestHeaderHandler extends ChannelInboundHandlerAdapter {

    private HttpRequestFilter httpFilter;

    public InterceptRequestHeaderHandler(){
        httpFilter = new NettyHeadersFilter();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程: " + Thread.currentThread().getName());
        FullHttpRequest fullHttpRequest = (FullHttpRequest)msg;
        httpFilter.filter(fullHttpRequest,ctx);
//        System.out.println("客户端发送的消息是: " + buf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
