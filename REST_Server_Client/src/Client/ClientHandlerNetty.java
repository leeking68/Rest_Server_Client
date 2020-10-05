package Client;
/**
 * 
 * @author Gilho Lee
 * Email : leeeeegilho 
 * linkdein : https://www.linkedin.com/in/gilho0608/
 * 
 */
import java.util.Iterator;
import java.util.Set;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

public class ClientHandlerNetty extends SimpleChannelInboundHandler<HttpObject> {

	private EventLoopGroup group;
	private SocketChannel sc;
	private int count = 0;

	public ClientHandlerNetty(EventLoopGroup group, SocketChannel sc) {
		this.group = group;
		this.sc = sc;

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);

		System.out.println("## Channel Connect : " + ctx.channel().isActive());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		
		if (msg instanceof HttpResponse) {

			HttpResponse response = (HttpResponse) msg;					// handle HTTP Response		msg --> response

			System.err.print("## RESPONSE STATUS  : " + response.status());
			System.out.println("");
			System.err.print("## RESPONSE PROTOCOL VERSION  : " + response.protocolVersion());
			System.out.println("");
			// response header check
			if (!response.headers().isEmpty()) {
				Set<String> str = response.headers().names();
				Iterator<String> itr = str.iterator();
				while (itr.hasNext()) {
					String name = itr.next();
					System.err.println("HEADER NAME : " + name);
					System.err.println("HEADER VALUE :" + response.headers().getAll(name));
					System.err.println("");
				}
			}

			if (HttpUtil.isTransferEncodingChunked(response)) {
				System.err.println("CHUNKED CONTENT {");
			} else {
				System.err.println("CONTENT {");
			}

		}

		if (msg instanceof HttpContent) {
			HttpContent content = (HttpContent) msg;
			String whichData = Rest_Client.uri;
			
			System.err.println(""+whichData+" of ONOS = " + content.content().toString(CharsetUtil.UTF_8));
			
			String data = content.content().toString(CharsetUtil.UTF_8);
			
			
			if (content instanceof LastHttpContent) {
				System.err.println("} END OF CONTENT");
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
		sc.close();
		group.shutdownGracefully();
	}
}
