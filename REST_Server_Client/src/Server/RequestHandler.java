package Server;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.json.JSONException;
import org.json.JSONObject;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class RequestHandler extends ChannelInboundHandlerAdapter {
	private static final Charset UTF8 = Charset.forName("UTF-8");


	public RequestHandler() {

		
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		if (msg instanceof FullHttpRequest) {
			FullHttpRequest req = (FullHttpRequest) msg;
			HttpContent hc = ((HttpContent)msg);
			ByteBuf bb = hc.content();
			
			String method = req.getMethod().toString();
			String uri = req.getUri();

			System.out.println("msg test : " + msg.toString());
	        System.out.println("ByteBuffer Test : " + bb.toString(CharsetUtil.UTF_8));
			
			switch (method) {

			case "GET":
				if (uri.contains("mir")) {
					getHandler(ctx, uri, req);
				}
				break;
			case "POST":
			case "PUT":
				putHandler(ctx, uri, req);
				break;
			}

			if (is100ContinueExpected(req)) {
				ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
			}
		}
	}

	public static void getHandler(ChannelHandlerContext ctx, String uri, HttpRequest req) {


		String[] parseMap = uri.split("/");

		int deviceId = Integer.parseInt(parseMap[2]);
		String resp ="";

		//Write logic

			flush(resp, req, ctx);
		}

	public static void putHandler(ChannelHandlerContext ctx, String uri, HttpRequest req) {

		String[] parseMap = uri.split("/");

		int deviceId = Integer.parseInt(parseMap[2]);
		String resp = "";

		
		//Write logic



			flush(resp, req, ctx);

	}

	public static void flush(String resp,HttpRequest req, ChannelHandlerContext ctx) {

		FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(resp.getBytes(UTF8)));
		res.headers().set(CONTENT_TYPE, "text/plain");
		res.headers().set(CONTENT_LENGTH, res.content().readableBytes());

		if (isKeepAlive(req)) {
			res.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
			ctx.writeAndFlush(res);
		} else {
			ctx.write(res).addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println("ERROR!");
		ctx.close();
	}
}