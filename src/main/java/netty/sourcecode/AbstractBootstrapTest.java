package netty.sourcecode;

import java.net.SocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.util.AttributeKey;

public abstract class AbstractBootstrapTest<B extends AbstractBootstrapTest<B, C>, C extends Channel> implements Cloneable {

	//recator线程池
	private volatile EventLoopGroup group;
    private volatile SocketAddress localAddress;
    // channel相关的选项参数
    private final Map<ChannelOption<?>, Object> options = new LinkedHashMap<ChannelOption<?>, Object>();
    // 初始化channel的属性值
    private final Map<AttributeKey<?>, Object> attrs = new LinkedHashMap<AttributeKey<?>, Object>();
    // 业务逻辑Handler，主要是HandlerInitializer，也可能是普通Handler
    private volatile ChannelHandler handler;

    AbstractBootstrapTest() {
        // Disallow extending from a different package.
    }

    AbstractBootstrapTest(AbstractBootstrapTest<B, C> bootstrap) {
        group = bootstrap.group;
        handler = bootstrap.handler;
        localAddress = bootstrap.localAddress;
        synchronized (bootstrap.options) {
            options.putAll(bootstrap.options);
        }
        synchronized (bootstrap.attrs) {
            attrs.putAll(bootstrap.attrs);
        }
    }
    
    //绑定recator线程池
    @SuppressWarnings("unchecked")
    public B group(EventLoopGroup group) {
        if (group == null) {
            throw new NullPointerException("group");
        }
        if (this.group != null) {
            throw new IllegalStateException("group set already");
        }
        this.group = group;
        return (B) this;
    }
}
