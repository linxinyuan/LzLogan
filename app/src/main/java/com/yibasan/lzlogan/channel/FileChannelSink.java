package com.yibasan.lzlogan.channel;

import java.io.IOException;
import java.nio.channels.FileChannel;

import okio.Buffer;
import okio.Sink;
import okio.Timeout;

/**
 * Author : Create by Linxinyuan on 2018/08/06
 * Email : linxinyuan@lizhi.fm
 * Desc : android dev
 */
public final class FileChannelSink implements Sink {
    private final FileChannel channel;
    private final Timeout timeout;

    private long position;

    public FileChannelSink(FileChannel channel, Timeout timeout) throws IOException {
        this.channel = channel;
        this.timeout = timeout;

        this.position = channel.position();
    }

    @Override
    public void write(Buffer source, long byteCount) throws IOException {
        if (!channel.isOpen()) throw new IllegalStateException("closed");
        if (byteCount == 0) return;

        long remaining = byteCount;
        while (remaining > 0) {
            long written = channel.transferFrom(source, position, remaining);
            position += written;
            remaining -= written;
        }
    }

    @Override
    public void flush() throws IOException {
        // Cannot alter meta data through this Sink
        channel.force(false);
    }

    @Override
    public Timeout timeout() {
        return timeout;
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
