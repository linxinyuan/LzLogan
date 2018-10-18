package com.yibasan.lzlogan.channel;

import java.io.IOException;
import java.nio.channels.FileChannel;

import okio.Buffer;
import okio.Source;
import okio.Timeout;

/**
 * Author : Create by Linxinyuan on 2018/08/06
 * Email : linxinyuan@lizhi.fm
 * Desc : android dev
 */
public final class FileChannelSource implements Source {
    private final FileChannel channel;
    private final Timeout timeout;

    private long position;

    public FileChannelSource(FileChannel channel, Timeout timeout) throws IOException {
        this.channel = channel;
        this.timeout = timeout;

        this.position = channel.position();
    }

    @Override
    public long read(Buffer sink, long byteCount) throws IOException {
        if (!channel.isOpen()) throw new IllegalStateException("closed");
        if (position == channel.size()) return -1L;

        long read = channel.transferTo(position, byteCount, sink);
        position += read;
        return read;
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

