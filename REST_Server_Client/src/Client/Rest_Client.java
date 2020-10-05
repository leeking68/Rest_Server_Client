package Client;
/**
 * 
 * @author Gilho Lee
 * Email : leeeeegilho 
 * linkdein : https://www.linkedin.com/in/gilho0608/
 * 
 */
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;

import io.netty.handler.codec.http.HttpVersion;


public class Rest_Client {


    ChannelFuture cf;
    EventLoopGroup group;
    
   //information about Server
    String host;
    int port;
    public static String uri;
    
    
    public Rest_Client(String ip, int port, String uri) {
    	
    this.host = ip;
    this.port = port;
    this.uri = uri;
    	
    	
    }
    
    public void connect() {
 
       group = new NioEventLoopGroup();
       
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new Pipeline(group));
            cf = b.connect(host, port).sync();
            createRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setUri(String uri) {
    	this.uri = uri;
    }
    public void createRequest() throws Exception {
        
        
            DefaultFullHttpRequest request = null;
            
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/"+uri);
        	
            String encoding = null;
        	
            request.headers().set(HttpHeaderNames.HOST, host+":"+port);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            cf.channel().writeAndFlush(request);
    }
    
    
    public void close() {
        cf.channel().close();
        group.shutdownGracefully();
    }
    
    
}
