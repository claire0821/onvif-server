package com.root.onvif.netty;

import com.root.onvif.model.ResponseInfo;
import com.root.onvif.thread.AsyncServiceImpl;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.Map;

public class ServerHandler  extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;

//    @Override
//    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        super.channelRegistered(ctx);
//        System.out.println("client");
//    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if(msg != null) {
//            //do something msg
//            ByteBuf buf = (ByteBuf)msg;
//            byte[] data = new byte[buf.readableBytes()];
//            buf.readBytes(data);
//            String request = new String(data, "utf-8");
//            System.out.println("Server: " + request);
//            //写给客户端
//            String response = "我是反馈的信息";
//            ctx.writeAndFlush(Unpooled.copiedBuffer("888".getBytes()));
//            //.addListener(ChannelFutureListener.CLOSE);
//        }
//
//    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;
            String uri = req.uri();

            if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
                // http请求
                QueryStringDecoder decoder = new QueryStringDecoder(uri);
                Map<String, List<String>> parameters = decoder.parameters();
                List<String> stream = parameters.get("stream");
                if(stream != null && stream.size() == 1) {//判断是否带有流地址
                    System.out.println(stream.get(0));
                    ResponseInfo responseInfo = new ResponseInfo();
                    responseInfo.setResponse(ctx);
                    responseInfo.setIsHttp(ResponseInfo.HTTP);
                    responseInfo.setUrl(stream.get(0));
                    responseInfo.setSendHeader(false);

                    sendFlvReqHeader(ctx);
                    AsyncServiceImpl.addRes(responseInfo);

                } else {
                    sendError(ctx, HttpResponseStatus.BAD_REQUEST);
                }

            } else {
                int startIndex = uri.indexOf("stream=");
                if(startIndex <= 0) {
                    return;
                }
                startIndex += 7;
                String stream = uri.substring(startIndex);
                String location = "ws://" + req.headers().get(HttpHeaderNames.HOST) + req.uri();

                // websocket握手，请求升级
                // 参数分别是ws地址，子协议，是否扩展，最大frame长度
                WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(
                        location, null, true, 5 * 1024 * 1024);
                handshaker = factory.newHandshaker(req);
                if (handshaker == null) {
                    WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                } else {
                    handshaker.handshake(ctx.channel(), req);
//                    mediaService.playForWs(camera, ctx, autoClose);
                }
                ctx.writeAndFlush(new PingWebSocketFrame());

                ResponseInfo responseInfo = new ResponseInfo();
                responseInfo.setResponse(ctx);
                responseInfo.setIsHttp(ResponseInfo.WS);
                responseInfo.setUrl(stream);
                responseInfo.setSendHeader(false);

                AsyncServiceImpl.addRes(responseInfo);
            }

            System.out.println("http request");
        } else if(msg instanceof WebSocketFrame) {
            handleWebSocketRequest(ctx, (WebSocketFrame) msg);
        }

    }

    /**
     * 错误请求响应
     *
     * @param ctx
     * @param status
     */
    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("请求地址有误: " + status + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * websocket处理
     *
     * @param ctx
     * @param frame
     */
    private void handleWebSocketRequest(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 关闭
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }

        // 握手PING/PONG
        if (frame instanceof PingWebSocketFrame) {
            ctx.write(new PongWebSocketFrame(frame.content().retain()));
            System.out.println("ping");
            return;
        }
        if (frame instanceof PongWebSocketFrame) {
            System.out.println("pong");
            return;
        }

        // 文本
        if (frame instanceof TextWebSocketFrame) {
            return;
        }

        if (frame instanceof BinaryWebSocketFrame) {
            return;
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println("client close" + ctx.channel().remoteAddress().toString());
        //去掉没有观看的视频流
        ChannelId id = ctx.channel().id();
        AsyncServiceImpl.remove(id);
    }

    /**
     * 发送req header，告知浏览器是flv格式
     *
     * @param ctx
     */
    private void sendFlvReqHeader(ChannelHandlerContext ctx) {
        HttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        rsp.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE)
                .set(HttpHeaderNames.CONTENT_TYPE, "video/x-flv").set(HttpHeaderNames.ACCEPT_RANGES, "bytes")
                .set(HttpHeaderNames.PRAGMA, "no-cache").set(HttpHeaderNames.CACHE_CONTROL, "no-cache")
                .set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED).set(HttpHeaderNames.SERVER, "zhang");
        ctx.writeAndFlush(rsp);
    }

    private String getWebSocketLocation(FullHttpRequest request) {
        System.out.println(request.uri());
        String location = request.headers().get(HttpHeaderNames.HOST) + request.uri();
        return "ws://" + location;
    }
}